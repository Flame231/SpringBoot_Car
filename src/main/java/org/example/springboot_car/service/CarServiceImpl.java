package org.example.springboot_car.service;

import org.example.springboot_car.dto.CarConverterDTO;
import org.example.springboot_car.dto.CarDTO;
import org.example.springboot_car.exceptions.ResourceNotFoundException;
import org.example.springboot_car.model.Car;
import org.springframework.stereotype.Service;
import org.example.springboot_car.repository.CarRepository;

import java.util.List;

@Service
public class CarServiceImpl {
    private CarRepository carRepository;
    private CarConverterDTO carConvertDTO;


    public CarServiceImpl(CarRepository carRepository, CarConverterDTO carConvertDTO) {
        this.carRepository = carRepository;
        this.carConvertDTO = carConvertDTO;
    }

    public Car saveCar(CarDTO carDTO) {
        return carRepository.save(carConvertDTO.toEntity(carDTO));
    }

    public Car findCar(Long id) {
        if(!carRepository.existsById(id)){
            throw new ResourceNotFoundException("Машина с id " + id + " не существует");
        }
        return carRepository.findById(id).get();
    }

    public void updateCar(CarDTO carDTO) {
        Car car = carRepository.findById(carDTO.getId()).get();
        car.setName(carDTO.getName());
        car.setType(carDTO.getType());
        carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }


    public List<CarDTO> findAllCars() {
        return carRepository.findAll().stream().map(carConvertDTO::toDTO).toList();

    }


}
