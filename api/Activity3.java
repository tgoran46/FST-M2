package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class Activity3 {
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setUp(){
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .setContentType(ContentType.JSON)
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody("status",equalTo("alive"))
                .expectResponseTime(lessThan(5L), TimeUnit.SECONDS)
                .build();


    }
    @Test(priority = 1)
    public void addPet(){
        String reqBody = "{\"id\":77232,\"name\":\"Railey\",\"status\":\"alive\"}";
        Response response = given().spec(requestSpec)
                .body(reqBody)
                .when().post();
        System.out.println(response.getBody().asString());
    }
    @Test(priority = 2)
    public void getPet(){
        Response response = given().spec(requestSpec)
                .pathParam("petId",77232)
                .when().get("/{petId}");
        System.out.println(response.getBody().asString());
        response.then().spec(responseSpec);
    }
    @Test(priority = 3)
    public void removePet(){
        Response response = given().spec(requestSpec)
                .pathParam("petId",77232)
                .when().delete("/{petId}");
        System.out.println(response.getBody().asString());
        response.then().statusCode(200);
        response.then().body("message",equalTo("77232"));
    }
}
