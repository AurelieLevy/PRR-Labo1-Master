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
      String message = "Hello from master!";
      byte[] buffer = new byte[256];
      InetAddress address;
      try
      {         
         DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
         socket.receive(packet);
         String recievedMessage = new String(packet.getData());
         System.out.println("Recieved : " + recievedMessage);
         
         address = packet.getAddress();
         String name = address.getHostName();
         int portClient = packet.getPort();
         buffer = message.getBytes();
         packet = new DatagramPacket(buffer, buffer.length, address, portClient);
         socket.send(packet);  
         
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
