package ru.zalimannard.dripchip.schema.area.point;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.zalimannard.dripchip.schema.area.Area;

import java.util.List;

public interface PointService {

    List<Point> createAllEntities(@NotNull Area area, List<@Valid Point> points);


    void deleteAll(@NotNull Area area);

}
