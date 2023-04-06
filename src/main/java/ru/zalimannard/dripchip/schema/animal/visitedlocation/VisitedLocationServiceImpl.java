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
import ru.zalimannard.dripchip.schema.animal.visitedlocation.update.VisitedLocationUpdateDto;
import ru.zalimannard.dripchip.schema.location.Location;
import ru.zalimannard.dripchip.schema.location.LocationService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitedLocationServiceImpl implements VisitedLocationService {

    private final VisitedLocationMapper visitedLocationMapper;
    private final VisitedLocationRepository visitedLocationRepository;

    private final AnimalService animalService;
    private final LocationService locationService;

    @Override
    public VisitedLocationDto create(long animalId, long locationId) {
        VisitedLocation visitedLocationResponse = createEntity(animalId, locationId);

        return visitedLocationMapper.toDto(visitedLocationResponse);
    }

    @Override
    public VisitedLocation createEntity(long animalId, long locationId) {
        Animal animal = animalService.readEntity(animalId);
        Location location = locationService.readEntity(locationId);
        VisitedLocation newVisitedLocation = new VisitedLocation();
        newVisitedLocation.setAnimal(animal);
        newVisitedLocation.setLocation(location);
        newVisitedLocation.setDateTimeOfVisitLocationPoint(Date.from(Instant.now()));

        if (animal.getLifeStatus().equals(AnimalLifeStatus.DEAD)) {
            throw new BadRequestException();
        }
        ArrayList<VisitedLocation> allAnimalVisitedLocations = getAllVisitedLocation(animal);
        allAnimalVisitedLocations.add(newVisitedLocation);
        checkCorrectnessOrder(allAnimalVisitedLocations);

        return saveToDatabase(newVisitedLocation);
    }

    @Override
    public VisitedLocationDto read(long id) {
        VisitedLocation visitedLocationResponse = readEntity(id);

        return visitedLocationMapper.toDto(visitedLocationResponse);
    }

    @Override
    public VisitedLocation readEntity(long id) {
        return visitedLocationRepository.findById(id).
                orElseThrow(NotFoundException::new);
    }

    @Override
    public List<VisitedLocationDto> search(long animalId, Date start, Date end, int from, int size) {
        Animal animal = animalService.readEntity(animalId);
        VisitedLocation filter = new VisitedLocation();
        filter.setAnimal(animal);

        List<VisitedLocation> visitedLocations = searchEntities(filter, start, end, from, size);

        return visitedLocationMapper.toDtoList(visitedLocations);
    }

    @Override
    public List<VisitedLocation> searchEntities(VisitedLocation filter, Date start, Date end, int from, int size) {
        Pageable pageable = new OffsetBasedPage(from, size);

        return visitedLocationRepository.search(
                start,
                end,
                filter.getAnimal(),
                pageable
        );
    }

    @Override
    public VisitedLocationDto update(long animalId, VisitedLocationUpdateDto visitedLocationUpdateDto) {
        VisitedLocation visitedLocationResponse = updateEntity(animalId,
                visitedLocationUpdateDto.getVisitedLocationPointId(), visitedLocationUpdateDto.getLocationPointId());

        return visitedLocationMapper.toDto(visitedLocationResponse);
    }

    @Override
    public VisitedLocation updateEntity(long animalId, long visitedLocationPointId, long locationPointId) {
        Animal animal = animalService.readEntity(animalId);
        VisitedLocation visitedLocation = readEntity(visitedLocationPointId);
        Location location = locationService.readEntity(locationPointId);
        ArrayList<VisitedLocation> allAnimalVisitedLocations = getAllVisitedLocation(animal);

        // Replacing the location with the same one
        if (visitedLocation.getLocation().equals(location)) {
            throw new BadRequestException();
        }
        checkHasAnimalVisitedLocation(animal, visitedLocation);
        int indexOfVisitedLocationForUpdate = allAnimalVisitedLocations.indexOf(visitedLocation);
        allAnimalVisitedLocations.get(indexOfVisitedLocationForUpdate).setLocation(location);

        checkCorrectnessOrder(allAnimalVisitedLocations);
        visitedLocation.setLocation(location);

        return saveToDatabase(visitedLocation);
    }

    @Override
    public void delete(long animalId, long visitedLocationId) {
        Animal animal = animalService.readEntity(animalId);
        VisitedLocation visitedLocation = readEntity(visitedLocationId);

        checkHasAnimalVisitedLocation(animal, visitedLocation);
        ArrayList<VisitedLocation> allAnimalVisitedLocations = getAllVisitedLocation(animal);
        int indexOfVisitedLocationToDelete = allAnimalVisitedLocations.indexOf(visitedLocation);
        allAnimalVisitedLocations.remove(indexOfVisitedLocationToDelete);

        // If the chip point coincides with the first, delete first
        if ((indexOfVisitedLocationToDelete == 1) && (allAnimalVisitedLocations.size() > 1)) {
            Location chippingLocation = animal.getChippingLocation();
            Location firstVisitedLocation = allAnimalVisitedLocations.get(1).getLocation();
            if (chippingLocation.equals(firstVisitedLocation)) {
                visitedLocationRepository.deleteById(allAnimalVisitedLocations.get(1).getId());
                allAnimalVisitedLocations.remove(1);
            }
        }
        checkCorrectnessOrder(allAnimalVisitedLocations);

        visitedLocationRepository.deleteById(visitedLocationId);
    }

    private VisitedLocation saveToDatabase(VisitedLocation visitedLocation) {
        try {
            return visitedLocationRepository.save(visitedLocation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

    private ArrayList<VisitedLocation> getAllVisitedLocation(Animal animal) {
        VisitedLocation chippingVisitedLocation = new VisitedLocation();
        chippingVisitedLocation.setLocation(animal.getChippingLocation());

        ArrayList<VisitedLocation> visitedLocations = new ArrayList<>(List.of(chippingVisitedLocation));
        visitedLocations.addAll(visitedLocationRepository.findAllByAnimalIdOrderByDateTimeOfVisitLocationPoint(animal.getId()));
        return visitedLocations;
    }

    private void checkCorrectnessOrder(List<VisitedLocation> visitedLocations) {
        if (!isCorrectOrder(visitedLocations)) {
            throw new BadRequestException();
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
            throw new NotFoundException();
        }
    }

}
