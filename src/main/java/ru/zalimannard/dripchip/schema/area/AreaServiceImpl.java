package ru.zalimannard.dripchip.schema.area;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.area.point.PointDto;
import ru.zalimannard.dripchip.schema.area.point.PointService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;
    private final AreaMapper areaMapper;
    private final PointService pointService;

    @Override
    @Transactional
    public AreaDto create(AreaDto areaDto) {
        Area areaRequest = areaMapper.toEntity(areaDto);
        Area areaResponse = saveToDatabase(areaRequest);
        AreaDto areaDtoResponse = areaMapper.toDto(areaResponse);

        List<PointDto> pointDtos = pointService.createAll(areaResponse, areaDto.getPoints());
        areaDtoResponse.setPoints(pointDtos);
        return areaDtoResponse;
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
