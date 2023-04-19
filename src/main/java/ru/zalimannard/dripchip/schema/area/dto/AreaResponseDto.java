package ru.zalimannard.dripchip.schema.area.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import ru.zalimannard.dripchip.schema.area.point.dto.PointResponseDto;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class AreaResponseDto {

    @JsonProperty("id")
    Long id;

    @JsonProperty("name")
    String name;

    @JsonProperty("areaPoints")
    List<PointResponseDto> points;

}