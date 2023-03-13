package ru.zalimannard.dripchip.schema.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
    public LocationDto create(@Valid LocationDto locationDto) {
        Location locationRequest = locationMapper.toEntity(locationDto);
        locationRequest.setId(null);

        Location locationResponse = saveToDatabase(locationRequest);
        return locationMapper.toDto(locationResponse);
    }

    @Override
    public LocationDto read(@Positive long id) {
        checkExist(id);
        Location location = locationRepository.findById(id).get();
        return locationMapper.toDto(location);
    }

    @Override
    public LocationDto update(@Positive long id, @Valid LocationDto locationDto) {
        checkExist(id);
        Location locationRequest = locationMapper.toEntity(locationDto);
        locationRequest.setId(id);

        Location locationResponse = saveToDatabase(locationRequest);
        return locationMapper.toDto(locationResponse);
    }

    @Override
    public void delete(@Positive long id) {
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
