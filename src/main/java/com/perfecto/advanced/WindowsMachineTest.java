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

		String securityToken = "SecurityToken";
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