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

    VisitedLocation createEntity(@Positive long animalId, @Positive long locationId);

    VisitedLocationDto read(@Positive long id);

    VisitedLocation readEntity(@Positive long id);

    List<VisitedLocationDto> search(@Positive long animalId, Date start, Date end,
                                    @PositiveOrZero int from, @Positive int size);

    List<VisitedLocation> searchEntities(VisitedLocation filter, Date start, Date end,
                                         @PositiveOrZero int from, @Positive int size);

    VisitedLocationDto update(@Positive long animalId, @Valid VisitedLocationUpdateDto visitedLocationUpdateDto);

    VisitedLocation updateEntity(@Positive long animalId, @Positive long visitedLocationPointId,
                                 @Positive long locationPointId);

    void delete(@Positive long animalId, @Positive long visitedLocationId);

}
