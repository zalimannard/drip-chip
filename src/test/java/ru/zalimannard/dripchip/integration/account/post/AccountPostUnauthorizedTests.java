package ru.zalimannard.dripchip.integration.account.post;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.integration.AccountToAuthConverter;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountFactory;
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountPostUnauthorizedTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountToAuthConverter accountToAuthConverter;

    @BeforeEach
    void setUp() {
        assertThat(accountController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();
    }

    @Test
    @DisplayName("Негативный тест. Запрос без авторизационных данных")
    void withoutAuth() {
        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountSteps.postExpectedUnauthorized(request, null);
    }

    @Test
    @DisplayName("Негативный тест. Запрос от несуществующего аккаунта")
    void invalidAuth() {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER);
        String auth = accountToAuthConverter.convert(account);

        AccountRequestDto request = AccountFactory.createAccountRequest(AccountRole.USER);
        AccountSteps.postExpectedUnauthorized(request, auth);
    }

}