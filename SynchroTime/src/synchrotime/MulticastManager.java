/**
 * Fichier: MulticastManager.java
 * Auteurs: Nadir Benallal, Aurelie Levy
 * Creation: Octobre 2017
 * But: Gestion de la communication en multicast du cote du maitre
 * Envoi un message SYNC en gardant en memoire l'heure de son envoi puis envoie
 * cette heure dans un message FOLLOW_UP
 */
package synchrotime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MulticastManager implements Runnable {

   private final String IP;
   private final int SOCKET_NBR;
   private MulticastSocket socket;
   private boolean runningMulticast;

   public MulticastManager(String ip, int port) {
      this.IP = ip;
      this.SOCKET_NBR = port;
      this.runningMulticast = true;
      try {
         socket = new MulticastSocket(this.SOCKET_NBR);
      } catch (IOException ex) {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public String getIP() {
      return IP;
   }

   public int getSOCKET_NBR() {
      return SOCKET_NBR;
   }

   @Override
   public void run() {
      byte id = 0x00;

      InetAddress group;
      try {
         group = InetAddress.getByName(IP);
         socket.joinGroup(group);
         DatagramPacket packet;

         while (runningMulticast) {
            id++;
            byte[] msg = new byte[]{Utils.getSync(), id};

            //envoi du message SYNC
            packet = new DatagramPacket(msg, msg.length, group, SOCKET_NBR);
            long syncSent = System.currentTimeMillis();
            socket.send(packet);
            System.out.println("SYNC sent!");
            //System.out.println("time in master sended " + syncSent);

            //envoi du message FOLLOW_UP
            msg = new byte[10];
            msg[0] = Utils.getFollowUp();
            msg[1] = id;

            byte[] send = Utils.getTimeByByteTab(syncSent);

            for (int i = 0; i < send.length; i++) {
               msg[i + 2] = send[i];
            }
            packet = new DatagramPacket(msg, msg.length, group, SOCKET_NBR);
            socket.send(packet);
            System.out.println("FOLLOW_UP sent!");

            //attente
            TimeUnit.SECONDS.sleep(Utils.getK());

         }
      } catch (UnknownHostException ex) {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
//TODO
      } catch (IOException ex) {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
//TODO
      } catch (InterruptedException ex) {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
//TODO
      }
   }

   public void stop() {
      runningMulticast = false;
   }

   public boolean isRunning() {
      return runningMulticast;
   }
}
