package ru.zalimannard.dripchip.schema.animal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountService;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeService;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationService;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class AnimalServiceImpl implements AnimalService {

    private final AnimalMapper mapper;
    private final AnimalRepository repository;

    private final AnimalTypeService animalTypeService;
    private final AccountService accountService;
    private final LocationService locationService;

    @Override
    public AnimalResponseDto create(AnimalPostRequestDto animalPostRequestDto) {
        List<AnimalType> animalTypes = animalTypeService.readAllEntitiesById(animalPostRequestDto.getAnimalTypeIds());
        Account chipper = accountService.readEntity(animalPostRequestDto.getChipperId());
        Location chippingLocation = locationService.readEntity(animalPostRequestDto.getChippingLocationId());
        Animal animalRequest = mapper.toEntity(animalPostRequestDto,
                animalTypes, chipper, chippingLocation);

        Animal animalResponse = createEntity(animalRequest);

        return mapper.toDto(animalResponse);
    }

    @Override
    public Animal createEntity(Animal animal) {
        animal.setLifeStatus(AnimalLifeStatus.ALIVE);
        animal.setChippingDateTime(Date.from(Instant.now()));

        try {
            return repository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ans-01", "animal", null);
        }
    }

    @Override
    public AnimalResponseDto read(long id) {
        Animal animalResponse = readEntity(id);

        return mapper.toDto(animalResponse);
    }

    @Override
    public Animal readEntity(long id) {
        Optional<Animal> responseOptional = repository.findById(id);
        if (responseOptional.isPresent()) {
            return responseOptional.get();
        } else {
            throw new NotFoundException("ans-02", "id", String.valueOf(id));
        }
    }

    @Override
    public List<AnimalResponseDto> search(AnimalPostRequestDto filterDto, Date start, Date end, int from, int size) {
        Animal filter = mapper.toEntity(filterDto, null, null, null);

        List<Animal> animals = searchEntities(filter, start, end, from, size);

        return mapper.toDtoList(animals);
    }

    @Override
    public List<Animal> searchEntities(Animal filter, Date start, Date end, int from, int size) {
        Pageable pageable = new OffsetBasedPage(from, size);

        return repository.search(
                start,
                end,
                filter.getChipper(),
                filter.getChippingLocation(),
                filter.getLifeStatus(),
                filter.getGender(),
                pageable
        );
    }

    @Override
    public AnimalResponseDto update(long id, AnimalPutRequestDto animalPutRequestDto) {
        Account chipper = accountService.readEntity(animalPutRequestDto.getChipperId());
        Location chippingLocation = locationService.readEntity(animalPutRequestDto.getChippingLocationId());
        Animal animalRequest = mapper.toEntity(animalPutRequestDto,
                chipper, chippingLocation);

        Animal animalResponse = updateEntity(id, animalRequest);

        return mapper.toDto(animalResponse);
    }

    @Override
    public Animal updateEntity(long id, Animal animal) {
        Animal existedAnimal = readEntity(id);

        // Animal has died since the last request
        if ((existedAnimal.getLifeStatus().equals(AnimalLifeStatus.ALIVE)) &&
                (animal.getLifeStatus().equals(AnimalLifeStatus.DEAD))) {
            animal.setDeathDateTime(Date.from(Instant.now()));
        }

        if (!existedAnimal.getVisitedLocations().isEmpty()) {
            Location newChipherLocation = animal.getChippingLocation();
            Location firstVisitedLocation = existedAnimal.getVisitedLocations().get(0).getLocation();
            if (newChipherLocation.equals(firstVisitedLocation)) {
                throw new BadRequestException("ans-03", "Chipping location coincides with the first visited location", String.valueOf(firstVisitedLocation.getId()));
            }
        }
        animal.setId(id);
        animal.setAnimalTypes(existedAnimal.getAnimalTypes());

        try {
            return repository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ans-04", "id", String.valueOf(id));
        }
    }

    @Override
    public void delete(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new NotFoundException("aps-05", "id", String.valueOf(id));
        }
    }

}
