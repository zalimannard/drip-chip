package ru.zalimannard.dripchip.integration.location.delete;

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
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;
import ru.zalimannard.dripchip.schema.location.LocationController;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocationDeleteBadRequestTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountController accountController;
    @Autowired
    private LocationController locationController;

    @Autowired
    private AccountToAuthConverter accountToAuthConverter;
    @Autowired
    private DefaultAuth defaultAuth;

    @BeforeEach
    void setUp() {
        assertThat(accountController).isNotNull();
        assertThat(locationController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверный locationId")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, 0",
            "ADMIN, -1",
            "ADMIN, -424242",
    }, nullValues = {"null"})
    void invalidLocationId(String role, Long locationId) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requester);

        LocationSteps.deleteExpectedBadRequest(locationId, auth);
    }

    @Test
    @DisplayName("Негативный тест. Точка локации связана с животным")
    void accountIsLinkedToAnimal() {
        AccountRequestDto requesterRequest = AccountFactory.createAccountRequest(AccountRole.CHIPPER.toString());
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
        AnimalSteps.post(requestAnimal, auth);

        LocationSteps.deleteExpectedBadRequest(chippingLocationResponse.getId(), defaultAuth.adminAuth());
    }

}