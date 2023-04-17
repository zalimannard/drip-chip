package ru.zalimannard.dripchip.integration.animal;

import io.restassured.response.ValidatableResponse;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;

import static io.restassured.RestAssured.given;

public class AnimalSteps {

    private static final String endpoint = "/animals";

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

    public static AnimalResponseDto get(Long id,
                                        String auth) {
        return baseGet(id, auth)
                .statusCode(200)
                .extract().as(AnimalResponseDto.class);
    }

    public static ExceptionResponse getExpectedBadRequest(Long id,
                                                          String auth) {
        return baseGet(id, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
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


    private static ValidatableResponse basePost(AnimalPostRequestDto animalRequestDto,
                                                String auth) {
        if (auth != null) {
            return given()
                    .body(animalRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        } else {
            return given()
                    .body(animalRequestDto)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        }
    }

    public static AnimalResponseDto post(AnimalPostRequestDto animalRequestDto,
                                         String auth) {
        return basePost(animalRequestDto, auth)
                .statusCode(201)
                .extract().as(AnimalResponseDto.class);
    }

    public static ExceptionResponse postExpectedBadRequest(AnimalPostRequestDto animalRequestDto,
                                                           String auth) {
        return basePost(animalRequestDto, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
    }

    public static void postExpectedUnauthorized(AnimalPostRequestDto animalRequestDto,
                                                String auth) {
        basePost(animalRequestDto, auth)
                .statusCode(401);
    }

    public static void postExpectedForbidden(AnimalPostRequestDto animalRequestDto,
                                             String auth) {
        basePost(animalRequestDto, auth)
                .statusCode(403);
    }

    public static ExceptionResponse postExpectedNotFound(AnimalPostRequestDto animalRequestDto,
                                                         String auth) {
        return basePost(animalRequestDto, auth)
                .statusCode(404)
                .extract().as(ExceptionResponse.class);
    }

    public static ExceptionResponse postExpectedConflict(AnimalPostRequestDto animalRequestDto,
                                                         String auth) {
        return basePost(animalRequestDto, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }


    private static ValidatableResponse basePut(Long id, AnimalPutRequestDto animalRequestDto,
                                               String auth) {
        if (auth != null) {
            return given()
                    .body(animalRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        } else {
            return given()
                    .body(animalRequestDto)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        }
    }

    public static AnimalResponseDto put(Long id, AnimalPutRequestDto animalRequestDto,
                                        String auth) {
        return basePut(id, animalRequestDto, auth)
                .statusCode(200)
                .extract().as(AnimalResponseDto.class);
    }

    public static ExceptionResponse putExpectedBadRequest(Long id, AnimalPutRequestDto animalRequestDto,
                                                          String auth) {
        return basePut(id, animalRequestDto, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
    }

    public static void putExpectedUnauthorized(Long id, AnimalPutRequestDto animalRequestDto,
                                               String auth) {
        basePut(id, animalRequestDto, auth)
                .statusCode(401);
    }

    public static void putExpectedForbidden(Long id, AnimalPutRequestDto animalRequestDto,
                                            String auth) {
        basePut(id, animalRequestDto, auth)
                .statusCode(403);
    }

    public static void putExpectedNotFound(Long id, AnimalPutRequestDto animalRequestDto,
                                           String auth) {
        basePut(id, animalRequestDto, auth)
                .statusCode(404);
    }

    public static ExceptionResponse putExpectedConflict(Long id, AnimalPutRequestDto animalRequestDto,
                                                        String auth) {
        return basePut(id, animalRequestDto, auth)
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

    public static ExceptionResponse deleteExpectedBadRequest(Long id,
                                                             String auth) {
        return baseDelete(id, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
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
