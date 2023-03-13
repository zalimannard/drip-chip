package ru.zalimannard.dripchip.schema.animal.ownedtype.type;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.zalimannard.dripchip.schema.animal.Animal;

import java.util.Set;

@Entity
@Table(name = "animalTypes")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "type", unique = true)
    private String type;

    @ManyToMany
    @Column(name = "animals")
    private Set<Animal> animals;

}
