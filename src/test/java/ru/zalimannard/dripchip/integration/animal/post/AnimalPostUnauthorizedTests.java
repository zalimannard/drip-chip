package ru.zalimannard.dripchip.integration.animal.post;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeController;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;
import ru.zalimannard.dripchip.schema.location.LocationController;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalPostUnauthorizedTests {

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
        assertThat(locationController).isNotNull();
        assertThat(animalTypeController).isNotNull();
        assertThat(animalController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();
    }

    @Test
    @DisplayName("Негативный тест. Запрос без авторизационных данных")
    void withoutAuth() {
        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                Set.of(animalTypeResponse.getId()),
                chipperResponse.getId(),
                chippingLocationResponse.getId());
        AnimalSteps.postExpectedUnauthorized(requestAnimal, null);
    }

    @Test
    @DisplayName("Негативный тест. Запрос от несуществующего аккаунта")
    void invalidAuth() {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(AccountRole.USER.toString());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                Set.of(animalTypeResponse.getId()),
                chipperResponse.getId(),
                chippingLocationResponse.getId());
        AnimalSteps.postExpectedUnauthorized(requestAnimal, auth);
    }

}
