package com.starkenn.Starkenn;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.starkenn.Starkenn.pageObjects.BasePage;
import com.starkenn.Starkenn.pageObjects.DashboardPage;
import com.starkenn.Starkenn.pageObjects.LoginPage;
import com.starkenn.Starkenn.pageObjects.VehicePage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Vehicle {
	
	WebDriver driver;
	WebDriverWait wait;
	LoginPage loginPage;
	DashboardPage dashboardPage;
	BasePage basePage;
	VehicePage vehiclePage;
	
	@BeforeClass
	public void intializeDriver(){
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("http://sktn-react.s3-website.ap-south-1.amazonaws.com/");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		loginPage = new LoginPage(driver);
		loginPage.enterEmail("john@gmail.com");
		loginPage.enterPassword("123456");
		loginPage.clickOnSubmit();
		wait= new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("/customer-dashboard"));
		dashboardPage = new DashboardPage(driver);
		basePage = new BasePage(driver);
		vehiclePage = new VehicePage(driver);
	}
	
	@BeforeMethod
	public void openUrl() throws InterruptedException{
	driver.get("http://sktn-react.s3-website.ap-south-1.amazonaws.com/customer-dashboard");
	dashboardPage.clickOnVehicle();
	Thread.sleep(3000);

	wait.until(ExpectedConditions.urlContains("/vehicle"));
	}
	
	@Test
	public void backToDashboardFromVehicle () 
	{
		vehiclePage.clickOnBackToDashboard();
		wait.until(ExpectedConditions.urlContains("/customer-dashboard"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/customer-dashboard");
	}
	
	@Test
	public void verifyTotaRecordsCount () 
	{
		basePage.selectRecordsPerPage("30");
		int noOfRecorsInTable = basePage.getNumberOfRecordsInTable();
		int totalRecordsCount = basePage.getTotalRecords();
		Assert.assertEquals(noOfRecorsInTable, totalRecordsCount);
	}
	
	@Test
	public void verifySearchFunctionality () 
	{
		String searchText = "tru" ;
		basePage.selectRecordsPerPage("30");
		vehiclePage.searchVehicleName(searchText);
		int recordsMatching = vehiclePage.getNoOfRecorsMatchingWithSearch(searchText);
		int noOfRecorsInTable = basePage.getNumberOfRecordsInTable();
		Assert.assertEquals(recordsMatching, noOfRecorsInTable);
	}
	
	public void invalidAddDeviceWithOutIOT () throws InterruptedException {
		vehiclePage.clickOnAddVehicle();
		wait.until(ExpectedConditions.urlContains("/add-vehicle"));
		vehiclePage.enterVehicaName("SWIFT");
		vehiclePage.enterRegistrationNo("MH 06 3344");
		vehiclePage.selectECU("EC78778");
		vehiclePage.selectStatus("Active");
		vehiclePage.clickOnAddVehicle();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.visibilityOf(vehiclePage.alertMsg));
		String alertMsg = vehiclePage.getAlertMsg();
		Assert.assertEquals(alertMsg, "Failed to add vehicle");
	}
	
	@Test
	public void invalidAddDeviceWithOutECU () throws InterruptedException {
		vehiclePage.clickOnAddVehicle();
		wait.until(ExpectedConditions.urlContains("/add-vehicle"));
		vehiclePage.enterVehicaName("SWIFT");
		vehiclePage.enterRegistrationNo("MH 06 3344");
		vehiclePage.selectIOT("YONO1");
		vehiclePage.selectStatus("Active");
		vehiclePage.clickOnAddVehicle();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.visibilityOf(vehiclePage.alertMsg));
		String alertMsg = vehiclePage.getAlertMsg();
		Assert.assertEquals(alertMsg, "Failed to add vehicle");
	}
	
	@Test
	public void successfullAddDeviceWithECUandIOT () throws InterruptedException {
		String vehiceName = "Lorry";
		String vehiceRegNo = "MH 06 0045";
		String ecuNo = "YONO";
		String iotNo = "FC0976";
		String status = "Active";
		vehiclePage.clickOnAddVehicle();
		wait.until(ExpectedConditions.urlContains("/add-vehicle"));
		vehiclePage.enterVehicaName(vehiceName);
		vehiclePage.enterRegistrationNo(vehiceRegNo);
		vehiclePage.selectECU(ecuNo);
		vehiclePage.selectIOT(iotNo);
		vehiclePage.selectStatus(status);
		vehiclePage.clickOnAddVehicle();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		wait.until(ExpectedConditions.visibilityOf(vehiclePage.alertMsg));
		String alertMsg = vehiclePage.getAlertMsg();
		Assert.assertEquals(alertMsg, "Vehicle Added Successfully");
		vehiclePage.clickOnBackToVehices();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/vehicle"));
		Assert.assertEquals(vehiceName, vehiclePage.getRecentlyAddedVehicleName());
		Assert.assertEquals(vehiceRegNo, vehiclePage.getRecentlyAddedVehicleRegNo());
		Assert.assertEquals(ecuNo, vehiclePage.getRecentlyAddedVehicleECUNo());
		Assert.assertEquals(iotNo, vehiclePage.getRecentlyAddedVehicleIOTNo());
		Assert.assertEquals(status, vehiclePage.getRecentlyAddedVehicleStatus());
	}
	
	@AfterClass
	public void closeBrowser() {
		driver.quit();
	}
}
