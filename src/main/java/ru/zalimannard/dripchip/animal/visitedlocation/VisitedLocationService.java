package ru.zalimannard.dripchip.animal.visitedlocation;

import jakarta.validation.constraints.Positive;

public interface VisitedLocationService {

    VisitedLocationDto create(@Positive long animalId, @Positive long locationId);

}
