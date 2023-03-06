package ru.zalimannard.dripchip.animal.visitedlocation;

import jakarta.validation.constraints.Positive;

import java.util.List;

public interface VisitedLocationService {

    VisitedLocationDto create(@Positive long animalId, @Positive long locationId);

    List<VisitedLocationDto> readAll(@Positive long animalId);

}
