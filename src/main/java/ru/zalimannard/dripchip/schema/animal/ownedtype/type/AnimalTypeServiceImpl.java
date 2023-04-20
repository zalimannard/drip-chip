package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

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

    private final AnimalTypeMapper mapper;
    private final AnimalTypeRepository repository;

    @Override
    public AnimalTypeResponseDto create(AnimalTypeRequestDto animalTypeRequestDto) {
        AnimalType animalTypeRequest = mapper.toEntity(animalTypeRequestDto);
        AnimalType animalTypeResponse = createEntity(animalTypeRequest);
        return mapper.toDto(animalTypeResponse);
    }

    @Override
    public AnimalType createEntity(AnimalType animalType) {
        try {
            return repository.save(animalType);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ant-01", "animalType", e.getLocalizedMessage());
        }
    }

    @Override
    public AnimalTypeResponseDto read(Long id) {
        AnimalType animalTypeResponse = readEntity(id);
        return mapper.toDto(animalTypeResponse);
    }

    @Override
    public AnimalType readEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ant-02", "id", String.valueOf(id)));
    }

    @Override
    public List<AnimalType> readAllEntitiesById(Set<Long> ids) {
        List<AnimalType> animalTypes = repository.findAllById(ids);
        if (ids.size() != animalTypes.size()) {
            throw new NotFoundException("ant-03", "animalType", "some of the list: " + ids);
        }
        return animalTypes;
    }

    @Override
    public AnimalTypeResponseDto update(Long id, AnimalTypeRequestDto animalTypeRequestDto) {
        AnimalType animalTypeRequest = mapper.toEntity(animalTypeRequestDto);
        AnimalType animalTypeResponse = updateEntity(id, animalTypeRequest);
        return mapper.toDto(animalTypeResponse);
    }

    @Override
    public AnimalType updateEntity(Long id, AnimalType animalType) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("ant-04", "id", String.valueOf(id)));
        AnimalType animalTypeToSave = animalType.toBuilder()
                .id(id)
                .build();

        try {
            return repository.save(animalTypeToSave);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("ans-04", "id", String.valueOf(id));
        }
    }

    @Override
    public void delete(Long id) {
        try {
            AnimalType animalType = readEntity(id);
            repository.delete(animalType);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("ant-05", "id", String.valueOf(id));
        }
    }

}
