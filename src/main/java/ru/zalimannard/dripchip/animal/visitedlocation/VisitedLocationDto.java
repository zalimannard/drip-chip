package ru.zalimannard.dripchip.animal.visitedlocation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class VisitedLocationDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("dateTimeOfVisitLocationPoint")
    private Timestamp dateTimeOfVisitLocationPoint;

    @JsonProperty("animalId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    @Positive
    private Long animalId;

    @JsonProperty("locationPointId")
    @NotNull
    @Positive
    private Long locationId;

}
