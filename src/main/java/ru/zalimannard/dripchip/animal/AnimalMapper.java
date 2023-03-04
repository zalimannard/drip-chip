package ru.zalimannard.dripchip.animal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AnimalMapper {

    Animal toEntity(AnimalDto dto);

    @Mapping(target = "animalTypes", ignore = true)
    @Mapping(target = "visitedLocations", ignore = true)
    AnimalDto toDto(Animal entity);

}
