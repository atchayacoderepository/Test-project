package org.gscode.base;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class SeleniumBase implements SeleniumApi{

	long secound = 30;
	long maxWaitTime = 10;
	WebDriverWait wait = null;
	RemoteWebDriver driver = null;
	ExtentReports extent;
	ExtentTest test;
	protected String desc =""; 
	protected String author ="";
	protected String category =""; 

	//create for visible of element
	protected WebElement isElementvisible(WebElement ele) {
		WebElement element = wait.
				withMessage("Element is not visible").
				until(ExpectedConditions.visibilityOf(ele));
		return element;
	}

	public void get(String url) {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(secound));
		driver.get(url);
		wait = new WebDriverWait(driver,Duration.ofSeconds(maxWaitTime));
	}

	public void setup(Browser browsername, String url) {
		switch (browsername) {
		case CHROME:
			driver = new ChromeDriver();
			break;
		case FIREFOX:
			driver = new FirefoxDriver();
			break;
		default:
			System.err.println("Driver is not defined");
			break;
		}
				
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(secound));
		driver.get(url);
		wait = new WebDriverWait(driver,Duration.ofSeconds(maxWaitTime));

	}

	public void close() {
		driver.close();
	}

	public void quit() {
		driver.quit();
	}

	public WebElement element(Locators type, String value) {
		try {
			switch (type) {
			case id:
				return driver.findElement(By.id(value));
			case name:
				return driver.findElement(By.name(value));
			case classname:
				return driver.findElement(By.className(value));
			case tagname:
				return driver.findElement(By.tagName(value));
			case xpath:
				return driver.findElement(By.xpath(value));
			case link:
				return driver.findElement(By.linkText(value));
			case partialLinkText:
				return driver.findElement(By.partialLinkText(value));
			case cssSelector:
				return driver.findElement(By.cssSelector(value));
			default:
				break;
			}
		} catch (NoSuchElementException e) {
			System.err.println("Element not found =>"+ e.getMessage());
			throw new NoSuchElementException("Element not found");
		}catch (WebDriverException e) {
			System.err.println(e.getMessage());
			throw new WebDriverException("some unknown exception error");
		}catch (Exception e) {
			System.err.println(e.getMessage());
			
		}

		return null;
	}

	public void switchtowindow(int i) {
		Set<String> windowHandles = driver.getWindowHandles();
		ArrayList<String> list = new ArrayList<String>(windowHandles);
		driver.switchTo().window(list.get(i));

	}

	public void selectvalue(WebElement ele, String value) {
		WebElement element = isElementvisible(ele);
		new Select(element).selectByValue(value);
	}

	public void selecttext(WebElement ele, String text) {
		WebElement element = isElementvisible(ele);
		new Select(element).selectByVisibleText(text);
	}

	public void selectindex(WebElement ele, int position) {
		WebElement element = isElementvisible(ele);
		new Select(element).deselectByIndex(position);

	}

	public void click(WebElement ele) {
		WebElement element = wait
				.withMessage("Element is not Clickable")
				.until(ExpectedConditions.elementToBeClickable(ele));
		element.click();

	}

	public void type(WebElement ele, String TestData) {
		WebElement element = isElementvisible(ele);
		element.clear();
		element.sendKeys(TestData);
	}

	public void type(WebElement ele ,String TestData,Keys keys) {
		WebElement element = isElementvisible(ele);
		element.clear();
		element.sendKeys(TestData,Keys.ENTER);
	}


	public void appendtext(WebElement ele ,String testdata) {
		WebElement element = isElementvisible(ele);
		element.sendKeys(testdata);

	}

	public String getTitle() {
		return driver.getTitle();
	}

	public String geturl() {
		return driver.getCurrentUrl();
	}

	public boolean isDisplayed(WebElement ele) {
		return ele.isDisplayed();
	}

	
	public void screenshot(String img){
		File firstscr = driver.getScreenshotAs(OutputType.FILE);
		File desc = new File("./test-output/" + img +".png");
		try {
			org.openqa.selenium.io.FileHandler.copy(firstscr, desc);
		} catch (IOException e) {
			System.err.println("Sanp file is not created");
			e.printStackTrace();
		}
		
	}

	
	public void  createtest(String desc, String author, String category) {
		ExtentSparkReporter reporter = new ExtentSparkReporter("./TestReport.html"); 
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		test = extent.createTest(desc);
		test.assignAuthor(author);
		test.assignCategory(category);
	}

	public void stepreport(String status, String description ,String imgname) {
		File firstscr = driver.getScreenshotAs(OutputType.FILE);
		File desc = new File("./test-output/" +imgname +".png");
		try {
			FileHandler.copy(firstscr, desc);
		} catch (IOException e) {
			System.err.println("Sanp file is not created");
			e.printStackTrace();
		}
		String absolutePath = desc.getAbsolutePath();
		
		switch (status.toLowerCase()) {
		case "pass":
			test.pass(description);
			test.log(Status.PASS,MediaEntityBuilder.createScreenCaptureFromPath(absolutePath).build());
			break;
		case "fail":
			test.fail(description);
			test.addScreenCaptureFromPath(imgname+".png") ;
			break;
		case "info":
			test.info(description);
			test.addScreenCaptureFromPath(imgname+".png") ;
			break;
		case "waring":
			test.warning(description);
			break;
		default:
			System.err.println("Status is not defined");
			break;
		}
		
		extent.flush();
	}
	
	
	


}
