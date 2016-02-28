import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;

public class Filer {
	
	  private static void ReadFileSequentially(File file, long startOffset, long endOffset, int byteSize) throws IOException {
		  
		//long startTime = System.currentTimeMillis();
		//long endTime = System.currentTimeMillis();
	      
	    //System.out.println("Total execution time: " + (endTime - startTime)  + " miliseconds");
	    //System.out.println("Records: "+data.length());
		  
	  }
	  public static void GetMemory()
		{
			Runtime runtime = Runtime.getRuntime();

			NumberFormat format = NumberFormat.getInstance();

			StringBuilder sb = new StringBuilder();
			long maxMemory = runtime.maxMemory();
			long allocatedMemory = runtime.totalMemory();
			long freeMemory = runtime.freeMemory();

			sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
			sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
			sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
			sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
			System.out.println(sb);
		}
	

}
