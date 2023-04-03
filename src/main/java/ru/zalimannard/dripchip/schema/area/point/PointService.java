package ru.zalimannard.dripchip.schema.area.point;

import ru.zalimannard.dripchip.schema.area.Area;

import java.util.List;

public interface PointService {

    List<PointDto> createAll(Area area, List<PointDto> pointDtos);

    void deleteAll(Area area);

}
