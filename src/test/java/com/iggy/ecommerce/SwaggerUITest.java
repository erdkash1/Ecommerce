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
        driver.get(BASE_URL);

        wait.until(ExpectedConditions.titleContains("Swagger"));

        String title = driver.getTitle();
        assertTrue(title.contains("Swagger"),
                "Page title should contain Swagger but was: " + title);
    }

    @Test
    void shouldDisplayApiEndpoints() {
        driver.get(BASE_URL);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".swagger-ui")));

        WebElement swaggerUI = driver.findElement(By.cssSelector(".swagger-ui"));
        assertNotNull(swaggerUI);
        assertTrue(swaggerUI.isDisplayed());
    }

    @Test
    void shouldDisplayAuthEndpoints() {
        driver.get(BASE_URL);

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(90));

        longWait.until(ExpectedConditions.titleContains("Swagger"));

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock-tag")));

        java.util.List<WebElement> tags = driver.findElements(
                By.cssSelector(".opblock-tag"));

        assertFalse(tags.isEmpty(), "Should have at least one API endpoint group");
        assertTrue(tags.size() >= 1, "Should display API endpoints");
    }
    @Test
    void shouldExpandAuthRegisterEndpoint() {
        driver.get(BASE_URL);

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        longWait.until(ExpectedConditions.titleContains("Swagger"));

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock")));

        java.util.List<WebElement> postEndpoints = driver.findElements(
                By.cssSelector(".opblock-post"));

        assertFalse(postEndpoints.isEmpty(),
                "Should have at least one POST endpoint");

        postEndpoints.get(0).click();

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock-body")));

        WebElement endpointBody = driver.findElement(By.cssSelector(".opblock-body"));
        assertTrue(endpointBody.isDisplayed(),
                "Endpoint body should be visible after clicking");
    }

    @Test
    void shouldShowCorrectHttpMethods() {
        driver.get(BASE_URL);

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        longWait.until(ExpectedConditions.titleContains("Swagger"));

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock")));

        java.util.List<WebElement> getEndpoints = driver.findElements(
                By.cssSelector(".opblock-get"));
        java.util.List<WebElement> postEndpoints = driver.findElements(
                By.cssSelector(".opblock-post"));
        java.util.List<WebElement> deleteEndpoints = driver.findElements(
                By.cssSelector(".opblock-delete"));
        java.util.List<WebElement> putEndpoints = driver.findElements(
                By.cssSelector(".opblock-put"));

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
    @Test
    void shouldHaveAtLeastTenEndpoints() {
       driver.get(BASE_URL);
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        longWait.until(ExpectedConditions.titleContains("Swagger"));

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock")));

        java.util.List<WebElement> endpoints = driver.findElements(
                By.cssSelector(".opblock"));

        assertTrue(endpoints.size() >= 10,
                "Should have at least 10 endpoints but found: " + endpoints.size());
    }

    @Test
    void shouldDisplayProductsEndpoints() {
        driver.get(BASE_URL);

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        longWait.until(ExpectedConditions.titleContains("Swagger"));

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock-tag")));

        java.util.List<WebElement> endpoints = driver.findElements(
                By.cssSelector(".opblock-tag"));

        boolean hasProductEndpoint = endpoints.stream()
                .anyMatch(e -> e.getText().toLowerCase().contains("product"));

        assertTrue(hasProductEndpoint,
                "Should display products endpoints");
    }

    @Test
    void shouldExecuteRegisterEndpoint() {
        driver.get(BASE_URL);

        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        longWait.until(ExpectedConditions.titleContains("Swagger"));

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".opblock-post")));

        java.util.List<WebElement> postEndpoints = driver.findElements(
                By.cssSelector(".opblock-post"));

        postEndpoints.get(0).click();

        longWait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".try-out__btn")));

        driver.findElement(By.cssSelector(".try-out__btn")).click();

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".body-param__text")));

        WebElement textarea = driver.findElement(By.cssSelector(".body-param__text"));
        textarea.clear();
        textarea.sendKeys("{\"name\": \"Test User\", \"email\": \"selenium@test.com\", \"password\": \"password123\"}");

        longWait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".execute")));

        driver.findElement(By.cssSelector(".execute")).click();

        longWait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".responses-inner")));

        WebElement response = driver.findElement(By.cssSelector(".responses-inner"));

        assertNotNull(response);
        assertTrue(response.isDisplayed(),
                "Response should be displayed after executing register endpoint");
    }


}