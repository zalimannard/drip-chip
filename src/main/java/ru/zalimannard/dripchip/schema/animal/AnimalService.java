package ru.zalimannard.dripchip.schema.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;

import java.util.Date;
import java.util.List;

public interface AnimalService {

    AnimalResponseDto create(@Valid AnimalPostRequestDto animalPostRequestDto);

    Animal createEntity(@Valid Animal animal);


    AnimalResponseDto read(@Positive long id);

    Animal readEntity(@Positive long id);

    List<AnimalResponseDto> search(Date startDateTime,
                                   Date endDateTime,
                                   Integer chipperId,
                                   Integer chippingLocationId,
                                   String lifeStatus,
                                   String gender,
                                   @PositiveOrZero int from,
                                   @Positive int size);

    List<Animal> searchEntities(Date start,
                                Date end,
                                Integer chipperId,
                                Integer chippingLocationId,
                                String lifeStatus,
                                String gender,
                                @PositiveOrZero int from,
                                @Positive int size);


    AnimalResponseDto update(@Positive long id, @Valid AnimalPutRequestDto animalPutRequestDto);

    Animal updateEntity(@Positive long id, @Valid Animal animal);


    void delete(@Positive long id);

}
