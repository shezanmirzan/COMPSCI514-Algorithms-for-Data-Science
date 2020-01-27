import java.io.*;  
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


import java.lang.*;
import java.lang.management.ManagementFactory;



class Multithreading_client extends Thread
{
      int port;
      ServerSocket server_socket=null;
      Client c;
      public Multithreading_client(int port, ServerSocket server_socket, Client c)
      {
         this.port = port;
         this.server_socket=server_socket;
         this.c=c;
      }
      public void run()
      {
        try{

            PrintStream o = new PrintStream(new File("new_file.txt"));
       	    System.setOut(o);
            while(c.get_flag()==0)
            {
                System.out.println("a_flag"+c.get_flag());	
               Socket socket = server_socket.accept();
               DataInputStream in =null;
               DataOutputStream out =null;
           
           
              in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
              out = new DataOutputStream(socket.getOutputStream());
              String line = "";

              // reads message from Master until "Over" is sent
              while (!line.equals("Over"))
              {
                  try
                  {
                      line = in.readUTF();   
	                    System.out.println(line);                  
                      if(line.equals("Hello"))
                       {
                          try
                          {  // sends own port number to the Master's port  
                              System.out.println(port);
	                        	out.flush();
                              out.writeUTF(String.valueOf(port));
                          }
                          catch(IOException i) { System.out.println(i);}
                       }
                  } 
                  catch(IOException i) { System.out.println(i);}
              }
       
           
 
            // close connection
            socket.close();
            in.close();
            out.close();
        }
        }
        catch(UnknownHostException u) { System.out.println(u); }
        catch(IOException i) { System.out.println(i);}
}
};
public class Client
{  
	static int work_flag=0;
	public void set_flag(int value)
	{
		work_flag=value;
	}
	public void word_frequency(String Filename, int port)	
	{
		
		File file = new File(Filename);
	   Scanner sc;
		try {
            sc = new Scanner(file);
            sc.useDelimiter(" ");


        //Insert words in a map
        Map<String, Integer> count = new HashMap<String, Integer>();
          while(sc.hasNextLine())
          {

              List<String> arr = new ArrayList<String>();
              arr=Arrays.asList(sc.nextLine().split(" "));

             for(int i=0;i<arr.size();i++) 
             {
                if(arr.get(i).length()!=0 || arr.get(i)!="  ") 
                {
                   if (count.containsKey(arr.get(i))) 
                   {

                      int c = count.get(arr.get(i));
                      count.remove(arr.get(i));
                      count.put(arr.get(i),c + 1);

                   }

                   else 
                   {
                     count.put(arr.get(i), 1);
                   }
                 }
             }
          }
               LinkedHashMap<String, Integer> sorted = new LinkedHashMap<>();                                                          count.entrySet()                                                                                                         .stream()                                                                                                               .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))                                                          .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));	    // Return list with high freq words at the top
           LinkedHashMap<String, Integer> sorted_fin = new LinkedHashMap<>();
           sorted.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .forEachOrdered(x -> sorted_fin.put(x.getKey(), x.getValue()));

           //System.out.println("Reverse Sorted Map   : " + sorted_fin);


           PrintWriter writer;
         try 
         {
             writer = new PrintWriter(new File(Filename+"op.txt"));
             writer.println(sorted_fin);
              writer.close();
              this.set_flag(1);
         } 
         catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("freq_cnt"+e);
         }
		} 
      catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("freq_filenot found"+e);
		}
	  
	}
	public int get_flag()
	{
		return(work_flag);
	}
    public static void main(String[] args) {
        int port = Integer.valueOf(args[0]);
        String Filename= args[1];
        try {
        	PrintStream print= new PrintStream(new File("file"+String.valueOf(port)+".txt"));
        	System.setOut(print);
        	ServerSocket server_client = new ServerSocket(port);
        	Client c= new Client();
         System.out.println("Client thread before with port " + String.valueOf(port)+String.valueOf(c.get_flag()));
         Multithreading_client c_thread = new Multithreading_client(port,server_client,c);
         c_thread.start();
         if(Filename!="NO")
          {
              c.word_frequency(Filename,port);
          }
	        System.out.println("Client thread before with port " + String.valueOf(port)+String.valueOf(c.get_flag()));
           System.out.println("Client thread interuppted");
           Socket socket= server_client.accept();
           DataOutputStream out =null;
           DataInputStream in=null;
           out = new DataOutputStream(socket.getOutputStream());
           in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); 
           out.writeUTF(String.valueOf(port)+"end");
           out.close();
           in.close();
           socket.close(); 
            c_thread.join();
            System.out.println("Client thread after join");
           server_client.close(); 
        }  catch (Exception e1) {
            // TODO Auto-generated catch block
           System.out.println("here in end"+e1);
        }

    }
}
