package ru.zalimannard.dripchip.integration.account.delete;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class AccountDeleteForbiddenTests {

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
    @DisplayName("Негативный тест. Удаление не себя")
    @CsvSource(value = {
            "USER, USER",
            "USER, CHIPPER",
            "USER, ADMIN",
            "CHIPPER, USER",
            "CHIPPER, CHIPPER",
            "CHIPPER, ADMIN",
    })
    void deleteNotSelf(AccountRole activeRole, AccountRole passiveRole) {
        AccountRequestDto passiveAccountRequest = AccountFactory.createAccountRequest(passiveRole);
        AccountResponseDto createdPassiveAccount = AccountSteps.post(passiveAccountRequest, adminAuth);

        AccountRequestDto activeAccountRequest = AccountFactory.createAccountRequest(activeRole);
        AccountSteps.post(activeAccountRequest, adminAuth);
        String auth = accountToAuthConverter.convert(activeAccountRequest);

        AccountSteps.deleteExpectedForbidden(createdPassiveAccount.getId(), auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Удаление несуществующего аккаунта")
    @CsvSource(value = {
            "USER, 42424242",
            "CHIPPER, 42424242",
    })
    void deleteNonexistentAccount(AccountRole activeRole, Integer accountId) {
        AccountRequestDto activeAccountRequest = AccountFactory.createAccountRequest(activeRole);
        AccountSteps.post(activeAccountRequest, adminAuth);
        String auth = accountToAuthConverter.convert(activeAccountRequest);

        AccountSteps.deleteExpectedForbidden(accountId, auth);
    }

}
