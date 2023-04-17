package ru.zalimannard.dripchip.schema.animal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Value;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class AnimalResponseDto {

    @JsonProperty("id")
    Long id;

    @JsonProperty("animalTypes")
    Set<Long> animalTypeIds;

    @JsonProperty("weight")
    Float weight;

    @JsonProperty("length")
    Float length;

    @JsonProperty("height")
    Float height;

    @JsonProperty("gender")
    AnimalGender gender;

    @JsonProperty("lifeStatus")
    AnimalLifeStatus lifeStatus;

    @JsonProperty("chippingDateTime")
    @Temporal(TemporalType.TIMESTAMP)
    Date chippingDateTime;

    @JsonProperty("chipperId")
    Integer chipperId;

    @JsonProperty("chippingLocationId")
    Long chippingLocationId;

    @JsonProperty("visitedLocations")
    List<Long> visitedLocations;

    @JsonProperty("deathDateTime")
    @Temporal(TemporalType.TIMESTAMP)
    Date deathDateTime;

}
