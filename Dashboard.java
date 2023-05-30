package com.starkenn.Starkenn;

import java.time.Duration;
import java.util.Set;
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

import io.github.bonigarcia.wdm.WebDriverManager;

public class Dashboard {
	WebDriver driver;
	WebDriverWait wait;
	LoginPage loginPage;
	DashboardPage dashboardPage;
	BasePage basePage;
	
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
	}
	
	@BeforeMethod
	public void openUrl(){
	driver.get("http://sktn-react.s3-website.ap-south-1.amazonaws.com/customer-dashboard");
	}
	
	@Test
	public  void naviagatingToVehiclePageFromMenu() throws InterruptedException {
		dashboardPage.clickOnMenuIcon();
		dashboardPage.clickOnVehiclesOnMenu();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/vehicle"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/vehicle");
		String pageTitle = basePage.getPageTitle();
		Assert.assertEquals(pageTitle, "Vehicles");
	}
	
	@Test
	public  void naviagatingToOngoingFromMenu() throws InterruptedException {
		dashboardPage.clickOnMenuIcon();
		dashboardPage.clickOnOngoingTripsOnMenu();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/ongoing-trips"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/ongoing-trips");
		String pageTitle = basePage.getPageTitle();
		Assert.assertEquals(pageTitle, "Ongoing Trips");
	}
	
	@Test
	public  void naviagatingToCompletedTripsFromMenu() throws InterruptedException {
		dashboardPage.clickOnMenuIcon();
		dashboardPage.clickOnCompletedTripsOnMenu();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/completed-trips"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/completed-trips");
		String pageTitle = basePage.getPageTitle();
		Assert.assertEquals(pageTitle, "Completed Trips");
	}
	
	@Test
	public  void naviagatingToDevicesFromMenu() throws InterruptedException {
		dashboardPage.clickOnMenuIcon();
		dashboardPage.clickOnDevicesOnMenu();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/customer-devices"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/customer-devices");
		String pageTitle = basePage.getPageTitle();
		Assert.assertEquals(pageTitle, "Devices");
	}
	
	@Test
	public  void naviagatingToVehiclePage() throws InterruptedException {
		dashboardPage.clickOnVehicle();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/vehicle"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/vehicle");
		String pageTitle = basePage.getPageTitle();
		Assert.assertEquals(pageTitle, "Vehicles");
	}
	
	@Test
	public  void naviagatingToOngoingPage() throws InterruptedException {
		dashboardPage.clickOnOngoing();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/ongoing-trips"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/ongoing-trips");
		String pageTitle = basePage.getPageTitle();
		Assert.assertEquals(pageTitle, "Ongoing Trips");
	}
	
	@Test
	public  void naviagatingToCompletedTripsPage() throws InterruptedException {
		dashboardPage.clickOnCompleted();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/completed-trips"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/completed-trips/");
		String pageTitle = basePage.getPageTitle();
		Assert.assertEquals(pageTitle, "Completed Trips");
	}
	
	@Test
	public  void naviagatingToDevicesPage() throws InterruptedException {
		dashboardPage.clickOnDevices();
		Thread.sleep(3000);

		wait.until(ExpectedConditions.urlContains("/customer-devices"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/customer-devices");
		String pageTitle = basePage.getPageTitle();
		Assert.assertEquals(pageTitle, "Devices");
	}

	@Test
	public  void clickOnStarkennLink() {
		dashboardPage.clickOnStarkennTecnologiesLink();
		String currentPage = driver.getWindowHandle();
		Set<String> winowsOpened = driver.getWindowHandles();
		for (String newWindow :winowsOpened )
		{
			if(!newWindow.equalsIgnoreCase(currentPage))
			{
				driver.switchTo().window(newWindow);
				String newPageUrl = driver.getCurrentUrl();
				Assert.assertEquals(newPageUrl, "https://starkenn.com/");
			}
		}
	}
	
	@Test (priority =10)
	public  void cickOnSignOutOnMenu() {
		dashboardPage.clickOnMenuIcon();
		dashboardPage.clickOnSignOutOnMenu();
		wait.until(ExpectedConditions.urlContains("amazonaws.com"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/");
	}
	
	@AfterClass
	public void closeBrowser() {
		driver.quit();
	}
}
