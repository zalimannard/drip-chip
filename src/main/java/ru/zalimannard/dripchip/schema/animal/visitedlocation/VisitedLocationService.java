package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.update.VisitedLocationUpdateDto;

import java.util.List;

@Validated
public interface VisitedLocationService {

    VisitedLocationDto create(@Positive long animalId, @Positive long locationId);

    VisitedLocationDto read(@Positive long id);

    List<VisitedLocationDto> readByAnimalId(@Positive long animalId);

    VisitedLocationDto update(@Positive long animalId, @Valid VisitedLocationUpdateDto visitedLocationUpdateDto);

    void delete(@Positive long animalId, @Positive long visitedLocationId);

}
