/**
 * Fichier: Utils.java
 * Auteurs: Nadir Benallal
 * Creation: Octobre 2017
 * But: Configuration du maitre
 * ATTENTION: Les valeurs des variables de cette classe doivent
 * correspondre a celles des esclaves!!!!
 */
package synchrotime;

public class Utils {

   private final static String ADRESS_MULTICAST = "239.10.10.1";
   private final static int PORT_MULTICAST = 2223;
   private final static int PORT_POINT_TO_POINT = 2222;
   private final static int K = 2;

   private final static byte SYNC = 0x01,
           FOLLOW_UP = 0x02,
           DELAY_REQUEST = 0X03,
           DELAY_RESPONSE = 0X04;

   /**
    * Permet d'obtenir l'identifiant en byte du message SYNC pour le detecter
    *
    * @return le byte correspondant
    */
   public static byte getSync() {
      return SYNC;
   }

   /**
    * Permet d'obtenir l'identifiant en byte du message FollowUp pour le
    * detecter
    *
    * @return le byte correspondant
    */
   public static byte getFollowUp() {
      return FOLLOW_UP;
   }

   /**
    * Permet d'obtenir l'identifiant en byte du message delayRequest pour le
    * detecter
    *
    * @return le byte correspondant
    */
   public static byte getDelayRequest() {
      return DELAY_REQUEST;
   }

   /**
    * Permet d'obtenir l'identifiant en byte du message delayResponse pour le
    * detecter
    *
    * @return le byte correspondant
    */
   public static byte getDelayResponse() {
      return DELAY_RESPONSE;
   }

   /**
    * Permet d'obtenir la variable k determinant l'attente
    *
    * @return int
    */
   public static int getK() {
      return K;
   }

   /**
    * Permet d'obtenir le port du multicast
    *
    * @return le port multicast
    */
   public static int getPortMulticast() {
      return PORT_MULTICAST;
   }

   /**
    * Permet d'obtenir le port du point a point
    *
    * @return le port point a point
    */
   public static int getPortPointToPoint() {
      return PORT_POINT_TO_POINT;
   }

   /**
    * Permet de recuperer l'adresse multicast
    *
    * @return l'adresse de multicast
    */
   public static String getAdressMulticast() {
      return ADRESS_MULTICAST;
   }

   /**
    * Permet de transformer le long representant le temps en tableau de byte
    * (taille: 8)
    *
    * @param syncSent long a transformer
    * @return tableau de byte
    */
   public static byte[] getTimeByByteTab(long syncSent) {
      byte[] buffer = new byte[8];
      for (int i = 0; i < 8; i++) {
         buffer[i] = (byte) (syncSent & 0xFF);
         syncSent >>= 8;
      }
      return buffer;
   }
}
