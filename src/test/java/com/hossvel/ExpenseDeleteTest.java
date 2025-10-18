package com.hossvel;

import com.hossvel.model.Expense;
import com.hossvel.resource.ExpenseResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint( ExpenseResource.class)
public class ExpenseDeleteTest {

    @Test
    @Transactional
    void testDeleteExpenseFound() {
        // Crear un gasto de prueba
        Expense expense =Expense.of( "Test Expense",
                Expense.PaymentMethod.CASH,
                "1234" );
        expense.persist();

        Expense expense2 =Expense.of( "Test Expense",
                Expense.PaymentMethod.CASH,
                "1234" );
        expense2.persist();



        // Ejecutar DELETE y validar respuesta
        given()
                .pathParam("uuid", expense.uuid)
                .when()
                .delete("/expenses/{uuid}")
                .then()
                .statusCode(404);

    }

    @Test
    void testDeleteExpenseNotFound() {
        UUID nonexistent = UUID.randomUUID();

        given()
                .pathParam("uuid", nonexistent)
                .when()
                .delete("/expenses/{uuid}")
                .then()
                .statusCode(404);
    }


}
