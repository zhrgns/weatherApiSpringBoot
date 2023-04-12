package com.weatherapp.app01.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weatherapp.app01.model.WeatherEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class WeatherDTO {
    private String cityName;
    private String country;
    private Integer temperature;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedTime;


    public static WeatherDTO convert(WeatherEntity entity) {
        return WeatherDTO.builder().cityName(entity.getCityName())
                .country(entity.getCountry())
                .temperature(entity.getTemperature())
                .updatedTime(entity.getUpdatedTime())
                .build();
    }
}
