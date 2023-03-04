package ru.zalimannard.dripchip.animal.type;

import org.mapstruct.Mapper;

@Mapper
public interface AnimalTypeMapper {

    AnimalType toEntity(AnimalTypeDto dto);

    AnimalTypeDto toDto(AnimalType entity);

}
