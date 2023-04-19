package ru.zalimannard.dripchip.schema.area;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.area.dto.AreaRequestDto;
import ru.zalimannard.dripchip.schema.area.dto.AreaResponseDto;
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

    private final AreaMapper mapper;
    private final AreaRepository repository;

    private final PointService pointService;
    private final PointMapper pointMapper;

    @Override
    public AreaResponseDto create(AreaRequestDto areaRequestDto) {
        Area areaRequest = mapper.toEntity(areaRequestDto);
        List<Point> points = pointMapper.toEntityList(areaRequestDto.getPoints(), areaRequest);
        Area areaResponse = createEntity(areaRequest, points);
        return mapper.toDto(areaResponse);
    }

    @Override
    public Area createEntity(Area area, List<Point> points) {
        for (int i = 0; i < points.size(); ++i) {
            points.get(i).setNumberInArea((long) i);
        }
        area.setPoints(points);
        List<Area> existedAreas = readAllEntities();

        checkAvailability(existedAreas, area);

        Area createdArea;
        try {
            createdArea = repository.saveAndFlush(area);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ars-01", "area", e.getLocalizedMessage());
        }

        List<Point> createdPoints = pointService.createAllEntities(createdArea, points);
        createdArea.setPoints(createdPoints);
        return createdArea;
    }

    private void checkAvailability(List<Area> existedAreas, Area targetArea) {
        for (Area existedArea : existedAreas) {
            if (!isConsistent(existedArea, targetArea)) {
                throw new BadRequestException("ars-02", "area", "Конфликт с другими");
            } else if (!isConsistent(targetArea, targetArea)) {
                throw new BadRequestException("ars-03", "area", "Конфликт с собой");
            } else if (Double.compare(targetArea.calcAreaValue(), 0.0) == 0) {
                throw new BadRequestException("ars-04", "area", "Нулевая площадь");
            }
        }
    }

    @Override
    public AreaResponseDto read(long id) {
        Area areaResponse = readEntity(id);
        return mapper.toDto(areaResponse);
    }

    @Override
    public Area readEntity(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ars-05", "id", String.valueOf(id)));
    }

    @Override
    public List<Area> readAllEntities() {
        return repository.findAll();
    }

    @Override
    public AreaResponseDto update(long id, AreaRequestDto areaRequestDto) {
        Area areaRequest = mapper.toEntity(areaRequestDto);
        List<Point> points = pointMapper.toEntityList(areaRequestDto.getPoints(), areaRequest);

        Area areaResponse = updateEntity(id, areaRequest, points);

        return mapper.toDto(areaResponse);
    }

    @Override
    @Transactional
    public Area updateEntity(long id, Area area, List<Point> points) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ant-06", "id", String.valueOf(id)));

        Area existedArea = readEntity(id);
        List<Area> existedAreas = readAllEntities();
        for (int i = 0; i < points.size(); ++i) {
            points.get(i).setNumberInArea((long) i);
        }

        area.setId(id);
        area.setPoints(points);
        existedAreas.remove(existedArea);
        checkAvailability(existedAreas, area);
        pointService.deleteAll(area);

        Area updatedArea;
        try {
            updatedArea = repository.save(area);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ars-07", "area", e.getLocalizedMessage());
        }
        List<Point> createdPoints = pointService.createAllEntities(updatedArea, points);
        updatedArea.setPoints(createdPoints);
        return updatedArea;
    }

    @Override
    @Transactional
    public void delete(long id) {
        try {
            Area area = readEntity(id);
            pointService.deleteAll(area);
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("ars-08", "id", String.valueOf(id));
        }
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

}
