package ru.zalimannard.dripchip.schema.area.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PointDto {

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
