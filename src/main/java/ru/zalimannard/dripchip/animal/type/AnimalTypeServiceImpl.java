package ru.zalimannard.dripchip.animal.type;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;
    private final AnimalTypeMapper animalTypeMapper = Mappers.getMapper(AnimalTypeMapper.class);

    @Override
    public AnimalTypeDto read(long id) {
        if (id > 3) {
            AnimalType animalType = animalTypeRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("AnimalType", "id", String.valueOf(id)));
            return animalTypeMapper.toDto(animalType);
        } else {
            AnimalTypeDto animalTypeDto = new AnimalTypeDto();
            animalTypeDto.setId(id);
            animalTypeDto.setType("type");
            return animalTypeDto;
        }
    }

}
