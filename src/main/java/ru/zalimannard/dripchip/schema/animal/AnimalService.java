package ru.zalimannard.dripchip.schema.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;

import java.util.Date;
import java.util.List;

@Validated
public interface AnimalService {

    AnimalResponseDto create(@Valid AnimalPostRequestDto animalPostRequestDto);

    Animal createEntity(Animal animal);


    AnimalResponseDto read(@Positive @NotNull Long id);

    Animal readEntity(@Positive @NotNull Long id);

    List<AnimalResponseDto> search(Date startDateTime,
                                   Date endDateTime,
                                   Integer chipperId,
                                   Long chippingLocationId,
                                   String lifeStatus,
                                   String gender,
                                   @PositiveOrZero int from,
                                   @Positive int size);

    List<Animal> searchEntities(Date start,
                                Date end,
                                Integer chipperId,
                                Long chippingLocationId,
                                String lifeStatus,
                                String gender,
                                @PositiveOrZero int from,
                                @Positive int size);


    AnimalResponseDto update(@Positive @NotNull Long id, @Valid AnimalPutRequestDto animalPutRequestDto);

    Animal updateEntity(@Positive @NotNull Long id, Animal animal);


    void delete(@Positive @NotNull Long id);

}
