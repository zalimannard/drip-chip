package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnimalTypeDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    Long id;

    @JsonProperty("type")
    @NotBlank
    String type;

}
