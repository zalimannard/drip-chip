package ru.zalimannard.dripchip.integration.location;

import io.restassured.response.ValidatableResponse;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.schema.location.dto.LocationRequestDto;
import ru.zalimannard.dripchip.schema.location.dto.LocationResponseDto;

import static io.restassured.RestAssured.given;

public class LocationSteps {

    private static final String endpoint = "/locations";

    private static ValidatableResponse baseGet(Long id,
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

    public static LocationResponseDto get(Long id,
                                          String auth) {
        return baseGet(id, auth)
                .statusCode(200)
                .extract().as(LocationResponseDto.class);
    }

    public static void getExpectedBadRequest(Long id,
                                             String auth) {
        baseGet(id, auth)
                .statusCode(400);
    }

    public static void getExpectedUnauthorized(Long id,
                                               String auth) {
        baseGet(id, auth)
                .statusCode(401);
    }

    public static ExceptionResponse getExpectedNotFound(Long id,
                                                        String auth) {
        return baseGet(id, auth)
                .statusCode(404)
                .extract().as(ExceptionResponse.class);
    }


    private static ValidatableResponse basePost(LocationRequestDto locationRequestDto,
                                                String auth) {
        if (auth != null) {
            return given()
                    .body(locationRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        } else {
            return given()
                    .body(locationRequestDto)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        }
    }

    public static LocationResponseDto post(LocationRequestDto locationRequestDto,
                                           String auth) {
        return basePost(locationRequestDto, auth)
                .statusCode(201)
                .extract().as(LocationResponseDto.class);
    }

    public static void postExpectedBadRequest(LocationRequestDto locationRequestDto,
                                              String auth) {
        basePost(locationRequestDto, auth)
                .statusCode(400);
    }

    public static void postExpectedUnauthorized(LocationRequestDto locationRequestDto,
                                                String auth) {
        basePost(locationRequestDto, auth)
                .statusCode(401);
    }

    public static void postExpectedForbidden(LocationRequestDto locationRequestDto,
                                             String auth) {
        basePost(locationRequestDto, auth)
                .statusCode(403);
    }

    public static ExceptionResponse postExpectedConflict(LocationRequestDto locationRequestDto,
                                                         String auth) {
        return basePost(locationRequestDto, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }


    private static ValidatableResponse basePut(Long id, LocationRequestDto locationRequestDto,
                                               String auth) {
        if (auth != null) {
            return given()
                    .body(locationRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        } else {
            return given()
                    .body(locationRequestDto)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        }
    }

    public static LocationResponseDto put(Long id, LocationRequestDto locationRequestDto,
                                          String auth) {
        return basePut(id, locationRequestDto, auth)
                .statusCode(200)
                .extract().as(LocationResponseDto.class);
    }

    public static void putExpectedBadRequest(Long id, LocationRequestDto locationRequestDto,
                                             String auth) {
        basePut(id, locationRequestDto, auth)
                .statusCode(400);
    }

    public static void putExpectedUnauthorized(Long id, LocationRequestDto locationRequestDto,
                                               String auth) {
        basePut(id, locationRequestDto, auth)
                .statusCode(401);
    }

    public static void putExpectedForbidden(Long id, LocationRequestDto locationRequestDto,
                                            String auth) {
        basePut(id, locationRequestDto, auth)
                .statusCode(403);
    }

    public static void putExpectedNotFound(Long id, LocationRequestDto locationRequestDto,
                                           String auth) {
        basePut(id, locationRequestDto, auth)
                .statusCode(404);
    }

    public static ExceptionResponse putExpectedConflict(Long id, LocationRequestDto locationRequestDto,
                                                        String auth) {
        return basePut(id, locationRequestDto, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }


    private static ValidatableResponse baseDelete(Long id,
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

    public static void delete(Long id,
                              String auth) {
        baseDelete(id, auth)
                .statusCode(200);
    }

    public static void deleteExpectedBadRequest(Long id,
                                                String auth) {
        baseDelete(id, auth)
                .statusCode(400);
    }

    public static void deleteExpectedUnauthorized(Long id,
                                                  String auth) {
        baseDelete(id, auth)
                .statusCode(401);
    }

    public static void deleteExpectedForbidden(Long id,
                                               String auth) {
        baseDelete(id, auth)
                .statusCode(403);
    }

    public static void deleteExpectedNotFound(Long id,
                                              String auth) {
        baseDelete(id, auth)
                .statusCode(404);
    }

}
