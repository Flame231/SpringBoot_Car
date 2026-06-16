package org.example.springboot_car;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.springboot_car.dto.CarDTO;
import org.example.springboot_car.exceptions.ResourceNotFoundException;
import org.example.springboot_car.model.Car;
import org.example.springboot_car.repository.CarRepository;
import org.example.springboot_car.service.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ServiceTest {

    @Autowired
    private CarServiceImpl carService;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private EntityManager entityManager;


    @Test
    void shouldSaveCar() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        carService.saveCar(carDTO);
        Car carFound = carRepository.findByName(carDTO.getName());
        assertNotNull(carFound);
        assertNotNull(carFound.getId());
        assertEquals(carFound.getName(),carDTO.getName());
    }
    @Test
    void shouldFindCar() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        carService.saveCar(carDTO);
        Car carFound = carRepository.findByName(carDTO.getName());
        assertThat(carService.findCar(carFound.getId())).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Timestamp.class).isEqualTo(carRepository.findById(carFound.getId()).get());
    }

    @Test
    void shouldThrowResourseNotFoundException() {
        assertThatThrownBy(() -> carService.findCar(200L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdateCar() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        carService.saveCar(carDTO);
        Car carFound = carRepository.findByName(carDTO.getName());
        String name = "updateTestName";
        entityManager.flush();
        entityManager.clear();
        carService.updateCar(CarDTO.builder().id(carFound.getId()).name(name).type(carFound.getType()).build());
        assertEquals(name, carRepository.findById(carFound.getId()).get().getName());
    }

    @Test
    void shouldDeleteCar() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        carService.saveCar(carDTO);
        Car carFound = carRepository.findByName(carDTO.getName());
        carService.deleteCar(carFound.getId());
        entityManager.flush();
        entityManager.clear();
        assertTrue(carRepository.findById(carFound.getId()).isEmpty());
    }

    @Test
    void shouldFindAllCars() {
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
