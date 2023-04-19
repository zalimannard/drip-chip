package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationResponseDto;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationUpdateDto;

import java.util.Date;
import java.util.List;

@Validated
public interface VisitedLocationService {

    VisitedLocationResponseDto create(@Positive long animalId, @Positive long locationId);

    VisitedLocation createEntity(@Positive long animalId, @Positive long locationId);

    VisitedLocationResponseDto read(@Positive long id);

    VisitedLocation readEntity(@Positive long id);

    List<VisitedLocationResponseDto> search(@Positive long animalId, Date start, Date end,
                                            @PositiveOrZero int from, @Positive int size);

    List<VisitedLocation> searchEntities(@Positive long animalId, Date start, Date end,
                                         @PositiveOrZero int from, @Positive int size);

    VisitedLocationResponseDto update(@Positive long animalId, @Valid VisitedLocationUpdateDto visitedLocationUpdateDto);

    VisitedLocation updateEntity(@Positive long animalId, @Positive long visitedLocationPointId,
                                 @Positive long locationPointId);

    void delete(@Positive long animalId, @Positive long visitedLocationId);

}
