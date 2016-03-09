import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.util.HashMap;

/* Helper Class that implements static File I/O Methods.
 * 
 * Author(s):
 * 	Muhammad Umer (40015021)
 * 	Muhammad Umer (40015021)
 * 	Shubham Singh (40004793)
 * 	Shabangi Sheel(40004796)
 * 
 * Version 1.1
 */

public class Filer {

	public static int loopV = 0;

	//Helper method to Get Memory information of the JVM for analysis.
	public static void GetMemory()
	{
		//Get the current instance of the program from JVM.
		Runtime runtime = Runtime.getRuntime();
		
		//NumberFormat to format the memory information values for better understanding.
		NumberFormat format = NumberFormat.getInstance();

		StringBuilder sb = new StringBuilder();
		
		//Get MaxMemory that program can hit from the RunTime
		long maxMemory = runtime.maxMemory();
		
		//Get allocated Memory for the instance from runtime.
		long allocatedMemory = runtime.totalMemory();
		
		//Get Free memory of the instance at any given time.
		long freeMemory = runtime.freeMemory();

		sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
		sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
		sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
		sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
		System.out.println(sb);
	}
	
//********************************************************************************************************
	
	//Method to Write Indexes from HashMap to the index Files.
	//This method create or append pointers of each key(age) in a seperate file.
	public static int fileWriteIO = 0;
	public static void writeIndexes(HashMap valueHash)
	{
		
		try { 
			//Loop through the predefined age (given from the project description)
			for(int k=18;k<=99;k++)
			{
				//Check for the specified key (from loop) in the HashMap and proceed with File Write method. 
				if(valueHash.containsKey(k))
				{
					File f = new File("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\Indexes\\"+k+".index");
					
					//If index files does not exists, then create it.
					if(!f.exists())
						f.createNewFile();

					//Get the file pointer of age from the HashMap.
					String s = valueHash.get(k).toString()+",";
					
					//Create Output Stream as BufferedOutPutStream in APPEND Mode and write at the end of File.
					OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\Indexes\\"+k+".index"), StandardOpenOption.CREATE,	StandardOpenOption.APPEND));
					
					//Convert the value into respective byte array.
					byte[] ss = s.getBytes();
					
					//Write to File starting from 0 index of byte array to the length of byte array (FULL WRITE)
					outputStream.write(ss, 0, ss.length);
					fileWriteIO++;
					
					//Flush the OutputStream and write any remaining to the file before we close the stream. 
					outputStream.flush();
					
					//Close the Stream and release any resources to free up memory - Write operation is completed. 
					outputStream.close();
				}
				
			}
			
			
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}

//********************************************************************************************************

	public static int fileAgeRead=0;
	public static int readIndexes(int age,int bbyteSize)
	{
		int byteSize = bbyteSize;
		byte[] myBytes = new byte[byteSize];
		int startSeek=0;
		int totalRecords=0;
		int fileIO=0;
		try { 
			//Create instance of FILE of the specific age.
			File f = new File("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\Indexes\\"+age+".index");
			
			//If the file does not exists, then there are no records of that specific age.
			//Display the message to the user.
			if(!f.exists())
			{
				System.out.println("No Such records exists of age "+age+" in the database.");
				return 0;
			}
			
			RandomAccessFile data = new RandomAccessFile(f, "r"); 
			
			//If Index file length is less than block size, read the whole file in one go.
			if(data.length() < byteSize)
			{
				//Assign the block size and initialize the byte array with specific block size.
				byteSize = (int) f.length();
				myBytes = new byte[byteSize];
			}

			//Calculate the loop iterations based on the chunk size.
			double loopToDo=  (double)data.length()/(double)byteSize;
			
			
			if(loopToDo<1)
			{	loopV = 1; }
			else {
				//Ceil the loop i.e. If loopV is 3.5 then make it run 4 times.
				loopV = (int) Math.ceil(loopToDo);
			}
			
			
			//loop through all records of fetched chunk.
			//iterate through the index file based on the block size till the file is completely read.
			for (long i = 1; i<=loopV; i++) 
			{
				//Calculate available data that is available to be read from the fetched chunk.
				long availableDataToRead = data.length()-(i*byteSize);
				
				//Read the pointers from index file and copy it in bytes array
				data.read(myBytes);
				fileAgeRead++;
				
				//Move the file read pointer to next value and increment the variables accordingly.
				//only applicable if file size is greater than block size.
				data.seek(startSeek+byteSize);
				startSeek+=byteSize;
				
				//Cast the fetched block to string using UTF-8 text encoding and split it by comma.
				//Each pointer in the index file is separated by comma hence using Split function,
				//We are getting the length (count) of the records and adding it in our array.
				totalRecords+= (new String(myBytes,"UTF-8")).split(",").length;
				
			}
			System.out.println("Total # of Disk I/O to fetch the records are: "+fileAgeRead);

			return totalRecords;

		} catch (Exception es)
		{
			System.out.println(es.getMessage());
			return 0;
		}
	}
	
	//Method to read the pointers from the index file of the given age.
	//and then read the data file using the pointers and display the persons data. 
	public static int fileDataIO=0;
	public static void readIndexes(int age,int bbyteSize,File ddataFile)
	{
		//Initialization of variables
		int byteSize = bbyteSize;
		byte[] myBytes = new byte[byteSize];
		File dataFile = ddataFile;
		long startTime = System.currentTimeMillis();
		
		//Variable to hold total number of records of the given age from index file.
		long recordNum=0;
		
		//StartPoint of the fileRead - It will always start from 0.
		int startSeek=0;
		
		int loopV = 1;
		
		try { 
			//Create instance of FILE of the specific age.
			File f = new File("C:\\Users\\Umer-PC\\Desktop\\Concordia\\Advance Database\\Project\\Indexes\\"+age+".index");
			
			//If the file does not exists, then there are no records of that specific age.
			//Display the message to the user.
			if(!f.exists())
			{
				System.out.println("No Such records exists of age "+age+" in the database.");
				return;
			}

			//If the index file exists, open it using RandomAccessFile
			RandomAccessFile data = new RandomAccessFile(f, "r"); 
			
			//If Index file length is less than block size, read the whole file in one go.
			if(data.length() < byteSize)
			{
				//Assign the block size and initialize the byte array with specific block size.
				byteSize = (int) f.length();
				myBytes = new byte[byteSize];
			}

			//Calculate the loop iterations based on the chunk size.
			double loopToDo=  (double)data.length()/(double)byteSize;

			
			
			if(loopToDo<1)
			{	loopV = 1; }
			else {
				//Ceil the loop i.e. If loopV is 3.5 then make it run 4 times.
				loopV = (int) Math.ceil(loopToDo);
			}
			
			
			//loop through all records of fetched chunk.
			//iterate through the index file based on the block size till the file is completely read.
			for (long i = 1; i<=loopV; i++) 
			{
				//Calculate available data that is available to be read from the fetched chunk.
				long availableDataToRead = data.length()-(i*byteSize);
				
				//Read the pointers from index file and copy it in bytes array
				data.read(myBytes);
				fileDataIO++;
				
				//Move the file read pointer to next value and increment the variables accordingly.
				//only applicable if file size is greater than block size.
				data.seek(startSeek+byteSize);
				startSeek+=byteSize;
				
				//Cast the fetched block to string using UTF-8 text encoding and split it by comma.
				//Each pointer in the index file is separated by comma hence using Split function,
				//We are getting all records into the string array.
				String[] records = (new String(myBytes,"UTF-8")).split(",");
				
				//Add the fetched number of pointers to the total number of records variable.
				recordNum+=records.length;
				
				//Read the original Input file using RandomAccessFile class in Read only mode.
				RandomAccessFile dataOriginal = new RandomAccessFile(dataFile, "r"); 
				
				//Loop through all the fetched pointers from the index file and
				//read the 100 bytes from original data file starting from the pointer location.
				for(int g=0;g<records.length;g++)
				{
					//Goto the starting point of the data. recordNum x 100 = exact location of that record in data file.
					dataOriginal.seek(Long.parseLong(records[g])*100);
					
					//Initialize the byte array of 100 bytes - to store 1 person data.
					byte[] len = new byte[100];
					
					//Read the data into byte array.
					dataOriginal.read(len);
					
					//print the output.
					System.out.println(new String(len,"UTF-8"));
				}
				//Close the original input file after the read is complete and free up memory.
				dataOriginal.close();
				
				//For Safe Reading into byteArray - Calculate the available data to read from this Chunk.
				//if Available data is less then byteSize then reAllocate the byteArray to available data length.
				if(availableDataToRead < byteSize)
				{
					byteSize = (int) availableDataToRead;
					myBytes = new byte[byteSize];
				}
			}
			//Print the total number of records of the given age.
			System.out.println("Total # of records: "+recordNum);
			System.out.println("Total # of Disk I/O to fetch the records are: "+fileDataIO);

		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		//long endTime2 = System.currentTimeMillis();
		//double indexCreationTime = ((double)(endTime2 - startTime)/(double)1000);
		//System.out.println("Time elapsed to create index files: " + indexCreationTime + " seconds");

	}

}
