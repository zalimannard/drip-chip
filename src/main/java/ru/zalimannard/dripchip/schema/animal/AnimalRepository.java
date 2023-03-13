package ru.zalimannard.dripchip.schema.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.location.Location;

import java.sql.Timestamp;
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
