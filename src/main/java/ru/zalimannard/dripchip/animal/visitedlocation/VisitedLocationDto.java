package ru.zalimannard.dripchip.animal.visitedlocation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @Min(1)
    private Long animalId;

    @JsonProperty("locationPointId")
    @NotNull
    @Min(1)
    private Long locationId;

}
