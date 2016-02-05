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
