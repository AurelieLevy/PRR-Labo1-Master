/**
 * Fichier: SyncroTime.java
 * Auteurs: Nadir Benallal, Aurelie Levy
 * Creation: Octobre 2017
 * But: Gestion principale du maitre
 * Lance les deux threads (multicast et point a point)
 *
 * Commentaires: Nous avons separe le laboratoire en deux parties afin d'avoir
 * un maitre et un esclave bien distincts. Cependant, dans les deux programmes,
 * vous trouverez un fichier Utils.java. Il s'agit des constantes en commun pour
 * les deux. Pour le bon fonctionnement du protocole entier, il faut s'assurer
 * que les deux fichiers correspondent.
 * Cela permet, dans le cas d'un changement de maitre par exemple, de pouvoir
 * modifier uniquement ce qui est necessaire sans devoir obligatoirement toucher
 * au code.
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
