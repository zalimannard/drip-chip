package ru.zalimannard.dripchip.schema.area;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.zalimannard.dripchip.schema.area.point.Point;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "areas")
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "area")
    @Getter(AccessLevel.NONE)
    private List<Point> points;

    public List<Point> getPoints() {
        points.sort(Comparator.comparingLong(Point::getNumberInArea));

        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Area area = (Area) o;
        return getId() != null && Objects.equals(getId(), area.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}