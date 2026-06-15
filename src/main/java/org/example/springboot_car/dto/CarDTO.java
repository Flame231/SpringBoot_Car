package org.example.springboot_car.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class CarDTO {
    private Long id;
    @NotBlank(message = "{car.name.valid}")
    private String name;
    @NotBlank(message = "{car.type.valid}")
    private String type;
}
