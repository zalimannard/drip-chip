package ru.zalimannard.dripchip.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zalimannard.dripchip.account.Account;
import ru.zalimannard.dripchip.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.location.Location;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByChippingDateTimeBetweenAndChipperBetweenAndChippingLocationBetweenAndLifeStatusLikeAndGenderLikeOrderById(
            Timestamp start,
            Timestamp end,
            Account chipperStart,
            Account chipperEnd,
            Location chippingLocationStart,
            Location chippingLocationEnd,
            String lifeStatus,
            String gender
    );

}
