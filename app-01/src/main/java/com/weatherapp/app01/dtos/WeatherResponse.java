package com.weatherapp.app01.dtos;

import lombok.Data;

@Data
public class WeatherResponse {
    private Request request;
    private Location location;
    private Current current;
}
