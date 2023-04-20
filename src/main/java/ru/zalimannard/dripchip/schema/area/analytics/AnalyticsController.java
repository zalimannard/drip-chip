package ru.zalimannard.dripchip.schema.area.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.zalimannard.dripchip.schema.area.analytics.dto.AnalyticsDto;

import java.util.Date;

@RestController
@RequestMapping("${application.endpoint.areas}/{areaId}")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("${application.endpoint.analytics}")
    public AnalyticsDto get(@PathVariable long areaId,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return analyticsService.read(areaId, startDate, endDate);
    }

}