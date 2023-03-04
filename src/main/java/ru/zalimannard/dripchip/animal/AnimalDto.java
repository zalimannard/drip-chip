package ru.zalimannard.dripchip.animal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.zalimannard.dripchip.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.animal.lifestatus.AnimalLifeStatus;

import java.sql.Timestamp;
import java.util.List;

@Data
public class AnimalDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("animalTypes")
    private List<Long> animalTypes;

    @JsonProperty("weight")
    private Float weight;

    @JsonProperty("length")
    private Float length;

    @JsonProperty("height")
    private Float height;

    @JsonProperty("gender")
    private AnimalGender gender;

    @JsonProperty("lifeStatus")
    private AnimalLifeStatus lifeStatus;

    @JsonProperty("chippingDateTime")
    private Timestamp chippingDateTime;

    @JsonProperty("chipperId")
    private Integer chipperId;

    @JsonProperty("chippingLocationId")
    private Long chippingLocationId;

    @JsonProperty("visitedLocations")
    private List<Long> visitedLocations;

    @JsonProperty("deathDateTime")
    private Timestamp deathDateTime;

}
