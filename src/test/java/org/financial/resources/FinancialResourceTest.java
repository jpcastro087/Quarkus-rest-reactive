package org.financial.resources;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class FinancialResourceTest {
    @Test
    public void testCreateValidSymbol() {
        given()
                .contentType("application/json")
                .body("{\"symbol\":\"AAPL\"}")
                .when()
                .post("/stock/create")
                .then()
                .statusCode(200);
    }

    @Test
    public void testCreateSymbolNotFound() {
        given()
                .contentType("application/json")
                .body("{\"symbol\":\"INVALID\"}")
                .when()
                .post("/stock/create")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetStockMarketActions() {
        given()
                .contentType("application/json")
                .when()
                .get("/stock/list")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
