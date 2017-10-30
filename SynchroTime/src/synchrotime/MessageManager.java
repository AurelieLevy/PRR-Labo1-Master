/**
 * Fichier: MessageManager.java
 * Auteurs: Nadir Benallal, Aurelie Levy
 * Creation: Octobre 2017
 * But: Gestion de la communication point a point du cote du maitre
 * Le maitre attend la reception d'un delay request puis envoi un message
 * delay response contenant l'heure a laquelle il a recu la requete
 */

package synchrotime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageManager implements Runnable {

   private int socketNbr;
   //private String name;
   private final DatagramSocket socket;
   private boolean runningPtToPt;

                                                //le port du master doit etre fixe, pas celui des esclaves
   public MessageManager(int port) throws SocketException {
      this.socketNbr = port;
      socket = new DatagramSocket(port);
      this.runningPtToPt = true;
   }

   /**
    * Run du point a point
    */
   @Override
   public void run() {


      while (runningPtToPt) {
         byte[] buffer = new byte[10];
         InetAddress address;
         
         try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            //System.out.println("Recieving...");

            socket.receive(packet);
            byte[] recievedMessage = packet.getData();

            long messageTime = System.currentTimeMillis();

            address = packet.getAddress();
            //name = address.getHostName();
            socketNbr = packet.getPort();
            if (recievedMessage[0] == Utils.getDelayRequest()) {
               System.out.println("delay request recieved");
               buffer = new byte[10]; //remise a zero du buffer pour le nouvel envoi
               buffer[0] = Utils.getDelayResponse();
               buffer[1] = recievedMessage[1];

               byte[] send = Utils.getTimeByByteTab(messageTime);

               for (int i = 0; i < send.length; i++) {
                  buffer[i + 2] = send[i];
               }
               
               packet = new DatagramPacket(buffer, buffer.length, address, socketNbr);
               socket.send(packet);
               System.out.println("delay response sent!");
            }

         } catch (IOException ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
                                                                                                //TODO recieve et send
         }
      }
      socket.close();
   }

   public void stop() {
      runningPtToPt = false;
   }

   public boolean isRunningPtToPt() {
      return runningPtToPt;
   }
}
