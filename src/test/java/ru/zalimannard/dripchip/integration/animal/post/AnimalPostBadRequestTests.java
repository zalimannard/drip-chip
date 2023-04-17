package ru.zalimannard.dripchip.integration.animal.post;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
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
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeController;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;
import ru.zalimannard.dripchip.schema.location.LocationController;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalPostBadRequestTests {

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
    @DisplayName("Негативный тест. AnimalType равен null")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
            "USER",
    })
    void nullAnimalType(AccountRole requesterRole) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                null,
                chipperResponse.getId(),
                chippingLocationResponse.getId());
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. AnimalType пустой")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
            "USER",
    })
    void emptyAnimalType(AccountRole requesterRole) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                new HashSet<>(),
                chipperResponse.getId(),
                chippingLocationResponse.getId());
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректный элемент в AnimalType")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0",
            "ADMIN, -1",
            "ADMIN, -424242",
            "CHIPPER, null",
            "CHIPPER, 0",
            "CHIPPER, -1",
            "CHIPPER, -424242",
            "USER, null",
            "USER, 0",
            "USER, -1",
            "USER, -424242",
    }, nullValues = {"null"})
    void nullElementOfAnimalType(AccountRole requesterRole, Long animalType) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                new HashSet<>(Set.of(animalType)),
                chipperResponse.getId(),
                chippingLocationResponse.getId());
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
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
            "USER, null",
            "USER, 0.0",
            "USER, -1.0",
            "USER, -424242.0",
    }, nullValues = {"null"})
    void invalidWeight(AccountRole requesterRole, Float weight) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                        Set.of(animalTypeResponse.getId()),
                        chipperResponse.getId(),
                        chippingLocationResponse.getId()).toBuilder()
                .weight(weight)
                .build();
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
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
            "USER, null",
            "USER, 0.0",
            "USER, -1.0",
            "USER, -424242.0",
    }, nullValues = {"null"})
    void invalidLength(AccountRole requesterRole, Float length) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                        Set.of(animalTypeResponse.getId()),
                        chipperResponse.getId(),
                        chippingLocationResponse.getId()).toBuilder()
                .length(length)
                .build();
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
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
            "USER, null",
            "USER, 0.0",
            "USER, -1.0",
            "USER, -424242.0",
    }, nullValues = {"null"})
    void invalidHeight(AccountRole requesterRole, Float height) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                        Set.of(animalTypeResponse.getId()),
                        chipperResponse.getId(),
                        chippingLocationResponse.getId()).toBuilder()
                .height(height)
                .build();
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Некорректный пол")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, CHEL",
            "CHIPPER, null",
            "CHIPPER, CHEL",
            "USER, null",
            "USER, CHEL",
    }, nullValues = {"null"})
    void invalidGender(AccountRole requesterRole, AnimalGender gender) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                        Set.of(animalTypeResponse.getId()),
                        chipperResponse.getId(),
                        chippingLocationResponse.getId()).toBuilder()
                .gender(gender)
                .build();
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
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
            "USER, null",
            "USER, 0",
            "USER, -1",
            "USER, -424242",
    }, nullValues = {"null"})
    void invalidChipperId(AccountRole requesterRole, Integer chipperId) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        LocationRequestDto chippingLocationRequest = LocationFactory.createLocationRequest();
        LocationResponseDto chippingLocationResponse = LocationSteps.post(chippingLocationRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                Set.of(animalTypeResponse.getId()),
                chipperId,
                chippingLocationResponse.getId());
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
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
            "USER, null",
            "USER, 0",
            "USER, -1",
            "USER, -424242",
    }, nullValues = {"null"})
    void invalidChipperId(AccountRole requesterRole, Long chippingLocationId) {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(requesterRole);
        AccountSteps.post(requesterRequest, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requesterRequest);

        AnimalTypeRequestDto animalTypeRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto animalTypeResponse = AnimalTypeSteps.post(animalTypeRequest, defaultAuth.adminAuth());

        AccountRequestDto chipperRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER);
        AccountResponseDto chipperResponse = AccountSteps.post(chipperRequest, defaultAuth.adminAuth());

        AnimalPostRequestDto requestAnimal = AnimalFactory.createAnimalPostRequest(
                Set.of(animalTypeResponse.getId()),
                chipperResponse.getId(),
                chippingLocationId);
        ExceptionResponse response = AnimalSteps.postExpectedBadRequest(requestAnimal, auth);
        assertThat(response).isNotNull();
    }

}
