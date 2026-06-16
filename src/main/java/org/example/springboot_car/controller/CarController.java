package org.example.springboot_car.controller;

import jakarta.validation.Valid;
import org.example.springboot_car.dto.CarDTO;
import org.example.springboot_car.model.Car;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.springboot_car.service.CarServiceImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class CarController {
    private final CarServiceImpl carService;

    public CarController(CarServiceImpl carService) {
        this.carService = carService;
    }

    @RequestMapping("/car")
    public String getCar(Model model, @RequestParam(name = "id", required = false) Long id,
                         @RequestParam(name = "updateId", required = false) Long updateId, RedirectAttributes redirectAttributes) {
        if (updateId != null) {
            CarDTO foundCar = carService.findCar(updateId);
            model.addAttribute("car", foundCar);
            return "updateCar";
        }
        CarDTO foundCar = carService.findCar(id);
        redirectAttributes.addFlashAttribute("foundCar", foundCar);
        return "redirect:/allCars";
    }

    @RequestMapping(value = "/saveCar", method = RequestMethod.POST)
    public String saveCar(@ModelAttribute @Valid CarDTO carDTO) {
        carService.saveCar(carDTO);
        return "redirect:/allCars";
    }

    @RequestMapping(value = "/deleteCar")
    public String deleteCar(@RequestParam Long id) {
        carService.deleteCar(id);
        return "redirect:/allCars";
    }

    @PostMapping(value = "/saveChangesCar")
    public String saveChangesCar(@ModelAttribute CarDTO carDTO) {
        carService.updateCar(carDTO);
        return "redirect:/allCars";
    }

    @GetMapping("/allCars")
    public String getAllCars(Model model) {
        List<CarDTO> carDTOList = carService.findAllCars();
        model.addAttribute("carsList", carDTOList);
        return "allCars";
    }

}
