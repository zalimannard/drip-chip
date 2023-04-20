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

    private static boolean pointOnSegment(Point p1, Point p2, Point point) {
        if (point.getLongitude() <= Math.max(p1.getLongitude(), p2.getLongitude()) && point.getLongitude() >= Math.min(p1.getLongitude(), p2.getLongitude()) &&
                point.getLatitude() <= Math.max(p1.getLatitude(), p2.getLatitude()) && point.getLatitude() >= Math.min(p1.getLatitude(), p2.getLatitude())) {
            double crossProduct = (point.getLatitude() - p1.getLatitude()) * (p2.getLongitude() - p1.getLongitude()) - (point.getLongitude() - p1.getLongitude()) * (p2.getLatitude() - p1.getLatitude());
            return crossProduct == 0;
        }
        return false;
    }

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

    @Override
    public AreaResponseDto read(Long id) {
        Area areaResponse = readEntity(id);
        return mapper.toDto(areaResponse);
    }

    @Override
    public Area readEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ars-05", "id", String.valueOf(id)));
    }

    @Override
    public List<Area> readAllEntities() {
        return repository.findAll();
    }

    @Override
    public AreaResponseDto update(Long id, AreaRequestDto areaRequestDto) {
        Area areaRequest = mapper.toEntity(areaRequestDto);
        List<Point> points = pointMapper.toEntityList(areaRequestDto.getPoints(), areaRequest);

        Area areaResponse = updateEntity(id, areaRequest, points);

        return mapper.toDto(areaResponse);
    }

    @Override
    @Transactional
    public Area updateEntity(Long id, Area area, List<Point> points) {
        Area existedArea = readEntity(id);
        List<Area> existedAreas = readAllEntities();
        for (int i = 0; i < points.size(); ++i) {
            points.get(i).setNumberInArea((long) i);
        }

        area.setId(id);
        area.setPoints(points);
        existedAreas.remove(existedArea);
        checkAvailability(existedAreas, area);
        try {
            pointService.deleteAll(area);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ars-13", "area", e.getLocalizedMessage());
        }

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
    public void delete(Long id) {
        try {
            Area area = readEntity(id);
            pointService.deleteAll(area);
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("ars-08", "id", String.valueOf(id));
        }
    }

    private boolean haveInside(Area polygon, Point point) {
        int n = polygon.getPoints().size();
        boolean inside = false;

        Point p1 = polygon.getPoints().get(0);
        for (int i = 1; i <= n; i++) {
            Point p2 = polygon.getPoints().get(i % n);

            if (pointOnSegment(p1, p2, point)) {
                return false;
            }

            if (point.getLatitude() > Math.min(p1.getLatitude(), p2.getLatitude()) && point.getLatitude() <= Math.max(p1.getLatitude(), p2.getLatitude())) {
                if (point.getLongitude() <= Math.max(p1.getLongitude(), p2.getLongitude())) {
                    if (!p1.getLatitude().equals(p2.getLatitude())) {
                        double xIntersection = (point.getLatitude() - p1.getLatitude()) * (p2.getLongitude() - p1.getLongitude()) / (p2.getLatitude() - p1.getLatitude()) + p1.getLongitude();
                        if (p1.getLongitude().equals(p2.getLongitude()) || point.getLongitude() <= xIntersection) {
                            inside = !inside;
                        }
                    }
                }
            }
            p1 = p2;
        }

        return inside;
    }

    private void checkAvailability(List<Area> existedAreas, Area targetArea) {
        if (!isConsistent(targetArea, targetArea)) {
            throw new BadRequestException("ars-03", "area", "Пересечение с собой");
        } else if (Double.compare(calcArea(targetArea), 0.0) == 0) {
            throw new BadRequestException("ars-04", "area", "Нулевая площадь");
        } else if (haveDuplicate(targetArea)) {
            throw new BadRequestException("ars-12", "points", "Содержатся дубликаты");
        }

        for (Area existedArea : existedAreas) {
            if (!isConsistent(existedArea, targetArea)) {
                throw new BadRequestException("ars-02", "area", "Пересечение с другими");
            } else if (haveIdenticallyPoints(existedArea, targetArea)) {
                throw new ConflictException("ars-09", "area", "Уже есть такая область");
            }

            // Существующая содержит точки новой
            if (targetArea.getPoints() != null) {
                for (Point targetAreaPoint : targetArea.getPoints()) {
                    if (haveInside(existedArea, targetAreaPoint)) {
                        throw new BadRequestException("ars-10", "poins", "Точки новой области находятся внутри другой");
                    }
                }
            }

            // Новая содержит точки существующей
            if (existedArea.getPoints() != null) {
                for (Point existedAreaPoint : existedArea.getPoints()) {
                    if (haveInside(targetArea, existedAreaPoint)) {
                        throw new BadRequestException("ars-11", "poins", "Точки существующей области находятся внутри новой");
                    }
                }
            }
        }
    }

    private boolean haveDuplicate(Area targetArea) {
        List<Point> points = targetArea.getPoints();
        for (int i = 0; i < points.size(); ++i) {
            for (int j = 0; j < points.size(); ++j) {
                if ((i != j)
                        && (Double.compare(points.get(i).getLatitude(), points.get(j).getLatitude()) == 0)
                        && (Double.compare(points.get(i).getLongitude(), points.get(j).getLongitude()) == 0)) {
                    return true;
                }
            }
        }
        return false;
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
        // Долгота зацикливается просто по кругу. Зацикливание широты выглядит как отражение по вертикали и сдвиг
        // долготы на половину, как бы опоясывание Земли
        // Основная линия зацикленности
        for (int x = -1; x <= 1; ++x) {
            Point newPoint = point.toBuilder()
                    .longitude(point.getLongitude() + x * 360)
                    .build();
            answer.add(newPoint);
        }
        // Линия сверху
        for (int x = 0; x <= 1; ++x) {
            Point newPoint = point.toBuilder()
                    .longitude(point.getLongitude() + x * 360 - 180)
                    .latitude(180 - point.getLatitude())
                    .build();
            answer.add(newPoint);
        }
        // Линия снизу
        for (int x = 0; x <= 1; ++x) {
            Point newPoint = point.toBuilder()
                    .longitude(point.getLongitude() + x * 360 - 180)
                    .latitude(-180 - point.getLatitude())
                    .build();
            answer.add(newPoint);
        }
        return answer;
    }


    private double calcArea(Area area) {
        double value1 = 0;
        double value2 = 0;
        List<Point> pointList = area.getPoints();

        for (int i = 0; i < pointList.size(); ++i) {
            value1 += pointList.get(i).getLongitude() *
                    pointList.get((i + 1) % pointList.size()).getLatitude();
            value2 += pointList.get(i).getLatitude() *
                    pointList.get((i + 1) % pointList.size()).getLongitude();
        }

        return value1 - value2;
    }

    private boolean haveIdenticallyPoints(Area a, Area b) {
        if (a.getPoints().size() != b.getPoints().size()) {
            return false;
        }

        List<Point> thisPoints = a.getPoints();
        List<Point> otherPoints = b.getPoints();
        // Проверяем для каждого сдвига
        for (int i = 0; i < thisPoints.size(); ++i) {
            // Каждую точку
            for (int j = 0; j < thisPoints.size(); ++j) {
                Point currentThisPoint = thisPoints.get(j);
                Point currentOtherPoint = otherPoints.get((j + i) % thisPoints.size());

                if ((Double.compare(currentThisPoint.getLongitude(), currentOtherPoint.getLongitude()) == 0)
                        && (Double.compare(currentThisPoint.getLatitude(), currentOtherPoint.getLatitude()) == 0)) {
                    if (j == thisPoints.size() - 1) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        // И для развёрнутого
        for (int i = 0; i < thisPoints.size(); ++i) {
            for (int j = 0; j < thisPoints.size(); ++j) {
                Point currentThisPoint = thisPoints.get(thisPoints.size() - j - 1);
                Point currentOtherPoint = otherPoints.get((j + i) % thisPoints.size());

                if ((Double.compare(currentThisPoint.getLongitude(), currentOtherPoint.getLongitude()) == 0)
                        && (Double.compare(currentThisPoint.getLatitude(), currentOtherPoint.getLatitude()) == 0)) {
                    if (j == thisPoints.size() - 1) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }

        return false;
    }

}
