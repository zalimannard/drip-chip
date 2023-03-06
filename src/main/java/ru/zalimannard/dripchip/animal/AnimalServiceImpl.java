package ru.zalimannard.dripchip.animal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.account.AccountRepository;
import ru.zalimannard.dripchip.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.animal.type.AnimalTypeService;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.location.LocationRepository;

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
    private final AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);
    private final AnimalTypeService animalTypeService;

    @Override
    public AnimalDto create(@Valid AnimalDto animalDto) {
        try {
            Animal animalRequest = animalMapper.toEntity(animalDto, accountRepository, locationRepository);
            animalRequest.setLifeStatus(AnimalLifeStatus.ALIVE);
            animalRequest.setChippingDateTime(Timestamp.from(Instant.now()));
            animalRequest.setAnimalTypes(new HashSet<>(animalTypeService.getAllById(animalDto.getAnimalTypeIds())));

            Animal animalResponse = animalRepository.save(animalRequest);
            return animalMapper.toDto(animalResponse);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Conflict in adding to the database");
        }
    }

    @Override
    public AnimalDto read(long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal", "id", String.valueOf(id)));
        return animalMapper.toDto(animal);
    }

    @Override
    public List<AnimalDto> search(AnimalDto filter, int from, int size) {
        return new ArrayList<>();
    }

}
