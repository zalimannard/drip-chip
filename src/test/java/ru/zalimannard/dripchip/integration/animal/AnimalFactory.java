package ru.zalimannard.dripchip.integration.animal;

import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AnimalFactory {

    public static AnimalPostRequestDto createAnimalPostRequest(Set<Long> animalTypeIds,
                                                               int chipperId,
                                                               long chippingLocationId) {
        return AnimalPostRequestDto.builder()
                .animalTypeIds(new HashSet<>(animalTypeIds))
                .weight(new Random().nextFloat())
                .length(new Random().nextFloat())
                .height(new Random().nextFloat())
                .gender(AnimalGender.MALE)
                .chipperId(chipperId)
                .chippingLocationId(chippingLocationId)
                .build();
    }

    public static AnimalPutRequestDto createAnimalPutRequest(int chipperId,
                                                             long chippingLocationId) {
        return AnimalPutRequestDto.builder()
                .weight(new Random().nextFloat())
                .length(new Random().nextFloat())
                .height(new Random().nextFloat())
                .gender(AnimalGender.MALE)
                .lifeStatus(AnimalLifeStatus.ALIVE)
                .chipperId(chipperId)
                .chippingLocationId(chippingLocationId)
                .build();
    }

    public static AnimalResponseDto createExpectedAnimalResponse(AnimalPostRequestDto request,
                                                                 AnimalResponseDto response) {
        return AnimalResponseDto.builder()
                .id(response.getId())
                .animalTypeIds(new HashSet<>(request.getAnimalTypeIds()))
                .weight(request.getWeight())
                .length(request.getLength())
                .height(request.getHeight())
                .gender(request.getGender())
                .lifeStatus(AnimalLifeStatus.ALIVE)
                .chippingDateTime(response.getChippingDateTime())
                .chipperId(request.getChipperId())
                .chippingLocationId(request.getChippingLocationId())
                .visitedLocations(response.getVisitedLocations())
                .deathDateTime(null)
                .build();
    }

    public static AnimalResponseDto createExpectedAnimalResponse(AnimalPutRequestDto request,
                                                                 AnimalResponseDto response) {
        return AnimalResponseDto.builder()
                .id(response.getId())
                .animalTypeIds(new HashSet<>(response.getAnimalTypeIds()))
                .weight(request.getWeight())
                .length(request.getLength())
                .height(request.getHeight())
                .gender(request.getGender())
                .lifeStatus(request.getLifeStatus())
                .chippingDateTime(response.getChippingDateTime())
                .chipperId(request.getChipperId())
                .chippingLocationId(request.getChippingLocationId())
                .visitedLocations(response.getVisitedLocations())
                .deathDateTime(null)
                .build();
    }

}