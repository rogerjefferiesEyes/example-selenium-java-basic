package com.applitools.example;
import com.applitools.eyes.*;
import com.applitools.eyes.selenium.*;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.*;
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
import java.util.Objects;

public class AcmeBankTests {

    private static BatchInfo BATCH;
    private static EyesRunner runner;

    private static final boolean USE_ULTRAFAST_GRID = false;

    private static final boolean USE_SELF_HEALING_EXECUTION_CLOUD = false;

    private void setup(){
        if(BATCH == null) {
            BATCH = new BatchInfo("Selenium Java Basic Quickstart");
            BATCH.setNotifyOnCompletion(true);
            BATCH.addProperty("AppVersion", "1.0.1");
        }

        if(runner == null){
            if(USE_ULTRAFAST_GRID) {
                // Configure Applitools SDK to run on the Ultrafast Grid
                runner = new VisualGridRunner(new RunnerOptions().testConcurrency(5));
            } else {
                runner = new ClassicRunner();
            }
        }
    }

    private void tearDown(){
        TestResultsSummary allTestResults = runner.getAllTestResults(false);
        System.out.println(allTestResults);
    }

    private Eyes getEyes(){
        Eyes eyes = new Eyes(runner);
        Configuration config = eyes.getConfiguration();
        config.setServerUrl("https://eyesapi.applitools.com");
        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        config.setUseDom(true);
        config.setSendDom(true);
        config.setStitchMode(StitchMode.CSS);
        config.setBatch(BATCH);
        if(USE_ULTRAFAST_GRID) {
            config.addBrowsers(
                    new DesktopBrowserInfo(800, 600, BrowserType.CHROME),
                    new DesktopBrowserInfo(1024, 768, BrowserType.FIREFOX),
                    new DesktopBrowserInfo(1024, 768, BrowserType.SAFARI),
                    new ChromeEmulationInfo(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT),
                    new ChromeEmulationInfo(DeviceName.Nexus_10, ScreenOrientation.LANDSCAPE),
                    new IosDeviceInfo(IosDeviceName.iPhone_15, ScreenOrientation.PORTRAIT)
            );
            config.setLayoutBreakpoints(true);
        }
        eyes.setConfiguration(config);
        return eyes;
    }

