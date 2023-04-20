package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationRequestUpdateDto;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationResponseDto;

import java.util.Date;
import java.util.List;

@Validated
public interface VisitedLocationService {

    VisitedLocationResponseDto create(@Positive @NotNull Long animalId, @Positive @NotNull Long locationId);

    VisitedLocation createEntity(@Positive @NotNull Long animalId, @Positive @NotNull Long locationId);

    VisitedLocationResponseDto read(@Positive @NotNull Long id);

    VisitedLocation readEntity(@Positive @NotNull Long id);

    List<VisitedLocationResponseDto> search(@Positive @NotNull Long animalId, Date start, Date end,
                                            @PositiveOrZero int from, @Positive int size);

    List<VisitedLocation> searchEntities(@Positive @NotNull Long animalId, Date start, Date end,
                                         @PositiveOrZero int from, @Positive int size);

    VisitedLocationResponseDto update(@Positive @NotNull Long animalId,
                                      @Valid VisitedLocationRequestUpdateDto visitedLocationRequestUpdateDto);

    VisitedLocation updateEntity(@Positive @NotNull Long animalId, @Positive @NotNull Long visitedLocationPointId,
                                 @Positive @NotNull Long locationPointId);

    void delete(@Positive @NotNull Long animalId, @Positive @NotNull Long visitedLocationId);

}
