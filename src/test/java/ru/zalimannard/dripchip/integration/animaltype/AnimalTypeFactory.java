package ru.zalimannard.dripchip.integration.animaltype;

import com.github.javafaker.Faker;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

public class AnimalTypeFactory {

    public static AnimalTypeRequestDto createAnimalTypeRequest() {
        return AnimalTypeRequestDto.builder()
                .type(Faker.instance().name().username())
                .build();
    }

    public static AnimalTypeResponseDto createAnimalTypeResponse(Long id, AnimalTypeRequestDto animalTypeRequestDto) {
        return AnimalTypeResponseDto.builder()
                .id(id)
                .type(animalTypeRequestDto.getType())
                .build();
    }

}
