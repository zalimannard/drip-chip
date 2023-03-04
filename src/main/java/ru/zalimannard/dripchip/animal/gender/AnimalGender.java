package ru.zalimannard.dripchip.animal.gender;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AnimalGender {
    MALE("M"),
    FEMALE("F"),
    OTHER("O");

    @Getter
    private final String code;
}
