package ru.zalimannard.dripchip.schema.area.point;

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
    private final PointMapper pointMapper;

    @Override
    public List<PointDto> createAll(Area area, List<PointDto> pointDtos) {
        List<Point> allPoints = pointRepository.findAll();
        List<Point> pointsRequest = pointMapper.toEntityList(pointDtos);
        for (int i = 0; i < pointsRequest.size(); ++i) {
            if ((!isConsistent(pointsRequest.get(i), pointsRequest.get((i + 1) % pointsRequest.size()), allPoints)) ||
                    (!isConsistent(pointsRequest.get(i), pointsRequest.get((i + 1) % pointsRequest.size()), pointsRequest))) {
                throw new BadRequestException("Point");
            }
        }

        pointsRequest.forEach(point -> point.setArea(area));
        try {
            List<Point> pointsResponse = pointRepository.saveAll(pointsRequest);
            return pointMapper.toDtoList(pointsResponse);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Point");
        }
    }

    @Override
    public void deleteAll(Area area) {
        pointRepository.deleteAllByArea(area);
    }

    private boolean isConsistent(Point a, Point b, List<Point> otherPoints) {
        for (int i = 0; i < otherPoints.size(); ++i) {
            if (hasIntersection(a, b, otherPoints.get(i), otherPoints.get((i + 1) % otherPoints.size()))) {
                return false;
            }
        }
        return true;
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

        if (a2Best.getLongitude().equals(a1Best.getLongitude())) {
            return false;
        }
        if (b2Best.getLongitude().equals(b1Best.getLongitude())) {
            return false;
        }
        double aCoefficient = (a2Best.getLatitude() - a1Best.getLatitude()) /
                (a2Best.getLongitude() - a1Best.getLongitude());
        double aFreeMember = -((a1Best.getLongitude() * a2Best.getLatitude()) / (a2Best.getLongitude() - a1Best.getLongitude())) +
                ((a1Best.getLatitude() * a2Best.getLongitude()) / (a2Best.getLongitude() - a1Best.getLongitude()));
        double bCoefficient = (b2Best.getLatitude() - b1Best.getLatitude()) /
                (b2Best.getLongitude() - b1Best.getLongitude());
        double bFreeMember = -((b1Best.getLongitude() * b2Best.getLatitude()) / (b2Best.getLongitude() - b1Best.getLongitude())) +
                ((b1Best.getLatitude() * b2Best.getLongitude()) / (b2Best.getLongitude() - b1Best.getLongitude()));
        // Избегаем деления на 0
        if (aCoefficient == bCoefficient) {
            return false;
        }
        double x = (bFreeMember - aFreeMember) / (aCoefficient - bCoefficient);

        return ((a1Best.getLongitude() < x) && (x < a2Best.getLongitude()) &&
                (b1Best.getLongitude() < x) && (x < b2Best.getLongitude()));
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
}
