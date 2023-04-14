package ru.zalimannard.dripchip.integration.registration;

import io.restassured.response.ValidatableResponse;
import ru.zalimannard.dripchip.exception.ExceptionResponse;
import ru.zalimannard.dripchip.schema.account.authentication.AuthenticationDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import static io.restassured.RestAssured.given;

public abstract class RegistrationSteps {

    private static final String endpoint = "/registration";

    private static ValidatableResponse baseRegistration(AuthenticationDto authenticationDto,
                                                        String auth) {
        if (auth != null) {
            return given()
                    .headers("Authorization",
                            "Basic " + auth)
                    .body(authenticationDto)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        } else {
            return given()
                    .body(authenticationDto)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        }
    }

    public static AccountResponseDto registration(AuthenticationDto authenticationDto,
                                                  String auth) {
        return baseRegistration(authenticationDto, auth)
                .statusCode(201)
                .extract().as(AccountResponseDto.class);
    }

    public static ExceptionResponse registrationExpectedBadRequest(AuthenticationDto authenticationDto,
                                                                   String auth) {
        return baseRegistration(authenticationDto, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
    }

    public static void registrationExpectedForbidden(AuthenticationDto authenticationDto,
                                                     String auth) {
        baseRegistration(authenticationDto, auth)
                .statusCode(403);
    }

    public static ExceptionResponse registrationExpectedConflict(AuthenticationDto authenticationDto,
                                                                 String auth) {
        return baseRegistration(authenticationDto, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }
}
