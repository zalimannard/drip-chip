package ru.zalimannard.dripchip.schema.area.analytics;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.area.analytics.dto.AnalyticsDto;

import java.util.Date;

@Validated
public interface AnalyticsService {

    AnalyticsDto read(@Positive @NotNull Long id, @NotNull Date startDate, @NotNull Date endDate);

}
