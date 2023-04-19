package ru.zalimannard.dripchip.schema.area.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import ru.zalimannard.dripchip.schema.area.point.dto.PointRequestDto;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class AreaRequestDto {

    @JsonProperty("name")
    @NotBlank
    String name;

    @JsonProperty("areaPoints")
    @NotNull
    @Size(min = 3)
    List<@Valid PointRequestDto> points;

}