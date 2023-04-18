package ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AnimalTypeResponseDto {

    @JsonProperty("id")
    Long id;

    @JsonProperty("type")
    String type;

}
