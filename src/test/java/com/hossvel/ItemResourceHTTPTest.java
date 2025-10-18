package com.hossvel;

import com.hossvel.resource.ItemResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.*;
        import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(ItemResource.class)
public class ItemResourceHTTPTest {
    static Long createdId;

    @Test
    @Order(1)
    public void testCreateItem() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"Mouse\",\"price\":25.0}")
                .when().post()
                .then().statusCode(201)
                .body("name", equalTo("Mouse"));
    }

    @Test
    @Order(1)
    public void testGetItems() {
        given()
                .when().get()
                .then().statusCode(200)
                .contentType(ContentType.JSON);

    }

    @Test
    @Order(2)
    public void testUpdateItem() {
        given()
                .contentType("application/json")
                .body("{\"name\":\"Laptop Pro\",\"price\":1800.0}")
                .when().put("/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Laptop Pro")).assertThat();
    }

    @Test
    @Order(3)
    public void testDeleteItem204() {
        given()
                .when().delete("/1")
                .then().statusCode(204);
    }


    @Test
    @Order(3)
    public void testDeleteItem404() {
        given()
                .when().delete("/10")
                .then().statusCode(404);
    }

    @Test
    @Order(4)
    public void testCreateAndGetItem() {
        // Crear un ítem
        String json = "{\"name\":\"Teclado\",\"price\":150.0}";
        Response postResponse = given()
                .contentType("application/json")
                .body(json)
                .when()
                .post();

        assertEquals(201, postResponse.statusCode());
        Long id = postResponse.jsonPath().getLong("id");
        System.out.println("ID: " + id);

        // Obtener el ítem creado
        Response getResponse = given()
                .when()
                .get("/" + id);

        assertEquals(200, getResponse.statusCode());
        assertEquals("Teclado", getResponse.jsonPath().getString("name"));
        assertEquals(150.0, getResponse.jsonPath().getDouble("price"), 0.01);

    }
}
