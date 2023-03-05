package ru.zalimannard.dripchip.animal.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnimalTypeDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    @NotBlank
    private String type;

}
