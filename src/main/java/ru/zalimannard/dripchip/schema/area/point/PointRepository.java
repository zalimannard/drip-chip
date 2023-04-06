package ru.zalimannard.dripchip.schema.area.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zalimannard.dripchip.schema.area.Area;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    void deleteAllByArea(Area area);

}
