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
/*Class implementing Runnable to become a Thread as well.
 * This Class read the Data File in Chunks of specified size
 * and Store the pointers of the specific index in HashMap via
 * Hashing Function.
 * After reading a block, this class Write the buffered index+pointers
 * to the index file and flush the main memory for next chunk.
 * 
 * Author(s):
 * 	Muhammad Umer (40015021)
 * 	Shubham Singh (40004793)
 * 	Shabangi Sheel(40004796)
 * 
 * Version 1.1
 */

public class MyClass {
	public static void main(String[] args) {


		File k = new File("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\testdata[Actual].txt");

		ParallelReader p = new ParallelReader(k);
		p.run();
		//Thread readThread = new Thread(new ParallelReader(k),"Reader");
		//readThread.start();

	}
}
