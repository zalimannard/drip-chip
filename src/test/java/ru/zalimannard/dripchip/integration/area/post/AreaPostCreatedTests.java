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
class AreaPostCreatedTests {

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
    @DisplayName("Позитивный тест. Соприкасаются по вертикали")
    void edges() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(110.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(109.0).latitude(2.0).build(),
                PointRequestDto.builder().longitude(110.0).latitude(3.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(109.0).latitude(11.0).build(),
                PointRequestDto.builder().longitude(110.0).latitude(12.0).build(),
                PointRequestDto.builder().longitude(109.0).latitude(13.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.post(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Позитивный тест. Соприкасаются одной точкой")
    void edges2() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(112.0).latitude(11.0).build(),
                PointRequestDto.builder().longitude(111.0).latitude(12.0).build(),
                PointRequestDto.builder().longitude(112.0).latitude(13.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(110.0).latitude(11.0).build(),
                PointRequestDto.builder().longitude(111.0).latitude(12.0).build(),
                PointRequestDto.builder().longitude(110.0).latitude(13.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.post(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Позитивный тест. Соприкасаются по горизонтали")
    void edges3() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(109.0).latitude(20.0).build(),
                PointRequestDto.builder().longitude(110.0).latitude(19.0).build(),
                PointRequestDto.builder().longitude(111.0).latitude(20.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(109.0).latitude(20.0).build(),
                PointRequestDto.builder().longitude(110.0).latitude(21.0).build(),
                PointRequestDto.builder().longitude(111.0).latitude(20.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.post(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Позитивный тест. Соприкасаются по диагонали")
    void edges4() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(110.0).latitude(30.0).build(),
                PointRequestDto.builder().longitude(110.0).latitude(31.0).build(),
                PointRequestDto.builder().longitude(111.0).latitude(30.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(111.0).latitude(31.0).build(),
                PointRequestDto.builder().longitude(111.0).latitude(30.0).build(),
                PointRequestDto.builder().longitude(110.0).latitude(31.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.post(area2, defaultAuth.adminAuth());
    }

}
