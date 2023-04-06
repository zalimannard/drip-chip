package ru.zalimannard.dripchip.schema.area;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.area.point.Point;
import ru.zalimannard.dripchip.schema.area.point.PointMapper;
import ru.zalimannard.dripchip.schema.area.point.PointService;
import ru.zalimannard.dripchip.schema.area.point.Segment;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;
    private final AreaMapper areaMapper;
    private final PointService pointService;
    private final PointMapper pointMapper;

    @Override
    @Transactional
    public AreaDto create(AreaDto areaDto) {
        Area requestArea = areaMapper.toEntity(areaDto);
        List<Point> tempPoints = pointMapper.toEntityList(areaDto.getPoints());
        for (int i = 0; i < tempPoints.size(); ++i) {
            tempPoints.get(i).setNumberInArea((long) i);
        }
        requestArea.setPoints(tempPoints);
        List<Area> existedAreas = readAll();

        for (Area existedArea : existedAreas) {
            if (!isConsistent(existedArea, requestArea)) {
                throw new BadRequestException("Area conflicts with existing areas");
            } else if (!isConsistent(requestArea, requestArea)) {
                throw new BadRequestException();
            } else if (Double.compare(requestArea.calcAreaValue(), 0.0) == 0) {
                throw new BadRequestException();
            }
        }

        Area areaResponse = saveToDatabase(requestArea);
        List<Point> areasPoins = pointMapper.toEntityList(areaDto.getPoints());
        List<Point> points = pointService.createAllEntities(areaResponse, areasPoins);
        AreaDto areaDtoResponse = areaMapper.toDto(areaResponse);
        areaDtoResponse.setPoints(pointMapper.toDtoList(points));
        return areaDtoResponse;
    }

    private boolean isConsistent(Area existedArea, Area requestArea) {
        return !hasIntersection(existedArea, requestArea);
    }

    private boolean hasIntersection(Area existedArea, Area requestArea) {
        for (int e = 0; e < existedArea.getPoints().size(); ++e) {
            for (int r = 0; r < requestArea.getPoints().size(); ++r) {
                Segment segment1 = bestSegmentVariation(existedArea.getPoints().get(e),
                        existedArea.getPoints().get((e + 1) % existedArea.getPoints().size()));
                Segment segment2 = bestSegmentVariation(requestArea.getPoints().get(r),
                        requestArea.getPoints().get((r + 1) % requestArea.getPoints().size()));

                if (segment1.hasIntersection(segment2)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public AreaDto read(long id) {
        Area area = readEntity(id);
        return areaMapper.toDto(area);
    }

    private Area readEntity(long id) {
        checkExist(id);
        return areaRepository.findById(id).get();
    }

    @Override
    public AreaDto update(long id, AreaDto accountDto) {
        checkExist(id);
        Area areaRequest = areaMapper.toEntity(accountDto);
        areaRequest.setId(id);

        Area areaResponse = saveToDatabase(areaRequest);
        return areaMapper.toDto(areaResponse);
    }

    @Override
    @Transactional
    public void delete(long id) {
        Area area = readEntity(id);
        try {
            pointService.deleteAll(area);
            areaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("It is impossible to delete area");
        }
    }

    private List<Area> readAll() {
        return areaRepository.findAll();
    }

    private Segment bestSegmentVariation(Point a, Point b) {
        Segment best = new Segment(a, b);
        double bestDistance = best.distance();
        for (Point aVariation : genPointVariation(a)) {
            for (Point bVariation : genPointVariation(b)) {
                Segment segment = new Segment(aVariation, bVariation);
                if (Double.compare(bestDistance, segment.distance()) == 1) {
                    best = new Segment(aVariation, bVariation);
                    bestDistance = segment.distance();
                }
            }
        }
        return best;
    }

    // Варианты координат из-за зацикленности
    private List<Point> genPointVariation(Point point) {
        List<Point> answer = new ArrayList<>();
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                Point newPoint = point.toBuilder().build();
                newPoint.setLongitude(newPoint.getLongitude() + x * 360);
                newPoint.setLatitude(newPoint.getLatitude() + y * 180);
                answer.add(newPoint);
            }
        }
        return answer;
    }

    private void checkExist(long id) {
        if (!areaRepository.existsById(id)) {
            throw new NotFoundException("Area", String.valueOf(id));
        }
    }

    private Area saveToDatabase(Area area) {
        try {
            return areaRepository.save(area);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Area");
        }
    }

}
