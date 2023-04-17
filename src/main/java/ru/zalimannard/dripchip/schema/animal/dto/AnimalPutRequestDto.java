package ru.zalimannard.dripchip.schema.animal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;

@Value
@Builder(toBuilder = true)
public class AnimalPutRequestDto {

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

    @JsonProperty("lifeStatus")
    AnimalLifeStatus lifeStatus;

    @JsonProperty("chipperId")
    @NotNull
    @Positive
    Integer chipperId;

    @JsonProperty("chippingLocationId")
    @NotNull
    @Positive
    Long chippingLocationId;

}
