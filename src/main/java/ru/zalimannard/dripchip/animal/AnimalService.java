package ru.zalimannard.dripchip.animal;

import java.util.List;

public interface AnimalService {

    AnimalDto read(long id);

    List<AnimalDto> search(AnimalDto filter, int from, int size);

}
