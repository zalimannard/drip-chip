package ru.zalimannard.dripchip.schema.animal.type;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimalTypeMapper {

    @Mapping(target = "animals", ignore = true)
    AnimalType toEntity(AnimalTypeDto dto);

    AnimalTypeDto toDto(AnimalType entity);

    List<AnimalType> toEntityList(List<AnimalTypeDto> dto);

    List<AnimalTypeDto> toDtoList(List<AnimalType> dto);

}
