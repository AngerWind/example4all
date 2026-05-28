package com.tiger;

import lombok.Data;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

public class _07_BeforeMapping和AfterMapping注解 {

    @Data
    public static class Car {
        private int id;
        private String name;
    }


    public static class BioDieselCar extends Car {
    }

    public static class ElectricCar extends Car {
    }

    @Data
    public static class CarDTO {
        private int id;
        private String name;
        private FuelType fuelType;
    }

    public enum FuelType {
        ELECTRIC, BIO_DIESEL
    }


    @Mapper
    public static abstract class CarsMapper {

        // 这个方法会在将Car转换为CarDTO之前被调用
        // @MappingTarget 表示这个参数用来表示从谁转换到谁
        @BeforeMapping
        protected void enrichDTOWithFuelType(Car car, @MappingTarget CarDTO carDto) {
            if (car instanceof ElectricCar) {
                carDto.setFuelType(FuelType.ELECTRIC);
            }
            if (car instanceof BioDieselCar) {
                carDto.setFuelType(FuelType.BIO_DIESEL);
            }
        }

        // 这个方法会在Car转换为CarDTO之后被调用
        // 可以在这里做一些自定义的处理
        @AfterMapping
        protected void convertNameToUpperCase(Car car, @MappingTarget CarDTO carDto) {
            carDto.setName(carDto.getName().toUpperCase());
        }

        public abstract CarDTO toCarDto(Car car);
    }


}
