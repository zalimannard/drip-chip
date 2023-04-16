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
import ru.zalimannard.dripchip.schema.account.role.AccountRole;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalTypePutOkTests {

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
    @DisplayName("Позитивный тест. Запрос успешно выполнен")
    @CsvSource(value = {
            "ADMIN",
            "CHIPPER",
    })
    void positiveTest(AccountRole role) {
        AccountRequestDto requester = AccountFactory.createAccountRequest(role);
        AccountSteps.post(requester, defaultAuth.adminAuth());
        String auth = accountToAuthConverter.convert(requester);

        AnimalTypeRequestDto request = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto created = AnimalTypeSteps.post(request, auth);

        AnimalTypeRequestDto request2 = AnimalTypeFactory.createAnimalTypeRequest();
        AnimalTypeResponseDto created2 = AnimalTypeSteps.put(created.getId(), request2, auth);
        AnimalTypeResponseDto expected2 = AnimalTypeFactory.createAnimalTypeResponse(created2.getId(), request2);
        assertThat(created2).isEqualTo(expected2);
    }

}