package stepdefinition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.CucumberRunner;

public class FirstStepDef extends CucumberRunner {

	@Given("I am on google search page")
	public void openChrome() {
		driver.get("https://www.google.com");

	}

	@When("I type cucumber")
	public void typeCucumber() {
		new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
		WebElement searchBar = driver.findElement(By.name("q"));

	}

	@Then("I click search button")
	public void searchButton() {
		driver.findElement(By.name("q")).sendKeys("Cucumber");
		driver.findElement(By.xpath("//*[@css='BUTTON.Tg7LZd']")).click();
	}

}
