package ru.zalimannard.dripchip.schema.area;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.zalimannard.dripchip.schema.area.dto.AreaRequestDto;
import ru.zalimannard.dripchip.schema.area.dto.AreaResponseDto;
import ru.zalimannard.dripchip.schema.area.point.Point;

import java.util.List;

public interface AreaService {

    AreaResponseDto create(@Valid AreaRequestDto areaRequestDto);

    Area createEntity(Area area, List<Point> points);


    AreaResponseDto read(@Positive @NotNull Long id);

    Area readEntity(@Positive @NotNull Long id);

    List<Area> readAllEntities();


    AreaResponseDto update(@Positive @NotNull Long id, @Valid AreaRequestDto areaRequestDto);

    Area updateEntity(@Positive @NotNull Long id, Area area, List<Point> points);


    void delete(@Positive @NotNull Long id);

}
