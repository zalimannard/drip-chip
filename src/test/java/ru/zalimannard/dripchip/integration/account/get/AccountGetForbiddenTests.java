package ru.zalimannard.dripchip.integration.account.get;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.integration.AccountToAuthCode;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountFactory;
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountGetForbiddenTests {

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
        assertThat(accountController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();

        adminAuth = accountToAuthCode.convert(adminEmail, adminPassword);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Попытка получить чужой аккаунт")
    @CsvSource(value = {
            "USER, USER",
            "CHIPPER, CHIPPER",
            "USER, CHIPPER",
            "CHIPPER, USER",
            "USER, USER",
            "USER, ADMIN",
            "USER, ADMIN",
    })
    void existingUserAccountByUser(AccountRole activeRole, AccountRole passiveRole) {
        AccountRequestDto activeAccount = AccountFactory.createAccountRequest(activeRole);
        AccountSteps.post(activeAccount, adminAuth);
        String activeAccountAuth = accountToAuthCode.convert(activeAccount);

        AccountRequestDto passiveAccount = AccountFactory.createAccountRequest(passiveRole);
        AccountResponseDto createdPassiveAccount = AccountSteps.post(passiveAccount, adminAuth);
        AccountSteps.getExpectedForbidden(createdPassiveAccount.getId(), activeAccountAuth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Попытка получить несуществующий аккаунт")
    @CsvSource(value = {
            "USER, 42424242",
            "CHIPPER, 42424242",
    })
    void nonexistentAccountByUser(AccountRole role, Integer accountId) {
        AccountRequestDto account = AccountFactory.createAccountRequest(role);
        AccountSteps.post(account, adminAuth);
        String auth = accountToAuthCode.convert(account);

        AccountSteps.getExpectedForbidden(accountId, auth);
    }

}
