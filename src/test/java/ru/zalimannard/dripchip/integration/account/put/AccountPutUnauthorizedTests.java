package ru.zalimannard.dripchip.integration.account.put;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class AccountPutUnauthorizedTests {

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
    @DisplayName("Негативный тест. Запрос без авторизационных данных")
    void withoutAuth() {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);

        AccountRequestDto changedAccount = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountSteps.putExpectedUnauthorized(createdAccount.getId(), changedAccount, null);
    }

    @Test
    @DisplayName("Негативный тест. Запрос от несуществующего аккаунта")
    void invalidAuth() {
        AccountRequestDto requester = AccountFactory.createAccountRequest(AccountRole.USER);
        String auth = accountToAuthConverter.convert(requester);

        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);

        AccountRequestDto changedAccount = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountSteps.putExpectedUnauthorized(createdAccount.getId(), changedAccount, auth);
    }

}