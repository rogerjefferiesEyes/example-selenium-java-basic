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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class AcmeBankTests {

    private static BatchInfo BATCH;
    private static EyesRunner runner;

    private static boolean USE_SELF_HEALING_EXECUTION_CLOUD = true;

    private static void setup(){
        BATCH = new BatchInfo("Selenium Java Basic Quickstart");

        // Configure Applitools SDK to run on the Ultrafast Grid
        runner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));
    }

    private static void tearDown(){
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        System.out.println(allTestResults);
    }

    private static Eyes getEyes(){
        Eyes eyes = null;
        eyes = new Eyes(runner);
        Configuration config = eyes.getConfiguration();
        config.setServerUrl("https://eyesapi.applitools.com");
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

    private static WebDriver getDriver() throws MalformedURLException {
        WebDriver driver = null;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        String headless = System.getenv("HEADLESS");
        if(headless != null) {
            options.addArguments("--headless");
        }
        if(USE_SELF_HEALING_EXECUTION_CLOUD){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("applitools:eyesServerUrl", "https://eyesapi.applitools.com");
            caps.setCapability("applitools:useSelfHealing", "true");
            caps.setBrowserName("chrome");
            caps.setCapability(ChromeOptions.CAPABILITY, options);
            driver = new RemoteWebDriver(new URL(Eyes.getExecutionCloudURL()), caps);
        } else {
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
        return driver;
    }

    private static void testAcmeBankPage(){

        Eyes eyes = null;
        WebDriver driver = null;

        try {
            eyes = getEyes();
            driver = getDriver();

            // Start Applitools Visual AI Test
            eyes.open(driver,"ACME Bank", new Object(){}.getClass().getEnclosingMethod().getName(), new RectangleSize(1200, 600));
            driver.get("https://sandbox.applitools.com/bank?layoutAlgo=true");

            // Full Page - Visual AI Assertion
            eyes.check(Target.window().fully().withName("Login page"));

            driver.findElement(By.id("username")).sendKeys("user");
            driver.findElement(By.id("password")).sendKeys("password");
            driver.findElement(By.id("log-in")).click();

            // Full Page - Visual AI Assertion
            eyes.check(
                    Target.window().fully().withName("Main page")
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
            eyes.open(driver,"ACME Bank", new Object(){}.getClass().getEnclosingMethod().getName(), new RectangleSize(1200, 600));
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

    private static void testAcmeBankSelfHealing(){
        Eyes eyes = null;
        WebDriver driver = null;

        try {
            eyes = getEyes();
            driver = getDriver();

            // Start Applitools Visual AI Test
            eyes.open(driver,"ACME Bank", new Object(){}.getClass().getEnclosingMethod().getName(), new RectangleSize(1200, 600));
            driver.get("https://sandbox.applitools.com/bank?layoutAlgo=true");

            // Full Page - Visual AI Assertion
            eyes.check(Target.window().fully().withName("Login page"));

            driver.findElement(By.id("username")).sendKeys("user");
            driver.findElement(By.id("password")).sendKeys("password");

            // Uncomment to simulate selector/locator change
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("document.querySelector('#log-in').setAttribute('id', 'newButtonId');");

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

    private static void testNonEyes(){
        WebDriver driver = null;
        try {
            driver = getDriver();

            // Start Non-Eyes Test Session in Execution Cloud
            ((JavascriptExecutor) driver).executeScript("applitools:startTest", new HashMap<String, Object>() {
                {
                    put("testName", "testNonEyes");
                    put("appName", "Cloud Comparisons");
                    put("batch", new HashMap<Object, Object>() {{
                                put("name", BATCH.getName());
                                put("id", BATCH.getId());
                            }}
                    );
                }
            });

            driver.get("https://sandbox.applitools.com/bank?layoutAlgo=true");

            assert driver.findElement(By.cssSelector("h4.loginForm_loginFormHeader__fiIRG")).getText() == "Login Form";

            driver.findElement(By.id("username")).sendKeys("user");
            driver.findElement(By.id("password")).sendKeys("password");

            // Uncomment to simulate selector/locator change
            //JavascriptExecutor jse = (JavascriptExecutor) driver;
            //jse.executeScript("document.querySelector('#log-in').setAttribute('id', 'newButtonId');");

            driver.findElement(By.id("log-in")).click();

            assert driver.findElement(By.cssSelector("div.dashboardSideBar_dbSideNavUserName__922dl")).getText() == "Jack Gomez";

            ((JavascriptExecutor) driver).executeScript("applitools:endTest", new HashMap<String, Object>() {{
                put("status", "Passed");
            }});
        }
        catch (Exception e) {
            e.printStackTrace();
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

            testAcmeBankSelfHealing();
            testNonEyes();
        }catch(Exception e){
            System.err.println("Error during test execution: " + e.getMessage());
            e.printStackTrace();
        }finally {
            tearDown();
            System.exit(0);
        }


    }
}