/**
 * Fichier: SyncroTime.java
 * Auteurs: Nadir Benallal, Aurelie Levy
 * Creation: Octobre 2017
 * But: Gestion principale du maitre
 * Lance les deux threads (multicast et point a point)
 */
package synchrotime;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SynchroTime {

   public static void main(String[] args) {
      try {
         Thread multicast = new Thread(new MulticastManager(Utils.getAdressMulticast(), Utils.getPortMulticast()));
         Thread communication = new Thread(new MessageManager(Utils.getPortPointToPoint()));

         multicast.start();
         communication.start();

      } catch (SocketException ex) {
         Logger.getLogger(SynchroTime.class.getName()).log(Level.SEVERE, null, ex);
         System.err.println("Thread not created");
      }
   }
}
