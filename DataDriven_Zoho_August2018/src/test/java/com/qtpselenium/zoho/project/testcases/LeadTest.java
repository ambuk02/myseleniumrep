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

public class LeadTest extends BaseTest {

	SoftAssert softAssert ;
	Xls_Reader xls;
	String testCaseName="CreateLeadTest";
	@Test(priority=1,dataProvider="getData")
	public void createLeadTest(Hashtable<String,String> data){
		
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
		click("leadLink_xpath");
		click("createLead_xpath");
		type("company_xpath",data.get("LeadCompany"));
		type("lastname_xpath",data.get("LeadLastName"));
		click("saveButton_xpath");
		clickAndWait("leadLink_xpath","createLead_xpath");
		
		int rNum =getLeadRowNum(data.get("LeadLastName"));
		if(rNum==-1)
		{
			reportFail("Lead not found with the lead name"+data.get("LeadLastName"));
		}
		
		reportPass("Lead found with the lead name"+data.get("LeadLastName"));
		takeScreenshot();
		
	}
	
	@Test(priority=2,dataProvider="getData")
	public void convertLeadTest(Hashtable<String,String> data) {
		test = rep.startTest("ConvertLeadTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable("ConvertLeadTest", xls) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(envProp.getProperty("Username"),envProp.getProperty("Password"));
		click("crmLink_xpath");
		click("leadLink_xpath");
		clickOnLead(data.get("LeadLastName"));
		click("convertLead_xpath");
		click("convertLead&Save_xpath");
		click("goBackToLead_xpath");
		int rNum =getLeadRowNum(data.get("LeadLastName"));
		if(rNum==-1)
		{
			reportPass(data.get("LeadLastName")+"has been converted");
		}
		else
		{
		reportFail(data.get("LeadLastName")+"has not been converted");
		}
		takeScreenshot();
		
	}
	
	@Test(priority=3,dataProvider="getDataDeleteLead")
	public void deleteLeadAccountTest(Hashtable<String,String> data) {
		
		test = rep.startTest("DeleteLeadAccountTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable("DeleteLeadAccountTest", xls) ||  data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(prop.getProperty("Username"),prop.getProperty("Password"));
		click("crmLink_xpath");
		click("leadLink_xpath");
		clickOnLead(data.get("LeadLastName"));
		waitForPageToLoad();
		click("options_xpath");
		waitForVisibility("delete_xpath");
		click("delete_xpath");
		waitForVisibility("recycle_bin_xpath");
		click("recycle_bin_xpath");
		waitForPageToLoad();
		clickAndWait("leadLink_xpath","createLead_xpath");
		
		int rNum =getLeadRowNum(data.get("LeadLastName"));
		if(rNum==-1)
		{
			reportPass(data.get("LeadLastName")+" has been deleted");
		}
		else
		{
		reportFail(data.get("LeadLastName")+" has not been deleted");
		}
		takeScreenshot();
		
	}
	
	@DataProvider(parallel=true)
	public Object[][] getData(){
		super.init();
	     xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls,testCaseName);
	}
	
	@DataProvider(parallel=true)
	public Object[][] getDataDeleteLead(){
		super.init();
	     xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls,"DeleteLeadAccountTest");
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
