package bershika.route.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Driver {
	private static WebDriver driver = new HtmlUnitDriver();

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		Driver.driver = driver;
	}
	
}
