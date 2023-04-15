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
class AccountGetOkTests {

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
    @DisplayName("Позитивный тест. Админ запрашивает чужой аккаунт")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
            "USER",
    })
    void positiveTestAccountByAdmin(AccountRole role) {
        AccountRequestDto admin = AccountFactory.createAccountRequest(AccountRole.ADMIN);
        AccountSteps.post(admin, adminAuth);
        String auth = accountToAuthConverter.convert(admin);

        AccountRequestDto account = AccountFactory.createAccountRequest(role);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);
        AccountResponseDto expectedAccount = AccountFactory.createAccountResponse(createdAccount.getId(), account);
        assertThat(expectedAccount).isEqualTo(createdAccount);

        AccountResponseDto gotAccount = AccountSteps.get(createdAccount.getId(), auth);
        assertThat(expectedAccount).isEqualTo(gotAccount);
    }

    @ParameterizedTest
    @DisplayName("Позитивный тест. Запрос данных о себе")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
            "USER",
    })
    void positiveTestAccountBySelf(AccountRole role) {
        AccountRequestDto account = AccountFactory.createAccountRequest(role);
        AccountResponseDto createdAccount = AccountSteps.post(account, adminAuth);
        String auth = accountToAuthConverter.convert(account);
        AccountResponseDto expectedAccount = AccountFactory.createAccountResponse(createdAccount.getId(), account);

        AccountResponseDto gotAccount = AccountSteps.get(createdAccount.getId(), auth);
        assertThat(expectedAccount).isEqualTo(gotAccount);
    }

}