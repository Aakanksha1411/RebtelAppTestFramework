package test.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class BaseTest {
	
	public AndroidDriver driver;
	
	AppiumDriverLocalService service;

	@BeforeClass
	public void configure() throws IOException {
		
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "//src//main//java//resources//data.properties");
		prop.load(fis);
		String apiumServerIPAddress = prop.getProperty("apiumServerIPAddress");
		String apiumServerPort = prop.getProperty("apiumServerPort");
		int port = Integer.parseInt(apiumServerPort);
		String hostName = apiumServerIPAddress + ":" + apiumServerPort;
		
		// This is the way to inject the any version of the apk
		String apkPath = prop.getProperty("apkPath");
		
		if (apkPath ==null || apkPath.isEmpty()) {
			// This is to provide a sample apk for testing the framework
			apkPath = System.getProperty("user.dir") + "//src//test//java//resources//rebtel6.24.0.apk";
		}
		
		String androidDeviceName = prop.getProperty("androidDeviceName");
		String nodejspath = prop.getProperty("nodejspath");
		
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("noReset", true);
		service = new AppiumServiceBuilder()
				.withAppiumJS(
						new File(nodejspath))
				.withIPAddress(apiumServerIPAddress).usingPort(port).withTimeout(Duration.ofSeconds(300)).build();
		service.start();
		
		UiAutomator2Options options = new UiAutomator2Options();
		options.setApp(apkPath);
		options.setDeviceName(androidDeviceName);
		
		driver = new AndroidDriver(new URL("http://" + hostName), options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	}

	@AfterClass
	public void teardown() {
		driver.quit();
		service.stop();
	}

}
