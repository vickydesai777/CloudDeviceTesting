package com.perfecto.advanced;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;
import com.perfecto.sampleproject.PerfectoLabUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class InstallApp_FromLocal_AndroidDriver {
	AndroidDriver<AndroidElement> driver;
	ReportiumClient reportiumClient;

	@Test
	public void androidDriverInstall() throws Exception {
		String cloudName = "trial";
		String securityToken = "SecurityToken";
		
		cloudName = PerfectoLabUtils.fetchCloudName(cloudName);
		securityToken = PerfectoLabUtils.fetchSecurityToken(securityToken);
		String repositoryKey = "PRIVATE:ExpenseTracker/Native/android/ExpenseAppVer1.0.apk";
		String localFilePath = System.getProperty("user.dir") + "//FileToUpload//ExpenseAppVer1.0.apk";
		PerfectoLabUtils.uploadMedia(cloudName, securityToken, localFilePath, repositoryKey);
		
		String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		capabilities.setCapability("model", "Pixel 7");
		capabilities.setCapability("enableAppiumBehavior", true);
		capabilities.setCapability("openDeviceTimeout", 2);
		capabilities.setCapability("app", repositoryKey); // Set Perfecto Media repository path of App under test.
		capabilities.setCapability("appPackage", "io.perfecto.expense.tracker"); // Set the unique identifier of your app
		capabilities.setCapability("autoLaunch", true); // Whether to install and launch the app automatically.
		capabilities.setCapability("takesScreenshot", false);
		capabilities.setCapability("screenshotOnError", true); // Take screenshot only on errors
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("securityToken", securityToken);
		driver = new AndroidDriver<AndroidElement>(new URL("https://" + cloudName  + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities); 
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		reportiumClient = PerfectoLabUtils.setReportiumClient(driver, reportiumClient); //Creates reportiumClient
		reportiumClient.testStart("Android Java Native Sample", new TestContext("tag2", "tag3")); //Starts the reportium test
		try {
			driver.activateApp("ExpenseAppVer1.0.apk");	
		}
         catch(Exception e)
		{
         System.out.print("Do nothing");
		}
		reportiumClient.stepStart("Enter email");
		WebDriverWait wait = new WebDriverWait(driver, 30);
		AndroidElement email = 
				driver.findElement(By.xpath("//*[@resource-id='io.perfecto.expense.tracker:id/login_email']"));
		email.sendKeys("test@perfecto.com");
		reportiumClient.stepEnd();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		reportiumClient.stepStart("Enter password");
		AndroidElement password = 
				driver.findElement(By.xpath("//*[@resource-id=\"io.perfecto.expense.tracker:id/login_password\"]"));
		password.sendKeys("test123");
		reportiumClient.stepEnd();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		reportiumClient.stepStart("Click login");
		AndroidElement login = 
				driver.findElement(By.xpath("//*[@resource-id='io.perfecto.expense.tracker:id/login_login_btn']"));
		login.click();
		reportiumClient.stepEnd();
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		reportiumClient.stepStart("Login Successful");
		driver.findElement(By.id("list_add_btn")).isDisplayed();
		driver.getCurrentPackage();
		driver.getOrientation();
		driver.toggleWifi();
		driver.toggleWifi();
		driver.openNotifications();
		driver.lockDevice();
		driver.unlockDevice();
		reportiumClient.stepEnd();
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		TestResult testResult = null;
		if(result.getStatus() == result.SUCCESS) {
			testResult = TestResultFactory.createSuccess();
		}
		else if (result.getStatus() == result.FAILURE) {
			testResult = TestResultFactory.createFailure(result.getThrowable());
		}
		reportiumClient.testStop(testResult);

		driver.close();
		driver.quit();
		// Retrieve the URL to the DigitalZoom Report 
		String reportURL = reportiumClient.getReportUrl();
		System.out.println(reportURL);
	}



}

