- Practice unit testing and mocking with Twitter
- Practice using Selenium to test browser interactions and elements
- Get iTrust tests passing

passing twitter tests [https://goo.gl/photos/LA7VtEiPUbZn5nnw7](https://goo.gl/photos/LA7VtEiPUbZn5nnw7)

passing web tests
[https://goo.gl/photos/7n3zLLPfM5YLVDcWA](https://goo.gl/photos/7n3zLLPfM5YLVDcWA)

passing itrust tests [https://goo.gl/photos/2kdsmFn9g6jKdHoM9](https://goo.gl/photos/2kdsmFn9g6jKdHoM9)

# WebTest.java

package cs326.hw1p2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.ChromeDriverManager;

public class WebTest
{
	private static WebDriver driver;
	
	@Before
	public void setUp() throws Exception 
	{
        //driver = new HtmlUnitDriver(true);
        System.setProperty("webdriver.chrome.driver", "/Users/gameweld/classes/326/HW1.P2/hw1p2/chromedriver");
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
		driver.get("http://checkbox.io/studies.html");
	}
	
	@Test
	@Ignore
	public void googleExists() throws Exception
	{
		this.driver.get("http://www.google.com");
        assertEquals("Google", this.driver.getTitle());		
	}
	
	@Test
	@Ignore
	public void googleiTrustNumberOne() throws Exception
	{
		this.driver.get("http://www.google.com");
		//this.driver.
		WebElement search = this.driver.findElement(By.name("q"));
		search.sendKeys("ncsu iTrust");
		search.sendKeys(Keys.RETURN);
		//this.driver.findElement(By.name("btnK")).click();
		
		WebDriverWait wait = new WebDriverWait(this.driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resultStats")));

		List<WebElement> links = this.driver.findElements(By.xpath("//a[@data-href]"));
		int rank = 0;
		for( WebElement link : links )
		{
			if( link.getAttribute("data-href").equals("http://agile.csc.ncsu.edu/iTrust/wiki/"))
			{
				break;
			}
			rank++;
		}
		
		assertEquals(0, rank);
	}

	
	// The participant count of "Frustration of Software Developers" is 55
	@Test
	public void test55Participation() {		
		WebElement elem = driver.findElement(By.cssSelector("#dynamicStudies > div:nth-child(17) > div.span4 > p > span.backers"));
		assertEquals("55", elem.getText());
	}
	
	@Test
	// The total number of studies closed is 5
	public void test5Close() {
		
		WebElement[] elems = new WebElement[9];
		
		// find the spans
		elems = driver.findElements(By.cssSelector("span[data-bind='text: status']")).toArray(elems);
		int count = 0;
		for (WebElement e : elems) {
			if (e.getText().equals("CLOSED")) {
				count++;
			}
		}
		assertEquals(5, count);
		driver.close();
	}
	
	@Test
	// If a status of a study is open, you can click on a "Participate" button
	public void testParticipate() {
		WebElement[] elems = new WebElement[9];
		
		// find the rows		
		elems = driver.findElements(By.cssSelector("#dynamicStudies > div")).toArray(elems);
		
		for (WebElement e : elems) {
			// verify the status span has OPEN text
			if (e.findElement(By.cssSelector("span[data-bind='text: status']")).getText().equals("OPEN")) {
				// verify there's a button and it's enabled
				String title = e.findElement(By.cssSelector("h3 span")).getText();
				e.findElement(By.cssSelector("button")).click();
				String parentHandle = driver.getWindowHandle();
				for (String s : driver.getWindowHandles()) {
					if (!s.equals(parentHandle)) {
						// switch to the new tab
						driver.switchTo().window(s);
						// wait until the text isn't loading
						while ( driver.findElement(By.id("surveyTitle")) == null ||
								driver.findElement(By.id("surveyTitle")).getText().equals("Loading...")) {}
						
						String newTitle = driver.findElement(By.id("surveyTitle")).getText();
						assertEquals(title, newTitle);
						driver.close();
						driver.switchTo().window(parentHandle);
					}
				}
			}
		}
		driver.close();
	}
	
	@Test
	// You can enter text into this study (don't actually submit, or you can't run test again!): 
	//	http://checkbox.io/studies/?id=569e667f12101f8a12000001
	public void testEnterText() {
		driver.get("http://checkbox.io/studies/?id=569e667f12101f8a12000001");
		WebElement myDynamicElement = (new WebDriverWait(driver, 10))
				  .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("textarea:nth-of-type(1)")));
		WebElement[] areas = new WebElement[2];
		areas = driver.findElements(By.cssSelector("textarea")).toArray(areas); // find the text area
		for (WebElement e : areas) {
			e.sendKeys("asdf"); //write into the text area
			assertEquals("asdf", e.getAttribute("value"));
		}
		driver.close();
	}
	
    @AfterClass
    public static void  tearDown() throws Exception
    {
        driver.close();
    }

}


# AppTest.java

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


# BestTweets.java

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