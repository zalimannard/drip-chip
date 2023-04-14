package ru.zalimannard.dripchip.integration.registration.post;

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
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.integration.registration.RegistrationDefaultDtos;
import ru.zalimannard.dripchip.integration.registration.RegistrationSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationController;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationPostCreatedTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AuthenticationController authenticationController;
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
        Assertions.assertNotNull(authenticationController);
        Assertions.assertNotNull(accountController);

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();

        adminAuth = accountToAuthCode.convert(adminEmail, adminPassword);
    }

    @Test
    @DisplayName("Позитивный тест. Запрос успешно выполнен")
    void positiveTest() {
        AuthenticationDto request = RegistrationDefaultDtos.defaultAuthentication.toBuilder()
                .email("registration@created.1")
                .build();
        AccountResponseDto actual = RegistrationSteps.registration(request, null);
        AccountResponseDto expected = RegistrationDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("registration@created.1")
                .build();

        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);
    }

}
