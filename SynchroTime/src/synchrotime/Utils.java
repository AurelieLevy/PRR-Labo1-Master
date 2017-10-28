/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synchrotime;

/**
 *
 * @author aurel
 */
public class Utils {

   static byte[] getTimeByByteTab(long syncSent) {
      byte[] buffer = new byte[8];
      for (int i = 0; i < 8; i++) {
         buffer[i] = (byte) (syncSent & 0xFF);
         syncSent >>= 8;
      }
      return buffer;
      
      /*long messageTime = 0;
      for (int i = 0; i < 8; i++) {
         buffer[i + 2] = (byte) (messageTime & 0xFF);
         messageTime >>= 8;
      }
      return messageTime;*/
   }
}
