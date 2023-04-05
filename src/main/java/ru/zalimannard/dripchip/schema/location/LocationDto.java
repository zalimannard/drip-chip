package ru.zalimannard.dripchip.schema.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    Long id;

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
