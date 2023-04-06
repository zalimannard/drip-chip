package ru.zalimannard.dripchip.schema.area.point;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PointMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "area", ignore = true)
    @Mapping(target = "numberInArea", ignore = true)
    Point toEntity(PointDto dto);

    PointDto toDto(Point entity);

    List<Point> toEntityList(List<PointDto> dto);

    List<PointDto> toDtoList(List<Point> entity);

}
