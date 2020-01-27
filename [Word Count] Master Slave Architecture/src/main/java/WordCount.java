import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.concurrent.TimeUnit;
class Multithreading extends Thread
{
    WordCount inps;
    int turn;
    public Multithreading(WordCount C,int i)
    {
        inps=C;
        turn=i;
    }
    public void run()
    {
            try
            {
                if(turn==1)
                inps.createWorker();
                if(turn==0)
                {
                    int f=0;
                    while(f==0)
                    {
                         inps.getActiveProcess();
                          System.out.print(inps.flag_work);
                         if(inps.flag_work==true)
                            break;
                     }
                     if(inps.flag_work==true)
                     {
                        System.out.println("flag_work_done");
                        inps.mergefiles();
                     }
                }
            }
            catch(Exception E)
            {
                System.out.println(E);
            }
      }
};
public class WordCount implements Master {
    int workerNum;
    List<Integer>port_id = new ArrayList<Integer>();
    ServerSocket server_socket = null;
    PrintStream result;
    int file_index=0;
    List<String>files = new ArrayList<String>();
    List<String>files_temp = new ArrayList<String>();
    int currentport = new Random().nextInt(1000+1)+5000;
     int worker_num=0,index=0;
    List<Integer>port_id_tmp = new ArrayList<Integer>();
    static boolean flag_work=false;
    Map<Integer, Process> active_processes = new HashMap<>();
	Map<Integer,String> assign= new HashMap<>();
	boolean flag=true;
	static boolean flag_nostream=false;
	LinkedHashMap<String, Integer> sorted = new LinkedHashMap<>();
    public WordCount(int workerNum, String[] filenames) throws IOException {
        this.workerNum = workerNum; 
        if(filenames.length==0)
		    flag_nostream=true;
        for (String filename : filenames) 
        {
            this.files.add(filename);
            this.files_temp.add(filename);
        }
    }

    public void setOutputStream(PrintStream out) {
        this.result=out;

    }

    public static void main(String[] args) throws Exception {
        /*WordCount wc = new WordCount(1, a);
        wc.run();*/
    }

    public void run() 
    {
        try {
                for(int i=0;i<this.workerNum;i++)
                {
                    this.createWorker();
                }
		for(int i=0;i<port_id.size();i++)
		{
			port_id_tmp.add(port_id.get(i));
		}
                Multithreading pg= new Multithreading(this,0);
                pg.start();
                pg.join();
            }
        catch(Exception E) 
        {
                System.out.print(E);
        }
        System.out.print("WordCount_ends");
        }

