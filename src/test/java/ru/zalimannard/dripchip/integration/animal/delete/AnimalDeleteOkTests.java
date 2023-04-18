package ru.zalimannard.dripchip.integration.animal.delete;

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
import ru.zalimannard.dripchip.integration.animal.AnimalSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.animal.AnimalController;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeController;
import ru.zalimannard.dripchip.schema.location.LocationController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalDeleteOkTests {

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
    @DisplayName("Позитивный тест. Удаление животного")
    @CsvSource(value = {
            "ADMIN",
    })
    void successfullyDelete(String role) {
        AccountRequestDto request = AccountFactory.createAccountRequest(role);
        AccountSteps.post(request, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(request);

        AnimalResponseDto animalResponseDto = AnimalSteps.createFromScratch(defaultAuth.adminAuth());
        AnimalSteps.delete(animalResponseDto.getId(), auth);
    }

}