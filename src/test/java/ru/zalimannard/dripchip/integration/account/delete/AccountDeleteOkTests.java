package ru.zalimannard.dripchip.integration.account.delete;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class AccountDeleteOkTests {

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

    @Test
    @DisplayName("Позитивный тест. Админ удаляет аккаунт")
    void adminDeleteAccount() {
        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto actual = AccountSteps.post(request, adminAuth);
        AccountResponseDto expected = AccountFactory.createAccountResponse(actual.getId(), request);
        assertThat(actual).isEqualTo(expected);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        assertThat(createdAccount).isEqualTo(expected);

        AccountSteps.delete(actual.getId(), adminAuth);
        ExceptionResponse response = AccountSteps.getExpectedNotFound(actual.getId(), adminAuth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Позитивный тест. Удаление своего аккаунта")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
            "USER",
    })
    void deleteSelfOwnAccount(AccountRole role) {
        AccountRequestDto request = AccountFactory.createAccountRequest(role);
        AccountResponseDto actual = AccountSteps.post(request, adminAuth);
        AccountResponseDto expected = AccountFactory.createAccountResponse(actual.getId(), request);
        assertThat(actual).isEqualTo(expected);
        String auth = accountToAuthConverter.convert(request);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        assertThat(createdAccount).isEqualTo(expected);

        AccountSteps.delete(actual.getId(), auth);
        ExceptionResponse response = AccountSteps.getExpectedNotFound(actual.getId(), adminAuth);
        assertThat(response).isNotNull();
    }

}
