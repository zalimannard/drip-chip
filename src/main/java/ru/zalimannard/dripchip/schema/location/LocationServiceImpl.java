package ru.zalimannard.dripchip.schema.location;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationDto create(LocationDto locationDto) {
        Location locationRequest = locationMapper.toEntity(locationDto);

        Location locationResponse = saveToDatabase(locationRequest);
        return locationMapper.toDto(locationResponse);
    }

    @Override
    public LocationDto read(long id) {
        checkExist(id);
        Location location = locationRepository.findById(id).get();
        return locationMapper.toDto(location);
    }

    @Override
    public LocationDto update(long id, LocationDto locationDto) {
        checkExist(id);
        Location locationRequest = locationMapper.toEntity(locationDto);
        locationRequest.setId(id);

        Location locationResponse = saveToDatabase(locationRequest);
        return locationMapper.toDto(locationResponse);
    }

    @Override
    public void delete(long id) {
        checkExist(id);
        try {
            locationRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("It is impossible to delete Location with id=" + id);
        }
    }

    private void checkExist(long id) {
        if (!locationRepository.existsById(id)) {
            throw new NotFoundException("Location", String.valueOf(id));
        }
    }

    private Location saveToDatabase(Location location) {
        try {
            return locationRepository.save(location);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Location");
        }
    }

}
