package ru.zalimannard.dripchip.schema.animal.visitedlocation.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class VisitedLocationUpdateDto {

    @JsonProperty("visitedLocationPointId")
    @NotNull
    @Positive
    private Long visitedLocationPointId;

    @JsonProperty("locationPointId")
    @NotNull
    @Positive
    private Long locationPointId;

}
