package tests;

import api.clientStellarBurgers.models.stellarburgers.User;
import api.clientStellarBurgers.UserClient;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

@Feature("Изменение данных пользователя")
public class ChangeUserTest {


    private String name;
    private String email;
    private String password;
    private UserClient userClient;
    private User user;
    private String accessToken;

    private final String modifiedName = "DGHsan";
    private final String modifiedEmail = "DGHann@yandex.ru";
    private final String modifiedPassword = "1645sv";
    User changeUser = new User();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = "Nikolay";
        email = "nikolay.praktikum@yandex.ru";
        password = "X1MtS44yLC";
        userClient = new UserClient();
        user = new User(name, email, password);
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.checkRequestAuthLogin(user).then().extract().path("accessToken");
    }


    @Test
    @DisplayName("Изменение имени пользователя с авторизацией.")
    @Description("Успешное изменение имени пользователя с авторизацией.")
    public void changeUserNameWithAuthorizationTest() {
        changeUser.setName(modifiedName);
        user.setName(modifiedName);
        Response response = userClient.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией.")
    @Description("Успешное изменение email пользователя с авторизацией.")
    public void changeUserEmailWithAuthorizationTest() {
        changeUser.setName(modifiedEmail);
        user.setEmail(modifiedEmail);
        Response response = userClient.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией.")
    @Description("Успешное изменение пароля пользователя с авторизацией.")
    public void changeUserPasswordWithAuthorizationTest() {
        changeUser.setPassword(modifiedPassword);
        user.setPassword(modifiedPassword);
        Response response = userClient.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        userClient.checkSuccessResponseAuthUser(response, email, name);
    }


    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
