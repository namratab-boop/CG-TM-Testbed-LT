import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;

@Listeners({ AllureTestNg.class })
@Epic("Mobile Automation")
@Feature("Android App - LambdaTest Proverbial")
public class AndroidApp {

    public static String userName = System.getenv("LT_USERNAME");
    public static String accessKey = System.getenv("LT_ACCESS_KEY");

    public String gridURL = "@mobile-hub.lambdatest.com/wd/hub";

    AppiumDriver driver;

    @Severity(SeverityLevel.CRITICAL)
    @Story("Verify Proverbial App End-to-End Flow")
    @Test
    @org.testng.annotations.Parameters(value = { "device", "version", "platform" })
    public void AndroidApp1(String device, String version, String platform) {

        try {
            Allure.step("Setting desired capabilities");

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("build", "Java TestNG Android 1");
            capabilities.setCapability("name", platform + " " + device + " " + version);
            capabilities.setCapability("deviceName", device);
            capabilities.setCapability("platformVersion", version);
            capabilities.setCapability("platformName", platform);
            capabilities.setCapability("isRealMobile", true);
            capabilities.setCapability("app", "lt://APP10160622431766424164986229");
            capabilities.setCapability("deviceOrientation", "PORTRAIT");
            capabilities.setCapability("console", true);
            capabilities.setCapability("network", false);
            capabilities.setCapability("autoAcceptAlerts", true);
            capabilities.setCapability("devicelog", true);
            capabilities.setCapability("autoGrantPermissions", true);

            String hub = "https://" + userName + ":" + accessKey + gridURL;
            driver = new AppiumDriver(new URL(hub), capabilities);

            Allure.step("App launched successfully");

            clickById("com.lambdatest.proverbial:id/color", "Click color button");
            Thread.sleep(1000);
            clickById("com.lambdatest.proverbial:id/color", "Reset color");

            clickById("com.lambdatest.proverbial:id/Text", "Click text button");
            clickById("com.lambdatest.proverbial:id/toast", "Show toast message");
            clickById("com.lambdatest.proverbial:id/notification", "Show notification");
            Thread.sleep(2000);

            clickById("com.lambdatest.proverbial:id/geoLocation", "Open geolocation page");
            Thread.sleep(5000);

            clickByAccessibilityId("Home", "Navigate back to Home");

            clickById("com.lambdatest.proverbial:id/geoLocation", "Open speed test");
            Thread.sleep(5000);

            clickByAccessibilityId("Home", "Navigate back to Home");

            clickByAccessibilityId("Browser", "Open browser");

            Allure.step("Entering URL in browser");
            MobileElement url = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/url");
            url.sendKeys("https://www.lambdatest.com");

            clickById("com.lambdatest.proverbial:id/find", "Click Find button");

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

    /* ------------------ Helper Methods with Allure Steps ------------------ */

    @Step("{stepDescription}")
    public void clickById(String id, String stepDescription) {
        driver.findElementById(id).click();
    }

    @Step("{stepDescription}")
    public void clickByAccessibilityId(String accId, String stepDescription) {
        driver.findElementByAccessibilityId(accId).click();
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] attachScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
