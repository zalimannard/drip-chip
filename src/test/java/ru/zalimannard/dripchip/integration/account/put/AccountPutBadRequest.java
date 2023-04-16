package ru.zalimannard.dripchip.integration.account.put;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountPutBadRequest {

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
    @DisplayName("Негативный тест. Неверный accountId")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0",
            "ADMIN, -1",
            "ADMIN, -424242",
            "CHIPPER, null",
            "CHIPPER, 0",
            "CHIPPER, -1",
            "CHIPPER, -424242",
            "USER, null",
            "USER, 0",
            "USER, -1",
            "USER, -424242",
    }, nullValues = {"null"})
    void invalidAccountId(AccountRole role, Integer id) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, adminAuth);
        String auth = accountToAuthConverter.convert(requester);

        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER);
        ExceptionResponse response = AccountSteps.putExpectedBadRequest(id, request, auth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверное имя")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidFirstname(String firstname) {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);

        AccountRequestDto changedAccount = account.toBuilder()
                .firstName(firstname)
                .build();
        AccountSteps.putExpectedBadRequest(createdAccount.getId(), changedAccount, adminAuth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверная фамилия")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidLastname(String lastname) {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);

        AccountRequestDto changedAccount = account.toBuilder()
                .lastName(lastname)
                .build();
        AccountSteps.putExpectedBadRequest(createdAccount.getId(), changedAccount, adminAuth);
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
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);

        AccountRequestDto changedAccount = account.toBuilder()
                .email(email)
                .build();
        AccountSteps.putExpectedBadRequest(createdAccount.getId(), changedAccount, adminAuth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверный пароль")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidPassword(String password) {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);

        AccountRequestDto changedAccount = account.toBuilder()
                .password(password)
                .build();
        AccountSteps.putExpectedBadRequest(createdAccount.getId(), changedAccount, adminAuth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверная роль")
    @CsvSource(value = {
            "null",
            "ADM",
            "SYS",
            "VOVA"}, nullValues = {"null"})
    void invalidRole(AccountRole role) {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);

        AccountRequestDto changedAccount = account.toBuilder()
                .role(role)
                .build();
        AccountSteps.putExpectedBadRequest(createdAccount.getId(), changedAccount, adminAuth);
    }

}