package ru.zalimannard.dripchip.schema.animal.type;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AnimalTypeMapper {

    @Mapping(target = "animals", ignore = true)
    AnimalType toEntity(AnimalTypeDto dto);

    AnimalTypeDto toDto(AnimalType entity);

}
