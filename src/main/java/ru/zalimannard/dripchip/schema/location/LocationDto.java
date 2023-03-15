package ru.zalimannard.dripchip.schema.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty("latitude")
    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitude;

    @JsonProperty("longitude")
    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitude;

}
