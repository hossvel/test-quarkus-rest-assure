package com.hossvel;

import com.hossvel.resource.ExpenseResource;
import com.hossvel.resource.MovieResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(MovieResource.class)
public class MovieResourceTest {

    @Test
    @Order(1)
    void getAll() {
        given()
                .when()
                .get()
                .then()
                .body("size()", equalTo(2))
                .body("id", hasItems(1, 2))
                .body("title", hasItems("FirstMovie", "SecondMovie"))
                .body("director", hasItem("Me"))
                .body("country", hasItem("Planet"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getById() {
        given()
                .when()
                .get("/1")
                .then()
                .body("id", equalTo(1))
                .body("title", equalTo("FirstMovie"))
                .body("description", equalTo("MyFirstMovie"))
                .body("director", equalTo("Me"))
                .body("country", equalTo("Planet"))
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
