package ru.zalimannard.dripchip.schema.animal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;

import java.util.*;

@Data
public class AnimalDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("animalTypes")
    private Set<@Positive Long> animalTypeIds = new HashSet<>();

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date chippingDateTime;

    @JsonProperty("chipperId")
    @NotNull
    @Positive
    private Integer chipperId;

    @JsonProperty("chippingLocationId")
    @NotNull
    @Positive
    private Long chippingLocationId;

    @JsonProperty("visitedLocations")
    private List<@Positive Long> visitedLocations = new ArrayList<>();

    @JsonProperty("deathDateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDateTime;

    public void addAnimalTypeId(long animalTypeId) {
        animalTypeIds.add(animalTypeId);
    }

}
