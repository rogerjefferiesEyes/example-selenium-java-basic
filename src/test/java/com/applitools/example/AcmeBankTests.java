package com.applitools.example;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.ChromeEmulationInfo;
import com.applitools.eyes.visualgrid.model.DesktopBrowserInfo;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class AcmeBankTests {

    private static BatchInfo BATCH;
    private static EyesRunner runner;
    private static void setup(){
        BATCH = new BatchInfo("Selenium Java Basic Quickstart");
        runner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));
    }

    private static void tearDown(){
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        System.out.println(allTestResults);
    }

    private static Eyes getEyes(){
        Eyes eyes = null;
        // Configure Applitools SDK to run on the Ultrafast Grid
        eyes = new Eyes(runner);
        Configuration config = eyes.getConfiguration();
        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        config.setBatch(BATCH);
        config.addBrowsers(
                new DesktopBrowserInfo(800, 1024, BrowserType.CHROME),
                new DesktopBrowserInfo(1600, 1200, BrowserType.FIREFOX),
                new DesktopBrowserInfo(1024, 768, BrowserType.SAFARI),
                new ChromeEmulationInfo(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT),
                new ChromeEmulationInfo(DeviceName.Nexus_10, ScreenOrientation.LANDSCAPE)
        );
        eyes.setConfiguration(config);
        return eyes;
    }

    private static WebDriver getDriver(){
        WebDriver driver = null;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        if(System.getenv("GITHUB_ACTIONS") != null) {
            options.addArguments("--headless");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }

    private static void testAcmeBankPage(){

        Eyes eyes = null;
        WebDriver driver = null;

        try {
            eyes = getEyes();
            driver = getDriver();

            // Start Applitools Visual AI Test
            eyes.open(driver,"ACME Bank", "Selenium Java Basic: Quickstart", new RectangleSize(1200, 600));
            driver.get("https://sandbox.applitools.com/bank?layoutAlgo=true");

            // Full Page - Visual AI Assertion
            eyes.check(Target.window().fully().withName("Login page"));

            driver.findElement(By.id("username")).sendKeys("user");
            driver.findElement(By.id("password")).sendKeys("password");
            driver.findElement(By.id("log-in")).click();

            // Full Page - Visual AI Assertion
            eyes.check(
                    Target.window().fully().withName("Main page")
                    // Uncomment to apply Layout regions and have test pass
                /* .layout(
                    By.cssSelector(".dashboardOverview_accountBalances__3TUPB"),
                    By.cssSelector(".dashboardTable_dbTable___R5Du")
                ) */
            );

            // End Applitools Visual AI Test
            eyes.closeAsync();
        }
        catch (Exception e) {
            e.printStackTrace();
            if (eyes != null)
                eyes.abortAsync();
        } finally {
            if (driver != null)
                driver.quit();
        }
    }

    private static void testAcmeBankLayout(){
        Eyes eyes = null;
        WebDriver driver = null;

        try {
            eyes = getEyes();
            driver = getDriver();

            // Start Applitools Visual AI Test
            eyes.open(driver,"ACME Bank", "Selenium Java Basic: Quickstart", new RectangleSize(1200, 600));
            driver.get("https://sandbox.applitools.com/bank?layoutAlgo=true");

            // Full Page - Visual AI Assertion
            eyes.check(Target.window().fully().withName("Login page"));

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
            eyes.closeAsync();
        }
        catch (Exception e) {
            e.printStackTrace();
            if (eyes != null)
                eyes.abortAsync();
        } finally {
            if (driver != null)
                driver.quit();
        }
    }

    public static void main(String [] args) {
        try{
            setup();
            testAcmeBankPage();
            testAcmeBankLayout();
        }catch(Exception e){
            System.err.println("Error during test execution: " + e.getMessage());
            e.printStackTrace();
        }finally {
            tearDown();
            System.exit(0);
        }


    }
}