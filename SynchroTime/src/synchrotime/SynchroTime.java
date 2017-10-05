package synchrotime;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SynchroTime
{

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args)
   {      
      /*try
      {
         //Thread communication = new Thread(new MessageManager(2222, "MSI"));
         Thread multicast = new Thread(new MulticastManager("224.0.0.1", 2223));
         //communication.start();
         multicast.start();
      } catch (SocketException ex)
      {
         Logger.getLogger(SynchroTime.class.getName()).log(Level.SEVERE, null, ex);
         System.out.println("Thread not created");
      }*/
      
      Thread multicast = new Thread(new MulticastManager("239.10.10.1", 2223));
      multicast.start();
      try
      {
         multicast.join();
      } catch (InterruptedException ex)
      {
         Logger.getLogger(SynchroTime.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   
}
