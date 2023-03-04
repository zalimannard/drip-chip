package ru.zalimannard.dripchip.animal;

import jakarta.persistence.*;
import lombok.Data;
import ru.zalimannard.dripchip.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.animal.lifestatus.AnimalLifeStatus;

import java.sql.Timestamp;

@Entity
@Table(name = "animals")
@Data
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "length")
    private Float length;

    @Column(name = "height")
    private Float height;

    @Column(name = "gender")
    private AnimalGender gender;

    @Column(name = "lifeStatus")
    private AnimalLifeStatus lifeStatus;

    @Column(name = "chippingDateTime")
    private Timestamp chippingDateTime;

    @Column(name = "chipperId")
    private Integer chipperId;

    @Column(name = "chippingLocationId")
    private Long chippingLocationId;

    @Column(name = "deathDateTime")
    private Timestamp deathDateTime;

}
