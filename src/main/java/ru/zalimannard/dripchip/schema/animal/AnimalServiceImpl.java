package ru.zalimannard.dripchip.schema.animal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.zalimannard.dripchip.exception.BadRequestException;
import ru.zalimannard.dripchip.exception.ConflictException;
import ru.zalimannard.dripchip.exception.NotFoundException;
import ru.zalimannard.dripchip.page.OffsetBasedPage;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    @Override
    public AnimalDto create(@Valid AnimalDto animalDto) {
        Animal animalRequest = animalMapper.toEntity(animalDto);
        animalRequest.setId(null);
        animalRequest.setLifeStatus(AnimalLifeStatus.ALIVE);
        animalRequest.setChippingDateTime(Date.from(Instant.now()));

        Animal animalResponse = saveToDatabase(animalRequest);
        return animalMapper.toDto(animalResponse);
    }

    @Override
    public AnimalDto read(@Positive long id) {
        checkExist(id);
        Animal animal = animalRepository.findById(id).get();
        return animalMapper.toDto(animal);
    }

    @Override
    public List<AnimalDto> search(AnimalDto filterDto, Date start, Date end, @PositiveOrZero int from,
                                  @Positive int size) {
        Animal filter = animalMapper.toEntity(filterDto);
        Pageable pageable = new OffsetBasedPage(from, size);
        List<Animal> animalList = animalRepository.search(start, end, filter.getChipper(),
                filter.getChippingLocation(), filter.getLifeStatus(), filter.getGender(), pageable);

        return animalMapper.toDtoList(animalList);
    }

    @Override
    public AnimalDto update(@Positive long id, @Valid AnimalDto animalDto) {
        checkExist(id);
        Animal animalRequest = animalMapper.toEntity(animalDto);
        animalRequest.setId(id);

        Animal accountResponse = saveToDatabase(animalRequest);
        return animalMapper.toDto(accountResponse);
    }

    @Override
    public void delete(@Positive long id) {
        checkExist(id);
        try {
            animalRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("It is impossible to delete animal");
        }
    }

    private void checkExist(long id) {
        if (!animalRepository.existsById(id)) {
            throw new NotFoundException("Animal", String.valueOf(id));
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
