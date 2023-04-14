package ru.zalimannard.dripchip.integration.account.get;

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
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountGetOkTests {

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
    @DisplayName("Позитивный тест. Админ запрашивает аккаунт пользователя")
    void positiveTestUserByAdmin() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.ok1")
                .role(AccountRole.USER)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.ok1")
                .role(AccountRole.USER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);
    }

    @Test
    @DisplayName("Позитивный тест. Админ запрашивает аккаунт чипировщика")
    void positiveTestChipperByAdmin() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.ok2")
                .role(AccountRole.CHIPPER)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.ok2")
                .role(AccountRole.CHIPPER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);
    }

    @Test
    @DisplayName("Позитивный тест. Админ запрашивает аккаунт админа")
    void positiveTestAdminByAdmin() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.ok3")
                .role(AccountRole.ADMIN)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.ok3")
                .role(AccountRole.ADMIN)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);
    }

    @Test
    @DisplayName("Позитивный тест. Пользователь запрашивает информацию о себе")
    void positiveTestUserByUser() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.ok4")
                .role(AccountRole.USER)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.ok4")
                .role(AccountRole.USER)
                .build();
        Assertions.assertEquals(expected, actual);

        String userAuth = accountToAuthCode.convert(accountToCreate);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), userAuth);
        Assertions.assertEquals(expected, createdAccount);
    }

    @Test
    @DisplayName("Позитивный тест. Чипировщик запрашивает информацию о себе")
    void positiveTestChipperByChipper() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.ok5")
                .role(AccountRole.CHIPPER)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.ok5")
                .role(AccountRole.CHIPPER)
                .build();
        Assertions.assertEquals(expected, actual);

        String chipperAuth = accountToAuthCode.convert(accountToCreate);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), chipperAuth);
        Assertions.assertEquals(expected, createdAccount);
    }

}