package ru.zalimannard.dripchip.schema.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.ForbiddenException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountRepository;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.type.AnimalTypeDto;
import ru.zalimannard.dripchip.schema.animal.type.AnimalTypeMapper;
import ru.zalimannard.dripchip.schema.animal.type.AnimalTypeService;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;
    private final AnimalTypeMapper animalTypeMapper;
    private final AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);
    private final AnimalTypeService animalTypeService;

    @Override
    public AnimalDto create(@Valid AnimalDto animalDto) {
        try {
            if (animalDto.getAnimalTypeIds().size() == 0) {
                throw new BadRequestException("The number of animal types must be greater than zero");
            }
            Animal animalRequest = animalMapper.toEntity(animalDto, accountRepository, locationRepository);
            animalRequest.setLifeStatus(AnimalLifeStatus.ALIVE);
            animalRequest.setChippingDateTime(Timestamp.from(Instant.now()));
            List<AnimalTypeDto> animalTypeDtos = animalTypeService.getAllById(animalDto.getAnimalTypeIds());
            animalRequest.setAnimalTypes(new HashSet<>(animalTypeMapper.toEntityList(animalTypeDtos)));

            Animal animalResponse = animalRepository.save(animalRequest);
            return animalMapper.toDto(animalResponse);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Conflict in adding to the database");
        }
    }

    @Override
    public AnimalDto read(@Positive long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal", "id", String.valueOf(id)));
        return animalMapper.toDto(animal);
    }

    @Override
    public List<AnimalDto> search(AnimalDto filter, Timestamp start, Timestamp end, @PositiveOrZero int from,
                                  @Positive int size) {

        Account accountStart = new Account();
        accountStart.setId(1);
        Account accountEnd = new Account();
        accountEnd.setId(Integer.MAX_VALUE);
        Location locationStart = new Location();
        locationStart.setId(1L);
        Location locationEnd = new Location();
        locationEnd.setId(Long.MAX_VALUE);

        List<Animal> animalList = new ArrayList<>();
//                animalRepository.findAllByChippingDateTimeBetweenAndChipperBetweenAndChippingLocationBetweenAndLifeStatusLikeAndGenderLikeOrderById(
//                        (start == null ? Timestamp.from(Instant.now().minusSeconds(10000)) : start),
//                        (end == null ? Timestamp.from(Instant.now()) : end),
//                        (accountStart),
//                        (accountEnd),
//                        (locationStart),
//                        (locationEnd));

        List<Animal> responseAnimalList = animalList
                .stream().skip(from)
                .limit(size).toList();

        return animalMapper.toDtoList(responseAnimalList);
    }

    @Override
    public AnimalDto update(@Positive long id, @Valid AnimalDto animalDto) {
        if (animalRepository.existsById(id)) {
            Animal animalRequest = animalMapper.toEntity(animalDto, accountRepository, locationRepository);
            animalRequest.setId(id);

            Animal accountResponse = animalRepository.save(animalRequest);
            return animalMapper.toDto(accountResponse);
        } else {
            throw new ForbiddenException();
        }
    }

    @Override
    public void delete(@Positive long id) {
        if (animalRepository.existsById(id)) {
            try {
                animalRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("It is impossible to delete animal");
            }
        } else {
            throw new NotFoundException("Animal", "id", String.valueOf(id));
        }
    }

}
