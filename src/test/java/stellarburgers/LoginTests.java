package stellarburgers;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.model.LoginRequest;
import stellarburgers.model.User;
import stellarburgers.steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@Epic("Авторизация")
@Feature("Логин пользователя")
public class LoginTests extends BaseTest {

    private final UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;

    @Before
    @Step("Создание нового пользователя через API")
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(8) + "@yandex.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(12));
        user.setName(RandomStringUtils.randomAlphabetic(10));

        userSteps.register(user)
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @Story("Успешный вход")
    @Description("Пользователь должен успешно войти при вводе правильного email и пароля")
    public void shouldLoginWithValidCredentials() {
        LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());

        userSteps.login(loginRequest)
                .statusCode(SC_OK)
                .body("success", is(true));

    }

    @Test
    @Story("Неуспешный вход")
    @Description("Пользователь не должен войти при вводе неверного email и пароля")
    public void shouldNotLoginWithInvalidCredentials() {
        LoginRequest invalidLogin = new LoginRequest("wrong" + user.getEmail(), "wrongPassword");

        userSteps.login(invalidLogin)
                .statusCode(SC_UNAUTHORIZED)
                .body("message", containsString("email or password are incorrect"));
    }

    @After
    @Step("Удаление пользователя через API")
    public void tearDown() {
        if (user != null) {
            try {
                accessToken = userSteps.getAccessToken(user);
                if (accessToken != null) {
                    userSteps.deleteUser(accessToken);
                }
            } catch (Exception e) {
                System.out.println("Не удалось получить токен для удаления пользователя: " + e.getMessage());
            }
        }
    }
}