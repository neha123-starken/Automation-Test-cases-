package com.starkenn.Starkenn;

import java.text.ParseException;
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
import com.starkenn.Starkenn.pageObjects.CompletedTripsPage;
import com.starkenn.Starkenn.pageObjects.DashboardPage;
import com.starkenn.Starkenn.pageObjects.DevicesPage;
import com.starkenn.Starkenn.pageObjects.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CompletedTrips {
	WebDriver driver;
	WebDriverWait wait;
	LoginPage loginPage;
	DashboardPage dashboardPage;
	BasePage basePage;
	DevicesPage devicePage ;
	CompletedTripsPage completedTripsPage; 
	
	@BeforeClass
	public void intializeDriver() throws InterruptedException{
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("http://sktn-react.s3-website.ap-south-1.amazonaws.com/");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		loginPage = new LoginPage(driver);
		loginPage.enterEmail("john@gmail.com");
		loginPage.enterPassword("123456");
		loginPage.clickOnSubmit();
		Thread.sleep(3000);
		wait= new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("/customer-dashboard"));
		dashboardPage = new DashboardPage(driver);
		basePage = new BasePage(driver);
		devicePage = new DevicesPage(driver);
		completedTripsPage = new CompletedTripsPage(driver);
		
	}
	
	@BeforeMethod
	public void openUrl() throws InterruptedException{
	driver.get("http://sktn-react.s3-website.ap-south-1.amazonaws.com/customer-dashboard");
	dashboardPage.clickOnCompleted();
	Thread.sleep(3000);

	wait.until(ExpectedConditions.urlContains("/completed-trips/"));
	}
	
	@Test
	public void verifyTotalRecordsCount () 
	{
		basePage.selectRecordsPerPage("30");
		int noOfRecorsInTable = basePage.getNumberOfRecordsInTable();
		int totalRecordsCount = basePage.getTotalRecords();
		Assert.assertEquals(noOfRecorsInTable, totalRecordsCount);
	}
	

	@Test
	public void verifyCompletedtripButton_OngoingTripButton () throws InterruptedException 
	
	{
	completedTripsPage.clickOnOngoingTrips();
	Thread.sleep(3000);
	String pageTitle=basePage.getPageTitle();
	Assert.assertEquals(pageTitle, "Ongoing Trips");
	completedTripsPage.clickOnCompletedTrips();
	Thread.sleep(3000);
	 pageTitle=basePage.getPageTitle();
	Assert.assertEquals(pageTitle, "Completed Trips");


	}
	@Test
	public void verifyTripIdSearch () 
	{
		String searchTripId = "EC0992A1684298258" ;
		basePage.selectRecordsPerPage("30");
		completedTripsPage.searchCompletedTripId(searchTripId);
		String tripId = completedTripsPage.getTripId(0);
		Assert.assertEquals(tripId, searchTripId);

	}
	
	@Test
	public void tripSummery () throws ParseException 
	{
		
		completedTripsPage.clickOnviewbutton(1);
		wait.until(ExpectedConditions.visibilityOf(completedTripsPage.Summarytab));

		String sourceDate = completedTripsPage.getSourcecDate();
		String destinationDate = completedTripsPage.getDestinationDate();
		long timedurationInSeconds = basePage.getDuration(sourceDate, destinationDate);
		String timeDuration = basePage.getDuration(timedurationInSeconds);
		System.out.println(timeDuration);
		String timeTravelled = completedTripsPage.getTravelledTime();
		Assert.assertEquals(timeDuration, timeTravelled);
		String distance = completedTripsPage.getTotalDistance();
		String averageSpeed = completedTripsPage.getAverageeSpeed();
		double averagespeedcalculated = basePage.averageSpeed(distance, timedurationInSeconds);
		Assert.assertEquals(averageSpeed, averagespeedcalculated+"Kmph");
        

	}

	@AfterClass
	public void closeBrowser() {
		driver.quit();
	}

	
	
	
}
