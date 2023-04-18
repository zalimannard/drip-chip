package ru.zalimannard.dripchip.integration.account;

import io.restassured.response.ValidatableResponse;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.schema.account.dto.AccountRequestDto;
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

    public static void getExpectedBadRequest(Integer id,
                                             String auth) {
        baseGet(id, auth)
                .statusCode(400);
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

    public static void postExpectedBadRequest(AccountRequestDto accountRequestDto,
                                              String auth) {
        basePost(accountRequestDto, auth)
                .statusCode(400);
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


    private static ValidatableResponse basePut(Integer id, AccountRequestDto accountRequestDto,
                                               String auth) {
        if (auth != null) {
            return given()
                    .body(accountRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        } else {
            return given()
                    .body(accountRequestDto)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        }
    }

    public static AccountResponseDto put(Integer id, AccountRequestDto accountRequestDto,
                                         String auth) {
        return basePut(id, accountRequestDto, auth)
                .statusCode(200)
                .extract().as(AccountResponseDto.class);
    }

    public static void putExpectedBadRequest(Integer id, AccountRequestDto accountRequestDto,
                                             String auth) {
        basePut(id, accountRequestDto, auth)
                .statusCode(400);
    }

    public static void putExpectedUnauthorized(Integer id, AccountRequestDto accountRequestDto,
                                               String auth) {
        basePut(id, accountRequestDto, auth)
                .statusCode(401);
    }

    public static void putExpectedForbidden(Integer id, AccountRequestDto accountRequestDto,
                                            String auth) {
        basePut(id, accountRequestDto, auth)
                .statusCode(403);
    }

    public static void putExpectedNotFound(Integer id, AccountRequestDto accountRequestDto,
                                           String auth) {
        basePut(id, accountRequestDto, auth)
                .statusCode(404);
    }

    public static ExceptionResponse putExpectedConflict(Integer id, AccountRequestDto accountRequestDto,
                                                        String auth) {
        return basePut(id, accountRequestDto, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }


    private static ValidatableResponse baseDelete(Integer id,
                                                  String auth) {
        if (auth != null) {
            return given()
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .delete(endpoint + "/" + id)
                    .then().log().all();
        } else {
            return given()
                    .when()
                    .delete(endpoint + "/" + id)
                    .then().log().all();
        }
    }

    public static void delete(Integer id,
                              String auth) {
        baseDelete(id, auth)
                .statusCode(200);
    }

    public static void deleteExpectedBadRequest(Integer id,
                                                String auth) {
        baseDelete(id, auth)
                .statusCode(400);
    }

    public static void deleteExpectedUnauthorized(Integer id,
                                                  String auth) {
        baseDelete(id, auth)
                .statusCode(401);
    }

    public static void deleteExpectedForbidden(Integer id,
                                               String auth) {
        baseDelete(id, auth)
                .statusCode(403);
    }

    public static void deleteExpectedNotFound(Integer id,
                                              String auth) {
        baseDelete(id, auth)
                .statusCode(404);
    }

}
