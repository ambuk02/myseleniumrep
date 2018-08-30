package com.qtpselenium.zoho.project.base;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.Assert;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.qtpselenium.zoho.project.util.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;



public class BaseTest {
	
	public WebDriver driver;
	public Properties prop;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
    public boolean gridRun=true;
	public Properties envProp;
public void init() {
	
	
	if(prop==null){
		prop=new Properties();
		envProp=new Properties();
		try {
			FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//projectconfig.properties");
			prop.load(fs);
			String env=prop.getProperty("env");
			fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//"+env+".properties");
			envProp.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
}
	
public void openBrowser(String bType){
	  if(!gridRun) {
	   test.log(LogStatus.INFO, "Opening Browser "+bType);
		   //System.out.println(prop.getProperty("appurl"));
		if(bType.equals("Mozilla"))
			driver=new FirefoxDriver();
		else if(bType.equals("Chrome")){
	        driver=new ChromeDriver();
		}
		else if (bType.equals("IE")){
			driver= new InternetExplorerDriver();
		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	  }
	  else{// grid run
			
			DesiredCapabilities cap=null;
			if(bType.equals("Mozilla")){
				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				
			}else if(bType.equals("Chrome")){
				 cap = DesiredCapabilities.chrome();
				 cap.setBrowserName("chrome");
				 cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			
			try {
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	}

  public void navigate(String urlKey) {
	  test.log(LogStatus.INFO, "Navigating to "+envProp.getProperty(urlKey));
	  driver.get(envProp.getProperty(urlKey));
  }
  
  public void click(String locatorKey) {
	 test.log(LogStatus.INFO, "Clicking on "+prop.getProperty(locatorKey));
	 getElement(locatorKey).click();
	 test.log(LogStatus.INFO, "Clicked Successfully on "+prop.getProperty(locatorKey));
  }
  
  public void clickOnStage(String locatorKey1,String stage,String locatorKey2)
  {
	  String part1 = prop.getProperty(locatorKey1);
	  String part2 = prop.getProperty(locatorKey2);
	  driver.findElement(By.xpath(part1+stage+part2)).click();;
  }
  public void type(String locatorKey, String data ) {
	 test.log(LogStatus.INFO, "Typing in "+prop.getProperty(locatorKey));
	 getElement(locatorKey).sendKeys(data);
	 test.log(LogStatus.INFO, "Typed successfully in "+prop.getProperty(locatorKey));
  }
  public WebElement getElement(String locatorKey){
      WebElement e=null;
      try{
      if(locatorKey.endsWith("_id"))
                 e = driver.findElement(By.id(prop.getProperty(locatorKey)));
      else if(locatorKey.endsWith("_name"))
                 e = driver.findElement(By.name(prop.getProperty(locatorKey)));
      else if(locatorKey.endsWith("_xpath"))
                 e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
      else if(locatorKey.endsWith("_cssSelector"))
                 e = driver.findElement(By.cssSelector(prop.getProperty(locatorKey)));
      else{
                 reportFail("Locator not correct - " + locatorKey);
                 AssertJUnit.fail("Locator not correct - " + locatorKey);
      }
      
      }catch(Exception ex){
                 // fail the test and report the error
                 reportFail(ex.getMessage());
                 ex.printStackTrace();
                 AssertJUnit.fail("Failed the test - " + ex.getMessage());
      }
      return e;
}
  /********************************Validations*************************/
  public boolean isElementPresent(String locatorKey) {
	  List<WebElement> elementList=null;
      try{
      if(locatorKey.endsWith("_id"))
                 elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
      else if(locatorKey.endsWith("_name"))
                 elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
      else if(locatorKey.endsWith("_xpath"))
                 elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
      else if(locatorKey.endsWith("_cssSelector"))
                 elementList = driver.findElements(By.cssSelector(prop.getProperty(locatorKey)));
      else{
                 reportFail("Locator not correct - " + locatorKey);
                 AssertJUnit.fail("Locator not correct - " + locatorKey);
      }
      
      }catch(Exception ex){
                 // fail the test and report the error
                 reportFail(ex.getMessage());
                 ex.printStackTrace();
                 AssertJUnit.fail("Failed the test - " + ex.getMessage());
      }
      if(elementList.size()==0) {
    	  return false;
      }
      else
      {
	  return true;
      }
  }
  
  public boolean verifyText(String locatorKey,String expectedTextKey) {
	  String actualText = getElement(locatorKey).getText().trim();
	  String expectedTest = prop.getProperty(expectedTextKey);
	  if(actualText.equals(expectedTest))
	     return true;
	  else
		 return false; 
	 
  }
  
  public void clickAndWait(String locator_clicked,String locator_pres) {
	  test.log(LogStatus.INFO, "Clicking and waiting for the element" + locator_pres);
	  int count=5;
	  for(int i=0;i<count;i++) {
		  getElement(locator_clicked).click();
		  wait(2);
		  if(isElementPresent(locator_pres))
			  break;
	  }
  }

  /********************************Reporting***************************/
 
  public void reportPass(String msg) {
	  test.log(LogStatus.PASS, msg);
  }
  public void reportFail(String msg) {
	  test.log(LogStatus.FAIL, msg);
	  takeScreenshot();
	  Assert.fail(msg);
  }
  
  public void takeScreenshot() {
	// fileName of the screenshot
			Date d=new Date();
			String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
			// store screenshot in that file
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//ScreenShots//"+screenshotFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//put screenshot file in reports
			test.log(LogStatus.INFO,"Screenshot-> "+ test.addScreenCapture(System.getProperty("user.dir")+"//ScreenShots//"+screenshotFile));
  }
  
  public void waitForPageToLoad() {
		wait(1);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		
		while(!state.equals("complete")){
			wait(2);
			state = (String)js.executeScript("return document.readyState");
		}
	}
	
	public void wait(int timeToWaitInSec){
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void acceptAlert(){
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.alertIsPresent());
		test.log(LogStatus.INFO,"Accepting alert");
		driver.switchTo().alert().accept();
		driver.switchTo().defaultContent();
	}
	
	public void waitForVisibility(String element){
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.visibilityOf(getElement(element)));
	}
  
	
	public String getText(String locatorKey){
		test.log(LogStatus.INFO, "Getting text from "+locatorKey);
		return getElement(locatorKey).getText();

	}
  /*************************************AppFunctions*************************/
  
  public boolean doLogin(String username, String password)
  {
	test.log(LogStatus.INFO, "Trying to login with"+username+","+password);
	click("loginLink_xpath");
	type("loginid_xpath",username);
	type("password_xpath",password);
	click("signinButton_xpath");
	
	if(isElementPresent("crmLink_xpath")){
		test.log(LogStatus.INFO, "Login Success");
		return true;
		
	}
	else {
		test.log(LogStatus.INFO, "Login Failed");
		return false;
	}
  }
  
  public int getLeadRowNum(String leadName) {
	  test.log(LogStatus.INFO, "Getting the leadname "+leadName);
	  List <WebElement> leadNames = driver.findElements(By.xpath(prop.getProperty("leadNameCol_xpath")));
	  for(int i=0;i<leadNames.size();i++) {
		 
		  System.out.println(leadNames.get(i).getText().trim());
		  if(leadNames.get(i).getText().trim().equals(leadName)) {
			  test.log(LogStatus.INFO, "Lead found at "+(i+1));
			  return (i+1);
		  }
	  }
	  test.log(LogStatus.INFO, "Lead not found ");
	  return -1;
  }
  
  public void clickOnLead(String leadName) {
     test.log(LogStatus.INFO, "Clicking on the lead"+leadName);
     int rNum = getLeadRowNum(leadName);
     driver.findElement(By.xpath(prop.getProperty("leadNameLink_xpath_1")+rNum+prop.getProperty("leadNameLink_xpath_2"))).click();;
  }
  
  public void selectDate(String d){
		test.log(LogStatus.INFO, "Selecting the date "+d);
		// convert the string date(input) in date object
		click("datetextfielddeal_xpath");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date dateTobeSelected = sdf.parse(d);
			Date currentDate = new Date();
			sdf = new SimpleDateFormat("MMMM");
			String monthToBeSelected=sdf.format(dateTobeSelected);
			sdf = new SimpleDateFormat("yyyy");
			String yearToBeSelected=sdf.format(dateTobeSelected);
			sdf = new SimpleDateFormat("d");
			String dayToBeSelected=sdf.format(dateTobeSelected);
			//June 2016
			String monthYearToBeSelected=monthToBeSelected+" "+yearToBeSelected;
			
			while(true){
				if(currentDate.compareTo(dateTobeSelected)==1){
					//back
					click("backdeal_xpath");
				}else if(currentDate.compareTo(dateTobeSelected)==-1){
					//front
					click("forwarddeal_xpath");
				}
				
				if(monthYearToBeSelected.equals(getText("monthyeardisplayed_xpath"))){
					break;
				}
				
				
			}
			driver.findElement(By.xpath("//td[text()='"+dayToBeSelected+"']")).click();
			test.log(LogStatus.INFO, "Date Selection Successful "+d);

			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

