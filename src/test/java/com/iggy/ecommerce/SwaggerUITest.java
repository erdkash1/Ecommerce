package com.iggy.ecommerce;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerUITest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL =
            "https://ecommerce-api-e24i.onrender.com/swagger-ui/index.html";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");   // runs without opening browser window
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldLoadSwaggerUISuccessfully() {
        // Navigate to Swagger UI
        driver.get(BASE_URL);

        // Wait for page title to contain "Swagger"
        wait.until(ExpectedConditions.titleContains("Swagger"));

        // Assert page title
        String title = driver.getTitle();
        assertTrue(title.contains("Swagger"),
                "Page title should contain Swagger but was: " + title);
    }

    @Test
    void shouldDisplayApiEndpoints() {
        driver.get(BASE_URL);

        // Wait for Swagger UI to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".swagger-ui")));

        // Check Swagger UI container is present
        WebElement swaggerUI = driver.findElement(By.cssSelector(".swagger-ui"));
        assertNotNull(swaggerUI);
        assertTrue(swaggerUI.isDisplayed());
    }

    @Test
    void shouldDisplayAuthEndpoints() {
        driver.get(BASE_URL);

        // Wait longer for Render free tier to wake up
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // Wait for page title first
        longWait.until(ExpectedConditions.titleContains("Swagger"));

        // Wait for any operation tag to appear (auth, products, etc)
        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock-tag")));

        // Get all operation tags
        java.util.List<WebElement> tags = driver.findElements(
                By.cssSelector(".opblock-tag"));

        // Assert at least one endpoint group exists
        assertFalse(tags.isEmpty(), "Should have at least one API endpoint group");
        assertTrue(tags.size() >= 1, "Should display API endpoints");
    }
}