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
   private final int PORT;
   private MulticastSocket socket;
   private boolean runningMulticast;

   /**
    * Constructeur du manager multicast
    * @param ip ip a utiliser
    * @param port port a utiliser
    */
   public MulticastManager(String ip, int port) {
      this.IP = ip;
      this.PORT = port;
      this.runningMulticast = true;
      try {
         socket = new MulticastSocket(this.PORT);
      } catch (IOException ex) {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Problem while creating socket");
      }
   }

   /**
    * Permet la recuperation de l'ip
    * @return une string contenant l'ip
    */
   public String getIP() {
      return IP;
   }

   /**
    * Permet d'obtenir le port
    * @return le port en int
    */
   public int getPORT() {
      return PORT;
   }

   /*
   Run du multicast du maitre
   */
   @Override
   public void run() {
      byte id = 0x00; //id du message en byte
      InetAddress group;
      
      try {
         //on rejoint le groupe multicast
         group = InetAddress.getByName(IP);
         socket.joinGroup(group);
         
         DatagramPacket packet;

         while (runningMulticast) { //tant que le multicast a lieu
            id++;
            //creation du nouveau message SYNC
            byte[] msg = new byte[]{Utils.getSync(), id};

            //envoi du message SYNC
            packet = new DatagramPacket(msg, msg.length, group, PORT);
            long syncSent = System.currentTimeMillis();
            socket.send(packet);
            System.out.println("SYNC sent!");

            //envoi du message FOLLOW_UP
            msg = new byte[10];
            msg[0] = Utils.getFollowUp();
            msg[1] = id;

            //transformation du temps pour le mettre dans un tableau de bytes
            byte[] send = Utils.getTimeByByteTab(syncSent);
            for (int i = 0; i < send.length; i++) {
               msg[i + 2] = send[i];
            }
            
            packet = new DatagramPacket(msg, msg.length, group, PORT);
            socket.send(packet);
            System.out.println("FOLLOW_UP sent!");

            //attente
            TimeUnit.SECONDS.sleep(Utils.getK());

         }
      } catch (UnknownHostException ex) {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Problem while getting group by ip");
//TODO
      } catch (IOException ex) {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Problem while joining group or sending packet");
//TODO
      } catch (InterruptedException ex) {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Problem while waiting k sec");
//TODO
      }
   }

   /**
    * Permettrait d'arreter le multicast si besoin
    */
   public void stop() {
      runningMulticast = false;
   }

   /**
    * Permet de savoir si le multicast est en fonctionnement
    * @return true si en marche, false sinon
    */
   public boolean isRunning() {
      return runningMulticast;
   }
}
