package com.demo.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class EcommerceFlowTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void testEcommerceFlow() {
        // Step 1: Open website
        driver.get("https://demowebshop.tricentis.com/");

        // Step 2: Click Login
        driver.findElement(By.className("ico-login")).click();

        // Step 3: Enter login credentials
        driver.findElement(By.id("Email")).sendKeys("adithya.qa.demo@gmail.com");
        driver.findElement(By.id("Password")).sendKeys("Test1234");
        driver.findElement(By.cssSelector("input[value='Log in']")).click();

        // Step 4: Wait for logout button (successful login)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ico-logout")));
        Assert.assertTrue(driver.findElement(By.className("ico-logout")).isDisplayed(), "Login failed!");

        // Step 5: Search for a product
        WebElement searchBox = driver.findElement(By.id("small-searchterms"));
        searchBox.sendKeys("computer");
        driver.findElement(By.cssSelector("input[value='Search']")).click();

        // Step 6: Add first item to cart
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-item")));
        WebElement firstAddToCart = driver.findElement(By.cssSelector(".product-item input[value='Add to cart']"));
        firstAddToCart.click();

        // Step 7: Wait for notification bar "The product has been added"
        WebElement notificationBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bar-notification")));

        // Step 8: Wait until the notification disappears
        wait.until(ExpectedConditions.invisibilityOf(notificationBar));

        // Step 9: Click on the Cart link
        WebElement cartLink = driver.findElement(By.className("cart-label"));
        cartLink.click();

        // Step 10: Verify cart page is opened
        wait.until(ExpectedConditions.titleContains("Shopping Cart"));
        Assert.assertTrue(driver.getTitle().contains("Shopping Cart"), "Cart page not opened!");

        System.out.println("✅ Test Passed: E-Commerce flow executed successfully.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
