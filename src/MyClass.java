import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



public class MyClass {

	
	
	
	public static void main(String[] args) {
		
		BlockingQueue<byte[]> dataQueue = new LinkedBlockingQueue<byte[]>();
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		
		for(int i=1;i<=100;i++)
			map.put(i, 0);
		
		//try {
		//	process(new File("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\testdata.txt"));
		Filer.GetMemory();
		File k = new File("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\testdata[Actual].txt");
		
		try (RandomAccessFile data = new RandomAccessFile(k, "r")) {
			double d = data.length()/100;
			double parts = (d/25);
			for(int g=0;g<25;g++)
			{
					long start = (long)(g*parts);
					long end = (long) (start+parts)-1;
			     Thread readThread = new Thread(new ParallelReader(dataQueue,k,start,end,100,map),"Reader-"+g);
			     readThread.start();
			}

		   //  Thread readThread = new Thread(new ParallelReader(dataQueue,k,200,700,100,map),"Reader-1");
		     //readThread.start();
		} catch (Exception e)
		{
			
		}

		
	     
	 //    Thread indexThread = new Thread(new ParallelIndexer(dataQueue,map,100),"Indexer-1");
	 //    indexThread.start();

	     
		
			/*File k = new File("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\test.txt");
			 try (RandomAccessFile data = new RandomAccessFile(k, "rw")) {
				 
				
			      byte[] myBytes = new byte[5];
			    //  final long startTime = System.currentTimeMillis();
			      String kk = "Umer";
			      
			      myBytes = kk.getBytes();
			      
			      insert("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\test.txt",2,myBytes);
			      
			      data.close();
			      System.out.println("Done");
			  } catch (Exception e)
			 {
				  System.out.println(e.getMessage());
			 }
			 	*/
	}

			
			      
			   /*   for (long i = 0, len = data.length() / 100; i < len; i++) {
			        data.readFully(myBytes);
			       System.out.println(new String(myBytes,"UTF-8"));
			      }
			      long endTime = System.currentTimeMillis();
			      
			      System.out.println("Total execution time: " + (endTime - startTime)  + " miliseconds");
			      System.out.println("Records: "+data.length());
			      
			    
	//	} catch (Exception e)
	///	{
			
		//}
		*/
		
	
		
		
	
	
	

}
