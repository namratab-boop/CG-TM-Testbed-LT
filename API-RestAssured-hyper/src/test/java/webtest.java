import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import java.io.FileWriter;
import java.net.URL;

public class webtest {
    RemoteWebDriver driver = null;
    public String username = System.getenv("LT_USERNAME");
    public String access_key = System.getenv("LT_ACCESS_KEY");

    @BeforeMethod
    @Parameters(value={"browser","version","platform"})
    public void setUp(String browser, String version, String platform) throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", browser);
        caps.setCapability("version", version);
        caps.setCapability("platform", platform);
        caps.setCapability("build", "HyperExecute_Sample_Project");
        caps.setCapability("name", "Web_API_Integration_Test");

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + access_key + "@hub.lambdatest.com/wd/hub"), caps);
        
        // Writing session ID to session.json
        JSONObject json = new JSONObject();
        json.put("session_id", driver.getSessionId().toString());
        try (FileWriter file = new FileWriter("session.json")) {
            file.write(json.toString());
        }
    }

    @Test
    public void testExecution() {
        driver.get("https://www.google.com");
        System.out.println("Web Test Passed for session: " + driver.getSessionId());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("lambda-status=passed");
            driver.quit();
        }
    }
}