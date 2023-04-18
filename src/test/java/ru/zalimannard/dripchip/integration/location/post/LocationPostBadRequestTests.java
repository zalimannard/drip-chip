package ru.zalimannard.dripchip.integration.location.post;

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
import ru.zalimannard.dripchip.integration.location.LocationFactory;
import ru.zalimannard.dripchip.integration.location.LocationSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.location.LocationController;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocationPostBadRequestTests {

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
    }, nullValues = {"null"})
    void invalidLongitude(String role, Double longitude) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requester);

        LocationRequestDto request = LocationFactory.createLocationRequest().toBuilder()
                .longitude(longitude)
                .build();
        LocationSteps.postExpectedBadRequest(request, auth);
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
    }, nullValues = {"null"})
    void invalidLatitude(String role, Double latitude) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requester);

        LocationRequestDto request = LocationFactory.createLocationRequest().toBuilder()
                .latitude(latitude)
                .build();
        LocationSteps.postExpectedBadRequest(request, auth);
    }

}