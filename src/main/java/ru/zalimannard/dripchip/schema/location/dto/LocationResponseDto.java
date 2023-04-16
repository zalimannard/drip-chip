package ru.zalimannard.dripchip.schema.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class LocationResponseDto {

    @JsonProperty("id")
    Long id;
    @JsonProperty("longitude")
    Double longitude;
    @JsonProperty("latitude")
    Double latitude;

}
