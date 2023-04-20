package ru.zalimannard.dripchip.schema.area.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class AnalyticsDto {

    @JsonProperty("totalQuantityAnimals")
    Long totalQuantityAnimals;

    @JsonProperty("totalAnimalsArrived")
    Long totalAnimalsArrived;

    @JsonProperty("totalAnimalsGone")
    Long totalAnimalsGone;

    @JsonProperty("animalsAnalytics")
    List<AnimalAnalyticsDto> animalsAnalytics;

}