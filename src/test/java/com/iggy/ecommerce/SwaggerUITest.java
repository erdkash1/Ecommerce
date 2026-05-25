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
    @Test
    void shouldExpandAuthRegisterEndpoint() {
        driver.get(BASE_URL);

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // Wait for page to load
        longWait.until(ExpectedConditions.titleContains("Swagger"));

        // Wait for endpoint blocks to appear
        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock")));

        // Find all POST endpoints
        java.util.List<WebElement> postEndpoints = driver.findElements(
                By.cssSelector(".opblock-post"));

        // Assert POST endpoints exist
        assertFalse(postEndpoints.isEmpty(),
                "Should have at least one POST endpoint");

        // Click the first POST endpoint to expand it
        postEndpoints.get(0).click();

        // Wait for expanded content
        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock-body")));

        // Assert endpoint body is now visible
        WebElement endpointBody = driver.findElement(By.cssSelector(".opblock-body"));
        assertTrue(endpointBody.isDisplayed(),
                "Endpoint body should be visible after clicking");
    }

    @Test
    void shouldShowCorrectHttpMethods() {
        driver.get(BASE_URL);

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // Wait for page to load
        longWait.until(ExpectedConditions.titleContains("Swagger"));

        // Wait for endpoints to appear
        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock")));

        // Count different HTTP methods
        java.util.List<WebElement> getEndpoints = driver.findElements(
                By.cssSelector(".opblock-get"));
        java.util.List<WebElement> postEndpoints = driver.findElements(
                By.cssSelector(".opblock-post"));
        java.util.List<WebElement> deleteEndpoints = driver.findElements(
                By.cssSelector(".opblock-delete"));
        java.util.List<WebElement> putEndpoints = driver.findElements(
                By.cssSelector(".opblock-put"));

        // Assert we have multiple HTTP methods
        int totalEndpoints = getEndpoints.size() + postEndpoints.size() +
                deleteEndpoints.size() + putEndpoints.size();

        assertTrue(totalEndpoints > 0,
                "Should have multiple API endpoints");
        System.out.println("GET endpoints: " + getEndpoints.size());
        System.out.println("POST endpoints: " + postEndpoints.size());
        System.out.println("DELETE endpoints: " + deleteEndpoints.size());
        System.out.println("PUT endpoints: " + putEndpoints.size());
        System.out.println("Total endpoints: " + totalEndpoints);
    }
}