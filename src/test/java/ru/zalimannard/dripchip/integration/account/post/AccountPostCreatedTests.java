package ru.zalimannard.dripchip.integration.account.post;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.integration.AccountToAuthCode;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountDefaultDtos;
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountPostCreatedTests {

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

    @Test
    @DisplayName("Позитивный тест. Запрос успешно выполнен")
    void positiveTest() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@post.created1")
                .build();
        AccountResponseDto actual = AccountSteps.post(request, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@post.created1")
                .build();

        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);
    }
}