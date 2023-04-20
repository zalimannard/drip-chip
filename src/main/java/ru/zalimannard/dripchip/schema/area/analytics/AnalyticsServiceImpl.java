package ru.zalimannard.dripchip.schema.area.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.schema.animal.AnimalService;
import ru.zalimannard.dripchip.schema.area.AreaService;
import ru.zalimannard.dripchip.schema.area.analytics.dto.AnalyticsDto;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AreaService areaService;
    private final AnimalService animalService;

    @Override
    public AnalyticsDto read(Long areaId, Date startDate, Date endDate) {
        if (endDate.before(startDate)) {
            throw new BadRequestException("ans-01", "startDate, endDate", "End>Start: " + endDate + ">" + startDate);
        }
        if (endDate.equals(startDate)) {
            throw new BadRequestException("ans-02", "startDate, endDate", "End=Start: " + endDate + "=" + startDate);
        }

        return AnalyticsDto.builder()
                .totalQuantityAnimals(0L)
                .totalAnimalsArrived(0L)
                .totalAnimalsGone(0L)
                .animalsAnalytics(new ArrayList<>())
                .build();
    }

}
