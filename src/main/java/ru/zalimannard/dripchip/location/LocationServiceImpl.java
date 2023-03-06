package ru.zalimannard.dripchip.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
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
        try {
            Location locationRequest = locationMapper.toEntity(locationDto);
            Location locationResponse = locationRepository.save(locationRequest);
            return locationMapper.toDto(locationResponse);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Conflict in adding to the database");
        }
    }

    @Override
    public LocationDto read(@Positive long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location", "id", String.valueOf(id)));
        return locationMapper.toDto(location);
    }

    @Override
    public LocationDto update(@Positive long id, @Valid LocationDto locationDto) {
        if (locationRepository.existsById(id)) {
            try {
                Location locationRequest = locationMapper.toEntity(locationDto);
                locationRequest.setId(id);

                Location locationResponse = locationRepository.save(locationRequest);
                return locationMapper.toDto(locationResponse);
            } catch (DataIntegrityViolationException e) {
                throw new ConflictException("Conflict in adding to the database");
            }
        } else {
            throw new NotFoundException("Location", "id", String.valueOf(id));
        }
    }

    @Override
    public void delete(@Positive long id) {
        if (locationRepository.existsById(id)) {
            try {
                locationRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("It is impossible to delete Location");
            }
        } else {
            throw new NotFoundException("Location", "id", String.valueOf(id));
        }
    }

}
