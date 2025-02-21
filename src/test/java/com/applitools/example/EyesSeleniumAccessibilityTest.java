package com.applitools.example;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.ChromeEmulationInfo;
import com.applitools.eyes.visualgrid.model.DesktopBrowserInfo;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.assertj.core.api.Assertions.*;

import java.time.Duration;

public class EyesSeleniumAccessibilityTest {
    private static final BatchInfo BATCH = new BatchInfo("Selenium Java Basic - Accessibility");
    private static final boolean USE_ULTRAFAST_GRID = true;

    private EyesRunner runner = null;
    private Eyes eyes = null;
    private WebDriver driver = null;

    @BeforeEach
    public void setUp(){
        Configuration config = new Configuration();
        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        config.setBatch(BATCH);
        config.setStitchMode(StitchMode.CSS);

        //Configure WCAG version and level for Accessibility/Contrast validation
        config.setAccessibilityValidation(
                new AccessibilitySettings(
                        AccessibilityLevel.AAA,
                        AccessibilityGuidelinesVersion.WCAG_2_1
                )
        );

        if(USE_ULTRAFAST_GRID) {
            // Configure Applitools SDK to run on the Ultrafast Grid
            runner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));
            config.addBrowsers(
                new DesktopBrowserInfo(800, 1024, BrowserType.CHROME),
                new DesktopBrowserInfo(1600, 1200, BrowserType.FIREFOX),
                new DesktopBrowserInfo(1024, 768, BrowserType.SAFARI),
                new ChromeEmulationInfo(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT),
                new ChromeEmulationInfo(DeviceName.Nexus_10, ScreenOrientation.LANDSCAPE)
            );
            config.setLayoutBreakpoints(true);
        } else {
            runner = new ClassicRunner();
        }

        eyes = new Eyes(runner);
        eyes.setConfiguration(config);

        ChromeOptions options = new ChromeOptions();
        String headless = System.getenv("HEADLESS");
        if(headless != null || USE_ULTRAFAST_GRID) {
            options.addArguments("--headless");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown(){
        if (eyes != null)
            eyes.abortIfNotClosed();

        if (driver != null)
            driver.quit();
    }

    @AfterAll
    public static void afterAll(){
        if (runner != null) {
            TestResultsSummary allTestResults = runner.getAllTestResults();
            System.out.println(allTestResults);
        }
    }

    @Test
    public void testAccessibility(){
        // Start Applitools Visual AI Test
        eyes.open(
                driver,
                "ACME Bank",
                "Selenium Java Basic: Accessibility",
                new RectangleSize(1200, 600)
        );
        driver.get("https://sandbox.applitools.com/bank?layoutAlgo=true");

        // Full Page - Visual AI Assertion
        eyes.check(Target.window().fully().withName("Login page").scrollRootElement(By.tagName("html")));

        driver.findElement(By.id("username")).sendKeys("user");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("log-in")).click();

        // Full Page - Visual AI Assertion
        eyes.check(
                Target.window().fully().withName("Main page")
                        .layout(
                                By.cssSelector(".dashboardOverview_accountBalances__3TUPB"),
                                By.cssSelector(".dashboardTable_dbTable___R5Du")
                        )
        );

        // End Applitools Visual AI Test
        TestResults result = eyes.close(false);

        // Assert that the Accessibility Contrast test passed
        assertThat(result.getAccessibilityStatus().getStatus()).isEqualTo(AccessibilityStatus.Passed);
    }
}