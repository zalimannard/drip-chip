package ru.zalimannard.dripchip.schema.animal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.account.AccountService;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPostRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalPutRequestDto;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
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
    public AnimalResponseDto read(Long id) {
        Animal animalResponse = readEntity(id);

        return mapper.toDto(animalResponse);
    }

    @Override
    public Animal readEntity(Long id) {
        Optional<Animal> responseOptional = repository.findById(id);
        if (responseOptional.isPresent()) {
            return responseOptional.get();
        } else {
            throw new NotFoundException("ans-02", "id", String.valueOf(id));
        }
    }

    @Override
    public List<AnimalResponseDto> search(Date startDateTime, Date endDateTime, Integer chipperId, Long chippingLocationId, String lifeStatus, String gender, int from, int size) {
        List<Animal> animals = searchEntities(startDateTime, endDateTime, chipperId, chippingLocationId, lifeStatus, gender, from, size);
        return mapper.toDtoList(animals);
    }

    @Override
    public List<Animal> searchEntities(Date start, Date end, Integer chipperId, Long chippingLocationId, String lifeStatus, String gender, int from, int size) {
        Pageable pageable = new OffsetBasedPage(from, size);
        Account chipper = null;
        Location chippingLocation = null;
        AnimalLifeStatus lifeStatusEnum = null;
        AnimalGender genderEnum = null;
        if (chipperId != null) {
            chipper = accountService.readEntity(chipperId);
        }
        if (chippingLocationId != null) {
            chippingLocation = locationService.readEntity(chippingLocationId);
        }
        if (lifeStatus != null) {
            try {
                lifeStatusEnum = AnimalLifeStatus.valueOf(lifeStatus);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("ans-07", "lifeStatus", lifeStatus);
            }
        }
        if (gender != null) {
            try {
                genderEnum = AnimalGender.valueOf(gender);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("ans-08", "gender", gender);
            }
        }
        return repository.search(
                start,
                end,
                chipper,
                chippingLocation,
                lifeStatusEnum,
                genderEnum,
                pageable
        );
    }

    @Override
    public AnimalResponseDto update(Long id, AnimalPutRequestDto animalPutRequestDto) {
        Account chipper = accountService.readEntity(animalPutRequestDto.getChipperId());
        Location chippingLocation = locationService.readEntity(animalPutRequestDto.getChippingLocationId());
        Animal animalRequest = mapper.toEntity(animalPutRequestDto,
                chipper, chippingLocation);

        Animal animalResponse = updateEntity(id, animalRequest);

        return mapper.toDto(animalResponse);
    }

    @Override
    public Animal updateEntity(Long id, Animal animal) {
        Animal existedAnimal = readEntity(id);
        Animal animalToUpdate = animal.toBuilder()
                .id(id)
                .animalTypes(existedAnimal.getAnimalTypes())
                .build();

        // Животное не может стать живым, если было мёртвым
        if ((existedAnimal.getLifeStatus().equals(AnimalLifeStatus.DEAD)) &&
                (animalToUpdate.getLifeStatus().equals(AnimalLifeStatus.ALIVE))) {
            throw new BadRequestException("ans-06", "lifeStatus", "DEAD -> ALIVE");
        }

        // Животное было живым и стало мёртвым
        if ((existedAnimal.getLifeStatus().equals(AnimalLifeStatus.ALIVE)) &&
                (animalToUpdate.getLifeStatus().equals(AnimalLifeStatus.DEAD))) {
            animalToUpdate.setDeathDateTime(Date.from(Instant.now()));
        }

        if (!existedAnimal.getVisitedLocations().isEmpty()) {
            Location newChipherLocation = animalToUpdate.getChippingLocation();
            Location firstVisitedLocation = existedAnimal.getVisitedLocations().get(0).getLocation();
            if (newChipherLocation.equals(firstVisitedLocation)) {
                throw new BadRequestException("ans-03", "Точка чипирования не должна совпадать с первой посещённой точкой", String.valueOf(firstVisitedLocation.getId()));
            }
        }

        try {
            return repository.save(animalToUpdate);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ans-04", "id", String.valueOf(id));
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Animal animal = readEntity(id);
            repository.delete(animal);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("ans-05", "id", String.valueOf(id));
        }
    }

}
