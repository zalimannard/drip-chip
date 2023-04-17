package ru.zalimannard.dripchip.schema.animal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;

import java.util.Set;

@Value
@Builder(toBuilder = true)
public class AnimalPostRequestDto {

    @JsonProperty("animalTypes")
    @NotNull
    @NotEmpty
    Set<@Positive Long> animalTypeIds;

    @JsonProperty("weight")
    @NotNull
    @Positive
    Float weight;

    @JsonProperty("length")
    @NotNull
    @Positive
    Float length;

    @JsonProperty("height")
    @NotNull
    @Positive
    Float height;

    @JsonProperty("gender")
    @NotNull
    AnimalGender gender;

    @JsonProperty("chipperId")
    @NotNull
    @Positive
    Integer chipperId;

    @JsonProperty("chippingLocationId")
    @NotNull
    @Positive
    Long chippingLocationId;

}
