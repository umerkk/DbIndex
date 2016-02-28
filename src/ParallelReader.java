import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;

public class ParallelReader implements Runnable {

	BlockingQueue<byte[]> dataQueue;
	long startOffset;
	long endOffset;
	int byteSize;
	File file;
	byte[] myBytes;
	HashMap<Integer,Integer> hashmap;
	String opFileName;



	public ParallelReader(BlockingQueue<byte[]> e,File file, long startOffset, long endOffset, int byteSize,HashMap<Integer,Integer> _hash)
	{
		this.dataQueue = e;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.byteSize = byteSize;
		this.file = file;
		myBytes = new byte[byteSize];
		this.hashmap = _hash;
		this.opFileName = "C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\DenseIndex.txt";

	}
	public long CalculateHashMapOffset(int key)
	{
		int total=0;
		for(int k=key;k>0;k--)
		{
			total+=hashmap.get(k);
		}


		return total;
	}
	public void run()
	{
		try (RandomAccessFile data = new RandomAccessFile(file, "r")) 
		{

			data.seek(startOffset);
			
			long mulval = (String.valueOf(data.length())).length();
			//System.out.println("Length: "+ data.length()+ "|| Array: "+ mulval);
				
			

			

			for (long i = 0, len = data.length() / byteSize; i < len; i++) {
				if(data.getFilePointer() == endOffset)
					break;
				long po = data.getFilePointer();
				data.readFully(myBytes);
				
				
				byte[] ab = new byte[(int)mulval];
				long endIndex = (String.valueOf(data.getFilePointer())).length();
				
			
				for(int s=0;s<mulval;s++)
				{
					try {
						String g = String.valueOf(po).substring((int)s, s+1);
						
						ab[s] = g.getBytes()[0];
					} catch (Exception e)
					{
						ab[s] = ' ';
					}
				}
				
			//	System.out.println(new String(ab,"UTF8"));
			
				//dataQueue.put(myBytes);

				String k= new String(myBytes,"UTF-8");
				int age = Integer.parseInt(k.substring(39, 41));

				synchronized(hashmap)
				{

					int tmpVal = hashmap.get(age);
					tmpVal++;
					long wOffset = CalculateHashMapOffset(age)*mulval;
					
					hashmap.put(age, tmpVal);
					//System.out.println(hashmap.get(age));
					System.out.println(wOffset);

					try {	

						RandomAccessFile r = new RandomAccessFile(new File(opFileName), "rw");
						synchronized(r)
						{
							RandomAccessFile rtemp = new RandomAccessFile(new File(opFileName + "~"), "rw");
							long fileSize = r.length();
							FileChannel sourceChannel = r.getChannel();
							FileChannel targetChannel = rtemp.getChannel();
							sourceChannel.transferTo(wOffset, (fileSize - wOffset), targetChannel);
							sourceChannel.truncate(wOffset);
							r.seek(wOffset);
							r.write(ab);
							long newOffset = r.getFilePointer();
							targetChannel.position(0L);
							sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - wOffset));
							sourceChannel.close();
							targetChannel.close();
						}
					} catch (Exception e)
					{
						System.out.println(e.getMessage());

					}





					System.out.println(new String(myBytes,"UTF-8"));


				}


				// System.out.println(dataQueue.size());
				//System.out.println(new String(myBytes, "UTF-8"));
				//System.out.println(data.getFilePointer());


			}
			// System.out.println(CalculateHashMapOffset(87));

			//System.out.println(dataQueue.size());
			Filer.GetMemory();



		} catch (Exception e)
		{
			System.out.println(e.getMessage());

		}


	}
	



}
