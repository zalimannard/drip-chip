package ru.zalimannard.dripchip.schema.area.point.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class PointResponseDto {

    @JsonProperty("longitude")
    Double longitude;

    @JsonProperty("latitude")
    Double latitude;

}