    private WebDriver getDriver() throws MalformedURLException {
        WebDriver driver = null;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        String headless = System.getenv("HEADLESS");
        if(headless != null || USE_ULTRAFAST_GRID) {
            options.addArguments("--headless=new");
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

    private void testAcmeBankPage(){

        Eyes eyes = null;
        WebDriver driver = null;

        try {
            eyes = getEyes();
            driver = getDriver();

            // Start Applitools Visual AI Test
            eyes.open(
                    driver,
                    "ACME Bank",
                    new Object(){}.getClass().getEnclosingMethod().getName(),
                    new RectangleSize(1200, 600)
            );
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

    private void testAcmeBankLayout(){
        Eyes eyes = null;
        WebDriver driver = null;

        try {
            eyes = getEyes();
            driver = getDriver();

            // Start Applitools Visual AI Test
            eyes.open(
                    driver,
                    "ACME Bank",
                    new Object(){}.getClass().getEnclosingMethod().getName(),
                    new RectangleSize(1200, 600)
            );
            driver.get("https://sandbox.applitools.com/bank?layoutAlgo=true");

            // Full Page - Visual AI Assertion
            eyes.check(Target.window().fully().withName("Login page").stitchMode(StitchMode.CSS));

            driver.findElement(By.id("username")).sendKeys("user");
            driver.findElement(By.id("password")).sendKeys("password");

            driver.findElement(By.id("log-in")).click();

            Thread.sleep(2000);

            // Full Page - Visual AI Assertion
            eyes.check(
                    Target.window().fully().withName("Main page").waitBeforeCapture(2000));

//            eyes.check(
//                    Target.window().dynamic(By.cssSelector(".dashboardOverview_accountBalances__3TUPB"), DynamicTextType.TextField,
//                    DynamicTextType.Number,
//                    DynamicTextType.Email,
//                    DynamicTextType.Date,
//                    DynamicTextType.Link,
//                    DynamicTextType.Currency)
//
//            );

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

    private void testAcmeBankABPage(){

        Eyes eyes = null;
        WebDriver driver = null;
        boolean siteVersionA = true;

        try {
            eyes = getEyes();
            driver = getDriver();

            if(siteVersionA){
                driver.get("https://demo.applitools.com/app.html");
                eyes.addProperty("SiteVersion", "A");
            } else {
                driver.get("https://sandbox.applitools.com/bank/dashboard");
                eyes.addProperty("SiteVersion", "B");
            }

            // Start Applitools Visual AI Test
            eyes.open(
                    driver,
                    "ACME Bank",
                    new Object(){}.getClass().getEnclosingMethod().getName(),
                    new RectangleSize(1200, 600)
            );

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

    private void testPrimerPrimitivesReadMe(){
        Eyes eyes = null;
        WebDriver driver = null;

        try {
            eyes = getEyes();
            driver = getDriver();

            eyes.setBaselineEnvName("README_701");

            // Start Applitools Visual AI Test
            eyes.open(
                    driver,
                    "Primer Primitives (Community)",
                    "README",
                    new RectangleSize(701, 1477)
            );
            driver.get("http://127.0.0.1:8081/");

            // Full Page - Visual AI Assertion
            eyes.check(Target.window().fully().matchLevel(MatchLevel.LAYOUT));

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

    private void testAcmeBankSelfHealing(){
        Eyes eyes = null;
        WebDriver driver = null;

        try {
            eyes = getEyes();
            driver = getDriver();

            // Start Applitools Visual AI Test
            eyes.open(
                    driver,"ACME Bank",
                    new Object(){}.getClass().getEnclosingMethod().getName(),
                    new RectangleSize(1200, 600)
            );
            driver.get("https://sandbox.applitools.com/bank?layoutAlgo=true");

            // Full Page - Visual AI Assertion
            eyes.check(Target.window().fully().withName("Login page").dynamic());

            driver.findElement(By.id("username")).sendKeys("user");
            driver.findElement(By.id("password")).sendKeys("password");

            // Uncomment to simulate selector/locator change
//            JavascriptExecutor jse = (JavascriptExecutor) driver;
//            jse.executeScript("document.querySelector('#log-in').setAttribute('id', 'newButtonId');");

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

    private void testNonEyes(){
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

            assert Objects.equals(
                    driver.findElement(By.cssSelector("h4.loginForm_loginFormHeader__fiIRG")).getText(),
                    "Login Form"
            );

            driver.findElement(By.id("username")).sendKeys("user");
            driver.findElement(By.id("password")).sendKeys("password");

            // Uncomment to simulate selector/locator change
//            JavascriptExecutor jse = (JavascriptExecutor) driver;
//            jse.executeScript("document.querySelector('#log-in').setAttribute('id', 'newButtonId');");

            driver.findElement(By.id("log-in")).click();

            assert Objects.equals(
                    driver.findElement(By.cssSelector("div.dashboardSideBar_dbSideNavUserName__922dl")).getText(),
                    "Jack Gomez"
            );

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
        AcmeBankTests acmeBankTests = new AcmeBankTests();
        try{
            acmeBankTests.setup();
//            acmeBankTests.testAcmeBankPage();
//            acmeBankTests.testAcmeBankLayout();
//            acmeBankTests.testPrimerPrimitivesReadMe();
            acmeBankTests.testAcmeBankABPage();

            if(USE_SELF_HEALING_EXECUTION_CLOUD) {
                acmeBankTests.testAcmeBankSelfHealing();
                acmeBankTests.testNonEyes();
            }
        }catch(Exception e){
            System.err.println("Error during test execution: " + e.getMessage());
            e.printStackTrace();
        }finally {
            acmeBankTests.tearDown();
            System.exit(0);
        }


    }
}