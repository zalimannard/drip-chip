package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;
    private final AnimalTypeMapper animalTypeMapper;

    @Override
    public AnimalTypeDto create(AnimalTypeDto animalTypeDto) {
        AnimalType animalTypeRequest = animalTypeMapper.toEntity(animalTypeDto);

        AnimalType animalTypeResponse = saveToDatabase(animalTypeRequest);
        return animalTypeMapper.toDto(animalTypeResponse);
    }

    @Override
    public AnimalTypeDto read(long id) {
        checkExist(id);
        AnimalType animalType = animalTypeRepository.findById(id).get();
        return animalTypeMapper.toDto(animalType);
    }

    @Override
    public List<AnimalTypeDto> getAllById(Set<Long> ids) {
        List<AnimalType> animalTypes = animalTypeRepository.findAllById(ids);
        if (animalTypes.size() != ids.size()) {
            throw new NotFoundException("Animal type", "with some of the ids");
        }
        return animalTypeMapper.toDtoList(animalTypes);
    }

    @Override
    public AnimalTypeDto update(long id, AnimalTypeDto animalTypeDto) {
        checkExist(id);
        AnimalType animalTypeRequest = animalTypeMapper.toEntity(animalTypeDto);
        animalTypeRequest.setId(id);

        AnimalType animalTypeResponse = saveToDatabase(animalTypeRequest);
        return animalTypeMapper.toDto(animalTypeResponse);
    }

    @Override
    public void delete(long id) {
        checkExist(id);
        try {
            animalTypeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("It is impossible to delete Animal type with id=" + id);
        }
    }

    private void checkExist(long id) {
        if (!animalTypeRepository.existsById(id)) {
            throw new NotFoundException("Animal type", String.valueOf(id));
        }
    }

    private AnimalType saveToDatabase(AnimalType animalType) {
        try {
            return animalTypeRepository.save(animalType);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Animal type");
        }
    }

}
