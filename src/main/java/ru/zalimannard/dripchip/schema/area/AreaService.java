package ru.zalimannard.dripchip.schema.area;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import ru.zalimannard.dripchip.schema.area.dto.AreaRequestDto;
import ru.zalimannard.dripchip.schema.area.dto.AreaResponseDto;
import ru.zalimannard.dripchip.schema.area.point.Point;

import java.util.List;

public interface AreaService {

    AreaResponseDto create(@Valid AreaRequestDto areaRequestDto);

    Area createEntity(@Valid Area area, List<Point> points);


    AreaResponseDto read(@Positive long id);

    Area readEntity(@Positive long id);

    List<Area> readAllEntities();


    AreaResponseDto update(@Positive long id, @Valid AreaRequestDto areaRequestDto);

    Area updateEntity(@Positive long id, @Valid Area area, List<Point> points);


    void delete(@Positive long id);

}
