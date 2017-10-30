/**
 * Fichier: Utils.java
 * Auteurs: Nadir Benallal
 * Creation: Octobre 2017
 * But: Configuration du maitre
 * ATTENTION: doit correspondre a celle des esclaves!!!!
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

   public static byte getSync() {
      return SYNC;
   }

   public static byte getFollowUp() {
      return FOLLOW_UP;
   }
   
   public static byte getDelayRequest(){
      return DELAY_REQUEST;
   }
   
   public static byte getDelayResponse(){
      return DELAY_RESPONSE;
   }

   public static int getK() {
      return K;
   }

   public static int getPortMulticast() {
      return PORT_MULTICAST;
   }

   public static int getPortPointToPoint() {
      return PORT_POINT_TO_POINT;
   }

   public static String getAdressMulticast() {
      return ADRESS_MULTICAST;
   }

   public static byte[] getTimeByByteTab(long syncSent) {
      byte[] buffer = new byte[8];
      for (int i = 0; i < 8; i++) {
         buffer[i] = (byte) (syncSent & 0xFF);
         syncSent >>= 8;
      }
      return buffer;
   }
}
