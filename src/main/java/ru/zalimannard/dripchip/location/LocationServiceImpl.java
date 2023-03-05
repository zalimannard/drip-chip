package ru.zalimannard.dripchip.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Validated
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    @Override
    public LocationDto create(@Valid LocationDto locationDto) {
        Location locationRequest = locationMapper.toEntity(locationDto);
        try {
            Location locationResponse = locationRepository.save(locationRequest);
            return locationMapper.toDto(locationResponse);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Conflict in adding to the database");
        }
    }

    @Override
    public LocationDto read(long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location", "id", String.valueOf(id)));
        return locationMapper.toDto(location);
    }

    @Override
    public void delete(long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
        } else {
            throw new NotFoundException("Location", "id", String.valueOf(id));
        }
    }

}
