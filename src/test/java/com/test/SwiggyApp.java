package com.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class SwiggyApp {

	private AndroidDriver driver;

	@BeforeMethod
	public void setUp()  throws Throwable{

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("platformName", "Android");
		desiredCapabilities.setCapability("appium:platformVersion", "6.0");
		desiredCapabilities.setCapability("appium:deviceName", "Android SDK built for x86");
		desiredCapabilities.setCapability("appium:appPackage", "in.swiggy.android");
		desiredCapabilities.setCapability("appium:appActivity", "in.swiggy.android.activities.HomeActivity");
		desiredCapabilities.setCapability("appium:ensureWebviewsHavePages", true);
		desiredCapabilities.setCapability("appium:nativeWebScreenshot", true);
		desiredCapabilities.setCapability("appium:newCommandTimeout", 3600);
		desiredCapabilities.setCapability("appium:connectHardwareKeyboard", true);

		URL remoteUrl = new URL("http://localhost:4723/wd/hub");

		driver = new AndroidDriver(remoteUrl, desiredCapabilities);
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		getTextViewbyText("Not Today").click();
		getTextViewbyText("SET DELIVERY LOCATION").click();
		driver.findElementByXPath("//android.widget.Button[@text='Allow']").click();
		Thread.sleep(3000);
		getTextViewbyText("CHANGE").click();
		driver.findElementById("in.swiggy.android:id/location_description").sendKeys("Bangalore");
		//getEditTextbyText("Search for area, street name…").sendKeys("Bangalore");
		getTextViewbyText("Bangalore").click();
		getTextViewbyText("CONFIRM LOCATION").click();
	}

	public MobileElement getTextViewbyText(String text) {

		return (MobileElement) driver.findElementByXPath("//android.widget.TextView[@text=\"" + text + "\"]");

	}

	public MobileElement getEditTextbyText(String text) {

		return (MobileElement) driver.findElementByXPath("//android.widget.EditText[@text=\"" + text + "\"]");

	}

	@Test
	public void SearchFood_AddtoCart() throws Throwable {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		getTextViewbyText("SEARCH").click();
		getEditTextbyText("Search for restaurants and food").click();
		getEditTextbyText("Search for restaurants and food").sendKeys("Pasta");
		Thread.sleep(3000);
		getTextViewbyText("Pasta").click();
		getTextViewbyText("Add").click();
		//getTextViewbyText("Continue").click();
		getTextViewbyText("Add Item").click();
		getTextViewbyText("Checkout").click();
		Thread.sleep(5000);
		driver.findElementByXPath("//android.widget.Button[@content-desc='Proceed with Phone Number']").click();
		String Expected="Almost There";
		String Actual=getTextViewbyText("Almost There").getText();
		Assert.assertEquals(Expected, Actual);
	}
	
	@Test
	public void validate_IncorrectLogin() throws Throwable {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		getTextViewbyText("ACCOUNT").click();
		getTextViewbyText("Login").click();
		Thread.sleep(5000);
		driver.findElementByClassName("android.widget.EditText").sendKeys("9876543211");
		getTextViewbyText("Continue").click();
		driver.findElementByXPath("//android.widget.Button[@text='Deny']").click();
		getEditTextbyText("EMAIL ADDRESS").sendKeys("simran1@xyz.com");
		getEditTextbyText("NAME").sendKeys("Simran");
		driver.findElementById("in.swiggy.android:id/signup_action").click();
		Thread.sleep(5000);
		driver.findElementByClassName("android.widget.EditText").sendKeys("123456");
		getTextViewbyText("VERIFY AND PROCEED").click();
		
		String Expected="Invalid or incorrect OTP";
		String Actual=getTextViewbyText("Invalid or incorrect OTP").getText();
		Assert.assertEquals(Expected, Actual);
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}
