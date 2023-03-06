package ru.zalimannard.dripchip.animal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
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
    private Set<@Min(1) Long> animalTypeIds;

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
    @Null
    private AnimalLifeStatus lifeStatus;

    @JsonProperty("chippingDateTime")
    @Null
    private Timestamp chippingDateTime;

    @JsonProperty("chipperId")
    @NotNull
    @Min(1)
    private Integer chipperId;

    @JsonProperty("chippingLocationId")
    @NotNull
    @Min(1)
    private Long chippingLocationId;

    @JsonProperty("visitedLocations")
    @Null
    private List<Long> visitedLocations;

    @JsonProperty("deathDateTime")
    private Timestamp deathDateTime;

    public void addAnimalTypeId(long animalTypeId) {
        animalTypeIds.add(animalTypeId);
    }

    public void removeAnimalTypeId(long animalTypeId) {
        animalTypeIds.remove(animalTypeId);
    }

}
