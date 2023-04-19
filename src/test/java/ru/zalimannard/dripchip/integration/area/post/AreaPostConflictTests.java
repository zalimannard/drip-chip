package ru.zalimannard.dripchip.integration.area.post;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.zalimannard.dripchip.integration.DefaultAuth;
import ru.zalimannard.dripchip.integration.Specifications;
import ru.zalimannard.dripchip.integration.area.AreaSteps;
import ru.zalimannard.dripchip.schema.account.AccountController;
import ru.zalimannard.dripchip.schema.area.dto.AreaRequestDto;
import ru.zalimannard.dripchip.schema.area.point.dto.PointRequestDto;
import ru.zalimannard.dripchip.schema.location.LocationController;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AreaPostConflictTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountController accountController;
    @Autowired
    private LocationController locationController;

    @Autowired
    private DefaultAuth defaultAuth;

    @BeforeEach
    void setUp() {
        assertThat(accountController).isNotNull();
        assertThat(locationController).isNotNull();

        RestAssured.port = port;
        RestAssured.requestSpecification = Specifications.requestSpec();
    }

    @Test
    @DisplayName("Негативный тест. Повторное добавление")
    void dublicate() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(1.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(2.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(1.0).latitude(2.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.post(area, defaultAuth.adminAuth());
        area = area.toBuilder()
                .name(Faker.instance().name().title())
                .build();
        AreaSteps.postExpectedConflict(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Добавление со смещением на одну точку")
    void dublicateShift() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(3.0).latitude(3.0).build(),
                PointRequestDto.builder().longitude(4.0).latitude(3.0).build(),
                PointRequestDto.builder().longitude(3.0).latitude(4.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(3.0).latitude(4.0).build(),
                PointRequestDto.builder().longitude(3.0).latitude(3.0).build(),
                PointRequestDto.builder().longitude(4.0).latitude(3.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.postExpectedConflict(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Обратный путь")
    void dublicateInverse() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(5.0).latitude(5.0).build(),
                PointRequestDto.builder().longitude(6.0).latitude(5.0).build(),
                PointRequestDto.builder().longitude(5.0).latitude(6.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(5.0).latitude(5.0).build(),
                PointRequestDto.builder().longitude(5.0).latitude(6.0).build(),
                PointRequestDto.builder().longitude(6.0).latitude(5.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.postExpectedConflict(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Обратный путь со смещением на одну точку")
    void dublicateInverseShift() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(7.0).latitude(7.0).build(),
                PointRequestDto.builder().longitude(8.0).latitude(7.0).build(),
                PointRequestDto.builder().longitude(7.0).latitude(8.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(8.0).latitude(7.0).build(),
                PointRequestDto.builder().longitude(7.0).latitude(7.0).build(),
                PointRequestDto.builder().longitude(7.0).latitude(8.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.postExpectedConflict(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Name занят")
    void nameAlreadyUsed() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(9.0).latitude(9.0).build(),
                PointRequestDto.builder().longitude(10.0).latitude(9.0).build(),
                PointRequestDto.builder().longitude(9.0).latitude(10.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(11.0).latitude(11.0).build(),
                PointRequestDto.builder().longitude(12.0).latitude(11.0).build(),
                PointRequestDto.builder().longitude(11.0).latitude(12.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(area.getName())
                .points(points2)
                .build();
        AreaSteps.postExpectedConflict(area2, defaultAuth.adminAuth());
    }

}
