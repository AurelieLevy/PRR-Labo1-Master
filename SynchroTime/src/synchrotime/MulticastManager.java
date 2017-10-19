package synchrotime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MulticastManager implements Runnable
{
   private final String IP;
   private final int PORT;
   private MulticastSocket socket;

   public MulticastManager(String ip, int port)
   {
      this.IP = ip;
      this.PORT = port;
      try
      {
         socket = new MulticastSocket(this.PORT);
      } catch (IOException ex)
      {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public String getIP()
   {
      return IP;
   }

   public int getPORT()
   {
      return PORT;
   }

    @Override
    public void run() {
        byte id             = 0x00;
        byte SYNC           = 0x01,
             FOLLOW_UP      = 0x02,
             DELAY_REQUEST  = 0X03,
             DELAY_RESPONSE = 0X04;
        int  K              = 2;
        while (true) {
            id++;
            byte[] msg = new byte[]{SYNC, id};

            long syncSent = System.currentTimeMillis();

            InetAddress group;
            try {
                group = InetAddress.getByName(IP);
                
                //envoi du message SYNC
                DatagramPacket packet = new DatagramPacket(msg, msg.length, group, PORT);
                socket.send(packet);
                System.out.println("SYNC sent!");
                
                //envoi du message FOLLOW_UP
                msg = new byte[] {
                    FOLLOW_UP,
                    id,
                    (byte)(syncSent >> 28),
                    (byte)(syncSent >> 24),
                    (byte)(syncSent >> 20),
                    (byte)(syncSent >> 16),
                    (byte)(syncSent >> 12),
                    (byte)(syncSent >> 8),
                    (byte)(syncSent >> 4),
                    (byte)(syncSent)
                };
                
                packet = new DatagramPacket(msg, msg.length, group, PORT);
                socket.send(packet);
                System.out.println("FOLLOW_UP sent!");

                //attente
                TimeUnit.SECONDS.sleep(K);
            } catch (UnknownHostException ex) {
                Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
