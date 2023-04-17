package ru.zalimannard.dripchip.schema.animal.ownedtype;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.animal.AnimalMapper;
import ru.zalimannard.dripchip.schema.animal.AnimalRepository;
import ru.zalimannard.dripchip.schema.animal.AnimalService;
import ru.zalimannard.dripchip.schema.animal.dto.AnimalResponseDto;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalTypeService;
import ru.zalimannard.dripchip.schema.animal.ownedtype.update.AnimalOwnedTypeUpdateDto;

@Service
@RequiredArgsConstructor
public class AnimalOwnedTypeServiceImpl implements AnimalOwnedTypeService {

    private final AnimalService animalService;
    private final AnimalMapper animalMapper;
    private final AnimalRepository animalRepository;
    private final AnimalTypeService animalTypeService;

    @Override
    public AnimalResponseDto create(long animalId, long typeId) {
        Animal animalResponse = createEntity(animalId, typeId);

        return animalMapper.toDto(animalResponse);
    }

    @Override
    public Animal createEntity(long animalId, long typeId) {
        Animal animal = animalService.readEntity(animalId);
        AnimalType animalType = animalTypeService.readEntity(typeId);

        animal.addType(animalType);

        return saveToDatabase(animal);
    }

    @Override
    public AnimalResponseDto update(long animalId, AnimalOwnedTypeUpdateDto animalOwnedTypeUpdateDto) {
        Animal animalResponse = updateEntity(animalId,
                animalOwnedTypeUpdateDto.getOldTypeId(), animalOwnedTypeUpdateDto.getNewTypeId());

        return animalMapper.toDto(animalResponse);
    }

    @Override
    public Animal updateEntity(long animalId, long oldTypeId, long newTypeId) {
        Animal animal = animalService.readEntity(animalId);
        AnimalType oldType = animalTypeService.readEntity(oldTypeId);
        AnimalType newType = animalTypeService.readEntity(newTypeId);

        if (!haveType(animal, oldType)) {
            throw new NotFoundException("", "", "");
        }
        if (haveType(animal, newType)) {
            throw new ConflictException("", "", "");
        }
        animal.removeType(oldType);
        animal.addType(newType);

        return saveToDatabase(animal);
    }

    @Override
    public void delete(long animalId, long typeId) {
        Animal animal = animalService.readEntity(animalId);
        AnimalType animalType = animalTypeService.readEntity(typeId);

        if (!haveType(animal, animalType)) {
            throw new NotFoundException("", "", "");
        }
        if (animal.getAnimalTypes().size() == 1) {
            throw new BadRequestException("", "", "");
        }
        animal.removeType(animalType);
        saveToDatabase(animal);
    }

    private Animal saveToDatabase(Animal animal) {
        try {
            return animalRepository.save(animal);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("", "", "");
        }
    }

    private boolean haveType(Animal animal, AnimalType animalType) {
        return animal.getAnimalTypes().contains(animalType);
    }

}
