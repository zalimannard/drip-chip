package ru.zalimannard.dripchip.integration.account.post;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.integration.AccountToAuthConverter;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountFactory;
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountPostBadRequestTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountToAuthConverter accountToAuthConverter;
    @Value("${application.init.accounts.admin.email}")
    private String adminEmail;
    @Value("${application.init.accounts.admin.password}")
    private String adminPassword;
    private String adminAuth;

    @BeforeEach
    void setUp() {
        assertThat(accountController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();

        adminAuth = accountToAuthConverter.convert(adminEmail, adminPassword);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверное имя")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidFirstname(String firstName) {
        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER.toString()).toBuilder()
                .firstName(firstName)
                .build();
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверная фамилия")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidLastname(String lastName) {
        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER.toString()).toBuilder()
                .lastName(lastName)
                .build();
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        assertThat(response).isNotNull();
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
        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER.toString()).toBuilder()
                .email(email)
                .build();
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверный пароль")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidPassword(String password) {
        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER.toString()).toBuilder()
                .password(password)
                .build();
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Негативный тест. Неверные авторизационные данные")
    void invalidAuth() {
        AccountRequestDto nonexistentAccount = AccountFactory.createAccountRequest(AccountRole.USER.toString());
        String auth = accountToAuthConverter.convert(nonexistentAccount);

        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER.toString());
        AccountSteps.postExpectedUnauthorized(request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверная роль")
    @CsvSource(value = {
            "null",
            "ADM",
            "SYS",
            "VOVA"}, nullValues = {"null"})
    void invalidRole(String role) {
        AccountRequestDto request = AccountFactory.createAccountRequest(role);
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        assertThat(response).isNotNull();
    }

}