    public Collection<Process> getActiveProcess() 
    {
        Socket socket = null;
        DataInputStream input = null;
        DataOutputStream out = null;
        String lines;
        for(index=0; index<port_id_tmp.size(); index++)
        {
		
	try{	
                try
                {
                   
                    System.out.println("Master connect to " + port_id.get(index));

                    socket = new Socket("localhost", port_id.get(index));
                    System.out.println("Master connected to " + port_id.get(index));
                    out = new DataOutputStream(socket.getOutputStream());

                    out.writeUTF("Hello");

                    input = new DataInputStream(
                            new BufferedInputStream(socket.getInputStream()));

                    lines = input.readUTF();
                    System.out.println(lines);
                    if(lines.equalsIgnoreCase(String.valueOf(port_id_tmp.get(index))+"end"))
                    {
                    	handle_work_end();
                    }
                    else if(lines.equals(String.valueOf(port_id_tmp.get(index))))
                    {
                        out.writeUTF("Over");
                    }
                    System.out.println("after work assigned");

                }
                catch(UnknownHostException u)
                {
                    System.out.println(u);
                }
                catch(IOException io)
                {
                    System.out.println("process not responding, new process building");
                    
                    //Remove port mappings
                	int port = port_id_tmp.get(index);
                	port_id_tmp.remove(index);
                	port_id.remove(index);
                    
                    //remove port to process mapping
                    this.active_processes.remove(port);
                    
                    //return file to list of unprocessed files
                	String file= this.assign.get(port);
                	this.files.add(file);
                	port_id_tmp.add(currentport);
                	this.createWorker();
                	//System.out.println("io"+io);
                }
                System.out.println("after writing");
                try
                {
                    if(input != null)
                    {
                        input.close();
                    }
                    if(out != null)
                    {
                        
                            out.close();
                    }
                    if (socket != null)
                    {
                        socket.close();
                    }
                }
                catch(IOException io)
                {
                    System.out.println("Socket closing " + io);
                }

            } 
            catch (Exception e) 
            {
                System.out.println(e);
                e.printStackTrace();
            }
       }
       try 
       {
            TimeUnit.SECONDS.sleep(3);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        List<Process> active = new LinkedList<>();
            for (int port: this.port_id_tmp)
            {
                active.add(this.active_processes.get(port));
            }
        
		return active;
}
public void handle_work_end(){
        try{
                if(files.size()>0)
                {
                    System.out.print("do new file");
                    System.out.println(files.get(file_index));
                    int port_num = port_id_tmp.get(index);
                    String port = String.valueOf(port_num);
                    String[] commands = {"java", "-cp", "./src/main/java/", "Client", port, files.get(file_index)};

                    ProcessBuilder pb = new ProcessBuilder(commands);  // creating the process         
                    Process process = pb.start();

                    assign.remove(port_num);
                    assign.put(port_num, files.get(file_index));
                    files.remove(file_index);

                }
                else
                {
                    //out.writeUTF("NO");
                    worker_num++;
                    System.out.print(index+"no new file");
                    port_id_tmp.remove(index);
                    index--;
                }
                System.out.println(port_id_tmp.size());

            //port_id_tmp.remove(i);
                if(port_id_tmp.size()==0)
                {
                    System.out.println("flag_work_done_partial");
                    this.flag_work=true;
                }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    
    }
    public void mergefiles()
    {
        //System.out.println("here in merge files");
        LinkedHashMap<String, Integer> count = new LinkedHashMap<>();
        for(int i=0;i<files_temp.size();i++)
        {
  		
            //System.out.println("here in merge files for");
        	//int index = files_temp.get(i).lastIndexOf("\\");
			//System.out.println(index);
        	System.out.println(files_temp.get(i));
            File file = new File(files_temp.get(i)+"op.txt");
            Scanner sc;
            try {
                //System.out.println("here in merge files inside try");
                sc = new Scanner(file);
                sc.useDelimiter(" ");
                while(sc.hasNextLine())
                {
                    //System.out.println("Inwhile ");
                    List<String> arr = new ArrayList<String>();
                    arr=Arrays.asList(sc.nextLine().replaceAll("[{} ]", "").replaceAll("[=,]", " ").split(" "));
                    //System.out.println(arr.get(0));
                    for(int j=0;j<arr.size()-1;j=j+2) 
                    {
                        System.out.println(arr.get(j));
                        if(arr.get(j).length()!=0 && arr.get(j+1).length()!=0)
                        {
                            if (count.containsKey(arr.get(j))) 
                            {
                                int c = count.get(arr.get(j));
                                count.remove(arr.get(j));
                                count.put(arr.get(j),c + Integer.valueOf(arr.get(j+1)));
                            }
                            else 
                            {
                                count.put(arr.get(j),Integer.valueOf(arr.get(j+1)));
                            }
                        }
                    }
                }
            }
            catch(Exception E) 
            {
                    System.out.println(E);
            }
        }
        //System.out.println(count);
        count.entrySet()
         .stream()
         .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
         .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
        System.out.println(sorted);
       PrintWriter writer;
		try {
			writer = new PrintWriter("results.txt", "UTF-8");
			Iterator it = sorted.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
					writer.println(pairs.getValue() + " : " + pairs.getKey());
					this.result.println(pairs.getValue() + " : " + pairs.getKey());
			}
		   writer.close();
		}
        catch(Exception e)
		{
			System.out.println(e);
		}
    }

    public void createWorker() throws IOException {
       try
        {
            int port = this.currentport++;
            this.port_id.add(port);
	        String b,filename;
	        if(flag_nostream==false)
	        {

            	filename= files.get(this.file_index);
                assign.put(port, filename);
                files.remove(file_index);
            
                b = String.valueOf(port); 
                System.out.println("createWorker start, with a port: " + b + filename);
	        }
	        else
	        {
                filename="NO";
                b=String.valueOf(port);
	        }
            String[] commands = {"java", "-cp", "./src/main/java/", "Client", b,filename};
            ProcessBuilder pb = new ProcessBuilder(commands);  // creating the process         
            Process process = pb.start(); // starting the process 
            
            this.active_processes.put(port, process);
            
            /*if(flag==true)
            {	
                flag=false;
                TimeUnit.SECONDS.sleep(3);
                System.out.println("killing");
                process.destroy();
            }*/
            System.out.println("createWorker end");
        }
        catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

    }
}

