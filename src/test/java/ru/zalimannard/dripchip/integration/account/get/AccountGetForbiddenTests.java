package ru.zalimannard.dripchip.integration.account.get;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.integration.AccountToAuthCode;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountDefaultDtos;
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

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
    @DisplayName("Негативный тест. Пользователь запрашивает несуществующий аккаунт")
    @ValueSource(ints = {42424242})
    void nonexistentAccountByUser(Integer accountId) {
        AccountSteps.getExpectedForbidden(accountId, userAuth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Чипировщик запрашивает несуществующий аккаунт")
    @ValueSource(ints = {42424242})
    void nonexistentAccountByChipper(Integer accountId) {
        AccountSteps.getExpectedForbidden(accountId, chipperAuth);
    }

    @Test
    @DisplayName("Негативный тест. Пользователь запрашивает существующий чужой аккаунт пользователя")
    void existingUserAccountByUser() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.forbidden3")
                .role(AccountRole.USER)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.forbidden3")
                .role(AccountRole.USER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountSteps.getExpectedForbidden(actual.getId(), userAuth);
    }

    @Test
    @DisplayName("Негативный тест. Чипировщик запрашивает существующий чужой аккаунт чипировщика")
    void existingChipperAccountByChipper() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.forbidden4")
                .role(AccountRole.CHIPPER)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.forbidden4")
                .role(AccountRole.CHIPPER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountSteps.getExpectedForbidden(actual.getId(), chipperAuth);
    }

    @Test
    @DisplayName("Негативный тест. Пользователь запрашивает существующий чужой аккаунт чипировщика")
    void existingChipperAccountByUser() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.forbidden5")
                .role(AccountRole.CHIPPER)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.forbidden5")
                .role(AccountRole.CHIPPER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountSteps.getExpectedForbidden(actual.getId(), userAuth);
    }

    @Test
    @DisplayName("Негативный тест. Чипировщик запрашивает существующий чужой аккаунт пользователя")
    void existingUserAccountByChipper() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.forbidden6")
                .role(AccountRole.USER)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.forbidden6")
                .role(AccountRole.USER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountSteps.getExpectedForbidden(actual.getId(), chipperAuth);
    }

    @Test
    @DisplayName("Негативный тест. Пользователь запрашивает существующий чужой аккаунт админа")
    void existingAdminAccountByUser() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.forbidden7")
                .role(AccountRole.ADMIN)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.forbidden7")
                .role(AccountRole.ADMIN)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountSteps.getExpectedForbidden(actual.getId(), userAuth);
    }

    @Test
    @DisplayName("Негативный тест. Чипировщик запрашивает существующий чужой аккаунт админа")
    void existingAdminAccountByChipper() {
        AccountRequestDto accountToCreate = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@get.forbidden8")
                .role(AccountRole.ADMIN)
                .build();
        AccountResponseDto actual = AccountSteps.post(accountToCreate, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@get.forbidden8")
                .role(AccountRole.ADMIN)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountSteps.getExpectedForbidden(actual.getId(), chipperAuth);
    }

}