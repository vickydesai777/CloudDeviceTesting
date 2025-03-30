
package com.perfecto.advanced;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;
import com.perfecto.sampleproject.PerfectoLabUtils;

import io.appium.java_client.AppiumDriver;

public class Audio_To_Text {
	RemoteWebDriver driver;
	ReportiumClient reportiumClient;
	

	@Test
	public void AudioToText() throws Exception {

		String cloudName = "trial";
		String securityToken = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2ZDM2NmJiNS01NDAyLTQ4MmMtYTVhOC1kODZhODk4MDYyZjIifQ.eyJpYXQiOjE3NDMxODc1NDAsImp0aSI6ImVlNDNjYzJhLTZiZjAtNGNlMS1iY2VhLWQ0NTQyYmI5ZDJjMCIsImlzcyI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsInN1YiI6ImExNjA4YzU1LWE4ZDktNGI4OS05ODA5LWZiODFjOWIzOWIzMiIsInR5cCI6Ik9mZmxpbmUiLCJhenAiOiJvZmZsaW5lLXRva2VuLWdlbmVyYXRvciIsIm5vbmNlIjoiM2U1NDE2YTctMjc4NC00MjBhLWExZjctNDVhYmVjZGMwNmFkIiwic2Vzc2lvbl9zdGF0ZSI6IjNiMzJkNmZiLWVlNzQtNDk0NC1hYmI5LWQ4NGE2M2Q5YzM1YSIsInNjb3BlIjoib3BlbmlkIG9mZmxpbmVfYWNjZXNzIHByb2ZpbGUgZW1haWwiLCJzaWQiOiIzYjMyZDZmYi1lZTc0LTQ5NDQtYWJiOS1kODRhNjNkOWMzNWEifQ.XsPRojWJ5OGn1LeG4P1OWRf39rjgxJHRs8xLiP38Mag";
		String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		capabilities.setCapability("securityToken", PerfectoLabUtils.fetchSecurityToken(securityToken));
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("enableAppiumBehavior", true);
		capabilities.setCapability("openDeviceTimeout", 2);
		capabilities.setCapability("appPackage", "com.android.settings");
		capabilities.setCapability("appActivity", "com.android.settings.Settings");
		capabilities.setCapability("automationName", "Appium");
		driver = (RemoteWebDriver) (new AppiumDriver<>(new URL("https://" + PerfectoLabUtils.fetchCloudName(cloudName)
				+ ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities));
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		reportiumClient = PerfectoLabUtils.setReportiumClient(driver, reportiumClient); // Creates reportiumClient
		reportiumClient.testStart("Audio_2_Text", new TestContext("audio"));
		reportiumClient.stepStart("audio to text");
		Map<String, Object> params1 = new HashMap<>();
		//need mp3 file in repository
		params1.put("key", "PRIVATE:mysong.mp3");
		params1.put("language", "us-english");
		params1.put("rate", "broad");
		params1.put("profile", "accuracy");
		String text = ((String) driver.executeScript("mobile:audio:text", params1));
		FileUtils.deleteQuietly(new File("output.txt"));
		FileWriter myWriter = new FileWriter(new File("output.txt"));
		myWriter.write(text);
		myWriter.close();
		reportiumClient.stepEnd();
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		TestResult testResult = null;
		if (result.getStatus() == result.SUCCESS) {
			testResult = TestResultFactory.createSuccess();
		} else if (result.getStatus() == result.FAILURE) {
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
