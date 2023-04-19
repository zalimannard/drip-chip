package ru.zalimannard.dripchip.integration.area;

import io.restassured.response.ValidatableResponse;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.schema.area.dto.AreaRequestDto;
import ru.zalimannard.dripchip.schema.area.dto.AreaResponseDto;

import static io.restassured.RestAssured.given;

public class AreaSteps {

    private static final String endpoint = "/areas";

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

    public static AreaResponseDto get(Long id,
                                      String auth) {
        return baseGet(id, auth)
                .statusCode(200)
                .extract().as(AreaResponseDto.class);
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


    private static ValidatableResponse basePost(AreaRequestDto areaRequestDto,
                                                String auth) {
        if (auth != null) {
            return given()
                    .body(areaRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        } else {
            return given()
                    .body(areaRequestDto)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        }
    }

    public static AreaResponseDto post(AreaRequestDto areaRequestDto,
                                       String auth) {
        return basePost(areaRequestDto, auth)
                .statusCode(201)
                .extract().as(AreaResponseDto.class);
    }

    public static void postExpectedBadRequest(AreaRequestDto areaRequestDto,
                                              String auth) {
        basePost(areaRequestDto, auth)
                .statusCode(400);
    }

    public static void postExpectedUnauthorized(AreaRequestDto areaRequestDto,
                                                String auth) {
        basePost(areaRequestDto, auth)
                .statusCode(401);
    }

    public static void postExpectedForbidden(AreaRequestDto areaRequestDto,
                                             String auth) {
        basePost(areaRequestDto, auth)
                .statusCode(403);
    }

    public static ExceptionResponse postExpectedConflict(AreaRequestDto areaRequestDto,
                                                         String auth) {
        return basePost(areaRequestDto, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }


    private static ValidatableResponse basePut(Long id, AreaRequestDto areaRequestDto,
                                               String auth) {
        if (auth != null) {
            return given()
                    .body(areaRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        } else {
            return given()
                    .body(areaRequestDto)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        }
    }

    public static AreaResponseDto put(Long id, AreaRequestDto areaRequestDto,
                                      String auth) {
        return basePut(id, areaRequestDto, auth)
                .statusCode(200)
                .extract().as(AreaResponseDto.class);
    }

    public static void putExpectedBadRequest(Long id, AreaRequestDto areaRequestDto,
                                             String auth) {
        basePut(id, areaRequestDto, auth)
                .statusCode(400);
    }

    public static void putExpectedUnauthorized(Long id, AreaRequestDto areaRequestDto,
                                               String auth) {
        basePut(id, areaRequestDto, auth)
                .statusCode(401);
    }

    public static void putExpectedForbidden(Long id, AreaRequestDto areaRequestDto,
                                            String auth) {
        basePut(id, areaRequestDto, auth)
                .statusCode(403);
    }

    public static void putExpectedNotFound(Long id, AreaRequestDto areaRequestDto,
                                           String auth) {
        basePut(id, areaRequestDto, auth)
                .statusCode(404);
    }

    public static ExceptionResponse putExpectedConflict(Long id, AreaRequestDto areaRequestDto,
                                                        String auth) {
        return basePut(id, areaRequestDto, auth)
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