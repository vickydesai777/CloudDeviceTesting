package com.perfecto.advanced;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Platform;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
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

public class PerfectoAppiumiOSTest {
	RemoteWebDriver driver;
	ReportiumClient reportiumClient;

	@Test
	public void PerfectoAppiumiOS() throws Exception {
		String cloudName = "trial";
		String securityToken = "SecurityToken";
		String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		capabilities.setCapability("securityToken", PerfectoLabUtils.fetchSecurityToken(securityToken));
		capabilities.setCapability("model", "iPhone-14 Pro Max");
		capabilities.setCapability("enableAppiumBehavior", true);
		capabilities.setCapability("openDeviceTimeout", 2);
		capabilities.setCapability("bundleId", "com.apple.mobilesafari");
		capabilities.setCapability("automationName", "Appium");
		driver = (RemoteWebDriver)(new AppiumDriver<>(new URL("https://trial.perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities)); 
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		reportiumClient = PerfectoLabUtils.setReportiumClient(driver, reportiumClient); //Creates reportiumClient
		Map<String, Object> params = new HashMap<>();
		params.put("identifier", "com.apple.Preferences");
		((AppiumDriver) driver).activateApp("demoddbank.perforce.com");
		((AppiumDriver) driver).terminateApp("demoddbank.perforce.com");
		((AppiumDriver) driver).activateApp("demoddbank.perforce.com");
		reportiumClient.stepEnd(); //Stops a reportium step
		reportiumClient.stepStart("Verify wifi turn off and on");
		driver.findElementByXPath("//*[@label=\"Enter Password\"]").sendKeys("xyz");
		driver.findElementByXPath("//*[@label=\"LogIn\"]").click();
		driver.findElementByXPath("//*[@label=\"Response status code was unacceptable: 400.\"]").isDisplayed();
		WebDriverWait wait = new WebDriverWait(driver, 15);
		Thread.sleep(5000);
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
		String reportURL = reportiumClient.getReportUrl();
		System.out.println(reportURL);
	}
}