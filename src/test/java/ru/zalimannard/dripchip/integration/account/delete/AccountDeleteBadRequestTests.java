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
import ru.zalimannard.dripchip.integration.AccountToAuthConverter;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountFactory;
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountDeleteBadRequestTests {

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
    }, nullValues = {"null"})
    void invalidAccountId(String role, Integer accountId) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, adminAuth);
        String auth = accountToAuthConverter.convert(requester);

        AccountSteps.deleteExpectedBadRequest(accountId, auth);
    }

    @Test
    @DisplayName("Негативный тест. Аккаунт связан с животным")
    void accountIsLinkedToAnimal() {
        // TODO: Написать тест когда животное будет связано с человеком
        assertThat(false).isTrue();
    }

}
