package ru.zalimannard.dripchip.schema.animal;

import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;

import java.util.List;

public interface AnimalMapper {

    Animal toEntity(AnimalPostRequestDto dto);

    Animal toEntity(AnimalPutRequestDto dto);

    AnimalResponseDto toDto(Animal entity);

    List<AnimalResponseDto> toDtoList(List<Animal> animals);

}
