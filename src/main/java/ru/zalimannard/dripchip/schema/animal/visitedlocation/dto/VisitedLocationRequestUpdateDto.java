package ru.zalimannard.dripchip.schema.animal.visitedlocation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class VisitedLocationRequestUpdateDto {

    @JsonProperty("visitedLocationPointId")
    @NotNull
    @Positive
    Long visitedLocationPointId;

    @JsonProperty("locationPointId")
    @NotNull
    @Positive
    Long locationPointId;

}
