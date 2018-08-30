package com.qtpselenium.zoho.project.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


import com.qtpselenium.zoho.project.base.BaseTest;
import com.qtpselenium.zoho.project.util.DataUtil;
import com.qtpselenium.zoho.project.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class LoginTest extends BaseTest {
   
	SoftAssert softAssert ;
	String testCaseName="LoginTest";
	Xls_Reader xls;
	
	@Test(dataProvider="getData")
	public void doLoginTest(Hashtable<String,String> data) {
		test = rep.startTest("LoginTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable(testCaseName, xls) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		openBrowser(data.get("Browser"));
		navigate("appurl");
		boolean expectedResult = doLogin(data.get("Username"), data.get("Password"));
		boolean actualResult= false;
		
		if(data.get("ExpectedResult").equals("Y"))
		{
			actualResult=true;
		}
		if(expectedResult!=actualResult)
			reportFail("Test has been failed");
		
		reportPass("Test has been passed");
  }
	
	@DataProvider(parallel=true)
	public Object[][] getData(){
		super.init();
	     xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls,testCaseName);
	}
	
	@BeforeMethod
	public void init() {
		
		softAssert = new SoftAssert();
		
		
	}
	
	@AfterMethod
	public void quit() {
		try {
			softAssert.assertAll();
		}catch(Error e) {
			test.log(LogStatus.FAIL, e.getMessage());
		}
		
		rep.endTest(test);
		rep.flush();
		if(driver!=null) {
			driver.quit();
		}

	}
}
