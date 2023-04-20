package ru.zalimannard.dripchip.schema.area.analytics;

import ru.zalimannard.dripchip.schema.area.analytics.dto.AnalyticsDto;

import java.util.Date;

public interface AnalyticsService {

    AnalyticsDto read(Long id, Date startDate, Date endDate);

}
