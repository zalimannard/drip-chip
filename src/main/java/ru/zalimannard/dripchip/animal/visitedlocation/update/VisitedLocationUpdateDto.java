package ru.zalimannard.dripchip.animal.visitedlocation.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.sql.Timestamp;

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
