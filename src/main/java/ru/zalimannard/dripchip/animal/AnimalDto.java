package ru.zalimannard.dripchip.animal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.zalimannard.dripchip.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.animal.lifestatus.AnimalLifeStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class AnimalDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("animalTypes")
    @NotEmpty
    private Set<@Positive Long> animalTypeIds;

    @JsonProperty("weight")
    @NotNull
    @Positive
    private Float weight;

    @JsonProperty("length")
    @NotNull
    @Positive
    private Float length;

    @JsonProperty("height")
    @NotNull
    @Positive
    private Float height;

    @JsonProperty("gender")
    @NotNull
    private AnimalGender gender;

    @JsonProperty("lifeStatus")
    private AnimalLifeStatus lifeStatus;

    @JsonProperty("chippingDateTime")
    private Timestamp chippingDateTime;

    @JsonProperty("chipperId")
    @NotNull
    @Positive
    private Integer chipperId;

    @JsonProperty("chippingLocationId")
    @NotNull
    @Positive
    private Long chippingLocationId;

    @JsonProperty("visitedLocations")
    private List<@Positive Long> visitedLocations;

    @JsonProperty("deathDateTime")
    private Timestamp deathDateTime;

    public void addAnimalTypeId(long animalTypeId) {
        animalTypeIds.add(animalTypeId);
    }

}
