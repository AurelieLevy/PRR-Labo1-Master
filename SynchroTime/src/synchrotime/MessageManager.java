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
   private int socketNbr;
   private String name;
   private DatagramSocket socket;
   private boolean running;
   
   //le port du master doit etre fixe, pas celui des esclaves
   public MessageManager(int port) throws SocketException
   {
      this.socketNbr = port;
      socket = new DatagramSocket(port);
      this.running = true;
   }

   @Override
   public void run()
   {
      byte DELAY_REQUEST  = 0X03,
           DELAY_RESPONSE = 0X04;
      
        while (running)
        {
            byte[] buffer = new byte[256];
            InetAddress address;
            try
            {        
               DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
               System.out.println("Recieving...");

               socket.receive(packet);
               byte[] recievedMessage = packet.getData();

               long messageTime = System.currentTimeMillis();
               System.out.println("Message Recieved!");

               address = packet.getAddress();
               name = address.getHostName();
               socketNbr = packet.getPort();

               if (recievedMessage[0] == DELAY_REQUEST)
               {
                  buffer = new byte[10];
                  buffer[0] = DELAY_RESPONSE;
                  buffer[1] = recievedMessage[1];
                  
                  byte[] send = Utils.getTimeByByteTab(messageTime);

                  for(int i = 0; i < send.length; i++){
                     buffer[i+2] = send[i];
                  }
                  
                   /*buffer = new byte[] 
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
                   };*/

                  packet = new DatagramPacket(buffer, buffer.length, address, socketNbr);
                  socket.send(packet); 
                  System.out.println(DELAY_RESPONSE + " sent!");
               }

            } catch (IOException ex)
            {
               Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
               //TODO recieve et send
            }
        }
      
      socket.close();
    }
   
   public void stop()
   {
       running = false;
   }
   
   public boolean isRunning()
   {
       return running;
   }
}
