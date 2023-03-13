package ru.zalimannard.dripchip.schema.animal.ownedtype.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AnimalOwnedTypeUpdateDto {

    @JsonProperty("oldTypeId")
    @NotNull
    @Positive
    private Long oldTypeId;

    @JsonProperty("newTypeId")
    @NotNull
    @Positive
    private Long newTypeId;

}
