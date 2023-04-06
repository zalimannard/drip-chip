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
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.location.Location;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AnimalServiceImpl implements AnimalService {

    private final AnimalMapper animalMapper;
    private final AnimalRepository animalRepository;

    @Override
    public AnimalDto create(AnimalDto animalDto) {
        Animal animalRequest = animalMapper.toEntity(animalDto);

        Animal animalResponse = createEntity(animalRequest);

        return animalMapper.toDto(animalResponse);
    }

    @Override
    public Animal createEntity(Animal animal) {
        if (animal.getAnimalTypes().isEmpty()) {
            throw new BadRequestException();
        }
        animal.setLifeStatus(AnimalLifeStatus.ALIVE);
        animal.setChippingDateTime(Date.from(Instant.now()));

        return saveToDatabase(animal);
    }

    @Override
    public AnimalDto read(long id) {
        Animal animalResponse = readEntity(id);

        return animalMapper.toDto(animalResponse);
    }

    @Override
    public Animal readEntity(long id) {
        return animalRepository.findById(id).
                orElseThrow(NotFoundException::new);
    }

    @Override
    public List<AnimalDto> search(AnimalDto filterDto, Date start, Date end, int from, int size) {
        Animal filter = animalMapper.toEntity(filterDto);

        List<Animal> animals = searchEntities(filter, start, end, from, size);

        return animalMapper.toDtoList(animals);
    }

    @Override
    public List<Animal> searchEntities(Animal filter, Date start, Date end, int from, int size) {
        Pageable pageable = new OffsetBasedPage(from, size);

        return animalRepository.search(
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
    public AnimalDto update(long id, AnimalDto animalDto) {
        Animal animalRequest = animalMapper.toEntity(animalDto);

        Animal animalResponse = updateEntity(id, animalRequest);

        return animalMapper.toDto(animalResponse);
    }

    @Override
    public Animal updateEntity(long id, Animal animal) {
        Animal existedAnimal = readEntity(id);

        // Animal has died since the last request
        if ((existedAnimal.getLifeStatus().equals(AnimalLifeStatus.ALIVE)) &&
                (animal.getLifeStatus().equals(AnimalLifeStatus.DEAD))) {
            animal.setDeathDateTime(Date.from(Instant.now()));
        }
        // Chipping location coincides with the first visited location
        if (!existedAnimal.getVisitedLocations().isEmpty()) {
            Location newChipherLocation = animal.getChippingLocation();
            Location firstVisitedLocation = existedAnimal.getVisitedLocations().get(0).getLocation();
            if (newChipherLocation.equals(firstVisitedLocation)) {
                throw new BadRequestException();
            }
        }
        animal.setAnimalTypes(existedAnimal.getAnimalTypes());
        animal.setId(id);

        return saveToDatabase(animal);
    }

    @Override
    public void delete(long id) {
        try {
            Animal animal = readEntity(id);
            animalRepository.delete(animal);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException();
        }
    }

    private Animal saveToDatabase(Animal animal) {
        try {
            return animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

}
