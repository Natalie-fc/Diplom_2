package stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellarburgers.model.LoginRequest;
import stellarburgers.model.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    private static String USER = "/api/auth/register";
    private static String LOGIN_USER = "/api/auth/login";
    private static String DELETE_USER = "/api/auth/user";


    @Step("Регистрация нового пользователя")
    public ValidatableResponse register(User user) {

        return given()
                .body(user)
                .when()
                .post(USER)
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(LoginRequest loginRequest) {

        return given()
                .body(loginRequest)
                .when()
                .post(LOGIN_USER)
                .then();
    }

    @Step("Получение accessToken после логина")
    public String getAccessToken(User user) {
        return login(new LoginRequest(user.getEmail(), user.getPassword()))
                .extract()
                .path("accessToken")
                .toString()
                .replace("Bearer ", "");
    }

    @Step("Удаление пользователя по токену")
    public ValidatableResponse deleteUser(String accessToken) {

        return given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete(DELETE_USER)
                .then();
    }
}
