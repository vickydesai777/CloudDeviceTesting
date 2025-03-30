
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
		String securityToken = "SecurityToken";
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
		String reportURL = reportiumClient.getReportUrl();
		System.out.println(reportURL);
	}

}
