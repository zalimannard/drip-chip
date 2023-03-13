package ru.zalimannard.dripchip.schema.animal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.Date;
import java.util.List;


@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Query("""
            SELECT
                a
            FROM
                Animal a
            WHERE
                ((cast(:start AS DATE) IS NULL ) OR a.chippingDateTime >= :start)
            AND
                ((cast(:end AS DATE) IS NULL ) OR a.chippingDateTime <= :end)
            AND
                (:chipper IS NULL OR a.chipper = :chipper)
            AND
                (:chippingLocation IS NULL OR a.chippingLocation = :chippingLocation)
            AND
                (:lifeStatus IS NULL OR a.lifeStatus = :lifeStatus)
            AND
                (:gender IS NULL OR a.gender = :gender)
            ORDER BY
                a.id
            """)
    List<Animal> search(@Param("start") Date start,
                        @Param("end") Date end,
                        @Param("chipper") Account chipper,
                        @Param("chippingLocation") Location chippingLocation,
                        @Param("lifeStatus") AnimalLifeStatus lifeStatus,
                        @Param("gender") AnimalGender gender,
                        Pageable pageable);

}
