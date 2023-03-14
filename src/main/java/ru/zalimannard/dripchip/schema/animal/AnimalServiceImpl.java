package ru.zalimannard.dripchip.schema.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocation;
import ru.zalimannard.dripchip.schema.location.Location;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    @Override
    public AnimalDto create(@Valid AnimalDto animalDto) {
        Animal animalRequest = animalMapper.toEntity(animalDto);
        if (animalRequest.getAnimalTypes().isEmpty()) {
            throw new BadRequestException("Animal types should not be empty");
        }
        animalRequest.setId(null);
        animalRequest.setLifeStatus(AnimalLifeStatus.ALIVE);
        animalRequest.setChippingDateTime(Date.from(Instant.now()));

        Animal animalResponse = saveToDatabase(animalRequest);
        return animalMapper.toDto(animalResponse);
    }

    @Override
    public AnimalDto read(@Positive long id) {
        Animal animal = readEntity(id);
        return animalMapper.toDto(animal);
    }

    private Animal readEntity(long id) {
        checkExist(id);
        return animalRepository.findById(id).get();
    }

    @Override
    public List<AnimalDto> search(AnimalDto filterDto, Date start, Date end, @PositiveOrZero int from,
                                  @Positive int size) {
        Animal filter = animalMapper.toEntity(filterDto);
        Pageable pageable = new OffsetBasedPage(from, size);
        List<Animal> animalList = animalRepository.search(start, end, filter.getChipper(),
                filter.getChippingLocation(), filter.getLifeStatus(), filter.getGender(), pageable);

        return animalMapper.toDtoList(animalList);
    }

    @Override
    public AnimalDto update(@Positive long id, @Valid AnimalDto animalDto) {
        AnimalDto animalFromDatabaseDto = read(id);
        Animal animalFromDatabase = animalMapper.toEntity(animalFromDatabaseDto);
        Animal animalRequest = animalMapper.toEntity(animalDto);

        if ((animalFromDatabase.getLifeStatus().equals(AnimalLifeStatus.ALIVE)) &&
                (animalRequest.getLifeStatus().equals(AnimalLifeStatus.DEAD))) {
            animalFromDatabase.setDeathDateTime(Date.from(Instant.now()));
        }

        animalFromDatabase.setWeight(animalRequest.getWeight());
        animalFromDatabase.setLength(animalRequest.getLength());
        animalFromDatabase.setHeight(animalRequest.getHeight());
        animalFromDatabase.setGender(animalRequest.getGender());
        animalFromDatabase.setLifeStatus(animalRequest.getLifeStatus());
        animalFromDatabase.setChipper(animalRequest.getChipper());
        animalFromDatabase.setChippingLocation(animalRequest.getChippingLocation());

        List<VisitedLocation> visitedLocations = readEntity(id).getVisitedLocations();
        if (visitedLocations != null) {
            if (visitedLocations.size() > 0) {
                Location newChipherLocation = animalFromDatabase.getChippingLocation();
                Location firstVisitedLocation = visitedLocations.get(0).getLocation();
                if (newChipherLocation.equals(firstVisitedLocation)) {
                    throw new BadRequestException("The first visited location should not coincide with the chipping " +
                            "location");
                }
            }
        }

        Animal accountResponse = saveToDatabase(animalRequest);
        return animalMapper.toDto(accountResponse);
    }

    @Override
    public void delete(@Positive long id) {
        checkExist(id);
        try {
            animalRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("It is impossible to delete animal");
        }
    }

    private void checkExist(long id) {
        if (!animalRepository.existsById(id)) {
            throw new NotFoundException("Animal", String.valueOf(id));
        }
    }

    private Animal saveToDatabase(Animal animal) {
        try {
            return animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Animal");
        }
    }

}
