package ru.zalimannard.dripchip.integration.account.get;

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
class AccountGetUnauthorizedTests {

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
    @DisplayName("Негативный тест. Запрос несуществующего аккаунта от неавторизированного аккаунта")
    @CsvSource(value = {
            "42424242",
    })
    void nonexistentAccountByUnauthorized() {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        String auth = accountToAuthConverter.convert(account);
        AccountSteps.getExpectedUnauthorized(42424242, auth);
    }

    @Test
    @DisplayName("Негативный тест. Запрос существующего аккаунта от неавторизированного аккаунта")
    void existingAccountByUserUnauthorized() {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);

        AccountRequestDto requester = AccountFactory.createAccountRequest(AccountRole.USER);
        String auth = accountToAuthConverter.convert(requester);
        AccountSteps.getExpectedUnauthorized(createdAccount.getId(), auth);
    }

}