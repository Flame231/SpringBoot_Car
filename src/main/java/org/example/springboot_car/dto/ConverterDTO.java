package org.example.springboot_car.dto;

public interface ConverterDTO<V, T> {

    T toDTO(V v);

    V toEntity(T t);
}
