package ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AnimalTypeRequestDto {

    @JsonProperty("type")
    @NotBlank
    String type;

}
