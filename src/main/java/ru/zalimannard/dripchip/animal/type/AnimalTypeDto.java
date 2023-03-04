package ru.zalimannard.dripchip.animal.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AnimalTypeDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    private String type;

}
