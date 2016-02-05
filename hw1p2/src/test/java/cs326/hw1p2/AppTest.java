package cs326.hw1p2;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import twitter4j.MediaEntity;
import twitter4j.Status;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import cs326.hw1p2.Analysis.BestTweets;
/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void BestTweets()
    {
    	BestTweets best = new BestTweets();

    	List<Status> statuses = new ArrayList<Status>();
    	Status testStatus;
    	
    	testStatus = Mockito.mock(Status.class);
    	when(testStatus.getId()).thenReturn(900000L);
    	when(testStatus.getRetweetCount()).thenReturn(33);
    	statuses.add(testStatus);
    	
    	testStatus = Mockito.mock(Status.class);
    	when(testStatus.getId()).thenReturn(100000L);
    	when(testStatus.getRetweetCount()).thenReturn(3);
    	statuses.add(testStatus);
    	
    	// mostRetweeted
    	Status bestTweet = best.mostRetweeted(statuses);
    	assertEquals(bestTweet.getId(), 900000L );
       	assertEquals(bestTweet.getRetweetCount(), 33 );
       	
       	// leastRetweeted
       	Status leasTweet = best.leastRetweeted(statuses);
       	assertEquals(100000L, leasTweet.getId());
       	assertEquals(3, leasTweet.getRetweetCount());
       	
       	// setup statuses
       	statuses = new ArrayList<Status>();       	
       	testStatus = Mockito.mock(Status.class);
        MediaEntity m = Mockito.mock(MediaEntity.class);
        when(m.getMediaURL()).thenReturn("http://www.picture.com/pic.png");
        when(m.getType()).thenReturn("photo");
        MediaEntity[] array = new MediaEntity[]{m};
        when(testStatus.getMediaEntities()).thenReturn(array); // make the status have a photo
        when(testStatus.getRetweetCount()).thenReturn(3);
        when(testStatus.getText()).thenReturn("asdf asdf2 asdf3");
        statuses.add(testStatus);
        
        testStatus = Mockito.mock(Status.class);
        MediaEntity m2 = Mockito.mock(MediaEntity.class);
        when(m2.getMediaURL()).thenReturn("http://www.picture.com/pic2.png");
        when(m2.getType()).thenReturn("photo");
        array = new MediaEntity[]{m, m2};
        when(testStatus.getMediaEntities()).thenReturn(array); // make the status have a photo
        when(testStatus.getRetweetCount()).thenReturn(1);
        when(testStatus.getText()).thenReturn("asdf asdf4 asdf5");
        statuses.add(testStatus); // tweet with 2 retweets
        testStatus = Mockito.mock(Status.class);
        m2 = Mockito.mock(MediaEntity.class);
        when(m2.getMediaURL()).thenReturn("http://www.picture.com/pic2.png");
        when(m2.getType()).thenReturn("photo");
        array = new MediaEntity[]{m2};
        when(testStatus.getMediaEntities()).thenReturn(array);
        statuses.add(testStatus); // single tweet
        m2 = Mockito.mock(MediaEntity.class);
        when(m2.getMediaURL()).thenReturn("http://www.picture.com/pic2.png");
        when(m2.getType()).thenReturn("photo");
        array = new MediaEntity[]{m2};
        when(testStatus.getMediaEntities()).thenReturn(array);
        statuses.add(testStatus); // single tweet
        m2 = Mockito.mock(MediaEntity.class);
        when(m2.getMediaURL()).thenReturn("http://www.picture.com/pic2.png");
        when(m2.getType()).thenReturn("photo");
        array = new MediaEntity[]{m2};
        when(testStatus.getMediaEntities()).thenReturn(array);
        statuses.add(testStatus); // single tweet
        m2 = Mockito.mock(MediaEntity.class);
        when(m2.getMediaURL()).thenReturn("http://www.picture.com/pic2.png");
        when(m2.getType()).thenReturn("photo");
        array = new MediaEntity[]{m2};
        when(testStatus.getMediaEntities()).thenReturn(array);
        statuses.add(testStatus); // single tweet
        m2 = Mockito.mock(MediaEntity.class);
        when(m2.getMediaURL()).thenReturn("http://www.picture.com/pic2.png");
        when(m2.getType()).thenReturn("photo");
        array = new MediaEntity[]{m2};
        when(testStatus.getMediaEntities()).thenReturn(array);
        statuses.add(testStatus); // single tweet
        
        
        // mostRetwetedPhoto
        // m  retweet count = 3
        // m2 retweet count = 2
        String retPhoto = best.mostRetweetedPhoto(statuses);
        assertEquals("http://www.picture.com/pic.png", retPhoto);
        
        // mostTweetedPhoto
        // m  tweet count = 3 + 1 = 4
        // m2 tweet count = 2 + 3 = 5
        //TODO include retweet counts
        String tweetPhoto = best.mostTweetedPhoto(statuses);
        assertEquals("http://www.picture.com/pic2.png", tweetPhoto); // tweeted count should be 52
        
        // mostCommonWord
        int numWord = best.mostCommonWord(statuses);
        assertEquals(2, numWord);
    }
}
