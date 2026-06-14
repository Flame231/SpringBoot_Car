package org.example.springboot_car.dto;

import org.example.springboot_car.model.Car;
import org.springframework.stereotype.Component;

@Component
public class CarConverterDTO implements ConverterDTO<Car, CarDTO> {

    @Override
    public Car toEntity(CarDTO carDTO) {
        return Car.builder().name(carDTO.getName()).type(carDTO.getType()).build();
    }

    @Override
    public CarDTO toDTO(Car car) {
        return CarDTO.builder().id(car.getId()).name(car.getName()).type(car.getType()).build();
    }
}
