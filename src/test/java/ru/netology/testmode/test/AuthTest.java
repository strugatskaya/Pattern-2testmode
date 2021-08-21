package ru.netology.testmode.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id='action-login']").click();
        $$(".heading").find(exactText("Личный кабинет")).should(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        form.$("[data-test-id='action-login']").click();
        $(byText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(5));
    }


    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(blockedUser.getLogin());
        form.$("[data-test-id=password] input").setValue(blockedUser.getPassword());
        form.$("[data-test-id='action-login']").click();
        $(byText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(5));
    }


    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(getRandomLogin());
        form.$("[data-test-id=password] input").setValue(registeredUser.getPassword());
        form.$("[data-test-id='action-login']").click();
        $(byText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        SelenideElement form = $(".form");
        form.$("[data-test-id=login] input").setValue(registeredUser.getLogin());
        form.$("[data-test-id=password] input").setValue(getRandomPassword());
        form.$("[data-test-id='action-login']").click();
        $(byText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(5));
    }
}

