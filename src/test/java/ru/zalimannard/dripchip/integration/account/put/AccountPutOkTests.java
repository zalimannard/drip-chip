package ru.zalimannard.dripchip.integration.account.put;

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
class AccountPutOkTests {

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
    @DisplayName("Позитивный тест. Админ меняет пользователя")
    void adminChangesUser() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@put.ok1")
                .role(AccountRole.USER)
                .build();
        AccountResponseDto actual = AccountSteps.post(request, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@put.ok1")
                .role(AccountRole.USER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);


        AccountRequestDto request2 = request.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountResponseDto expected2 = expected.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountSteps.put(actual.getId(), request2, adminAuth);

        AccountResponseDto updatedAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected2, updatedAccount);
    }

    @Test
    @DisplayName("Позитивный тест. Админ меняет чипировщика")
    void adminChangesChipper() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@put.ok2")
                .role(AccountRole.CHIPPER)
                .build();
        AccountResponseDto actual = AccountSteps.post(request, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@put.ok2")
                .role(AccountRole.CHIPPER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);


        AccountRequestDto request2 = request.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountResponseDto expected2 = expected.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountSteps.put(actual.getId(), request2, adminAuth);

        AccountResponseDto updatedAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected2, updatedAccount);
    }

    @Test
    @DisplayName("Позитивный тест. Админ меняет админа")
    void adminChangesAdmin() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@put.ok3")
                .role(AccountRole.ADMIN)
                .build();
        AccountResponseDto actual = AccountSteps.post(request, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@put.ok3")
                .role(AccountRole.ADMIN)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);


        AccountRequestDto request2 = request.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountResponseDto expected2 = expected.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountSteps.put(actual.getId(), request2, adminAuth);

        AccountResponseDto updatedAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected2, updatedAccount);
    }

    @Test
    @DisplayName("Позитивный тест. Чипировщик меняет себя")
    void chipperChangesChipper() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@put.ok4")
                .role(AccountRole.CHIPPER)
                .build();
        AccountResponseDto actual = AccountSteps.post(request, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@put.ok4")
                .role(AccountRole.CHIPPER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);
        String auth = accountToAuthCode.convert(request);


        AccountRequestDto request2 = request.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountResponseDto expected2 = expected.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountSteps.put(actual.getId(), request2, auth);

        AccountResponseDto updatedAccount = AccountSteps.get(actual.getId(), auth);
        Assertions.assertEquals(expected2, updatedAccount);
    }

    @Test
    @DisplayName("Позитивный тест. Пользователь меняет себя")
    void userChangesUser() {
        AccountRequestDto request = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email("account@put.ok5")
                .role(AccountRole.USER)
                .build();
        AccountResponseDto actual = AccountSteps.post(request, adminAuth);
        AccountResponseDto expected = AccountDefaultDtos.defaultAccountResponse.toBuilder()
                .id(actual.getId())
                .email("account@put.ok5")
                .role(AccountRole.USER)
                .build();
        Assertions.assertEquals(expected, actual);

        AccountResponseDto createdAccount = AccountSteps.get(actual.getId(), adminAuth);
        Assertions.assertEquals(expected, createdAccount);
        String auth = accountToAuthCode.convert(request);


        AccountRequestDto request2 = request.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountResponseDto expected2 = expected.toBuilder()
                .firstName("Дмитрий")
                .build();
        AccountSteps.put(actual.getId(), request2, auth);

        AccountResponseDto updatedAccount = AccountSteps.get(actual.getId(), auth);
        Assertions.assertEquals(expected2, updatedAccount);
    }

}