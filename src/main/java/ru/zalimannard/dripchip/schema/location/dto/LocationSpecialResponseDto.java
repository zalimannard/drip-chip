package ru.zalimannard.dripchip.schema.location.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class LocationSpecialResponseDto {

    @JsonProperty("locationId")
    Long id;

}
