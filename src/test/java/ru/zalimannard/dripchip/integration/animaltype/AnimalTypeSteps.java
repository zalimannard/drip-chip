package ru.zalimannard.dripchip.integration.animaltype;

import io.restassured.response.ValidatableResponse;
import ru.zalimannard.dripchip.exception.response.ExceptionResponse;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

import static io.restassured.RestAssured.given;

public class AnimalTypeSteps {

    private static final String endpoint = "/animals/types";

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

    public static AnimalTypeResponseDto get(Long id,
                                            String auth) {
        return baseGet(id, auth)
                .statusCode(200)
                .extract().as(AnimalTypeResponseDto.class);
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


    private static ValidatableResponse basePost(AnimalTypeRequestDto animalTypeRequestDto,
                                                String auth) {
        if (auth != null) {
            return given()
                    .body(animalTypeRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        } else {
            return given()
                    .body(animalTypeRequestDto)
                    .when()
                    .post(endpoint)
                    .then().log().all();
        }
    }

    public static AnimalTypeResponseDto post(AnimalTypeRequestDto animalTypeRequestDto,
                                           String auth) {
        return basePost(animalTypeRequestDto, auth)
                .statusCode(201)
                .extract().as(AnimalTypeResponseDto.class);
    }

    public static ExceptionResponse postExpectedBadRequest(AnimalTypeRequestDto animalTypeRequestDto,
                                                           String auth) {
        return basePost(animalTypeRequestDto, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
    }

    public static void postExpectedUnauthorized(AnimalTypeRequestDto animalTypeRequestDto,
                                                String auth) {
        basePost(animalTypeRequestDto, auth)
                .statusCode(401);
    }

    public static void postExpectedForbidden(AnimalTypeRequestDto animalTypeRequestDto,
                                             String auth) {
        basePost(animalTypeRequestDto, auth)
                .statusCode(403);
    }

    public static ExceptionResponse postExpectedConflict(AnimalTypeRequestDto animalTypeRequestDto,
                                                         String auth) {
        return basePost(animalTypeRequestDto, auth)
                .statusCode(409)
                .extract().as(ExceptionResponse.class);
    }


    private static ValidatableResponse basePut(Long id, AnimalTypeRequestDto animalTypeRequestDto,
                                               String auth) {
        if (auth != null) {
            return given()
                    .body(animalTypeRequestDto)
                    .headers("Authorization",
                            "Basic " + auth)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        } else {
            return given()
                    .body(animalTypeRequestDto)
                    .when()
                    .put(endpoint + "/" + id)
                    .then().log().all();
        }
    }

    public static AnimalTypeResponseDto put(Long id, AnimalTypeRequestDto animalTypeRequestDto,
                                          String auth) {
        return basePut(id, animalTypeRequestDto, auth)
                .statusCode(200)
                .extract().as(AnimalTypeResponseDto.class);
    }

    public static ExceptionResponse putExpectedBadRequest(Long id, AnimalTypeRequestDto animalTypeRequestDto,
                                                          String auth) {
        return basePut(id, animalTypeRequestDto, auth)
                .statusCode(400)
                .extract().as(ExceptionResponse.class);
    }

    public static void putExpectedUnauthorized(Long id, AnimalTypeRequestDto animalTypeRequestDto,
                                               String auth) {
        basePut(id, animalTypeRequestDto, auth)
                .statusCode(401);
    }

    public static void putExpectedForbidden(Long id, AnimalTypeRequestDto animalTypeRequestDto,
                                            String auth) {
        basePut(id, animalTypeRequestDto, auth)
                .statusCode(403);
    }

    public static void putExpectedNotFound(Long id, AnimalTypeRequestDto animalTypeRequestDto,
                                           String auth) {
        basePut(id, animalTypeRequestDto, auth)
                .statusCode(404);
    }

    public static ExceptionResponse putExpectedConflict(Long id, AnimalTypeRequestDto animalTypeRequestDto,
                                                        String auth) {
        return basePut(id, animalTypeRequestDto, auth)
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
