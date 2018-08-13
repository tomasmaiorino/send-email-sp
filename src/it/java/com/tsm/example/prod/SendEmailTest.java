package com.tsm.example.prod;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class SendEmailTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45, true);
		baseUrl = "http://tomasmaiorino.github.io/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	@Ignore
	public void testSendEmail() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("Contact Me")).click();
		driver.findElement(By.id("senderName")).clear();
		driver.findElement(By.id("senderName")).sendKeys("tomas");
		driver.findElement(By.id("senderEmail")).clear();
		driver.findElement(By.id("senderEmail")).sendKeys("user@domain.com");
		driver.findElement(By.id("message")).clear();
		driver.findElement(By.id("message")).sendKeys("test selenium");
		driver.findElement(By.id("btnSubmit")).click();
		driver.findElement(By.cssSelector("div.alert.alert-success > button.close")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	@SuppressWarnings("unused")
	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
