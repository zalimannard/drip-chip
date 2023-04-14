package ru.zalimannard.dripchip.integration.account.post;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.integration.AccountToAuthCode;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountDefaultDtos;
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountPostBadRequestTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountToAuthCode accountToAuthCode;
    @Value("${application.init.accounts.admin.email}")
    private String adminEmail;
    @Value("${application.init.accounts.admin.password}")
    private String adminPassword;
    private String adminAuth;

    @BeforeEach
    void setUp() {
        Assertions.assertNotNull(accountController);

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();

        adminAuth = accountToAuthCode.convert(adminEmail, adminPassword);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверное имя")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void incorrectFirstname(String firstName) {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .firstName(firstName)
                .build();
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        Assertions.assertNotNull(response);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверная фамилия")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void incorrectLastname(String lastName) {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .lastName(lastName)
                .build();
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        Assertions.assertNotNull(response);
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
    void incorrectEmail(String email) {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email(email)
                .build();
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        Assertions.assertNotNull(response);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверный пароль")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void incorrectPassword(String password) {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email(password)
                .build();
        ExceptionResponse response = AccountSteps.postExpectedBadRequest(request, adminAuth);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Негативный тест. Неверные авторизационные данные")
    void incorrectAuth() {
        String auth = accountToAuthCode.convert("unexistedaccount", "unexistedaccount");

        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .build();
        AccountSteps.postExpectedUnauthorized(request, auth);
    }

    @Test
    @DisplayName("Негативный тест. Неавторизованный аккаунт")
    void withoutAuth() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .build();
        AccountSteps.postExpectedUnauthorized(request, null);
    }

}