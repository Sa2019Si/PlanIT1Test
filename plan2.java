package test1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class plan2 {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Drivers/chromedriver-win32/chromedriver-win32/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void navigateToHomePage() {
        driver.get("http://jupiter.cloud.planittesting.com");
    }


    // Test Case 1: Contact Form Error Handling
    @Test
    public void testContactFormValidation() {
        driver.findElement(By.linkText("Contact")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.findElement(By.xpath("//a[text()='Submit']")).click(); 


        // Verify error messages
        WebElement forenameError = driver.findElement(By.id("forename-err"));
        WebElement emailError = driver.findElement(By.id("email-err"));
        WebElement messageError = driver.findElement(By.id("message-err"));

        Assert.assertTrue(forenameError.isDisplayed(), "Forename error message is not displayed");
        Assert.assertTrue(emailError.isDisplayed(), "Email error message is not displayed");
        Assert.assertTrue(messageError.isDisplayed(), "Message error message is not displayed");

        Assert.assertEquals(forenameError.getText(), "Forename is required", "Forename error message mismatch");
        Assert.assertEquals(emailError.getText(), "Email is required", "Email error message mismatch");
        Assert.assertEquals(messageError.getText(), "Message is required", "Message error message mismatch");

        // Populate mandatory fields
        driver.findElement(By.id("forename")).sendKeys("Sagarika");
        driver.findElement(By.id("email")).sendKeys("Sagarika@emaill.com");
        driver.findElement(By.id("message")).sendKeys("This is a test message.");

        // Validate errors are gone
        driver.findElement(By.xpath("//a[text()='Submit']")).click();
    }

    // Test Case 2: Successful Contact Form Submission (Run 5 Times)
    @Test(invocationCount = 5)
    public void testSuccessfulFormSubmission() {
        driver.findElement(By.linkText("Contact")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        
        // Populate fields
        driver.findElement(By.id("forename")).sendKeys("Sagarika");
        driver.findElement(By.id("email")).sendKeys("Sagarika@emaill.com");
        driver.findElement(By.id("message")).sendKeys("This is a test message.");

        driver.findElement(By.xpath("//a[text()='Submit']")).click();

     // Wait for the success message to appear
        WebElement successMessage = driver.findElement(By.xpath("//div[contains(@class,'alert-success')]"));

        // Verify success message
        Assert.assertTrue(successMessage.isDisplayed(), "Success message is not displayed");
        Assert.assertTrue(successMessage.getText().contains("Thanks Sagarika"), "Success message text mismatch");
    }


    // Test Case 3: Shopping Cart Test
    @Test
    public void testShoppingCart() {
        driver.findElement(By.linkText("Shop")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

       
        // Add items to cart
        addItemToCart("Stuffed Frog", 2);
        addItemToCart("Fluffy Bunny", 5);
        addItemToCart("Valentine Bear", 3);

        // Go to cart
        driver.findElement(By.linkText("Cart (10)")).click();

      double total = getCartTotal();
        double expectedTotal = (10.99 * 2) + (9.99 * 5) + (14.99 * 3);
        Assert.assertEquals(total, expectedTotal, "Total amount is incorrect");
    }

    // Helper method to add items to cart
    private void addItemToCart(String productName, int quantity) {
        for (int i = 0; i < quantity; i++) {
            driver.findElement(By.xpath("//h4[text()='" + productName + "']/following-sibling::p/a")).click();
        }
    }

    // Helper method to get cart total
    private double getCartTotal() {
        WebElement totalElement = driver.findElement(By.xpath("//strong[contains(text(),'Total:')]"));
        return Double.parseDouble(totalElement.getText().replace("Total:", ""));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
    
}
    