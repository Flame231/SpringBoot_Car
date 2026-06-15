package org.example.springboot_car;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.springboot_car.dto.CarConverterDTO;
import org.example.springboot_car.dto.CarDTO;
import org.example.springboot_car.exceptions.ResourceNotFoundException;
import org.example.springboot_car.model.Car;
import org.example.springboot_car.repository.CarRepository;
import org.example.springboot_car.service.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ServiceTest {

    @Autowired
    private CarServiceImpl carService;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    CarConverterDTO carConverterDTO;

    @BeforeEach
    void clearAll(){
        carRepository.deleteAll();
    }

    @Test
    void shouldSaveCar() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        Car carCreated = carService.saveCar(carDTO);
        Car carFound = carRepository.findById(carCreated.getId()).orElse(null);
        assertThat(carFound).usingRecursiveComparison().ignoringFieldsOfTypes(Timestamp.class).isEqualTo(carCreated);
    }

    @Test
    void shouldFindCar() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        Car car = carService.saveCar(carDTO);
        assertThat(carService.findCar(car.getId())).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Timestamp.class).isEqualTo(carRepository.findById(car.getId()).get());
    }

    @Test
    void shouldThrowResourseNotFoundException() {
        assertThatThrownBy(() -> carService.findCar(200L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdateCar() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        Car car = carService.saveCar(carDTO);
        String name = "updateTestName";
        entityManager.clear();
        carService.updateCar(CarDTO.builder().id(car.getId()).name(name).type(car.getType()).build());
        assertEquals(name, carRepository.findById(car.getId()).get().getName());
    }

    @Test
    @Transactional
    void shouldDeleteCar() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        Car car = carService.saveCar(carDTO);
        carService.deleteCar(car.getId());
        entityManager.flush();
        entityManager.clear();
        assertTrue(carRepository.findById(car.getId()).isEmpty());
    }

    @Test
    @Transactional
    void ShouldFindAllCars() {
        CarDTO carDTO1 = CarDTO.builder().name("testName1").type("testType1").build();
        CarDTO carDTO2 = CarDTO.builder().name("testName2").type("testType2").build();
        CarDTO carDTO3 = CarDTO.builder().name("testName3").type("testType3").build();
        carService.saveCar(carDTO1);
        carService.saveCar(carDTO2);
        carService.saveCar(carDTO3);
        entityManager.flush();
        entityManager.clear();
        assertEquals(3, carService.findAllCars().size());
    }
}
