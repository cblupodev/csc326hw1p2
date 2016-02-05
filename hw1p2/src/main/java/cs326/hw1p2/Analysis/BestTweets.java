package cs326.hw1p2.Analysis;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import twitter4j.MediaEntity;
import twitter4j.Status;

public class BestTweets 
{
	public Status mostRetweeted(List<Status> list)
	{
		if (list == null) return null;
		int max = 0;
		Status maxStatus = list.get(0);
		for( Status s : list )
		{
			if( max < s.getRetweetCount() )
			{
				max = s.getRetweetCount();
				maxStatus = s;
			}
		}
		return maxStatus;
	}
		
    public Status leastRetweeted(List<Status> list) 
    {
    	if (list == null) return null;
		int min = Integer.MAX_VALUE;
		Status minStatus = list.get(0);
		for( Status s : list )
		{
			if( s.getRetweetCount() < min)
			{
				min = s.getRetweetCount();
				minStatus = s;
			}
		}
		return minStatus;
    }
    
    public String mostRetweetedPhoto(List<Status> list) {
    	if (list == null) return "";
    	Hashtable<String, Integer> photos = new Hashtable<String, Integer>();
    	
    	//iterate all the statuses
    	for (Status s : list) { 
    		// get all the media entities in the single status
    		MediaEntity[] mes = s.getMediaEntities();
    		if (mes != null) {
    			// iterate over the media entities
    			for (MediaEntity em : mes) {
    				// only procede of the entitiy is a photo
    				if (em.getType().equals("photo")) {
		    			// initialize the value if it's not in there already
		    			if (photos.get(em.getMediaURL()) == (null)) {
		    				photos.put(em.getMediaURL(), 0);
		    			}
		    			// increment the photo url retweet count by the parent statuses retweet count
		    			photos.put(
		    					em.getMediaURL(), // find the entry 
		    					photos.get(em.getMediaURL()) + s.getRetweetCount() // update the entry
						); 
    				}
    			}
    		}
    	}
    	
    	return hashMax(photos);
    }
    
    //TODO include retweet counts
    public String mostTweetedPhoto(List<Status> list) {
    	if (list == null) return "";
    	Hashtable<String, Integer> photos = new Hashtable<String, Integer>();
    	
    	// create hash table for total tweets
    	for (Status s: list) {
    		MediaEntity[] mes = s.getMediaEntities();
    		if (mes != null) {
    			for (MediaEntity em : mes) {
    				if (em.getType().equals("photo")) {
		    			// initialize the value if it's not in there already
		    			if (photos.get(em.getMediaURL()) == (null)) {
		    				photos.put(em.getMediaURL(), 0);
		    			}
		    			// increment the photo url tweet count by one plus the retweet value
		    			photos.put(
		    					em.getMediaURL(), // find the entry 
		    					photos.get(em.getMediaURL()) + 1 + s.getRetweetCount()
						); 		    			
    				}
    			}
    		}
    	}
    	return hashMax(photos);
    }
    
    public int mostCommonWord(List<Status> list) {
    	if (list == null) return -1;
    	Hashtable<String, Integer> words = new Hashtable<String, Integer>();
    	
    	// iterate the stasuses
    	for (Status s : list) {
    		String str = s.getText(); // get status text
    		if (str != null) {
				// split the words into an array, remove special punctuation
				str = str.replaceAll("[!?,.]", "");
				String[] strs = str.split("\\s+");
				// add all words to the hash table
				for (String wrd : strs) {
					wrd = wrd.toLowerCase();
					if (words.get(wrd) == null) {
						words.put(wrd, 0);
					}
					words.put(wrd, words.get(wrd) + 1);
				} 
			}
    	}
    	return words.get(hashMax(words));
    }
    
    // find the max values in the hash table
	private String hashMax(Hashtable<String, Integer> table) {
		Set<String> keys = table.keySet();
    	int max = 0;
    	String rtn = "";
    	for (String key : keys) {
    		if (table.get(key) > max) {
    			max = table.get(key); // update the max value
    			rtn = key; // update the url to return
    		}
    	}
    	return rtn;
	}
}