package ru.zalimannard.dripchip.schema.area.point;

import ru.zalimannard.dripchip.schema.area.Area;
import ru.zalimannard.dripchip.schema.area.point.dto.PointRequestDto;
import ru.zalimannard.dripchip.schema.area.point.dto.PointResponseDto;

import java.util.List;

public interface PointMapper {

    Point toEntity(PointRequestDto dto,
                   Area area,
                   long numberInArea);

    PointResponseDto toDto(Point entity);

    List<Point> toEntityList(List<PointRequestDto> dto,
                             Area area);

    List<PointResponseDto> toDtoList(List<Point> entity);

}
