package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.update.VisitedLocationUpdateDto;

import java.util.Date;
import java.util.List;

@Validated
public interface VisitedLocationService {

    VisitedLocationDto create(@Positive long animalId, @Positive long locationId);

    VisitedLocationDto read(@Positive long id);

    List<VisitedLocationDto> search(@Positive long animalId, Date startDateTime, Date endDateTime,
                                    @PositiveOrZero int from, @Positive int size);

    VisitedLocationDto update(@Positive long animalId, @Valid VisitedLocationUpdateDto visitedLocationUpdateDto);

    void delete(@Positive long animalId, @Positive long visitedLocationId);

}
