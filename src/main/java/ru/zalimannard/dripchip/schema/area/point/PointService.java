package ru.zalimannard.dripchip.schema.area.point;

import jakarta.validation.Valid;
import ru.zalimannard.dripchip.schema.area.Area;

import java.util.List;

public interface PointService {

    List<Point> createAllEntities(Area area, List<@Valid Point> points);


    void deleteAll(Area area);

}
