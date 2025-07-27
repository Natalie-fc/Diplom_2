package stellarburgers;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.model.User;
import stellarburgers.steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@Epic("Пользователи")
@Feature("Регистрация пользователя")
public class UserTests extends BaseTest {
    private final UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;

    @Before
    @Step("Подготовка данных пользователя и настройка логирования RestAssured")
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(8) + "@yandex.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(12));
        user.setName(RandomStringUtils.randomAlphabetic(10));
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    @Story("Создание нового пользователя")
    @Description("Проверка успешной регистрации нового пользователя")
    public void shouldCreateUserTest() {

        userSteps.register(user)
                .statusCode(SC_OK)
                .body("success", is(true));

        accessToken = userSteps.getAccessToken(user);

    }

    @Test
    @DisplayName("Регистрация уже существующего пользователя")
    @Story("Создание нового пользователя")
    @Description("Проверка, что нельяза зарегистрировать существующего пользователя дважды")
    public void shouldNotAllowToCreateExistingUserTest() {

        userSteps.register(user)
                .statusCode(SC_OK);
        userSteps.register(user)
                .statusCode(SC_FORBIDDEN)
                .body("message", containsString("User already exists"));
    }

    @Test
    @DisplayName("Регистрация нового пользователя без email")
    @Story("Проверка валидации")
    @Description("Регистрация должна завершиться ошибкой если не указан email")
    public void shouldNotCreateUserWithoutEmail() {
        User userWithoutEmail = User.withoutEmail();

        userSteps.register(userWithoutEmail)
                .statusCode(SC_FORBIDDEN)
                .body("message", containsString("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Регистрация нового пользователя без пароля")
    @Story("Проверка валидации")
    @Description("Регистрация должна завершиться ошибкой если не указан пароль")
    public void shouldNotCreateUserWithoutPassword() {
        User userWithoutPassword = User.withoutPassword();

        userSteps.register(userWithoutPassword)
                .statusCode(SC_FORBIDDEN)
                .body("message", containsString("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация нового пользователя без имени")
    @Story("Проверка валидации")
    @Description("Регистрация должна завершиться ошибкой если не указано имя")
    public void shouldNotCreateUserWithoutName() {
        User userWithoutName = User.withoutName();

        userSteps.register(userWithoutName)
                .statusCode(SC_FORBIDDEN)
                .body("message", containsString("Email, password and name are required fields"));
    }

    @After
    @Step("Удаление тестового пользователя через API")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);

        }
    }
}
