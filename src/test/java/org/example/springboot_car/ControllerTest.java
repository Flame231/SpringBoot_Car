package org.example.springboot_car;

import org.example.springboot_car.controller.CarController;
import org.example.springboot_car.dto.CarDTO;
import org.example.springboot_car.service.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    private CarServiceImpl carService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private CarController controller;


    @Test
    void shouldSaveCarAndRedirect() {
        CarDTO carDTO = CarDTO.builder().name("testName").type("testType").build();
        assertEquals("redirect:/allCars", controller.saveCar(carDTO));
        verify(carService, times(1)).saveCar(carDTO);
    }

    @Test
    void shouldDeleteCarAndRedirect() {
        long id = 10;
        assertEquals("redirect:/allCars", controller.deleteCar(id));
        verify(carService, times(1)).deleteCar(id);
    }

    @Test
    void shouldSaveNewCarAndRedirect() {
        long id = 12;
        CarDTO carDTO = CarDTO.builder().id(id).name("TestName").type("TestType").build();
        doReturn(carDTO).when(carService).findCar(id);
        assertEquals("redirect:/allCars", controller.getCar(model, id, null, redirectAttributes));
        verify(carService, times(1)).findCar(id);
        verify(redirectAttributes, times(1)).addFlashAttribute("foundCar", carDTO);
    }

    @Test
    void shouldReturnUpdateMethod() {
        long id = 12;
        CarDTO carDTO = CarDTO.builder().id(id).name("TestName").type("TestType").build();
        doReturn(carDTO).when(carService).findCar(id);
        assertEquals("updateCar", controller.getCar(model, id, id, redirectAttributes));
        verify(carService, times(1)).findCar(id);
        verify(model,times(1)).addAttribute("car", carDTO);
    }

    @Test
    void shouldSaveChangesCar(){
        long id = 12;
        CarDTO carDTO = CarDTO.builder().id(id).name("TestName").type("TestType").build();
        assertEquals("redirect:/allCars", controller.saveChangesCar(carDTO));
        verify(carService, times(1)).updateCar(carDTO);
    }

    @Test
    void shouldReturnCarListPage(){
        CarDTO carDTO1 = CarDTO.builder().id(1L).name("TestName1").type("TestType1").build();
        CarDTO carDTO2 = CarDTO.builder().id(2L).name("TestName2").type("TestType2").build();
        CarDTO carDTO3 = CarDTO.builder().id(3L).name("TestName3").type("TestType3").build();
        List<CarDTO> carDTOList = new ArrayList<>();
        carDTOList.add(carDTO1);
        carDTOList.add(carDTO2);
        carDTOList.add(carDTO3);
        doReturn(carDTOList).when(carService).findAllCars();
        assertEquals("allCars", controller.getAllCars(model));
        verify(model, times(1)).addAttribute("carsList", carDTOList);
    }
}
