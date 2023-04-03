package ru.zalimannard.dripchip.schema.area.point;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PointMapper {

    Point toEntity(PointDto dto);

    PointDto toDto(Point entity);

    List<Point> toEntityList(List<PointDto> dto);

    List<PointDto> toDtoList(List<Point> entity);

}
