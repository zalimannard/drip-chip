package ru.zalimannard.dripchip.integration.animal.get;

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
import ru.zalimannard.dripchip.integration.account.AccountSteps;
import ru.zalimannard.dripchip.integration.animal.AnimalFactory;
import ru.zalimannard.dripchip.integration.animal.AnimalSteps;
import ru.zalimannard.dripchip.integration.animaltype.AnimalTypeFactory;
import ru.zalimannard.dripchip.integration.animaltype.AnimalTypeSteps;
import ru.zalimannard.dripchip.integration.location.LocationFactory;
import ru.zalimannard.dripchip.integration.location.LocationSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;
import ru.zalimannard.dripchip.schema.animal.AnimalController;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeController;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;
import ru.zalimannard.dripchip.schema.location.LocationController;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalGetUnauthorizedTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountController accountController;
    @Autowired
    private LocationController locationController;
    @Autowired
    private AnimalTypeController animalTypeController;
    @Autowired
    private AnimalController animalController;

    @Autowired
    private AccountToAuthConverter accountToAuthConverter;
    @Autowired
    private DefaultAuth defaultAuth;

    @BeforeEach
    void setUp() {
        assertThat(accountController).isNotNull();
        assertThat(animalController).isNotNull();
        assertThat(animalTypeController).isNotNull();
        assertThat(animalController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Запрос несуществующего животного без авторизации")
    @CsvSource(value = {
            "42424242",
    })
    void nonexistingAnimalWithoutAuth(Long animalId) {
        AnimalSteps.getExpectedUnauthorized(animalId, null);
    }

    @Test
    @DisplayName("Негативный тест. Запрос существующего животного без авторизации")
    void existingAnimalWithoutAuth() {
        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                Set.of(animalTypeResponse.getId()),
                chipperResponse.getId(),
                chippingLocationResponse.getId());
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());

        AnimalSteps.getExpectedUnauthorized(responseAnimal.getId(), null);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Запрос несуществующего животного от несуществующего аккаунта")
    @CsvSource(value = {
            "42424242",
    })
    void nonexistingAnimalByNonexistentUser(Long animalId) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(AccountRole.USER);
        String auth = accountToAuthConverter.convert(requester);
        AnimalSteps.getExpectedUnauthorized(animalId, auth);
    }

    @Test
    @DisplayName("Негативный тест. Запрос существующего животного от несуществующего аккаунта")
    void existingAnimalByNonexistentUser() {
        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                Set.of(animalTypeResponse.getId()),
                chipperResponse.getId(),
                chippingLocationResponse.getId());
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());

        AccountRequestDto requester = AccountFactory.createAccountRequest(AccountRole.USER);
        String auth = accountToAuthConverter.convert(requester);
        AnimalSteps.getExpectedUnauthorized(responseAnimal.getId(), auth);
    }

}
