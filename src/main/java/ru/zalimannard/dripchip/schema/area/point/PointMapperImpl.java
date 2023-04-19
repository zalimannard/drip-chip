package ru.zalimannard.dripchip.schema.area.point;

import org.springframework.stereotype.Component;
import ru.zalimannard.dripchip.schema.area.Area;
import ru.zalimannard.dripchip.schema.area.point.dto.PointRequestDto;
import ru.zalimannard.dripchip.schema.area.point.dto.PointResponseDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class PointMapperImpl implements PointMapper {

    @Override
    public Point toEntity(PointRequestDto dto,
                          Area area,
                          long numberInArea) {
        return Point.builder()
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .area(area.toBuilder().build())
                .numberInArea(numberInArea)
                .build();
    }

    @Override
    public PointResponseDto toDto(Point entity) {
        return PointResponseDto.builder()
                .longitude(entity.getLongitude())
                .latitude(entity.getLatitude())
                .build();
    }

    @Override
    public List<Point> toEntityList(List<PointRequestDto> dto,
                                    Area area) {
        List<Point> entities = new ArrayList<>();
        for (int i = 0; i < dto.size(); ++i) {
            entities.add(toEntity(dto.get(i), area, i));
        }
        return entities;
    }

    @Override
    public List<PointResponseDto> toDtoList(List<Point> entity) {
        return entity.stream().map(this::toDto).toList();
    }

}
