package ru.zalimannard.dripchip.integration.account;

import io.restassured.response.ValidatableResponse;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
import ru.zalimannard.dripchip.schema.account.dto.AccountResponseDto;
import ru.zalimannard.dripchip.schema.account.role.AccountRole;

import static io.restassured.RestAssured.given;

public class AccountSteps {

    private static final String endpoint = "/accounts";

    public static AccountResponseDto create(String email, String password, AccountRole role,
                                            String auth) {
        AccountRequestDto requestDto = AccountDefaultDtos.defaultAccountRequest.toBuilder()
                .email(email)
                .password(password)
                .role(role)
                .build();
        return post(requestDto, auth);
    }

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

    public static void getExpectedUnauthorized(Integer id,
                                               String auth) {
        baseGet(id, auth)
                .statusCode(401);
    }

    public static void getExpectedForbidden(Integer id,
                                            String auth) {
        baseGet(id, auth)
                .statusCode(403);
    }

    public static ExceptionResponse getExpectedNotFound(Integer id,
                                                        String auth) {
        return baseGet(id, auth)
                .statusCode(404)
                .extract().as(ExceptionResponse.class);
    }


    private static ValidatableResponse basePost(AccountRequestDto accountRequestDto,
                                                String auth) {
        if (auth != null) {
            return given()
                    .body(accountRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        } else {
            return given()
                    .body(accountRequestDto)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        }
    }

    public static AccountResponseDto post(AccountRequestDto accountRequestDto,
                                          String auth) {
        return basePost(accountRequestDto, auth)
                .statusCode(201)
                .extract().as(AccountResponseDto.class);
    }

    public static ExceptionResponse postExpectedBadRequest(AccountRequestDto accountRequestDto,
                                                           String auth) {
        return basePost(accountRequestDto, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
    }

    public static void postExpectedUnauthorized(AccountRequestDto accountRequestDto,
                                                String auth) {
        basePost(accountRequestDto, auth)
                .statusCode(401);
    }

    public static void postExpectedForbidden(AccountRequestDto accountRequestDto,
                                             String auth) {
        basePost(accountRequestDto, auth)
                .statusCode(403);
    }

    public static ExceptionResponse postExpectedConflict(AccountRequestDto accountRequestDto,
                                                         String auth) {
        return basePost(accountRequestDto, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }

}
