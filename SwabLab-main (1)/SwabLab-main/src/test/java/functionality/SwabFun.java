package functionality;

import org.gscode.base.Locators;
import org.gscode.base.SeleniumBase;
import org.testng.annotations.Test;

public class SwabFun extends SeleniumBase {
	
	
	@Test
	void funality() {
		//login function
		createtest("SWAG-lABfunality","Guru" ,"smoke");
		get("https://www.saucedemo.com/");
		type(element(Locators.id,"user-name"),"standard_user");
		type(element(Locators.id,"password"),"secret_sauce");
		screenshot("login");
		stepreport("pass","login successfully","login");
		click(element(Locators.id,"login-button"));
		
		//icon is displayed
		isDisplayed(element(Locators.xpath,"//div[text()='Swag Labs']"));
		isDisplayed(element(Locators.xpath,"//span[text()='Products']"));
		stepreport("pass","Icon are displayed","icons");
		
		
		// add to cart
		
		click(element(Locators.id,"add-to-cart-sauce-labs-backpack"));
		click(element(Locators.id,"add-to-cart-sauce-labs-bike-light"));
		click(element(Locators.id,"add-to-cart-test.allthethings()-t-shirt-(red)"));
		isDisplayed(element(Locators.classname,"shopping_cart_badge"));
		stepreport("pass","add to cart successfully" ,"add to cart");
	
		
		//claer the cart
		click(element(Locators.xpath,"//div[@class='shopping_cart_container']//a[1]"));
		click(element(Locators.id,"remove-sauce-labs-bike-light"));
		stepreport("pass","claer the one of the item in the list","claer the cart");
		
		//checkout 
		click(element(Locators.id,"checkout"));
		isDisplayed(element(Locators.xpath,"//span[text()='Checkout: Your Information']"));
		type(element(Locators.id,"first-name"),"Gurusivam");
		type(element(Locators.id,"last-name"),"K L");
		type(element(Locators.id,"postal-code"),"641666");
		stepreport("pass","enter the user details","user details");
		click(element(Locators.id,"continue"));
		stepreport("pass","final Purchase summary","final summary");
		click(element(Locators.id,"finish"));
		stepreport("pass","checkout successfully","checkout");
		
		quit();
	}
	
	

}
