package ru.zalimannard.dripchip.integration.account.get;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.integration.AccountToAuthCode;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountGetBadRequestTests {

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
    @Value("${application.init.accounts.chipper.email}")
    private String chipperEmail;
    @Value("${application.init.accounts.chipper.password}")
    private String chipperPassword;
    private String chipperAuth;
    @Value("${application.init.accounts.user.email}")
    private String userEmail;
    @Value("${application.init.accounts.user.password}")
    private String userPassword;
    private String userAuth;

    @BeforeEach
    void setUp() {
        Assertions.assertNotNull(accountController);

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();

        adminAuth = accountToAuthCode.convert(adminEmail, adminPassword);
        chipperAuth = accountToAuthCode.convert(chipperEmail, chipperPassword);
        userAuth = accountToAuthCode.convert(userEmail, userPassword);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Админ запрашивает некорректный accountId")
    @NullSource
    @ValueSource(ints = {
            0,
            -1,
            -424242})
    void incorrectAccountIdByAdmin(Integer accountId) {
        ExceptionResponse response = AccountSteps.getExpectedBadRequest(accountId, adminAuth);
        Assertions.assertNotNull(response);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Чипировщик запрашивает некорректный accountId")
    @NullSource
    @ValueSource(ints = {
            0,
            -1,
            -424242})
    void incorrectAccountIdByChipper(Integer accountId) {
        ExceptionResponse response = AccountSteps.getExpectedBadRequest(accountId, chipperAuth);
        Assertions.assertNotNull(response);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Пользователь запрашивает некорректный accountId")
    @NullSource
    @ValueSource(ints = {
            0,
            -1,
            -424242})
    void incorrectAccountIdByUser(Integer accountId) {
        ExceptionResponse response = AccountSteps.getExpectedBadRequest(accountId, userAuth);
        Assertions.assertNotNull(response);
    }

}