package synchrotime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
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
   public void run()
   {
      String message = "Multicast from master!";
      byte[] buffer = message.getBytes();
      
      InetAddress groupe;
      try
      {
         groupe = InetAddress.getByName(IP);
         DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupe, PORT);
         socket.send(packet);
         System.out.println("Message sent!");
      } catch (UnknownHostException ex)
      {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
      } catch(IOException ex)
      {
         Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
