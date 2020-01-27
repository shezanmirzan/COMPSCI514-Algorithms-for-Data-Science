import org.rocksdb.RocksDB;

import java.io.*;
import java.util.*;
import org.rocksdb.RocksDBException;
import org.rocksdb.Options;
public class Key_value_Store {
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println(System.getProperty("user.dir"));
		Scanner inFile1 = new Scanner(new File("../../../CS532_ASSIGNMENT_1/Spark/output_ID/part-00000"));
	    List<String> temps = new ArrayList<String>();
	    while (inFile1.hasNext()) {
	      String token1 = inFile1.next();
	      temps.add(token1);
	    }
	    inFile1 = new Scanner(new File("../../../CS532_ASSIGNMENT_1/Spark/output_ID/part-00001"));
	    while (inFile1.hasNext()) {
		      String token1 = inFile1.next();
		      temps.add(token1);
		    }
	    inFile1.close();

	    String[] tempsArray = temps.toArray(new String[0]);
	    String[] key = temps.toArray(new String[0]);
	    String[] value = temps.toArray(new String[0]);
	    for (int i=0;i<tempsArray.length;i++) {
	       key[i]=tempsArray[i].split(",")[0].substring(1);
	       int len=key[i].length();
	       value[i]= tempsArray[i].substring(len+2,tempsArray[i].length()-1);
	    }
   RocksDB.loadLibrary();

   try(final Options options=new Options().setCreateIfMissing(true))
   {
	   try(final RocksDB db= RocksDB.open(options,"./output/")){
			for(int i=0;i<tempsArray.length;i++) {
			
			try {
					db.put(key[i].getBytes(),value[i].getBytes());
					System.out.println(value[i]);
			}
			catch(RocksDBException e) {
				System.out.print(e);
			}
	   }   
   }catch(RocksDBException e) {
   }
   }
	}
}
