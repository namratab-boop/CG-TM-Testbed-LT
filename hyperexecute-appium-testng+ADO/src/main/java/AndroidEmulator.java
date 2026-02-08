import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Listeners({ AllureTestNg.class })
@Epic("Mobile Automation")
@Feature("Android Emulator App - LambdaTest")
public class AndroidEmulator {

    String username = System.getenv("LT_USERNAME") == null ? "LT_USERNAME"
            : System.getenv("LT_USERNAME");
    String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "LT_ACCESS_KEY"
            : System.getenv("LT_ACCESS_KEY");

    public static RemoteWebDriver driver = null;
    public String gridURL = "@mobile-hub.lambdatest.com/wd/hub";
    public String status = "passed";

    @BeforeMethod
    @Parameters(value = { "deviceName", "platformVersion", "os" })
    @Step("Setup Android Emulator session")
    public void setUp(String deviceName, String platformVersion, String os) {

        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            capabilities.setCapability("build", "HYP JUNIT Native App automation");
            capabilities.setCapability("name", "HYP Java JUnit Android Test");
            capabilities.setCapability("platformName", os);
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("isRealMobile", false);
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability("app", "lt://APP10160441901770493306505751");
            capabilities.setCapability("deviceOrientation", "PORTRAIT");
            capabilities.setCapability("console", true);
            capabilities.setCapability("network", false);
            capabilities.setCapability("visual", true);
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("appiumVersion", "1.21.0");
            capabilities.setCapability("devicelog", true);

            driver = new RemoteWebDriver(
                    new URL("http://" + username + ":" + accessKey + gridURL),
                    capabilities
            );

            Allure.step("Driver session created successfully");

        } catch (MalformedURLException e) {
            Allure.step("Invalid Grid URL");
            throw new RuntimeException(e);
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("Verify Proverbial App flow on Android Emulator")
    @Test
    public void testSimple() {

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);

            click(MobileBy.id("color"), wait, "Click on color button");
            click(MobileBy.id("geoLocation"), wait, "Open geolocation");
            Thread.sleep(5000);
            driver.navigate().back();

            click(MobileBy.id("Text"), wait, "Click on text button");
            click(MobileBy.id("notification"), wait, "Open notification");
            click(MobileBy.id("toast"), wait, "Show toast message");

            click(By.id("webview"), wait, "Open WebView");
            Thread.sleep(10000);

            Allure.step("Entering URL in WebView");
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.id("url")))
                    .sendKeys("https://www.lambdatest.com/");

            click(MobileBy.id("find"), wait, "Click Find button");
            Thread.sleep(5000);
            driver.navigate().back();

            status = "passed";
            Allure.step("Test completed successfully");

        } catch (Exception e) {
            status = "failed";
            Allure.step("Test failed due to exception: " + e.getMessage());
            attachScreenshot();
            throw new RuntimeException(e);
        }
    }

    @AfterMethod
    @Step("Tear down session")
    public void tearDown() {

        if (driver != null) {
            driver.executeScript("lambda-status=" + status);
            driver.quit();
            Allure.step("Driver session closed");
        }
    }

    /* ---------------- Helper Methods ---------------- */

    @Step("{description}")
    public void click(By locator, WebDriverWait wait, String description) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator)).click();
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] attachScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
