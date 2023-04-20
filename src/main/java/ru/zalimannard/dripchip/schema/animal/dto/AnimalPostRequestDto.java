package ru.zalimannard.dripchip.schema.animal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder(toBuilder = true)
public class AnimalPostRequestDto {

    @JsonProperty("animalTypes")
    @NotNull
    @NotEmpty
    Set<@Positive @NotNull Long> animalTypeIds;

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
    String gender;

    @JsonProperty("chipperId")
    @NotNull
    @Positive
    Integer chipperId;

    @JsonProperty("chippingLocationId")
    @NotNull
    @Positive
    Long chippingLocationId;

}
