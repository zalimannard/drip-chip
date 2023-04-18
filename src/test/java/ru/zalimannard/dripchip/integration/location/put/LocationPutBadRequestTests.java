package ru.zalimannard.dripchip.integration.location.put;

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
import ru.zalimannard.dripchip.integration.location.LocationFactory;
import ru.zalimannard.dripchip.integration.location.LocationSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.location.LocationController;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocationPutBadRequestTests {

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
    @DisplayName("Негативный тест. Неправильная долгота")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, -400.0",
            "ADMIN, -200.0",
            "ADMIN, -180.00001",
            "ADMIN, 180.00001",
            "ADMIN, 200.0",
            "ADMIN, 400.0",
            "CHIPPER, null",
            "CHIPPER, -400.0",
            "CHIPPER, -200.0",
            "CHIPPER, -180.00001",
            "CHIPPER, 180.00001",
            "CHIPPER, 200.0",
            "CHIPPER, 400.0",
            "USER, null",
            "USER, -400.0",
            "USER, -200.0",
            "USER, -180.00001",
            "USER, 180.00001",
            "USER, 200.0",
            "USER, 400.0",
    }, nullValues = {"null"})
    void invalidLongitude(String role, Double longitude) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requester);

        LocationRequestDto preRequest = LocationFactory.createLocationRequest();
        LocationResponseDto preResponse = LocationSteps.post(preRequest, auth);

        LocationRequestDto request = preRequest.toBuilder()
                .longitude(longitude)
                .build();
        ExceptionResponse response = LocationSteps.putExpectedBadRequest(preResponse.getId(), request, auth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неправильный locationId")
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
    void invalidTypeId(String role, Long locationId) {
        AccountRequestDto account = AccountFactory.createAccountRequest(role);
        AccountSteps.post(account, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(account);

        LocationRequestDto location = LocationFactory.createLocationRequest();
        ExceptionResponse response = LocationSteps.putExpectedBadRequest(locationId, location, auth);
        assertThat(response).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неправильная широта")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, -200.0",
            "ADMIN, -100.0",
            "ADMIN, -90.00001",
            "ADMIN, 90.00001",
            "ADMIN, 100.0",
            "ADMIN, 200.0",
            "CHIPPER, null",
            "CHIPPER, -200.0",
            "CHIPPER, -100.0",
            "CHIPPER, -90.00001",
            "CHIPPER, 90.00001",
            "CHIPPER, 100.0",
            "CHIPPER, 200.0",
            "USER, null",
            "USER, -200.0",
            "USER, -100.0",
            "USER, -90.00001",
            "USER, 90.00001",
            "USER, 100.0",
            "USER, 200.0",
    }, nullValues = {"null"})
    void invalidLatitude(String role, Double latitude) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requester);

        LocationRequestDto preRequest = LocationFactory.createLocationRequest();
        LocationResponseDto preResponse = LocationSteps.post(preRequest, auth);

        LocationRequestDto request = preRequest.toBuilder()
                .latitude(latitude)
                .build();
        ExceptionResponse response = LocationSteps.putExpectedBadRequest(preResponse.getId(), request, auth);
        assertThat(response).isNotNull();
    }

}