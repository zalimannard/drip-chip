package ru.zalimannard.dripchip.integration.area.post;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
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
class AreaPostBadRequestTests {

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

    @ParameterizedTest
    @DisplayName("Негативный тест. Неверный name")
    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "   "})
    void invalidName(String name) {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(101.0).latitude(2.0).build(),
                PointRequestDto.builder().longitude(102.0).latitude(1.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(name)
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints равно null")
    void invalidAreaPoints() {
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(null)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит неверные элементы")
    void invalidAreaPoints2() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(null).latitude(1.0).build(),
                PointRequestDto.builder().longitude(101.0).latitude(2.0).build(),
                PointRequestDto.builder().longitude(102.0).latitude(1.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит неверные элементы")
    void invalidAreaPoints3() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(null).build(),
                PointRequestDto.builder().longitude(101.0).latitude(2.0).build(),
                PointRequestDto.builder().longitude(102.0).latitude(1.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит неверные элементы")
    void invalidAreaPoints4() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(null).latitude(null).build(),
                PointRequestDto.builder().longitude(102.0).latitude(1.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит неверные элементы")
    void invalidAreaPoints5() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(null).latitude(2.0).build(),
                PointRequestDto.builder().longitude(102.0).latitude(null).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит меньше 3 точек")
    void invalidAreaPoints7() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(101.0).latitude(2.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит меньше 3 точек")
    void invalidAreaPoints8() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит неверные элементы")
    void invalidAreaPoints9() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(181.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(101.0).latitude(2.0).build(),
                PointRequestDto.builder().longitude(102.0).latitude(1.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит неверные элементы")
    void invalidAreaPoints10() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(101.0).latitude(91.0).build(),
                PointRequestDto.builder().longitude(102.0).latitude(1.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит неверные элементы")
    void invalidAreaPoints11() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(101.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(102.0).latitude(-91.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. AreaPoints содержит неверные элементы")
    void invalidAreaPoints12() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(-181.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(102.0).latitude(1.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Пересекает себя")
    void invalidArea1() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(100.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(101.0).latitude(2.0).build(),
                PointRequestDto.builder().longitude(101.0).latitude(1.0).build(),
                PointRequestDto.builder().longitude(100.0).latitude(2.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();

        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Внутри другой зоны")
    void insideOther() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(1.0).latitude(80.0).build(),
                PointRequestDto.builder().longitude(1.0).latitude(84.0).build(),
                PointRequestDto.builder().longitude(5.0).latitude(84.0).build(),
                PointRequestDto.builder().longitude(5.0).latitude(80.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(2.0).latitude(81.0).build(),
                PointRequestDto.builder().longitude(2.0).latitude(83.0).build(),
                PointRequestDto.builder().longitude(4.0).latitude(83.0).build(),
                PointRequestDto.builder().longitude(4.0).latitude(81.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.postExpectedBadRequest(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Внутри этой зоны")
    void insideThis() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(2.0).latitude(11.0).build(),
                PointRequestDto.builder().longitude(2.0).latitude(13.0).build(),
                PointRequestDto.builder().longitude(4.0).latitude(13.0).build(),
                PointRequestDto.builder().longitude(4.0).latitude(11.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(1.0).latitude(10.0).build(),
                PointRequestDto.builder().longitude(1.0).latitude(14.0).build(),
                PointRequestDto.builder().longitude(5.0).latitude(14.0).build(),
                PointRequestDto.builder().longitude(5.0).latitude(10.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.postExpectedBadRequest(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Дубликаты точек")
    void dublicatePoints() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(20.0).latitude(20.0).build(),
                PointRequestDto.builder().longitude(20.0).latitude(21.0).build(),
                PointRequestDto.builder().longitude(20.0).latitude(21.0).build(),
                PointRequestDto.builder().longitude(21.0).latitude(20.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Нулевая площадь с дубликатом")
    void dublicatePoints2() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(20.0).latitude(20.0).build(),
                PointRequestDto.builder().longitude(20.0).latitude(21.0).build(),
                PointRequestDto.builder().longitude(21.0).latitude(20.0).build(),
                PointRequestDto.builder().longitude(20.0).latitude(21.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Состоит из части и внутри")
    void commonPointsAndInside() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(30.0).latitude(20.0).build(),
                PointRequestDto.builder().longitude(32.0).latitude(21.0).build(),
                PointRequestDto.builder().longitude(30.0).latitude(22.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.post(area, defaultAuth.adminAuth());

        List<PointRequestDto> points2 = List.of(
                PointRequestDto.builder().longitude(30.0).latitude(20.0).build(),
                PointRequestDto.builder().longitude(31.0).latitude(21.0).build(),
                PointRequestDto.builder().longitude(30.0).latitude(22.0).build()
        );
        AreaRequestDto area2 = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points2)
                .build();
        AreaSteps.postExpectedBadRequest(area2, defaultAuth.adminAuth());
    }

    @Test
    @DisplayName("Негативный тест. Точки на одной прямой")
    void pointOnLine() {
        List<PointRequestDto> points = List.of(
                PointRequestDto.builder().longitude(40.0).latitude(20.0).build(),
                PointRequestDto.builder().longitude(40.0).latitude(21.0).build(),
                PointRequestDto.builder().longitude(40.0).latitude(22.0).build()
        );
        AreaRequestDto area = AreaRequestDto.builder()
                .name(Faker.instance().name().title())
                .points(points)
                .build();
        AreaSteps.postExpectedBadRequest(area, defaultAuth.adminAuth());
    }

}
