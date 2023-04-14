package ru.zalimannard.dripchip.integration.account;

import io.restassured.response.ValidatableResponse;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;

import static io.restassured.RestAssured.given;

public class AccountSteps {

    private static final String endpoint = "/accounts";

    private static ValidatableResponse baseGet(Integer id,
                                               String auth) {
        if (auth != null) {
            return given()
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .get(endpoint + "/" + id)
                    .then().log().all();
        } else {
            return given()
                    .when()
                    .get(endpoint + "/" + id)
                    .then().log().all();
        }
    }

    public static AccountResponseDto get(Integer id,
                                         String auth) {
        return baseGet(id, auth)
                .statusCode(200)
                .extract().as(AccountResponseDto.class);
    }

    public static ExceptionResponse getExpectedBadRequest(Integer id,
                                                          String auth) {
        return baseGet(id, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
    }

    public static void getExpectedForbidden(Integer id,
                                            String auth) {
        baseGet(id, auth)
                .statusCode(403);
    }

    public static ExceptionResponse getExpectedConflict(Integer id,
                                                        String auth) {
        return baseGet(id, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }
}
