package ru.zalimannard.dripchip.animal;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.zalimannard.dripchip.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.exception.NotFoundException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);

    @Override
    public AnimalDto read(long id) {
        if (id > 3) {
            Animal animal = animalRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Animal", "id", String.valueOf(id)));
            return animalMapper.toDto(animal);
        } else {
            AnimalDto animalDto = new AnimalDto();
            animalDto.setId(id);
            animalDto.setAnimalTypes(new ArrayList<>(List.of(1L)));
            animalDto.setWeight(1.0F);
            animalDto.setLength(1.0F);
            animalDto.setHeight(1.0F);
            animalDto.setGender(AnimalGender.MALE);
            animalDto.setLifeStatus(AnimalLifeStatus.ALIVE);
            animalDto.setChippingDateTime(new Timestamp(0));
            animalDto.setChipperId(1);
            animalDto.setChippingLocationId(1L);
            animalDto.setVisitedLocations(new ArrayList<>(List.of(1L)));
            animalDto.setDeathDateTime(null);
            return animalDto;
        }
    }

    @Override
    public List<AnimalDto> search(AnimalDto filter, int from, int size) {
        return new ArrayList<>();
    }

}
