package ru.zalimannard.dripchip.integration.animaltype.get;

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
import ru.zalimannard.dripchip.integration.animaltype.AnimalTypeFactory;
import ru.zalimannard.dripchip.integration.animaltype.AnimalTypeSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalTypeGetOkTests {

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
    @DisplayName("Позитивный тест. Запрос типа животного")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
            "USER",
    })
    void positiveTest(String role) {
        AccountRequestDto account = AccountFactory.createAccountRequest(role);
        AccountSteps.post(account, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(account);

        AnimalTypeRequestDto animalType = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto createdAnimalType = AnimalTypeSteps.post(animalType, defaultAuth.adminAuth());
        AnimalTypeResponseDto responsePostAnimalType = AnimalTypeFactory.createAnimalTypeResponse(createdAnimalType.getId(), animalType);

        AnimalTypeResponseDto gotAnimalType = AnimalTypeSteps.get(createdAnimalType.getId(), auth);
        assertThat(gotAnimalType).isEqualTo(responsePostAnimalType);
    }

}