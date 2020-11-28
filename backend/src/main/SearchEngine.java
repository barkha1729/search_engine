import java.io.*;
import java.lang.*;
import java.util.*;

public class SearchEngine {

	public static void main(String[] args) throws IOException{
		filepath_sample = "IR DataBase\\data\\sample.txt"
		filepath_stop_words = "IR DataBase\\data\\stopwords_large.txt"
		
		
		ArrayList<ArrayList<String>> filestr = new ArrayList<ArrayList<String>>();
		ArrayList<String> ids = new ArrayList<String>(); 
		ArrayList<String> urls = new ArrayList<String>();
		int i=0,iq=0;
		BufferedReader in,in1;
		int stop=0;
		
		// Instantiating an object of the class Porter (custom class built based on Proter's algorithm)
		Porter p = new Porter();
		List<String> lemmas;
		Lemmatization lm = new Lemmatization();
		ArrayList<String> stopw = new ArrayList<String>();
		      in = new BufferedReader(new FileReader(filepath_sample));
		      in1 = new BufferedReader(new FileReader(filepath_stop_words));
		      String line,phrase;
		      while((line=in1.readLine())!=null)
		      {
		    	  stopw.add(line);
		      }
		    while((line = in.readLine())!=null){
		    	//reading line by line from corpus
		    	  line = line.toLowerCase();
		    	  //splitting line using space/multiple space delimiter
		          String[] pair = line.split("\\s+");
		          for (int j = 0; j < pair.length; j++) {
		        	  stop=0;
		        	  if(j==0)
		        	  {
		        		  //first token: title of document
		        		  ids.add(pair[j]);
		        	  }
		        	  if(j==1)
		        	  {
		        		  //second token: url of document
		        		  urls.add(pair[j]);
		        	  }
		        	  //checking if the word is a stop-word.
		        	  else if(j!=0&&j!=1)
		        	  {
		        		  for(int k=0;k<stopw.size();k++)
		        		  {
		        			  if(pair[j]==stopw.get(k))
		        			  {
		        				  stop=1;
		        				  break;
		        			  }
		        		  }
		        		  if(stop==1)
		        		  {
		        			  continue;
		        		  }
		        		  //lemmatising the token
		        		  lemmas = lm.lemmatize(pair[j]);
		        		  pair[j] = lemmas.toString();
		        		  //stemming the token using porter's algoritm
		        		  pair[j] = p.stripAffixes(pair[j]);
		        		  //adding all tokens to 2D arraylist filestr("row of filestr:document||columns of filestr: tokens of that document")
		        		  filestr.add(new ArrayList<String>());
			              filestr.get(i).add(pair[j]);
		        	  }
		        	  //System.out.print(pair[j]+" ");
		        	 
		          }
		          //System.out.println();
		          i++;
		          
		      }
		    
		    //N : no. of documents in corpus
		     int N =48;
		     
		     //"dict" represents the hashtable where all the terms are stored.
		HashMap<String,double[]> dict = new HashMap<String,double[]>();
		
		
		
		//Adding terms to hashtable while traversing all the documents, and simultaneously calculating term frequency w.r.t each document
		for(i=0;i<filestr.size();i++)
		{
			for(int k=0;k<filestr.get(i).size();k++)
			{
				
				double[] temp = dict.get(filestr.get(i).get(k));
				if(temp==null)
				{
					//System.out.println("swetha");
					temp = new double[51+3];
					for(int h=0;h<temp.length;h++)
					{
						temp[h]=0;
					}
					temp[i+2]=1;
					dict.put(filestr.get(i).get(k), temp);
				}
				else
				{
					//System.out.println("barkha");
					temp[i+2]++;
					
					dict.put(filestr.get(i).get(k), temp);
				}
			}
		}
		
		//calculating doc frequency.
		//int[] docFreq = new int[5371];
		
		
		//Calculating document frequency of each word in the hash-table and weight
		double[] doclen = new double[48];
		for (HashMap.Entry<String, double[]> entry : dict.entrySet()) {
		    String key = entry.getKey();
		    double[] value = entry.getValue();
		    int f=0;
		    for(int k=2;k<N+2;k++)
		    {
		    	if(value[k]!=0)
		    	{
		    		f++;
		    	}
		    }
		    value[0] = f;
		    value[1]=Math.log10(N/f);
		    
		    for(int k=2;k<N+2;k++)
		    {
		    	double temp = Math.log10(1+value[k]);
		    	value[k] = temp*value[1];
		    	doclen[k-2] = doclen[k-2] + value[k]*value[k];
		    }
		    
		}
		for(int k=0;k<N;k++)
		{
			doclen[k] = Math.sqrt(doclen[k]);
		}
		
		//Normalising all the values.
		for (HashMap.Entry<String, double[]> entry : dict.entrySet()) {
		    String key = entry.getKey();
		    System.out.println(key);
		    double[] value = entry.getValue();
		    for(int k=2;k<N+2;k++)
		    {
		    	value[k] = value[k]/doclen[k-2];
		    	//System.out.print(value[k]+" ");
		    }
		    //System.out.println();
		    
		}
		System.out.println(" doc processed " + dict.size());
	
	
	
	int once=0;
	//PHRASE QUERY
		BufferedReader qu = new BufferedReader(new FileReader("C:\\Users\\swetha.jvn\\Desktop\\IR DataBase\\query.txt"));
		phrase=qu.readLine();	
		String[] wo = phrase.split("\\s+");
		ArrayList<String> tok = new ArrayList<String>();
        //System.out.println("first word in query: "+wo[0]+"9999");
        for (int j = 0; j < wo.length; j++) {
      	   stop = 0;
      	  //checking for stop words
      		  for(int k=0;k<  stopw.size();k++)
      		  {
      			  if(wo[j]==stopw.get(k))
      			  {
      				  stop=1;
      				  break;
      			  }
      		  }
      		  if(stop==1)
      		  {
      			  continue;
      		  }
      		  //lemmatising tokens
      		  lemmas = lm.lemmatize(wo[j]);
      		  wo[j] = lemmas.toString();
      		  //stemming
      		  wo[j] = p.stripAffixes(wo[j]);
      		  //tok.add(new ArrayList<String>());
	              tok.add(wo[j]);
        }
  	  double len=0;
		for(int k=0;k<tok.size();k++)
		{
			//System.out.print("tok: "+ tok.get(k));
			
			double[] arr = dict.get(tok.get(k));
			if(arr==null)
			continue;
			else
			{arr[N+2]++;
			System.out.println("qq "+ tok.get(k)+"freq "+arr[N+2]);
			}
		}
		System.out.println();
		
		//Normalising the tokens in phrase query
		for (HashMap.Entry<String, double[]> entry : dict.entrySet()) {
		    double[] ar = entry.getValue();
		    double temp = Math.log10(1+ar[N+2]);
	    	ar[N+2] = temp*ar[1];
	    	len=len+ar[N+2]*ar[N+2]; 
	    	}
		
		len =Math.sqrt(len);
	    	for (HashMap.Entry<String, double[]> entry : dict.entrySet()) {
			    double[] ar = entry.getValue();
			    if(ar!=null)
			   ar[N+2] = ar[N+2]/len;
			    if(ar[N+2]!=0)
			    System.out.println("final qt "+entry.getKey()+"nValue : "+ar[N]);
    }
	
	    	
	    	
	    	//cosine product
	    	Pocket cosp[] = new Pocket[N];
	    	for(int h=0;h<N;h++)
	    	{
	    		cosp[h] = new Pocket();
	    		cosp[h].url=urls.get(h);
	    		cosp[h].title=ids.get(h);
	    	}
	    	for(HashMap.Entry<String, double[]> entry : dict.entrySet()){
	    		double[] ar= entry.getValue();
	    		for(int pp=0;pp<N;pp++)
	    			cosp[pp].dotP +=ar[N+2]*ar[pp+2];
	    		}
	    	
	    	
	    	//Retrieving top 10 results using priority queue which implements heap internally.
	    	Comparator<Pocket> comparator = new PocketDotP();
	    	PriorityQueue<Pocket> pq = new PriorityQueue<Pocket>(10,comparator);
	    	
	    	
	    	for(i=0;i<N;i++)
	    	{
	    		System.out.println(cosp[i].title+"--"+cosp[i].url+":- "+cosp[i].dotP+" ");
	    		pq.add(cosp[i]);
	    	}
	    	System.out.println("RESULTS:-");
	    	int results = 10;
	    	int[] topp = new int[10];
	        i=0;
	    	while(results--!=0)
	    	{
	    		Pocket temp = pq.remove();
	    		System.out.println((10-results)+")"+temp.title+"--"+temp.url+":- "+temp.dotP+" ");
	    		topp[i] = Integer.parseInt(temp.title.substring(4));
	    		//System.out.println(topp[i]);
	    	}
	    	
	    	////*******************OUT OF BOX************************************************
	    	
	    	double alpha=1,beta=0.75,gamma=0.15;
	    	double rel[] = new double[dict.size()];
	    	for(HashMap.Entry<String, double[]> entry : dict.entrySet()){
	    		double[] ar= entry.getValue();
	    		double nr=0;
	    		for(int q=0;q<10;q++)
	    		{
	    			ar[N+3]+=ar[topp[q]];
	    		}
	    		ar[N+3]/=10;
	    		for(int r=0;r<ar.length;r++)
	    		{
	    			nr+=ar[r];
	    		}
	    		ar[N+4]=(nr-ar[N+3])/(48-10);
	    			ar[N+5]= (alpha*ar[N+2]) + (beta*ar[N+3])- (gamma*ar[N+4]);
	    			ar[N+2] = ar[N+5];
	    	}
	    	
	    	
	    	for(int h=0;h<N;h++)
	    	{
	    		cosp[h] = new Pocket();
	    		cosp[h].url=urls.get(h);
	    		cosp[h].title=ids.get(h);
	    	}
	    	for(HashMap.Entry<String, double[]> entry : dict.entrySet()){
	    		double[] ar= entry.getValue();
	    		for(int pp=0;pp<N;pp++)
	    			cosp[pp].dotP +=ar[N+2]*ar[pp+2];
	    		}
	    	
	    	
	    	//Retrieving top 10 results using priority queue which implements heap internally.
	    	Comparator<Pocket> comparator1 = new PocketDotP();
	    	PriorityQueue<Pocket> pq1= new PriorityQueue<Pocket>(10,comparator);
	    	
	    	
	    	for(i=0;i<N;i++)
	    	{
	    		//System.out.println(cosp[i].title+"--"+cosp[i].url+":- "+cosp[i].dotP+" ");
	    		pq.add(cosp[i]);
	    	}
	    	System.out.println("RESULTS in 2nd round:-");
	    	int results1 = 10;
	    	//int[] topp = new int[10];
	        i=0;
	    	while(results1--!=0)
	    	{
	    		Pocket temp = pq.remove();
	    		System.out.println((10-results1)+")"+temp.title+"--"+temp.url+":- "+temp.dotP+" ");
	    		//topp[i] = Integer.parseInt(temp.title.substring(4));
	    		//System.out.println(topp[i]);
	    	}
	    	
	    	//
	    	
	    	
}
}
