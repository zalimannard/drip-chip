package ru.zalimannard.dripchip.animal.visitedlocation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import ru.zalimannard.dripchip.animal.visitedlocation.update.VisitedLocationUpdateDto;

import java.util.List;

public interface VisitedLocationService {

    VisitedLocationDto create(@Positive long animalId, @Positive long locationId);

    List<VisitedLocationDto> readAll(@Positive long animalId);

    VisitedLocationDto update(@Positive long animalId, @Valid VisitedLocationUpdateDto visitedLocationUpdateDto);

    void delete(@Positive long animalId, @Positive long visitedLocationId);

}
