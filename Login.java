package com.starkenn.Starkenn;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.starkenn.Starkenn.pageObjects.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Login {
	WebDriver driver;
	

	
	@BeforeMethod
	public void openUrl(){
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("http://sktn-react.s3-website.ap-south-1.amazonaws.com/");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	@Test
	public  void invalidEmail() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.enterPassword("12345");
		loginPage.clickOnSubmit();
		Thread.sleep(3000);
	

		String invaidEmailMsg = loginPage.getInvalidEmailMsg();
		Assert.assertEquals(invaidEmailMsg, "Please provide a valid Email");
	}
	
	@Test
	public  void invalidPassword() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.enterEmail("john@gmail.com");
		loginPage.clickOnSubmit();
		Thread.sleep(3000);


		String invaidPassworMsg = loginPage.getInvalidPasswordMsg();
		Assert.assertEquals(invaidPassworMsg, "Please provide a valid password.");
	}
	
	@Test
	public  void invalidEmailAndPassword() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.clickOnSubmit();
		Thread.sleep(3000);

		

		String invaidEmailMsg = loginPage.getInvalidEmailMsg();
		Assert.assertEquals(invaidEmailMsg, "Please provide a valid Email");
		String invaidPassworMsg = loginPage.getInvalidPasswordMsg();
		Assert.assertEquals(invaidPassworMsg, "Please provide a valid password.");
	}
	
	@Test
	public  void sucessMsgAfterValidCredentials() throws InterruptedException {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.enterEmail("john@gmail.com");
		loginPage.enterPassword("123456");
		loginPage.clickOnSubmit();
		Thread.sleep(3000);

		

		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("/customer-dashboard"));
		String currentUrl = driver.getCurrentUrl();
		Assert.assertEquals(currentUrl, "http://sktn-react.s3-website.ap-south-1.amazonaws.com/customer-dashboard");
	}

	@AfterMethod
	public void closeBrowser( ) {
		driver.quit();
	}
	
	
}
