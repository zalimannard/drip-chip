package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeRequestDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.dto.AnimalTypeResponseDto;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalTypeMapper animalTypeMapper;
    private final AnimalTypeRepository animalTypeRepository;

    @Override
    public AnimalTypeResponseDto create(AnimalTypeRequestDto animalTypeRequestDto) {
        AnimalType animalTypeRequest = animalTypeMapper.toEntity(animalTypeRequestDto);

        AnimalType animalTypeResponse = createEntity(animalTypeRequest);

        return animalTypeMapper.toDto(animalTypeResponse);
    }

    @Override
    public AnimalType createEntity(AnimalType animalType) {
        return saveToDatabase(animalType);
    }

    @Override
    public AnimalTypeResponseDto read(long id) {
        AnimalType animalTypeResponse = readEntity(id);

        return animalTypeMapper.toDto(animalTypeResponse);
    }

    @Override
    public AnimalType readEntity(long id) {
        return animalTypeRepository.findById(id).
                orElseThrow();
    }

    @Override
    public List<AnimalType> readAllEntitiesById(Set<@Positive Long> ids) {
        List<AnimalType> animalTypes = animalTypeRepository.findAllById(ids);

        if (ids.size() != animalTypes.size()) {
            throw new NotFoundException("", "", "");
        }

        return animalTypes;
    }

    @Override
    public AnimalTypeResponseDto update(long id, AnimalTypeRequestDto animalTypeRequestDto) {
        AnimalType animalTypeRequest = animalTypeMapper.toEntity(animalTypeRequestDto);

        AnimalType animalTypeResponse = updateEntity(id, animalTypeRequest);

        return animalTypeMapper.toDto(animalTypeResponse);
    }

    @Override
    public AnimalType updateEntity(long id, AnimalType animalType) {
        if (animalTypeRepository.existsById(id)) {
            animalType.setId(id);
            return saveToDatabase(animalType);
        } else {
            throw new NotFoundException("", "", "");
        }
    }

    @Override
    public void delete(long id) {
        try {
            AnimalType animalType = readEntity(id);
            animalTypeRepository.delete(animalType);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("", "", "");
        }
    }

    private AnimalType saveToDatabase(AnimalType animalType) {
        try {
            return animalTypeRepository.save(animalType);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("", "", "");
        }
    }

}
