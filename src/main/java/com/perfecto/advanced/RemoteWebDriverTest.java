package com.perfecto.advanced;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;
import com.perfecto.sampleproject.PerfectoLabUtils;

public class RemoteWebDriverTest {
	private static RemoteWebDriver driver;
	ReportiumClient reportiumClient;
	StopWatch pageLoad = new StopWatch();

	@Test
	public void RemoteWebDriverPractice() throws Exception {
		
		String securityToken = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2ZDM2NmJiNS01NDAyLTQ4MmMtYTVhOC1kODZhODk4MDYyZjIifQ.eyJpYXQiOjE3NDMxODc1NDAsImp0aSI6ImVlNDNjYzJhLTZiZjAtNGNlMS1iY2VhLWQ0NTQyYmI5ZDJjMCIsImlzcyI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsInN1YiI6ImExNjA4YzU1LWE4ZDktNGI4OS05ODA5LWZiODFjOWIzOWIzMiIsInR5cCI6Ik9mZmxpbmUiLCJhenAiOiJvZmZsaW5lLXRva2VuLWdlbmVyYXRvciIsIm5vbmNlIjoiM2U1NDE2YTctMjc4NC00MjBhLWExZjctNDVhYmVjZGMwNmFkIiwic2Vzc2lvbl9zdGF0ZSI6IjNiMzJkNmZiLWVlNzQtNDk0NC1hYmI5LWQ4NGE2M2Q5YzM1YSIsInNjb3BlIjoib3BlbmlkIG9mZmxpbmVfYWNjZXNzIHByb2ZpbGUgZW1haWwiLCJzaWQiOiIzYjMyZDZmYi1lZTc0LTQ5NDQtYWJiOS1kODRhNjNkOWMzNWEifQ.XsPRojWJ5OGn1LeG4P1OWRf39rjgxJHRs8xLiP38Mag";
		String platformName = "Android";
		DesiredCapabilities capabilities = new DesiredCapabilities("mobileOS", "", Platform.ANY);
		capabilities.setCapability("securityToken", PerfectoLabUtils.fetchSecurityToken(securityToken));
		capabilities.setCapability("useAppiumForWeb", "true");
		capabilities.setCapability("model", "Pixel 7");
		capabilities.setCapability("platformName", platformName);
		capabilities.setCapability("openDeviceTimeout", 15);
		capabilities.setCapability("autoLaunch", true);
		capabilities.setCapability("automationName", "Appium");
		driver = new RemoteWebDriver(new URL("https://trial.perfectomobile.com/nexperience/perfectomobile/wd/hub"),
				capabilities);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

		reportiumClient = PerfectoLabUtils.setReportiumClient(driver, reportiumClient); // Creates reportiumClient
		reportiumClient.testStart("SUT Performance test", new TestContext("tag1")); // Starts the reportium test
		reportiumClient.stepStart("start virtual network");
		Map<String, Object> pars = new HashMap<>();
		pars.put("profile", "4g_lte_good");
		pars.put("generateHarFile", "true");
		driver.executeScript("mobile:vnetwork:start", pars);

		// Set location
		reportiumClient.stepStart("set location");
		Map<String, Object> params = new HashMap<>();
		params.put("address", "Pune, India");
		driver.executeScript("mobile:location:set", params);

		// Run background application
		reportiumClient.stepStart("start background apps");
		String backGroundApps = "YouTube,Messages,Maps,Calculator,Calendar,Chrome";
		String[] bApps = backGroundApps.split(",");
		for (String i : bApps) {
			try {
				Map<String, Object> params1 = new HashMap<>();
				params1.put("name", i);
				driver.executeScript("mobile:application:open", params1);
			} catch (Exception e) {
			}
		}

		// Device Vitals
		reportiumClient.stepStart("start device vitals");
		Map<String, Object> params2 = new HashMap<>();
		params2.put("sources", "Device");
		driver.executeScript("mobile:monitor:start", params2);

		// Launch Web application
		reportiumClient.stepStart("User experience timer with Visual text");
		switchToContext(driver, "WEBVIEW");
		driver.get("https://www.perfecto.io");
		Thread.sleep(2000);
		driver.get("https://www.etihad.com/en-us/book");

		Map<String, Object> installParams = new HashMap<>();
		installParams.put("file", "PUBLIC:PMTest-v1.0.apk");
		driver.executeScript("mobile:application:install", installParams);
		
		HashMap<String, Object> params4 = new HashMap<>();
		params4.put("name", "PMTest");
		driver.executeScript("mobile:application:open", params4);
		Thread.sleep(20000);

		// Method 2: Custom timer for xpaths
		reportiumClient.stepStart("Custom timer with xpath");
		driver.get("https://www.perfecto.io");
		Thread.sleep(2000);
		startTimer(pageLoad);
		driver.get("https://www.etihad.com/en-us/book");
		// stop Network Virtualization
		reportiumClient.stepStart("stop virtual network and device vitals");
		Map<String, Object> pars1 = new HashMap<>();
		driver.executeScript("mobile:vnetwork:stop", pars1);

		// stop vitals
		Map<String, Object> params3 = new HashMap<>();
		driver.executeScript("mobile:monitor:stop", params3);
	}

	private static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	private static long timerGet(RemoteWebDriver driver, String timerType) {
		String command = "mobile:timer:info";
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", timerType);
		params.put("timerId", "myTime");
		long result = (long) driver.executeScript(command, params);
		return result;
	}

	private static void TextValidation(RemoteWebDriver driver, String content) {
		// verify that the correct page is displayed as result of signing in.
		Map<String, Object> params1 = new HashMap<>();
		// Check for the text that indicates that the sign in was successful
		params1.put("content", content);
		// allow up-to 30 seconds for the page to display
		params1.put("timeout", "40");
		// Wind Tunnel: Adding accurate timers to text checkpoint command
		params1.put("measurement", "accurate");
		params1.put("source", "camera");
		params1.put("analysis", "automatic");
		params1.put("threshold", "90");
		params1.put("index", "1");
		String resultString = (String) driver.executeScript("mobile:checkpoint:text", params1);
	}

	public static String reportTimer(RemoteWebDriver driver, long result, long threshold, String description,
			String name) {
		Map<String, Object> params = new HashMap<String, Object>(7);
		params.put("result", result);
		params.put("threshold", threshold);
		params.put("description", description);
		params.put("name", name);
		String status = (String) driver.executeScript("mobile:status:timer", params);
		return status;
	}

	public static void startTimer(StopWatch pageLoad) {
		pageLoad.start();
	}

	public static void stopTimer(StopWatch pageLoad) {
		pageLoad.stop();
	}

	public static void measureTimer(RemoteWebDriver driver, StopWatch pageLoad, long threshold, String description,
			String name) throws Exception {
		long result = pageLoad.getTime() > 820 ? pageLoad.getTime() - 820 : 0;
		System.out.println("Captured custom time for App launch is: " + result + "ms");
		reportTimer(driver, result, threshold, description, name);
		if (result > threshold) {
			throw new Exception("Timer for " + description + " failed!");
		}
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		try {
			TestResult testResult = null;
			if (result.getStatus() == result.SUCCESS) {
				testResult = TestResultFactory.createSuccess();
			} else if (result.getStatus() == result.FAILURE) {
				testResult = TestResultFactory.createFailure(result.getThrowable());
			}
			reportiumClient.testStop(testResult);
			// Retrieve the URL to the DigitalZoom Report
			String reportURL = reportiumClient.getReportUrl();
			System.out.println(reportURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.quit();

	}

}