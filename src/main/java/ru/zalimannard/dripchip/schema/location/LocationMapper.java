package ru.zalimannard.dripchip.schema.location;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toEntity(LocationDto dto);

    LocationDto toDto(Location entity);

}
