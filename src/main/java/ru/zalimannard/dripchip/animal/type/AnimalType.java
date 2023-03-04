package ru.zalimannard.dripchip.animal.type;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "animalTypes")
@Data
public class AnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

}
