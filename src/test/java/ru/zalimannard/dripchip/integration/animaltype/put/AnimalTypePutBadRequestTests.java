package ru.zalimannard.dripchip.integration.animaltype.put;

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
class AnimalTypePutBadRequestTests {

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
    @DisplayName("Негативный тест. Неправильный typeId")
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
    void invalidTypeId(String role, Long typeId) {
        AccountRequestDto account = AccountFactory.createAccountRequest(role);
        AccountSteps.post(account, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(account);

        AnimalTypeRequestDto type = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeSteps.putExpectedBadRequest(typeId, type, auth);
    }

    @ParameterizedTest
    @DisplayName("Негативный тест. Неправильное название типа")
    @CsvSource(value = {
            "ADMIN, null",
            "ADMIN, ''",
            "ADMIN, ' '",
            "ADMIN, '   '",
            "CHIPPER, null",
            "CHIPPER, ''",
            "CHIPPER, ' '",
            "CHIPPER, '   '",
    }, nullValues = {"null"})
    void invalidTypeName(String role, String typeName) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requester);

        AnimalTypeRequestDto preRequest = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto preResponse = AnimalTypeSteps.post(preRequest, auth);

        AnimalTypeRequestDto request = preRequest.toBuilder()
                .type(typeName)
                .build();
        AnimalTypeSteps.putExpectedBadRequest(preResponse.getId(), request, auth);
    }

}