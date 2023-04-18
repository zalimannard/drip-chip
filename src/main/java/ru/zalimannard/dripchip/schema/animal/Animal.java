package ru.zalimannard.dripchip.schema.animal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocation;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "animals")
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToMany
    @Column(name = "animal_types")
    private List<AnimalType> animalTypes;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "length")
    private Float length;

    @Column(name = "height")
    private Float height;

    @Column(name = "gender")
    private AnimalGender gender;

    @Column(name = "life_status")
    private AnimalLifeStatus lifeStatus;

    @Column(name = "chipping_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chippingDateTime;

    @ManyToOne
    @JoinColumn(name = "chipper_id")
    private Account chipper;

    @ManyToOne
    @JoinColumn(name = "chippingLocation_id")
    private Location chippingLocation;

    @Column(name = "death_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDateTime;

    @OneToMany(mappedBy = "animal")
    private List<VisitedLocation> visitedLocations;

    public void addType(AnimalType animalType) {
        animalTypes.add(animalType);
    }

    public void removeType(AnimalType animalType) {
        animalTypes.remove(animalType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Animal animal = (Animal) o;
        return getId() != null && Objects.equals(getId(), animal.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
