package stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellarburgers.model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    private final String ORDER = "/api/orders";

    @Step("Создание заказа с токеном")
    public ValidatableResponse createOrderWithAuth(String token, Order order) {
        return given()
                .header("Authorization", "Bearer " + token)
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Создание заказа без токена")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post(ORDER)
                .then();
    }
}
