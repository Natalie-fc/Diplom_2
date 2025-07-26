package stellarburgers;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.enums.Ingredient;
import stellarburgers.model.Order;
import stellarburgers.model.User;
import stellarburgers.steps.OrderSteps;
import stellarburgers.steps.UserSteps;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("Заказы")
@Feature("Создание заказов")
public class OrderTests extends BaseTest {

    private final OrderSteps orderSteps = new OrderSteps();
    private final UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;

    @Before
    @Step("Создание пользователя через API")
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        user = new User();

        user.setEmail(RandomStringUtils.randomAlphabetic(8) + "@yandex.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(12));
        user.setName(RandomStringUtils.randomAlphabetic(10));

        userSteps.register(user)
                .statusCode(SC_OK);
        accessToken = userSteps.getAccessToken(user);
    }

    @Test
    @Story("Создание заказа")
    @Description("Пользователь может создать заказ с авторизацией и валидными ингредиентами")
    public void shouldCreateOrderWithAuth() {
        List<String> ingredients = List.of(
                Ingredient.BUN.getId(),
                Ingredient.MAIN.getId()
        );

        orderSteps.createOrderWithAuth(accessToken, new Order(ingredients))
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @Story("Создание заказа")
    @Description("Пользователь может создать заказ без авторизации и валидными ингредиентами")
    public void shouldCreateOrderWithoutAuth() {
        List<String> ingredients = List.of(
                Ingredient.SAUCE.getId()
        );

        orderSteps.createOrderWithoutAuth(new Order(ingredients))
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @Story("Создание заказа")
    @Description("Нельзя создать заказ с авторизацией без ингредиентов")
    public void shouldNotCreateOrderWithoutIngredientsWithAuth() {
        orderSteps.createOrderWithAuth(accessToken, new Order())
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @Story("Создание заказа")
    @Description("Нельзя создать заказ без авторизации без ингредиентов")
    public void shouldNotCreateOrderWithoutIngredientsWithoutAuth() {
        orderSteps.createOrderWithoutAuth(new Order())
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @Story("Создание заказа")
    @Description("Попытка создать заказ с невалидным ингредиентом возвращает 500")
    public void shouldNotCreateOrderWithInvalidIngredient() {
        List<String> invalidIngredients = List.of(
                Ingredient.INVALID.getId()
        );

        orderSteps.createOrderWithAuth(accessToken, new Order(invalidIngredients))
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    @Step("Удаление тестового пользователя через API")
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}

