package com.qa.tests.api;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.framework.pojo.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserApiTests {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    @Test
    public void verifyGetSingleUser() {
        given().baseUri(BASE_URI)
        .when().get("/users/1")
        .then().statusCode(200)
        .body("id", equalTo(1))
        .body("name", equalTo("Leanne Graham"));
    }

    @DataProvider(name = "userData")
    public Object[][] getUserData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User[] users = objectMapper.readValue(
                new File("src/test/resources/testdata/users.json"), User[].class);

        Object[][] data = new Object[users.length][1];
        for (int i = 0; i < users.length; i++) {
            data[i][0] = users[i];
        }
        return data;
    }

    @Test(dataProvider = "userData")
    public void verifyCreateUser(User user) {
        given().baseUri(BASE_URI).contentType("application/json").body(user)
        .when().post("/users")
        .then().statusCode(201)
        .body("name", equalTo(user.getName()))
        .body("username", equalTo(user.getUsername()));
    }
}