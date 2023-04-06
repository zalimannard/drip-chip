package ru.zalimannard.dripchip.schema.area;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import ru.zalimannard.dripchip.schema.area.point.Point;

import java.util.List;

public interface AreaService {

    AreaDto create(@Valid AreaDto areaDto);

    Area createEntity(@Valid Area area, List<Point> points);


    AreaDto read(@Positive long id);

    Area readEntity(@Positive long id);

    List<Area> readAllEntities();


    AreaDto update(@Positive long id, @Valid AreaDto areaDto);

    Area updateEntity(@Positive long id, @Valid Area area, List<Point> points);


    void delete(@Positive long id);

}
