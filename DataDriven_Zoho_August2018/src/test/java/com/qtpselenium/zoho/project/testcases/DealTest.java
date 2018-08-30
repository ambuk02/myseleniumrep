package com.qtpselenium.zoho.project.testcases;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
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


public class DealTest extends BaseTest{
	String testCaseName="CreateDealTest";
	Xls_Reader xls;
	SoftAssert softAssert ;
	@Test(priority=1,dataProvider="getData")
	public void createDealTest(Hashtable<String,String> data){
		test = rep.startTest(testCaseName);
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable(testCaseName, xls) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(envProp.getProperty("Username"),envProp.getProperty("Password"));
		click("crmLink_xpath");
		click("dealLink_xpath");
		click("createdeal_xpath");
		type("dealname_xpath",data.get("DealName"));
		type("accountname_xpath",data.get("AccountName"));
		selectDate(data.get("ClosingDate"));
		click("stage_xpath");
		waitForVisibility("stage2_xpath");
		type("stage2_xpath",data.get("Stage"));
		clickOnStage("stage1_text_xpath",data.get("Stage"),"stage2_text_xpath");
		click("savedealbutton_xpath");
	    if(isElementPresent("deal_name_xpath"))
	    {
	    	reportPass(data.get("DealName")+" has been added");
	    }
	    else
	    	reportFail(data.get("DealName")+" has not been added");
	    
	    takeScreenshot();
	
		
	}
	
	@Test(priority=2,dataProvider="getData")
	public void deleteDealTest(Hashtable<String,String> data) {
		test = rep.startTest("DeleteDealTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable("DeleteDealTest", xls) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(envProp.getProperty("Username"),envProp.getProperty("Password"));
		click("crmLink_xpath");
		click("dealLink_xpath");
		Actions act = new Actions(driver);
		act.moveToElement(getElement("deal_checkbox_xpath")).build().perform();
		waitForVisibility("deal_checkbox_xpath");
		click("deal_checkbox_xpath");
		waitForVisibility("options_delete_deal_xpath");
		click("options_delete_deal_xpath");
		waitForVisibility("delete_deal_xpath");
		click("delete_deal_xpath");
		waitForVisibility("recycle_bin_xpath");
		click("recycle_bin_xpath");
		if(!isElementPresent("deal_name_xpath"))
	    {
	    	reportPass(data.get("DealName")+" has been deleted");
	    }
	    else
	    	reportFail(data.get("DealName")+" has not been added");
	    
	    takeScreenshot();
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
		if(rep!=null) {
		rep.endTest(test);
		rep.flush();
		}
		if(driver!=null) {
			driver.quit();
		}
	}
	
	
}

