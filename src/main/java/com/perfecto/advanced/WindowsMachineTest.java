package com.perfecto.advanced;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import com.perfecto.sampleproject.PerfectoLabUtils;

import io.appium.java_client.android.AndroidDriver;

public class WindowsMachineTest {
	private static RemoteWebDriver driver;
	static ReportiumClient reportiumClient;
	StopWatch pageLoad = new StopWatch();

	public static void main(String[] args) throws Exception {
		System.out.println("Run started");

		// TODO: change your cloud url
		String securityToken = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2ZDM2NmJiNS01NDAyLTQ4MmMtYTVhOC1kODZhODk4MDYyZjIifQ.eyJpYXQiOjE3NDMxODc1NDAsImp0aSI6ImVlNDNjYzJhLTZiZjAtNGNlMS1iY2VhLWQ0NTQyYmI5ZDJjMCIsImlzcyI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsInN1YiI6ImExNjA4YzU1LWE4ZDktNGI4OS05ODA5LWZiODFjOWIzOWIzMiIsInR5cCI6Ik9mZmxpbmUiLCJhenAiOiJvZmZsaW5lLXRva2VuLWdlbmVyYXRvciIsIm5vbmNlIjoiM2U1NDE2YTctMjc4NC00MjBhLWExZjctNDVhYmVjZGMwNmFkIiwic2Vzc2lvbl9zdGF0ZSI6IjNiMzJkNmZiLWVlNzQtNDk0NC1hYmI5LWQ4NGE2M2Q5YzM1YSIsInNjb3BlIjoib3BlbmlkIG9mZmxpbmVfYWNjZXNzIHByb2ZpbGUgZW1haWwiLCJzaWQiOiIzYjMyZDZmYi1lZTc0LTQ5NDQtYWJiOS1kODRhNjNkOWMzNWEifQ.XsPRojWJ5OGn1LeG4P1OWRf39rjgxJHRs8xLiP38Mag";
		// TODO: change your security Token
		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("securityToken", securityToken);
		capabilities.setCapability("platformVersion", "11");
		capabilities.setCapability("browserName", "Chrome");
		capabilities.setCapability("browserVersion", "134");
		capabilities.setCapability("location", "US East");
		capabilities.setCapability("resolution", "1024x768");
		
		driver = new RemoteWebDriver(new URL("https://trial.perfectomobile.com/nexperience/perfectomobile/wd/hub"),
				capabilities);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		reportiumClient = PerfectoLabUtils.setReportiumClient(driver, reportiumClient); // Creates reportiumClient
		reportiumClient.testStart("SUT Performance test", new TestContext("tag1"));
		driver.get("https://bstackdemo.com/");
        String product_name = driver.findElement(By.xpath("//*[@id='1']/p")).getText();
        driver.findElement(By.xpath("//*[@id='1']/div[4]")).click();
        String product_in_cart=driver.findElement(By.xpath("//*[@class='float-cart float-cart--open']//p[@class='title']")).getText();
	}
}