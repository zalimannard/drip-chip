package ru.zalimannard.dripchip.schema.area.point;

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
    private final PointMapper pointMapper;

    @Override
    public List<PointDto> createAll(Area area, List<PointDto> pointDtos) {
        List<Point> pointsRequest = pointMapper.toEntityList(pointDtos);
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
}
