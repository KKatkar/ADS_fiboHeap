
import java.io.*;
import java.util.*;

public class HashtagCounter {


	//------------------------------------main method------------------------------------------------------
	public static void main(String[] args) throws IOException
	{
		int key;
		String hashtag;
		boolean outputType = false; // determines where result will be displayed

		Hashtable<String, FiboNode> hTable=new Hashtable<String,FiboNode>(); // contains all the entries of existing nodes as <hashtag, node>
		
		String line; // variable to contain readLine strings
		
		MaxFibHeap heap = new MaxFibHeap(); // initialize Max Fibonacci Heap
		
		//Initialize Readers and Writers
		BufferedReader br = null;
		BufferedWriter bw = null;
		FileWriter output = null;
		FileReader input = null;

		if (args.length==2)
		{
		  outputType = true; //output will be saved in an output file with name specified in args[1]
		  output = new FileWriter(args[1]); //output filename
		  input = new FileReader(args[0]); // input filename
		}else if(args.length == 1) 
		{
			outputType = false; // output will be displayed on console
			input = new FileReader(args[0]);  // input filename
		}
		else
		{
			System.out.println("Please input correct number of arguments");
			System.exit(0);
		}
		

		try {
		// define reader and writer
		br = new BufferedReader(input);
		
		if(outputType) {
			bw = new BufferedWriter(output);
		}
		
		
		 while((line = br.readLine())!=null) {
				if (line.toLowerCase().equals("stop")) { //terminating condition
					break;
					
				}
				else if(line.charAt(0)=='#') { //identifies that a hashtag has been read
					int space = line.indexOf(" ");
					hashtag = line.substring(1,space);
					String strKey = line.substring(space+1).trim();
					key = Integer.parseInt(strKey); // frequency value of the hashtag
					
					if(hTable.containsKey(hashtag)) {
						int increasedKey=hTable.get(hashtag).key + key;
						heap.increaseKey(hTable.get(hashtag), increasedKey);
						
					}else {
						FiboNode n1=new FiboNode(hashtag, key);
					 	heap.insert(n1);
						hTable.put(hashtag,n1);
					}
					
				}else if(Character.isDigit(line.charAt(0))) //identifies if line starts with digit
				{
					int numMaxOuts = Integer.parseInt(line); 
					ArrayList<FiboNode> rNodes = new ArrayList<FiboNode>(numMaxOuts); //initialize array list to maintain removed nodes
					
					for(int i=0; i< numMaxOuts; i++)
				{
				
					FiboNode n2 = heap.removeMax();
					hTable.remove(n2.hashtag);
					
					FiboNode rNode=new FiboNode(n2.hashtag,n2.key);
					rNodes.add(rNode); // add removed nodes into the ArrayList
					
					if(outputType)
					{
						if(i < numMaxOuts-1)
					    {
						  bw.write(n2.hashtag+",");
					     }
					   else
					   {
						 bw.write(n2.hashtag);
					   }
					
				    }else{
				    	if(i < numMaxOuts-1)
					    {
						  System.out.print(n2.hashtag+",");
					     }
					   else
					   {
						 System.out.print(n2.hashtag);
					   }  
				    }
				}
				
				for( int k=0;k<rNodes.size();k++)
				{
					heap.insert(rNodes.get(k)); //insert removed Nodes back into the heap
					hTable.put(rNodes.get(k).hashtag, rNodes.get(k)); //update hash table
				}
				
				if( outputType)	
		        	 {
		        	 bw.write("\n");
		        	 }
			        
		         else
					{
						System.out.print("\n");
					}
				
			}
			
		  }
		 

		  
		} catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
		    br.close();
		    if(outputType) {
			    bw.flush();
			    bw.close();
		    }
		    	}
			
		}
		  
		

}