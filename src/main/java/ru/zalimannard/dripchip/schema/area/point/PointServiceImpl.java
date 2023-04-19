package ru.zalimannard.dripchip.schema.area.point;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.schema.area.Area;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public List<Point> createAllEntities(Area area, List<Point> points) {
        List<Point> pointsRequest = points.stream().map(point -> point.toBuilder().area(area).build()).toList();

        try {
            return pointRepository.saveAll(pointsRequest);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("pos-01", "points", points.stream().map(Point::getId).toList().toString());
        }
    }

    @Transactional
    @Override
    public void deleteAll(Area area) {
        pointRepository.deleteAllByArea(area);
    }

}
