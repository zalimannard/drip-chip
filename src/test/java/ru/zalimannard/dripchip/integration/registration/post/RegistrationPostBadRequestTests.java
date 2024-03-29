package ru.zalimannard.dripchip.integration.registration.post;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.integration.AccountToAuthConverter;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.registration.RegistrationDefaultDtos;
import ru.zalimannard.dripchip.integration.registration.RegistrationSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationController;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationPostBadRequestTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountToAuthConverter accountToAuthConverter;

    @BeforeEach
    void setUp() {
        assertThat(authenticationController).isNotNull();
        assertThat(accountController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверное имя")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidFirstname(String firstName) {
        AuthenticationDto request = RegistrationDefaultDtos.defaultAuthentication.toBuilder()
                .firstName(firstName)
                .build();
        RegistrationSteps.registrationExpectedBadRequest(request, null);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверная фамилия")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidLastname(String lastName) {
        AuthenticationDto request = RegistrationDefaultDtos.defaultAuthentication.toBuilder()
                .lastName(lastName)
                .build();
        RegistrationSteps.registrationExpectedBadRequest(request, null);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверный email")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   ",
            "a",
            "a@.a",
            "----@",
            "a@mail@ru",
            "a@mail@mail.ru"})
    void invalidEmail(String email) {
        AuthenticationDto request = RegistrationDefaultDtos.defaultAuthentication.toBuilder()
                .email(email)
                .build();
        RegistrationSteps.registrationExpectedBadRequest(request, null);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверный пароль")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidPassword(String password) {
        AuthenticationDto request = RegistrationDefaultDtos.defaultAuthentication.toBuilder()
                .email(password)
                .build();
        RegistrationSteps.registrationExpectedBadRequest(request, null);
    }

// TODO: Я не знаю как хорошо реализовать это разграничение

//    @Test
//    @DisplayName("Негативный тест. Неверные авторизационные данные")
//    void invalidAuth() {
//        String auth = accountToAuthConverter.convert("unexistedaccount", "unexistedaccount");
//
//        AuthenticationDto request = RegistrationDefaultDtos.defaultAuthentication.toBuilder()
//                .build();
//        RegistrationSteps.registrationExpectedBadRequest(request, auth);
//    }

}
