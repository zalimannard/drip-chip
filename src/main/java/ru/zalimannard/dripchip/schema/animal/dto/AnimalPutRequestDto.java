package ru.zalimannard.dripchip.schema.animal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

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
    String gender;

    @JsonProperty("lifeStatus")
    String lifeStatus;

    @JsonProperty("chipperId")
    @NotNull
    @Positive
    Integer chipperId;

    @JsonProperty("chippingLocationId")
    @NotNull
    @Positive
    Long chippingLocationId;

}
