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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountPostForbiddenTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountController accountController;

    @Autowired
    private AccountToAuthCode accountToAuthCode;
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

        chipperAuth = accountToAuthCode.convert(chipperEmail, chipperPassword);
        userAuth = accountToAuthCode.convert(userEmail, userPassword);
    }

    @Test
    @DisplayName("Негативный тест. Пользователь пытается создать аккаунт")
    void requestByUser() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@post.forbidden1")
                .build();
        AccountSteps.postExpectedForbidden(request, chipperAuth);
    }

    @Test
    @DisplayName("Негативный тест. Пользователь пытается создать аккаунт")
    void requestByChipper() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@post.forbidden2")
                .build();
        AccountSteps.postExpectedForbidden(request, chipperAuth);
    }

}