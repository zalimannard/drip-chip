package ru.zalimannard.dripchip.schema.animal.lifestatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AnimalLifeStatus {

    ALIVE("A"),
    DEAD("D");

    @Getter
    private final String code;

}
