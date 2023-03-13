package ru.zalimannard.dripchip.schema.animal.ownedtype;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.animal.*;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeMapper;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeService;
import ru.zalimannard.dripchip.schema.animal.ownedtype.update.AnimalOwnedTypeUpdateDto;

@Service
@RequiredArgsConstructor
public class AnimalOwnedTypeServiceImpl implements AnimalOwnedTypeService {

    private final AnimalService animalService;
    private final AnimalMapper animalMapper;
    private final AnimalRepository animalRepository;
    private final AnimalTypeService animalTypeService;
    private final AnimalTypeMapper animalTypeMapper;

    @Override
    public AnimalDto create(long animalId, long typeId) {
        AnimalDto animalDto = animalService.read(animalId);
        Animal animal = animalMapper.toEntity(animalDto);

        AnimalTypeDto animalTypeDto = animalTypeService.read(typeId);
        AnimalType animalType = animalTypeMapper.toEntity(animalTypeDto);

        animal.getAnimalTypes().add(animalType);
        Animal animalResponse = saveToDatabase(animal);

        return animalMapper.toDto(animalResponse);
    }

    @Override
    public AnimalDto update(long animalId, AnimalOwnedTypeUpdateDto animalOwnedTypeUpdateDto) {
        AnimalDto animalDto = animalService.read(animalId);
        Animal animal = animalMapper.toEntity(animalDto);

        long oldTypeId = animalOwnedTypeUpdateDto.getOldTypeId();
        checkAnimalHaveType(animal, oldTypeId);

        long newTypeId = animalOwnedTypeUpdateDto.getOldTypeId();
        AnimalTypeDto animalTypeDto = animalTypeService.read(newTypeId);
        AnimalType animalType = animalTypeMapper.toEntity(animalTypeDto);

        return new AnimalDto();
    }

    @Override
    public void delete(long animalId, long typeId) {
        AnimalDto animalDto = animalService.read(animalId);
        Animal animal = animalMapper.toEntity(animalDto);

        checkAnimalHaveType(animal, typeId);
        if (animal.getAnimalTypes().size() == 1) {
            throw new BadRequestException("The animal must have at least one type");
        }

        AnimalTypeDto animalTypeDto = animalTypeService.read(typeId);
        AnimalType animalType = animalTypeMapper.toEntity(animalTypeDto);

        animal.getAnimalTypes().remove(animalType);
        saveToDatabase(animal);
    }

    private void checkAnimalHaveType(Animal animal, long typeId) {
        AnimalType animalType = new AnimalType();
        animalType.setId(typeId);
        if (!animal.getAnimalTypes().contains(animalType)) {
            throw new NotFoundException("Animal type", typeId + " at animal with id=" + animal.getId());
        }
    }

    private Animal saveToDatabase(Animal animal) {
        try {
            return animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Animal");
        }
    }

}
