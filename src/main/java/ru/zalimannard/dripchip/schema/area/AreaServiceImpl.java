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
import ru.zalimannard.dripchip.schema.area.point.PointDto;
import ru.zalimannard.dripchip.schema.area.point.PointMapper;
import ru.zalimannard.dripchip.schema.area.point.PointService;

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
        requestArea.setPoints(pointMapper.toEntityList(areaDto.getPoints()));
        List<Area> existedAreas = readAll();

        for (Area existedArea : existedAreas) {
            System.out.println();
            System.out.println("Id существующей области:");
            System.out.println(existedArea.getId());
            if (!isConsistent(existedArea, requestArea)) {
                throw new BadRequestException("Area conflicts with existing areas");
            }
        }

        Area areaResponse = saveToDatabase(requestArea);
        List<PointDto> pointDtos = pointService.createAll(areaResponse, areaDto.getPoints());
        AreaDto areaDtoResponse = areaMapper.toDto(areaResponse);
        areaDtoResponse.setPoints(pointDtos);
        return areaDtoResponse;
    }

    private boolean isConsistent(Area existedArea, Area requestArea) {
        return !hasIntersection(existedArea, requestArea);
    }

    private boolean hasIntersection(Area existedArea, Area requestArea) {
        System.out.println("Проверяем на пересечение");
        for (int e = 0; e < existedArea.getPoints().size(); ++e) {
            System.out.println("У существующей берём ребро (" + existedArea.getPoints().get(e).getLongitude() + "; " + existedArea.getPoints().get(e).getLatitude() + ") -> (" +
                    existedArea.getPoints().get((e + 1) % existedArea.getPoints().size()).getLongitude() + "; " + existedArea.getPoints().get((e + 1) % existedArea.getPoints().size()).getLatitude() + ")");
            for (int r = 0; r < requestArea.getPoints().size(); ++r) {
                System.out.println("У новой берём ребро (" + requestArea.getPoints().get(r).getLongitude() + "; " + requestArea.getPoints().get(r).getLatitude() + ") -> (" +
                        requestArea.getPoints().get((r + 1) % requestArea.getPoints().size()).getLongitude() + "; " + requestArea.getPoints().get((r + 1) % requestArea.getPoints().size()).getLatitude() + ")");
                if (hasIntersection(existedArea.getPoints().get(e),
                        existedArea.getPoints().get((e + 1) % existedArea.getPoints().size()),
                        requestArea.getPoints().get(r),
                        requestArea.getPoints().get((r + 1) % requestArea.getPoints().size()))) {
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

    private boolean hasIntersection(Point a1, Point a2, Point b1, Point b2) {
        Point a1Best = new Point(a1);
        Point a2Best = new Point(a2);
        double minADistance = distance(a1Best, a2Best);
        for (Point a1Var : genPointVariation(a1)) {
            for (Point a2Var : genPointVariation(a2)) {
                if (distance(a1Var, a2Var) < minADistance) {
                    a1Best = new Point(a1Var);
                    a2Best = new Point(a2Var);
                    minADistance = distance(a1Best, a2Best);
                }
            }
        }
        // Чтобы первая была левой
        if (a1Best.getLongitude() > a2Best.getLongitude()) {
            Point temp = new Point(a1Best);
            a1Best = new Point(a2Best);
            a2Best = new Point(temp);
        }
        Point b1Best = new Point(b1);
        Point b2Best = new Point(b2);
        double minBDistance = distance(b1Best, b2Best);
        for (Point b1Var : genPointVariation(b1)) {
            for (Point b2Var : genPointVariation(b2)) {
                if (distance(b1Var, b2Var) < minBDistance) {
                    b1Best = new Point(b1Var);
                    b2Best = new Point(b2Var);
                    minBDistance = distance(b1Best, b2Best);
                }
            }
        }
        // Чтобы первая была левой
        if (b1Best.getLongitude() > b2Best.getLongitude()) {
            Point temp = new Point(b1Best);
            b1Best = new Point(b2Best);
            b2Best = new Point(temp);
        }

        double aCoefficient = (a2Best.getLatitude() - a1Best.getLatitude()) /
                (a2Best.getLongitude() - a1Best.getLongitude());
        double aFreeMember = -((a1Best.getLongitude() * a2Best.getLatitude()) / (a2Best.getLongitude() - a1Best.getLongitude())) +
                ((a1Best.getLatitude() * a2Best.getLongitude()) / (a2Best.getLongitude() - a1Best.getLongitude()));
        double bCoefficient = (b2Best.getLatitude() - b1Best.getLatitude()) /
                (b2Best.getLongitude() - b1Best.getLongitude());
        double bFreeMember = -((b1Best.getLongitude() * b2Best.getLatitude()) / (b2Best.getLongitude() - b1Best.getLongitude())) +
                ((b1Best.getLatitude() * b2Best.getLongitude()) / (b2Best.getLongitude() - b1Best.getLongitude()));
        // Избегаем деления на 0. Если равно, то прямые параллельны.
        if (aCoefficient == bCoefficient) {
            return false;
        }
        double x = (bFreeMember - aFreeMember) / (aCoefficient - bCoefficient);
        double y = (aCoefficient * bFreeMember - bCoefficient * aFreeMember) / (bCoefficient - aCoefficient);
        System.out.println("Точка пересечения: (" + x + "; " + y + ")");

        boolean xInRange = ((doubleLess(a1Best.getLongitude(), x)) && (doubleLess(x, a2Best.getLongitude())) &&
                doubleLess(b1Best.getLongitude(), x) && doubleLess(x, b2Best.getLongitude()));
        boolean yInRange = ((doubleLess(a1Best.getLatitude(), y)) && (doubleLess(y, a2Best.getLatitude())) &&
                doubleLess(b1Best.getLatitude(), y) && doubleLess(y, b2Best.getLatitude()));
        return xInRange && yInRange;
    }

    private boolean doubleLess(double a, double b) {
        if (Math.abs(a - b) <= 0.000001) {
            return false;
        } else {
            return a < b;
        }
    }

    // Варианты координат из-за зацикленности
    private List<Point> genPointVariation(Point point) {
        List<Point> answer = new ArrayList<>();
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                Point newPoint = new Point(point);
                newPoint.setLongitude(newPoint.getLongitude() + x * 360);
                newPoint.setLatitude(newPoint.getLatitude() + y * 180);
                answer.add(newPoint);
            }
        }
        return answer;
    }

    private Double distance(Point a, Point b) {
        Double xDiff = a.getLongitude() - b.getLongitude();
        Double yDiff = a.getLatitude() - b.getLatitude();
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
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
