import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.Test;
import java.io.FileReader;
import static io.restassured.RestAssured.given;

public class postapi {
    public String username = System.getenv("LT_USERNAME");
    public String accesskey = System.getenv("LT_ACCESS_KEY");

    @Test
    public void validateSessionPostExecution() {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("session.json"));
            JSONObject jsonObject = (JSONObject) obj;
            String session = (String) jsonObject.get("session_id");

            RestAssured.baseURI = "https://api.lambdatest.com/automation/api/v1/sessions/" + session;
            Response response = given().auth().basic(username, accesskey).when().get();

            System.out.println("Response from LambdaTest API: " + response.getStatusCode());
            System.out.println("Test Status: " + response.jsonPath().getString("data.status_ind"));
        } catch (Exception e) {
            System.out.println("Error reading session file: " + e.getMessage());
        }
    }
}