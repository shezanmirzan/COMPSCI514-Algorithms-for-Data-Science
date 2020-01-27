import org.rocksdb.RocksDB;
import java.io.IOException;
import java.io.FileWriter;
import org.rocksdb.RocksDBException;
import org.rocksdb.Options;
public class test {
	public static String str_map(String args,String path) throws IOException {	
		FileWriter writer = new FileWriter(path+"/results.txt");
		String s_s=args;
        writer.write(s_s);
        writer.append("\n");
        s_s=s_s.toLowerCase();
        s_s=new String(s_s);
	if(s_s.split(" ").length>1)
        {
        	 s_s=s_s.split(" ")[0];
        }	
        System.out.println("You entered string "+s_s);       
   RocksDB.loadLibrary();
   String op=""; 
   
   try(final Options rosksoptions = new Options().setCreateIfMissing(true))
   {
	try(final RocksDB db_out = RocksDB.open(rosksoptions,path+"/output/")){
			byte[] values = db_out.get(s_s.getBytes());
			System.out.print(db_out.get(s_s.getBytes()));
			String url=new String(values);
			String[] out = url.split(",");
	  try(final RocksDB db2= RocksDB.open(rosksoptions,path+"/op_map/")){
		  for(int i=0;i<out.length;i=i+2) {
		 	String s=out[i].substring(1); 
		    byte[] value2=db2.get(s.getBytes());
			String url2=new String(value2);
			url2=url2.substring(1);
			writer.append(url2);
			op+=url2;
			op+="\n";
			writer.append("\n");
			System.out.println(url2);
	      }
		  writer.close();
		 }
	   }
   }catch(RocksDBException e) {
	   System.out.println(e);
   }
   return(op);
   }
}
