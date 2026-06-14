package org.example.springboot_car.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CarDTO {
    private Long id;
    @NotBlank(message = "{car.name.valid}")
    private String name;
    private String type;
}
