package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long> {

}
