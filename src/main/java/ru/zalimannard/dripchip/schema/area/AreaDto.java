package ru.zalimannard.dripchip.schema.area;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.zalimannard.dripchip.schema.area.point.PointDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class AreaDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("areaPoints")
    @Size(min = 3)
    private List<@Valid PointDto> points = new ArrayList<>();

}