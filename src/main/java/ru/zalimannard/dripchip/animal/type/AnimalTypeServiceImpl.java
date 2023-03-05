package ru.zalimannard.dripchip.animal.type;

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
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;
    private final AnimalTypeMapper animalTypeMapper = Mappers.getMapper(AnimalTypeMapper.class);

    @Override
    public AnimalTypeDto create(@Valid AnimalTypeDto animalTypeDto) {
        AnimalType animalTypeRequest = animalTypeMapper.toEntity(animalTypeDto);
        try {
            AnimalType animalTypeResponse = animalTypeRepository.save(animalTypeRequest);
            return animalTypeMapper.toDto(animalTypeResponse);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Conflict in adding to the database");
        }
    }

    @Override
    public AnimalTypeDto read(long id) {
        AnimalType animalType = animalTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("AnimalType", "id", String.valueOf(id)));
        return animalTypeMapper.toDto(animalType);
    }

    @Override
    public void delete(long id) {
        if (animalTypeRepository.existsById(id)) {
            animalTypeRepository.deleteById(id);
        } else {
            throw new NotFoundException("AnimalType", "id", String.valueOf(id));
        }
    }

}
