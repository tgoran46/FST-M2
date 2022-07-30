package liveproject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static au.com.dius.pact.consumer.dsl.PactDslJsonRootValue.stringType;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    Map<String,String> reqHeaders = new HashMap<>();
    String resourcepath = "/api/users";
    @Pact(consumer = "UserConsumer",provider = "UserProvider")
    public RequestResponsePact createpact(PactDslWithProvider builder){
        reqHeaders.put("Content-Type","application/json");
        DslPart reqResBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");
        return builder.given("Request to create a user")
                .uponReceiving("Request to create a user")
                .method("POST")
                .headers(reqHeaders)
                .path(resourcepath)
                .body(reqResBody)
                .willRespondWith()
                .status(201)
                .body(reqResBody)
                .toPact();

    }
    @Test
    @PactTestFor(providerName = "UserProvider",port = "8282")
    public void consumerSideTest(){
        String baseURI = "http://localhost:8282";
        Map<String,Object>reqBody = new HashMap<>();
        reqBody.put("id",123);
        reqBody.put("firstName","Thri");
        reqBody.put("lastName","veni");
        reqBody.put("email","thri@gmail.com");

        given().headers(reqHeaders).body(reqBody).
                when().post(baseURI+resourcepath).
                then().log().all().statusCode(201);
    }



}
