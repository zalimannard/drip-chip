package ru.zalimannard.dripchip.schema.area.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AnimalAnalyticsDto {

    @JsonProperty("animalType")
    String animalType;

    @JsonProperty("animalTypeId")
    Long animalTypeId;

    @JsonProperty("quantityAnimals")
    Long quantityAnimals;

    @JsonProperty("animalsArrived")
    Long animalsArrived;

    @JsonProperty("animalsGone")
    Long animalsGone;

}
