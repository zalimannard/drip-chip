package ru.zalimannard.dripchip.schema.area.point;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.schema.area.Area;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public List<Point> createAllEntities(Area area, List<Point> points) {
        List<Point> pointsRequest = new ArrayList<>();

        for (long i = 0; i < points.size(); ++i) {
            Point currentPoint = points.get((int) i);
            Point newPoint = currentPoint.toBuilder()
                    .area(area)
                    .numberInArea(i)
                    .build();
            pointsRequest.add(newPoint);
        }

        return saveToDatabase(pointsRequest);
    }

    private List<Point> saveToDatabase(List<Point> points) {
        try {
            return pointRepository.saveAll(points);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("", "", "");
        }
    }

    @Transactional
    @Override
    public void deleteAll(Area area) {
        pointRepository.deleteAllByArea(area);
    }

}
