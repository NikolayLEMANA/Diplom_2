package tests;

import api.clientStellarBurgers.models.stellarburgers.User;
import api.clientStellarBurgers.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Feature("Логин пользователя")
public class LoginUserTests {
    private String email;
    private String password;
    private String name;
    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = "Nikolay";
        email = "nikolay.praktikum@yandex.ru";
        password = "X1MtS44yLC";
        userClient = new UserClient();
        user = new User(name, email, password);
    }


    @Test
    @DisplayName("Авторизация пользователя.")
    @Description("Авторизация пользователя под существующем логином")
    public void authorizationTest() {
        user = new User(name, email, password);
        UserClient.postCreateNewUser(user);
        Response response = UserClient.checkRequestAuthLogin(user);
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным логином.")
    @Description("Авторизация пользователя c некорректным логином.")
    public void authorizationIncorrectLoginTest() {
        user = new User(email, password);
        user.setEmail("Hdnasjdhbajhnwjdnakljndj2783o127" + email);
        Response response = UserClient.checkRequestAuthLogin(user);
        userClient.checkFailedResponseAuthLogin(response);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем.")
    @Description("Авторизация пользователя c некорректным паролем.")
    public void authorizationIncorrectPasswordTest() {
        user = new User(email, password);
        user.setPassword("6482HSVbsj" + password);
        Response response = UserClient.checkRequestAuthLogin(user);
        userClient.checkFailedResponseAuthLogin(response);
    }

    @Test
    @DisplayName("Авторизация без логина.")
    @Description("Авторизация пользователя без логина.")
    public void authorizationWithoutLoginTest() {
        user = new User(email, password);
        user.setPassword(password);
        Response response = UserClient.checkRequestAuthLogin(user);
        userClient.checkFailedResponseAuthLogin(response);
    }

    @Test
    @DisplayName("Авторизация без пароля.")
    @Description("Авторизация пользователя без пароля")
    public void authorizationWithoutPasswordTest() {
        user = new User(email, password);
        user.setEmail(email);
        Response response = UserClient.checkRequestAuthLogin(user);
        userClient.checkFailedResponseAuthLogin(response);
    }

    @Test
    @DisplayName("Авторизация без логина пароля.")
    @Description("Авторизация пользователя без пароля")
    public void authorizationWithoutLoginAndPasswordTest() {
        Response response = UserClient.checkRequestAuthLogin(user);
        userClient.checkFailedResponseAuthLogin(response);
    }

    @After
    public void tearDown(){
        String accessToken = UserClient.checkRequestAuthLogin(user).then().extract().path("accessToken");
        if (accessToken !=null) {
            userClient.deleteUser(accessToken);
        }
    }
}
