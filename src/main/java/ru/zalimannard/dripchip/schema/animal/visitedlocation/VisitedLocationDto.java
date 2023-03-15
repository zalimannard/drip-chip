package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;

@Data
public class VisitedLocationDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty("dateTimeOfVisitLocationPoint")
    private Date dateTimeOfVisitLocationPoint;

    @JsonProperty(value = "animalId", access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Positive
    private Long animalId;

    @JsonProperty("locationPointId")
    @NotNull
    @Positive
    private Long locationId;

}
