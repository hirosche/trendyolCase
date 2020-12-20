import org.json.simple.JSONObject;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;

/**
 * Created by Mehmet Yıldırım on 19.12.2020.
 */
public class TestRunner {
    public String apiPath = APITestCase.API_ROOT;

    @BeforeTest

    @Test
    public void verifyThatTheAPIStartsWithAnEmptyStore() {

        when().
                get(apiPath+"/api/books/").
        then().
                assertThat().
                statusCode(200).
                body("$", hasKey("id"));

    }

    @Test
    public void verifyThatAuthorIsRequiredFields(){
        JSONObject request = new JSONObject();
        request.put("title", "yazarı yok");

        given().
                body(request.toJSONString()).
        when().
                put(apiPath+"/api/books/").
        then().
                assertThat().
                statusCode(400).
                body("error", equalTo("Field 'author' is required"));
    }

    @Test
    public void verifyThatTitleIsRequiredFields(){
        JSONObject request = new JSONObject();
        request.put("author", "basligi yok");

        given().
                body(request.toJSONString()).

        when().
                put(apiPath+"/api/books/").
                then().assertThat().statusCode(400).body("error", equalTo("Field 'title' is required") );
    }

    @Test
    public void verifyThatAuthorCannotBeEmpty(){
        JSONObject request = new JSONObject();
        request.put("author", "");
        request.put("title", "yazarı yok");

        given().
                body(request.toJSONString()).
        when().
                put(apiPath+"/api/books/").
                then().assertThat().statusCode(400).body("error", equalTo("Field 'author' cannot be empty") );
    }

    @Test
    public void verifyThatTitleCannotBeEmpty(){
        JSONObject request = new JSONObject();
        request.put("author", "basligi yok");
        request.put("title","");


        given().
                body(request.toJSONString()).
        when().
                put(apiPath+"/api/books/").
                then().assertThat().statusCode(400).body("error", equalTo("Field 'title' cannot be empty") );
    }

    @Test
    public void verifyThatTheIdFieldIsReadOnly(){
        JSONObject request = new JSONObject();
        request.put("author", "yazar");
        request.put("title","baslik");
        request.put("id", "8");


        given().
                body(request.toJSONString()).
        when().
                put(apiPath+"/api/books/").
                then().assertThat().statusCode(400);

    }

    @Test
    public void verifyThatYouCanCreateANewBookViaPUT(){
        JSONObject request = new JSONObject();
        request.put("author", "yazar");
        request.put("title","baslik");


        given().
                body(request.toJSONString()).
                when().
                put(apiPath+"/api/books/").
                then().assertThat().statusCode(200).body("author", hasItem("yazar")).body("title", hasItem("baslik"));


        when().
                get(apiPath+"/api/books/1").
                then().
                assertThat().
                statusCode(200).
                body("title", equalTo("baslik"));

    }

    @Test
    public void verifyThatYouCannotCreateADuplicateBbook(){
        JSONObject request = new JSONObject();
        request.put("author", "yazar");
        request.put("title","baslik");


        given().
                body(request.toJSONString()).
                when().
                put(apiPath+"/api/books/").
                then().assertThat().statusCode(200).body("author", hasItem("yazar")).body("title", hasItem("baslik"));

        given().
                body(request.toJSONString()).
                when().
                put(apiPath+"/api/books/").
                then().assertThat().statusCode(400).body("error", equalTo("Another book with similar title and author already exists.") );
    }

    @AfterTest()
    public void afterTest() throws IOException {
    }

}
