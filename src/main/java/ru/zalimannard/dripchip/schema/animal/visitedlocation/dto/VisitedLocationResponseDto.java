package ru.zalimannard.dripchip.schema.animal.visitedlocation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder(toBuilder = true)
public class VisitedLocationResponseDto {

    @JsonProperty("id")
    Long id;

    @JsonProperty("dateTimeOfVisitLocationPoint")
    Date dateTimeOfVisitLocationPoint;

    @JsonProperty("locationPointId")
    Long locationId;

}
