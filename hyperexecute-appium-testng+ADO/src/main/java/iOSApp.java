import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;

@Listeners({ AllureTestNg.class })
@Epic("Mobile Automation")
@Feature("iOS App - LambdaTest Proverbial")
public class iOSApp {

    public static String userName = System.getenv("LT_USERNAME");
    public static String accessKey = System.getenv("LT_ACCESS_KEY");

    public String gridURL = "@mobile-hub.lambdatest.com/wd/hub";

    AppiumDriver driver;

    @Severity(SeverityLevel.CRITICAL)
    @Story("Verify Proverbial App flow on iOS")
    @Test
    @org.testng.annotations.Parameters(value = { "device", "version", "platform" })
    public void iOSApp1(String device, String version, String platform) {

        try {
            Allure.step("Setting desired capabilities for iOS device");

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("build", "Java TestNG iOS");
            capabilities.setCapability("name", platform + " " + device + " " + version);
            capabilities.setCapability("deviceName", device);
            capabilities.setCapability("platformVersion", version);
            capabilities.setCapability("platformName", platform);
            capabilities.setCapability("isRealMobile", true);
            capabilities.setCapability("app", "lt://APP1016018631760361477812757");
            capabilities.setCapability("deviceOrientation", "PORTRAIT");
            capabilities.setCapability("console", true);
            capabilities.setCapability("network", false);
            capabilities.setCapability("devicelog", true);

            String hub = "https://" + userName + ":" + accessKey + gridURL;
            driver = new AppiumDriver(new URL(hub), capabilities);

            Allure.step("iOS App launched successfully");

            WebDriverWait wait = new WebDriverWait(driver, 30);

            clickByAccessibilityId(wait, "color", "Change text color");
            Thread.sleep(1000);

            clickByAccessibilityId(wait, "Text", "Change text to Proverbial");
            Thread.sleep(1000);

            clickByAccessibilityId(wait, "toast", "Show toast message");
            Thread.sleep(1000);

            clickByAccessibilityId(wait, "notification", "Open notification");
            Thread.sleep(4000);

            clickByAccessibilityId(wait, "geoLocation", "Open geo location");
            Thread.sleep(4000);
            driver.navigate().back();

            clickByAccessibilityId(wait, "speedTest", "Open speed test");
            Thread.sleep(4000);
            driver.navigate().back();

            clickByAccessibilityId(wait, "Browser", "Open browser");
            Thread.sleep(1000);

            Allure.step("Entering URL in browser");
            MobileElement url = (MobileElement) driver.findElementByAccessibilityId("url");
            url.click();
            url.sendKeys("https://www.lambdatest.com");

            clickByAccessibilityId(wait, "find", "Click Find button");
            Thread.sleep(1000);

            Allure.step("Marking test as PASSED on LambdaTest");
            driver.executeScript("lambda-status=passed");

        } catch (Exception e) {
            Allure.step("Test failed due to exception: " + e.getMessage());
            attachScreenshot();
            e.printStackTrace();

            try {
                if (driver != null) {
                    driver.executeScript("lambda-status=failed");
                }
            } catch (Exception ignored) {}

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    /* ---------------- Helper Methods ---------------- */

    @Step("{stepDescription}")
    public void clickByAccessibilityId(WebDriverWait wait, String accId, String stepDescription) {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                MobileBy.AccessibilityId(accId))).click();
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] attachScreenshot() {
        return ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);
    }
}
