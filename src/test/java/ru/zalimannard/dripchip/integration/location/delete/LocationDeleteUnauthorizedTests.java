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
import ru.zalimannard.dripchip.integration.location.LocationFactory;
import ru.zalimannard.dripchip.integration.location.LocationSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;
import ru.zalimannard.dripchip.schema.location.LocationController;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocationDeleteUnauthorizedTests {

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
    @DisplayName("Негативный тест. Удаление несуществующей локации без авторизации")
    @CsvSource(value = {
            "42424242",
    })
    void nonexistingLocationWithoutAuth(Long locationId) {
        LocationSteps.deleteExpectedUnauthorized(locationId, null);
    }

    @Test
    @DisplayName("Негативный тест. Удаление существующей локации без авторизации")
    void existingLocationWithoutAuth() {
        LocationRequestDto location = LocationFactory.createLocationRequest();
        LocationResponseDto createdLocation = LocationSteps.post(location, defaultAuth.adminAuth());

        LocationSteps.deleteExpectedUnauthorized(createdLocation.getId(), null);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Удаление несуществующей локации несуществующим аккаунтом")
    @CsvSource(value = {
            "42424242",
    })
    void nonexistingLocationByNonexistentUser(Long locationId) {
        AccountRequestDto account = AccountFactory.createAccountRequest(AccountRole.USER.toString());
        String auth = accountToAuthConverter.convert(account);

        LocationSteps.deleteExpectedUnauthorized(locationId, auth);
    }

    @Test
    @DisplayName("Негативный тест. Удаление существующей локации несуществующим аккаунтом")
    void existingLocationByNonexistentUser() {
        LocationRequestDto location = LocationFactory.createLocationRequest();
        LocationResponseDto createdLocation = LocationSteps.post(location, defaultAuth.adminAuth());

        AccountRequestDto requester = AccountFactory.createAccountRequest(AccountRole.USER.toString());
        String auth = accountToAuthConverter.convert(requester);
        LocationSteps.deleteExpectedUnauthorized(createdLocation.getId(), auth);
    }

}