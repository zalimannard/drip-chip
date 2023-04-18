package ru.zalimannard.dripchip.integration.animal.put;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeController;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;
import ru.zalimannard.dripchip.schema.location.LocationController;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalPutBadRequestTests {

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

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректный вес")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0.0",
            "ADMIN, -1.0",
            "ADMIN, -424242.0",
            "CHIPPER, null",
            "CHIPPER, 0.0",
            "CHIPPER, -1.0",
            "CHIPPER, -424242.0",
    }, nullValues = {"null"})
    void invalidWeight(String requesterRole, Float weight) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
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
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());


        AccountRequestDto chipperRequest2 = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse2 = AccountSteps.post(chipperRequest2, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest2 = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse2 = LocationSteps.post(chippingLocationRequest2, defaultAuth.adminAuth());

        AnimalPutRequestDto request = AnimalFactory.createAnimalPutRequest(chipperResponse2.getId(), chippingLocationResponse2.getId()).toBuilder()
                .weight(weight)
                .build();
        AnimalSteps.putExpectedBadRequest(responseAnimal.getId(), request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректная длина")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0.0",
            "ADMIN, -1.0",
            "ADMIN, -424242.0",
            "CHIPPER, null",
            "CHIPPER, 0.0",
            "CHIPPER, -1.0",
            "CHIPPER, -424242.0",
    }, nullValues = {"null"})
    void invalidLength(String requesterRole, Float length) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
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
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());


        AccountRequestDto chipperRequest2 = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse2 = AccountSteps.post(chipperRequest2, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest2 = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse2 = LocationSteps.post(chippingLocationRequest2, defaultAuth.adminAuth());

        AnimalPutRequestDto request = AnimalFactory.createAnimalPutRequest(chipperResponse2.getId(), chippingLocationResponse2.getId()).toBuilder()
                .length(length)
                .build();
        AnimalSteps.putExpectedBadRequest(responseAnimal.getId(), request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректная высота")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0.0",
            "ADMIN, -1.0",
            "ADMIN, -424242.0",
            "CHIPPER, null",
            "CHIPPER, 0.0",
            "CHIPPER, -1.0",
            "CHIPPER, -424242.0",
    }, nullValues = {"null"})
    void invalidHeight(String requesterRole, Float height) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
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
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());


        AccountRequestDto chipperRequest2 = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse2 = AccountSteps.post(chipperRequest2, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest2 = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse2 = LocationSteps.post(chippingLocationRequest2, defaultAuth.adminAuth());

        AnimalPutRequestDto request = AnimalFactory.createAnimalPutRequest(chipperResponse2.getId(), chippingLocationResponse2.getId()).toBuilder()
                .height(height)
                .build();
        AnimalSteps.putExpectedBadRequest(responseAnimal.getId(), request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректный пол")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, CHEL",
            "CHIPPER, null",
            "CHIPPER, CHEL",
    }, nullValues = {"null"})
    void invalidGender(String requesterRole, String gender) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
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
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());


        AccountRequestDto chipperRequest2 = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse2 = AccountSteps.post(chipperRequest2, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest2 = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse2 = LocationSteps.post(chippingLocationRequest2, defaultAuth.adminAuth());

        AnimalPutRequestDto request = AnimalFactory.createAnimalPutRequest(chipperResponse2.getId(), chippingLocationResponse2.getId()).toBuilder()
                .gender(gender)
                .build();
        AnimalSteps.putExpectedBadRequest(responseAnimal.getId(), request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректный жизненный статус")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, SLEEP",
            "CHIPPER, null",
            "CHIPPER, SLEEP",
    }, nullValues = {"null"})
    void invalidLifeStatus(String requesterRole, String lifeStatus) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
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
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());


        AccountRequestDto chipperRequest2 = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse2 = AccountSteps.post(chipperRequest2, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest2 = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse2 = LocationSteps.post(chippingLocationRequest2, defaultAuth.adminAuth());

        AnimalPutRequestDto request = AnimalFactory.createAnimalPutRequest(chipperResponse2.getId(), chippingLocationResponse2.getId()).toBuilder()
                .lifeStatus(lifeStatus)
                .build();
        AnimalSteps.putExpectedBadRequest(responseAnimal.getId(), request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Запрос некорректного animalId")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0",
            "ADMIN, -1",
            "ADMIN, -424242",
            "CHIPPER, null",
            "CHIPPER, 0",
            "CHIPPER, -1",
            "CHIPPER, -424242",
    }, nullValues = {"null"})
    void invalidAnimalId(String role, Long animalId) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPutRequestDto requestAnimal = AnimalFactory.createAnimalPutRequest(
                chipperResponse.getId(),
                chippingLocationResponse.getId());

        AnimalSteps.putExpectedBadRequest(animalId, requestAnimal, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректный chipperId")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0",
            "ADMIN, -1",
            "ADMIN, -424242",
            "CHIPPER, null",
            "CHIPPER, 0",
            "CHIPPER, -1",
            "CHIPPER, -424242",
    }, nullValues = {"null"})
    void invalidChipperId(String role, Integer chipperId) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
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
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());


        LocationRequestDto chippingLocationRequest2 = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse2 = LocationSteps.post(chippingLocationRequest2, defaultAuth.adminAuth());

        AnimalPutRequestDto request = AnimalFactory.createAnimalPutRequest(chipperId, chippingLocationResponse2.getId());
        AnimalSteps.putExpectedBadRequest(responseAnimal.getId(), request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректный chippingLocationId")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0",
            "ADMIN, -1",
            "ADMIN, -424242",
            "CHIPPER, null",
            "CHIPPER, 0",
            "CHIPPER, -1",
            "CHIPPER, -424242",
    }, nullValues = {"null"})
    void invalidChippingLocationId(String role, Long chippingLocationId) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
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
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());


        AccountRequestDto chipperRequest2 = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
        AccountResponseDto chipperResponse2 = AccountSteps.post(chipperRequest2, defaultAuth.adminAuth());

        AnimalPutRequestDto request = AnimalFactory.createAnimalPutRequest(chipperResponse2.getId(), chippingLocationId);
        AnimalSteps.putExpectedBadRequest(responseAnimal.getId(), request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Смена с DEAD на ALIVE")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
    })
    void lifeStatusFromDeadToAlive(String requesterRole) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
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
        AnimalResponseDto responseAnimal = AnimalSteps.post(requestAnimal, defaultAuth.adminAuth());

        AnimalPutRequestDto requestForDead = AnimalFactory.createAnimalPutRequest(chipperResponse.getId(), chippingLocationResponse.getId()).toBuilder()
                .lifeStatus(AnimalLifeStatus.DEAD.toString())
                .build();
        AnimalSteps.put(responseAnimal.getId(), requestForDead, auth);

        AnimalPutRequestDto request = AnimalFactory.createAnimalPutRequest(chipperResponse.getId(), chippingLocationResponse.getId()).toBuilder()
                .lifeStatus(AnimalLifeStatus.ALIVE.toString())
                .build();
        AnimalSteps.putExpectedBadRequest(responseAnimal.getId(), request, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Смена с DEAD на ALIVE")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
    })
    void chippingPointEqualsToFirstVisitedLocation(AccountRole requesterRole) {
        assertThat(true).isFalse();
    }

}
