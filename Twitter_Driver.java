//Miss Galanos
//version 12.8.2015

import twitter4j.*;       //set the classpath to lib\twitter4j-core-4.0.2.jar
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.util.Collections;

public class Twitter_Driver
{
   private static PrintStream consolePrint;
   
   public static void main (String []args) throws TwitterException, IOException
   {
      consolePrint = System.out;
      
      // PART 1
      // set up classpath and properties file
             
      TJTwitter bigBird = new TJTwitter(consolePrint);
      //bigBird.tweetOut("");
      //create message to tweet, then call the tweetOut method
      // bigBird.tweetOut(message);
     
      // PART 2
      // Choose a public Twitter user's handle 
         
    //  Scanner scan = new Scanner(System.in);
     // consolePrint.print("Please enter a Twitter handle, do not include the @symbol --> ");
     // String twitter_handle = scan.next();
             
     // while (!twitter_handle.equals("done"))
    //  {
             // Print the most popular word they tweet
     //    bigBird.makeSortedListOfWordsFromTweets(twitter_handle);
     //    consolePrint.println("The most common word from @" + twitter_handle + " is: " + bigBird.mostPopularWord());
     //    consolePrint.println();
     //    consolePrint.print("Please enter a Twitter handle, do not include the @ symbol --> ");
     //    twitter_handle = scan.next();
    //  }
         
      // PART 3
      //bigBird.investigate();
         
         
   }//end main         
         
}//end driver        
         
class TJTwitter 
{
   private Twitter twitter;
   private PrintStream consolePrint;
   private List<Status> statuses;
   private List<String> sortedTerms;
   
   public TJTwitter(PrintStream console)
   {
      // Makes an instance of Twitter - this is re-useable and thread safe.
      twitter = TwitterFactory.getSingleton(); //connects to Twitter and performs authorizations
      consolePrint = console;
      statuses = new ArrayList<Status>();
      sortedTerms = new ArrayList<String>();   
   }
   
   /******************  Part 1 *******************/
   public void tweetOut(String message) throws TwitterException, IOException
   {
      twitter.updateStatus(message);   
   }
   @SuppressWarnings("unchecked")
   /******************  Part 2 *******************/
   public void makeSortedListOfWordsFromTweets(String handle) throws TwitterException, IOException
   {
      statuses.clear();
      sortedTerms.clear();
      PrintStream fileout = new PrintStream(new FileOutputStream("tweets.txt")); // Creates file for dedebugging purposes
      Paging page = new Paging (1,200);
      int p = 1;
      while (p <= 10)
      {
         page.setPage(p);
         statuses.addAll(twitter.getUserTimeline(handle,page)); 
         p++;        
      }
      int numberTweets = statuses.size();
      fileout.println("Number of tweets = " + numberTweets);
      
      fileout = new PrintStream(new FileOutputStream("garbageOutput.txt"));
   
      int count=1;
      for (Status j: statuses)
      {
         fileout.println(count+".  "+j.getText());
         count++;
      }		
         	
     	//Makes a list of words from user timeline
      for (Status status : statuses)
      {			
         String[]array = status.getText().split(" ");
         for (String word : array)
            sortedTerms.add(removePunctuation(word).toLowerCase());
      }	
   					
      // Remove common English words, which are stored in commonWords.txt
      sortedTerms = removeCommonEnglishWords(sortedTerms);
      sortAndRemoveEmpties();
      
   }
   
   // Sort words in alpha order. You should use your old code from the Sort/Search unit.
   // Remove all empty strings ""
   @SuppressWarnings("unchecked")
   private void sortAndRemoveEmpties()
   {
     
     Collections.sort(sortedTerms);
     for(int x = 0; x<sortedTerms.size(); x++){
     String temp = sortedTerms.get(x);
     temp.replace(" ", "");
     sortedTerms.set(x, temp);
          }
       
    }
     

     
     
     
     
     
     
     
  
       
      
      
      
      
      
   
   
   // Removes common English words from list
   // Remove all words found in commonWords.txt  from the argument list.
   // The count will not be given in commonWords.txt. You must count the number of words in this method.
   // This method should NOT throw an exception. Use try/catch.
   @SuppressWarnings("unchecked")
   private List removeCommonEnglishWords (List<String> list) throws FileNotFoundException
   {	
      String check = "";
      Scanner scan = new Scanner(new File("commonWords.txt"));
           
         while(scan.hasNextLine()){
         check = scan.nextLine();
     scan.nextLine();
     int y = list.size();
     for(int x = 0; x<y; x++){
     if(list.get(x).equals(check))
     list.remove(x);   
     y=list.size();       
   }
   }   
   
      return list; 
   }
   
   
   //Remove punctuation - borrowed from prevoius lab
   //Consider if you want to remove the # or @ from your words. They could be interesting to keep (or remove).
   private String removePunctuation( String s ) throws FileNotFoundException
   {
   Scanner infile = new Scanner(new File("punc.txt"));
   while(infile.hasNext()){
   String temporary = infile.nextLine();
   for(int x = 0; x<s.length() -1; x++){
   if(s.substring(x, x+1).equals(temporary)){
   String temp = s;
   s=temp.substring(0, x)+temp.substring(x+1);
   }  
   }
   }
 return s;
   }
   
   
   
   
   

   
   //Should return the most common word from sortedTerms. 
   //Consider case. Should it be case sensitive? The choice is yours.
   @SuppressWarnings("unchecked")
   public String mostPopularWord()
   {
   int index =0; 
   int reference = 1; 
   int current = 1;
   String temp = "";
   String tempref = ""; 
      for(int x = 0; x<sortedTerms.size()-1; x++){
      temp = sortedTerms.get(x);
      tempref=sortedTerms.get(x+1);
      if(temp.equals(tempref))
      current++;
      if(!temp.equals(tempref)){
      reference=Math.max(current, reference);
      index = x; 
      }
      
      
      
      }
      return sortedTerms.toString();
   }
   

   /******************  Part 3 *******************/
   public void investigate ()
   {
   }
   // A sample query to determine how many people in Arlington, VA tweet about the Miami Dolphins
   public void sampleInvestigate ()
   {
      Query query = new Query("Miami Dolphins");
      query.setCount(100);
      query.setGeoCode(new GeoLocation(38.8372839,-77.1082443), 5, Query.MILES);
      query.setSince("2015-12-1");
      try {
         QueryResult result = twitter.search(query);
         System.out.println("Count : " + result.getTweets().size()) ;
         for (Status tweet : result.getTweets()) {
            System.out.println("@"+tweet.getUser().getName()+ ": " + tweet.getText());  
         }
      } 
      catch (TwitterException e) {
         e.printStackTrace();
      } 
      System.out.println(); 
   }  
   
}  

// Consider adding a sorter class here.
