package ru.zalimannard.dripchip.integration.animaltype.get;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.integration.AccountToAuthConverter;
import ru.zalimannard.dripchip.integration.DefaultAuth;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.account.AccountFactory;
import ru.zalimannard.dripchip.integration.animaltype.AnimalTypeFactory;
import ru.zalimannard.dripchip.integration.animaltype.AnimalTypeSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalTypeGetUnauthorizedTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountController accountController;
    @Autowired
    private AccountController animalTypeController;

    @Autowired
    private AccountToAuthConverter accountToAuthConverter;
    @Autowired
    private DefaultAuth defaultAuth;

    @BeforeEach
    void setUp() {
        assertThat(accountController).isNotNull();
        assertThat(animalTypeController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Запрос несуществующего типа животного без авторизации")
    @CsvSource(value = {
            "42424242",
    })
    void nonexistingAnimalTypeWithoutAuth(Long animalTypeId) {
        AnimalTypeSteps.getExpectedUnauthorized(animalTypeId, null);
    }

    @Test
    @DisplayName("Негативный тест. Запрос существующего типа животного без авторизации")
    void existingAnimalTypeWithoutAuth() {
        AnimalTypeRequestDto animalType = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto createdAnimalType = AnimalTypeSteps.post(animalType, defaultAuth.adminAuth());

        AnimalTypeSteps.getExpectedUnauthorized(createdAnimalType.getId(), null);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Запрос несуществующего типа животного от несуществующего аккаунта")
    @CsvSource(value = {
            "42424242",
    })
    void nonexistingAnimalTypeByNonexistentUser(Long animalTypeId) {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER.toString());
        String auth = accountToAuthConverter.convert(account);
        AnimalTypeSteps.getExpectedUnauthorized(animalTypeId, auth);
    }

    @Test
    @DisplayName("Негативный тест. Запрос существующего типа животного от несуществующего аккаунта")
    void existingAnimalTypeByNonexistentUser() {
        AnimalTypeRequestDto animalType = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto createdAnimalType = AnimalTypeSteps.post(animalType, defaultAuth.adminAuth());

        AccountRequestDto requester = AccountFactory.createAccountRequest(AccountRole.USER.toString());
        String auth = accountToAuthConverter.convert(requester);
        AnimalTypeSteps.getExpectedUnauthorized(createdAnimalType.getId(), auth);
    }

}