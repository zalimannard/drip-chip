package ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    // У Jackson проблема с десериализацией классов с одним полем. Поэтому конкретный метод
    @JsonCreator
    public AnimalTypeRequestDto(@JsonProperty("type") String type) {
        this.type = type;
    }

}
