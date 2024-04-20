package org.example.webtesting;

import com.t4a.predict.PredictionLoader;
import com.t4a.processor.AIProcessingException;
import com.t4a.processor.selenium.SeleniumProcessor;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;

public class WebTestingWithAI {
    public static void main(String[] args) throws IOException, AIProcessingException {
        // Set the path of the ChromeDriver executable
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Setting headless mode
        options.addArguments("--disable-gpu");  // GPU hardware acceleration isn't useful in headless mode
        options.addArguments("--window-size=1920,1080");  // Set the window size
        WebDriver driver = new ChromeDriver(options);


        SeleniumProcessor seleniumProcessor = new SeleniumProcessor(driver);
        seleniumProcessor.processWebAction("go to website https://the-internet.herokuapp.com");
        boolean buttonPresent =  seleniumProcessor.trueFalseQuery("do you see Add/Remove Elements?");
        if(buttonPresent) {
            seleniumProcessor.processWebAction("click on Add/Remove Elements");
        } else {
            seleniumProcessor.processSingleAction("Create Jira ticket that add/remove elements is missing");
        }


        // Perform further actions or validations as needed

        // Close the browser window
        driver.quit();
    }
}
