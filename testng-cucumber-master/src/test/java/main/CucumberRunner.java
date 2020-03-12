package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.google.common.io.Files;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import helpers.ReportHelper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileBrowserType;

@CucumberOptions(strict = true, monochrome = true, features = "src/test/resources/features", glue = "stepdefinition", format = {"pretty","json:target/cucumber.json"}, tags = { "@appium" })

public class CucumberRunner extends AbstractTestNGCucumberTests {

	public static Properties config = null;
	private String accessKey = "eyJ4cC51IjozODI3MjEsInhwLnAiOjIsInhwLm0iOiJNVFUxTmpnd09UVTROakk1T0EiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE4NzIxNjk1ODYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.aTgX-5SHxDCBFF2opiw6tAoiRSifp84tJtrQd9-N2go";
    protected static AndroidDriver<AndroidElement> driver = null;
    DesiredCapabilities dc = new DesiredCapabilities();

	public void LoadConfigProperty() throws IOException {
		config = new Properties();
		FileInputStream ip = new FileInputStream(
				System.getProperty("user.dir") + "//src//test//resources//config//config.properties");
		config.load(ip);
	}
	
	
	

	public void configureDriverPath() throws IOException {
		if(System.getProperty("os.name").startsWith("Linux")) {
			String firefoxDriverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/linux/geckodriver";
			System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
			String chromeDriverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/linux/chromedriver";
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		}
		if(System.getProperty("os.name").startsWith("Mac")) {
			String firefoxDriverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/mac/geckodriver";
			System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
			String chromeDriverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/mac/chromedriver";
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		}
		if(System.getProperty("os.name").startsWith("Windows")) {
			String firefoxDriverPath = System.getProperty("user.dir") + "//src//test//resources//drivers//windows//geckodriver.exe";
			System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
			String chromeDriverPath = System.getProperty("user.dir") + "//src//test//resources//drivers//windows//chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		}
	}

	public void openBrowser() throws Exception {
		// loads the config options
	}

	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	public void implicitWait(int time) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}

	public void explicitWait(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 3000);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void pageLoad(int time) {
		driver.manage().timeouts().pageLoadTimeout(time, TimeUnit.SECONDS);
	}

	public void deleteAllCookies() {
		driver.manage().deleteAllCookies();
	}

	public void setEnv() throws Exception {
		LoadConfigProperty();
		String baseUrl = config.getProperty("siteUrl");
		driver.get(baseUrl);
	}

	public static String currentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String cal1 = dateFormat.format(cal.getTime());
		return cal1;
	}
	

	@BeforeSuite(alwaysRun = true)
	public void setUp() throws Exception {
		 	dc.setCapability("testName", "Quick Start Android Browser Demo");
	        dc.setCapability("accessKey", accessKey);
	        dc.setCapability("deviceQuery", "@os='android' and @category='PHONE'");
	        dc.setBrowserName(MobileBrowserType.CHROMIUM);
	        driver = new AndroidDriver<AndroidElement>(new URL("https://uscloud.experitest.com/wd/hub"), dc);
	        driver.rotate(ScreenOrientation.PORTRAIT);
//	        driver.get("https://www.google.com");
//	        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
//	        WebElement searchBar = driver.findElement(By.name("q"));
//	        searchBar.sendKeys("Experitest");
//	        driver.findElement(By.xpath("//*[@css='BUTTON.Tg7LZd']")).click();

	}
	 

	@AfterClass(alwaysRun = true)
	public void takeScreenshot() throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File trgtFile = new File(System.getProperty("user.dir") + "//screenshots/screenshot.png");
		trgtFile.getParentFile().mkdir();
		trgtFile.createNewFile();
		Files.copy(scrFile, trgtFile);

	}

	@AfterMethod(alwaysRun = true)
	public void tearDownr(ITestResult result) throws IOException {
		if (result.isSuccess()) {
			File imageFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String failureImageFileName = result.getMethod().getMethodName()
					+ new SimpleDateFormat("MM-dd-yyyy_HH-ss").format(new GregorianCalendar().getTime()) + ".png";
			File failureImageFile = new File(System.getProperty("user.dir") + "//screenshots//" + failureImageFileName);
			failureImageFile.getParentFile().mkdir();
			failureImageFile.createNewFile();
			Files.copy(imageFile, failureImageFile);
		}

	}
	@AfterSuite(alwaysRun=true)
	public void generateHTMLReports() {
		ReportHelper.generateCucumberReport();
	}

	@AfterSuite(alwaysRun = true)
	public void quit() throws IOException, InterruptedException {
		driver.quit();
	}
}
