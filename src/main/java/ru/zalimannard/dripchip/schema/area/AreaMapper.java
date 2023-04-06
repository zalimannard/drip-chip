package ru.zalimannard.dripchip.schema.area;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AreaMapper {

    @Mapping(target = "points", ignore = true)
    Area toEntity(AreaDto dto);

    AreaDto toDto(Area entity);

    List<AreaDto> toDtoList(List<Area> entity);

}