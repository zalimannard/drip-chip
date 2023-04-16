package ru.zalimannard.dripchip.schema.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class LocationRequestDto {

    @JsonProperty("longitude")
    @NotNull
    @Min(-180)
    @Max(180)
    Double longitude;

    @JsonProperty("latitude")
    @NotNull
    @Min(-90)
    @Max(90)
    Double latitude;

}
