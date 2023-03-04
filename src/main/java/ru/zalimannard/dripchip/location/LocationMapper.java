package ru.zalimannard.dripchip.location;

import org.mapstruct.Mapper;

@Mapper
public interface LocationMapper {

    Location toEntity(LocationDto dto);

    LocationDto toDto(Location entity);

}