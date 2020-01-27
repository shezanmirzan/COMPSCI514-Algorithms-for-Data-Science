import org.rocksdb.RocksDB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import org.rocksdb.RocksDBException;
import org.rocksdb.util.*;
import org.rocksdb.Options;
public class id_url_map {

		public static void main(String[] args) throws FileNotFoundException {
			/*System.out.println("enter the path of id_url_pairs");
			Scanner in = new Scanner(System.in);
	        String s = in.nextLine();
	        System.out.println("enter the path to store db");
	        Scanner in2 = new Scanner(System.in);
	        String s_db = in2.nextLine();*/
			Scanner inFile1 = new Scanner(new File("./src/id_URL_pairs.txt"));

		    
		    List<String> temps = new ArrayList<String>();

		    // while loop
		    while (inFile1.hasNext()) {
		      // find next line
		      String token1 = inFile1.next();
		      temps.add(token1);
		    }
		    inFile1.close();

		    String[] tempsArray = temps.toArray(new String[0]);
		    String[] key = temps.toArray(new String[0]);
		    String[] value = temps.toArray(new String[0]);
		    for (int i=0;i<tempsArray.length;i++) {
		       key[i]=tempsArray[i].split(",")[0];
		       int len=key[i].length();
		       //System.out.println(len);
		       value[i]= tempsArray[i].substring(len,tempsArray[i].length());
		       //value[i]=value[i].split(",")[1];
		       System.out.println(key[i]);
		    }
	   RocksDB.loadLibrary();

	   try(final Options options=new Options().setCreateIfMissing(true))
	   {
		   try(final RocksDB db= RocksDB.open(options,"./op_map/")){
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
