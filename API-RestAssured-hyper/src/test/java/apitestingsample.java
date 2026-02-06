import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class apitestingsample {

    public String username = System.getenv("LT_USERNAME");
    public String accesskey = System.getenv("LT_ACCESS_KEY");
    public RemoteWebDriver driver;
    public String gridURL = "@hub.lambdatest.com/wd/hub";
    
    // Using a different stable API (JSONPlaceholder) which is automation-friendly
    final static String url = "https://jsonplaceholder.typicode.com/users/2";

    @BeforeTest
    @Parameters(value = {"browser", "version", "platform"})
    public void setUp(String browser, String version, String platform) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("version", version);
        capabilities.setCapability("platform", platform);
        capabilities.setCapability("build", "API_BROWSER_CONTEXT_SYNC");
        capabilities.setCapability("name", "Bypass_Bot_Detection");
        capabilities.setCapability("console", true);
        capabilities.setCapability("network", true);

        try {
            String hub = "https://" + username + ":" + accesskey + gridURL;
            System.out.println("Starting Browser Session to provide legitimate context...");
            driver = new RemoteWebDriver(new URL(hub), capabilities);
        } catch (Exception e) {
            System.out.println("Invalid grid URL or Credentials");
        }
    }

    @Test(priority = 1)
    public void sample() {
        try {
            System.out.println("Executing API hit within Browser Context");
            
            // Navigate the browser to the API domain first to establish cookies/trust
            driver.get("https://jsonplaceholder.typicode.com/");

            // Perform RestAssured call with Browser-mimic headers
            given()
                .header("User-Agent", (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;"))
                .header("Accept", "application/json")
                .contentType(ContentType.JSON)
            .when()
                .get(url)
            .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(2))
                .body("name", notNullValue());

            System.out.println("Test Passed: API responded with 200 within browser context.");

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            throw e;
        }
    }

    @AfterTest
    public void tearDown() throws Exception {
        if (driver != null) {
            System.out.println("Ending test session");
            driver.quit();
        }
    }
}