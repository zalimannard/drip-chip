package ru.zalimannard.dripchip.animal.type;

import jakarta.persistence.*;
import lombok.Data;
import ru.zalimannard.dripchip.animal.Animal;

import java.util.Set;

@Entity
@Table(name = "animalTypes")
@Data
public class AnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type", unique = true)
    private String type;

    @ManyToMany
    @Column(name = "animals")
    private Set<Animal> animals;

}
