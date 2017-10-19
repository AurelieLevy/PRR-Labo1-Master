package synchrotime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageManager implements Runnable
{
   private final int port; //TODO change name (not in french)
   private final String name;
   private DatagramSocket socket;
   
   public MessageManager(int port, String name) throws SocketException
   {
      this.port = port;
      this.name = name;
      socket = new DatagramSocket(port);
   }

   @Override
   public void run()
   {
      byte SYNC           = 0x01,
           FOLLOW_UP      = 0x02,
           DELAY_REQUEST  = 0X03,
           DELAY_RESPONSE = 0X04;
      
      while (true)
      {
        byte[] buffer = new byte[256];
        InetAddress address;
        try
        {         
           DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("recieving...");
           socket.receive(packet);
           byte[] recievedMessage = packet.getData();
           long messageTime = System.currentTimeMillis();
            System.out.println("DATA : " + recievedMessage[0]);
           System.out.println("Message Recieved!");

           address = packet.getAddress();
           String name = address.getHostName();
           int portClient = packet.getPort();

           if (recievedMessage[0] == DELAY_REQUEST)
           {
               buffer = new byte[] 
               {
                   DELAY_RESPONSE,
                   recievedMessage[1],
                   (byte)(messageTime >> 28),
                  (byte)(messageTime >> 24),
                  (byte)(messageTime >> 20),
                  (byte)(messageTime >> 16),
                  (byte)(messageTime >> 12),
                  (byte)(messageTime >> 8),
                  (byte)(messageTime >> 4),
                  (byte)(messageTime)
               };

              packet = new DatagramPacket(buffer, buffer.length, address, portClient);
              socket.send(packet); 
               System.out.println("DELAY_RESPONSE sent!");
           }
         
         socket.close();
         
        } catch (UnknownHostException ex)
        {
           Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
           //TODO
        } catch (IOException ex)
        {
           Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
           //TODO
        }
    }
   }
}
