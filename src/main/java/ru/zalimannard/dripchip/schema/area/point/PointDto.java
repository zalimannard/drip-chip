package ru.zalimannard.dripchip.schema.area.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PointDto {

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
