package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.AnimalService;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationRequestUpdateDto;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.dto.VisitedLocationResponseDto;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitedLocationServiceImpl implements VisitedLocationService {

    private final VisitedLocationMapper mapper;
    private final VisitedLocationRepository repository;

    private final AnimalService animalService;
    private final LocationService locationService;

    @Override
    public VisitedLocationResponseDto create(Long animalId, Long locationId) {
        VisitedLocation visitedLocationResponse = createEntity(animalId, locationId);

        return mapper.toDto(visitedLocationResponse);
    }

    @Override
    public VisitedLocation createEntity(Long animalId, Long locationId) {
        Animal animal = animalService.readEntity(animalId);
        Location location = locationService.readEntity(locationId);
        VisitedLocation newVisitedLocation = new VisitedLocation();
        newVisitedLocation.setAnimal(animal);
        newVisitedLocation.setLocation(location);
        newVisitedLocation.setDateTimeOfVisitLocationPoint(Date.from(Instant.now()));

        if (animal.getLifeStatus().equals(AnimalLifeStatus.DEAD)) {
            throw new BadRequestException("vls-01", "lifeStatus", AnimalLifeStatus.DEAD.toString());
        }
        ArrayList<VisitedLocation> allAnimalVisitedLocations = getAllVisitedLocation(animal);
        allAnimalVisitedLocations.add(newVisitedLocation);
        checkCorrectnessOrder(allAnimalVisitedLocations);

        try {
            return repository.save(newVisitedLocation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("vls-02", "visitedLocation", null);
        }
    }

    @Override
    public VisitedLocationResponseDto read(Long id) {
        VisitedLocation visitedLocationResponse = readEntity(id);
        return mapper.toDto(visitedLocationResponse);
    }

    @Override
    public VisitedLocation readEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("vls-03", "id", String.valueOf(id)));
    }

    @Override
    public List<VisitedLocationResponseDto> search(Long animalId, Date start, Date end, int from, int size) {
        List<VisitedLocation> visitedLocations = searchEntities(animalId, start, end, from, size);
        return mapper.toDtoList(visitedLocations);
    }

    @Override
    public List<VisitedLocation> searchEntities(Long animalId, Date start, Date end, int from, int size) {
        Animal animal = animalService.readEntity(animalId);
        Pageable pageable = new OffsetBasedPage(from, size);
        return repository.search(
                start,
                end,
                animal,
                pageable
        );
    }

    @Override
    public VisitedLocationResponseDto update(Long animalId, VisitedLocationRequestUpdateDto visitedLocationRequestUpdateDto) {
        VisitedLocation visitedLocationResponse = updateEntity(animalId,
                visitedLocationRequestUpdateDto.getVisitedLocationPointId(), visitedLocationRequestUpdateDto.getLocationPointId());

        return mapper.toDto(visitedLocationResponse);
    }

    @Override
    public VisitedLocation updateEntity(Long animalId, Long visitedLocationPointId, Long locationPointId) {
        Animal animal = animalService.readEntity(animalId);
        VisitedLocation visitedLocation = readEntity(visitedLocationPointId);
        Location location = locationService.readEntity(locationPointId);
        ArrayList<VisitedLocation> allAnimalVisitedLocations = getAllVisitedLocation(animal);

        // Replacing the location with the same one
        if (visitedLocation.getLocation().equals(location)) {
            throw new BadRequestException("vls-04", "visitedLocation", "Замена на такую же точку");
        }
        checkHasAnimalVisitedLocation(animal, visitedLocation);
        int indexOfVisitedLocationForUpdate = allAnimalVisitedLocations.indexOf(visitedLocation);
        allAnimalVisitedLocations.get(indexOfVisitedLocationForUpdate).setLocation(location);

        checkCorrectnessOrder(allAnimalVisitedLocations);
        visitedLocation.setLocation(location);

        try {
            return repository.save(visitedLocation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("vls-05", "visitedLocation", null);
        }
    }

    @Override
    public void delete(Long animalId, Long visitedLocationId) {
        Animal animal = animalService.readEntity(animalId);
        VisitedLocation visitedLocation = readEntity(visitedLocationId);

        checkHasAnimalVisitedLocation(animal, visitedLocation);
        ArrayList<VisitedLocation> allAnimalVisitedLocations = getAllVisitedLocation(animal);
        int indexOfVisitedLocationToDelete = allAnimalVisitedLocations.indexOf(visitedLocation);
        allAnimalVisitedLocations.remove(indexOfVisitedLocationToDelete);

        // Если точка чипирования совпадает с первой посещённой - удалить первую посещённую
        if ((indexOfVisitedLocationToDelete == 1) && (allAnimalVisitedLocations.size() > 1)) {
            Location chippingLocation = animal.getChippingLocation();
            Location firstVisitedLocation = allAnimalVisitedLocations.get(1).getLocation();
            if (chippingLocation.equals(firstVisitedLocation)) {
                repository.deleteById(allAnimalVisitedLocations.get(1).getId());
                allAnimalVisitedLocations.remove(1);
            }
        }
        checkCorrectnessOrder(allAnimalVisitedLocations);

        repository.deleteById(visitedLocationId);
    }

    private ArrayList<VisitedLocation> getAllVisitedLocation(Animal animal) {
        VisitedLocation chippingVisitedLocation = new VisitedLocation();
        chippingVisitedLocation.setLocation(animal.getChippingLocation());

        ArrayList<VisitedLocation> visitedLocations = new ArrayList<>(List.of(chippingVisitedLocation));
        visitedLocations.addAll(repository.findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(animal.getId()));
        return visitedLocations;
    }

    private void checkCorrectnessOrder(List<VisitedLocation> visitedLocations) {
        if (!isCorrectOrder(visitedLocations)) {
            throw new BadRequestException("vls-06", "visitedLocationOrder", null);
        }
    }

    private boolean isCorrectOrder(List<VisitedLocation> visitedLocations) {
        for (int i = 0; i < visitedLocations.size() - 1; ++i) {
            Location prevLocation = visitedLocations.get(i).getLocation();
            Location nextLocation = visitedLocations.get(i + 1).getLocation();
            if (prevLocation.equals(nextLocation)) {
                return false;
            }
        }
        return true;
    }

    private void checkHasAnimalVisitedLocation(Animal animal, VisitedLocation visitedLocation) {
        List<VisitedLocation> visitedLocations = animal.getVisitedLocations();
        if (!visitedLocations.contains(visitedLocation)) {
            throw new NotFoundException("vls-07", "visitedLocation", "Животное не имеет такой точки: " + visitedLocation.getId());
        }
    }

}
