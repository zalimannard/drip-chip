package ru.zalimannard.dripchip.schema.animal.visitedlocation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull
    @Positive
    Long locationId;

}
