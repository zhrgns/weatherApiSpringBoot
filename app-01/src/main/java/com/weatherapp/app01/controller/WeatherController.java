package com.weatherapp.app01.controller;


import com.weatherapp.app01.controller.validation.CityNameConstraint;
import com.weatherapp.app01.dtos.WeatherDTO;
import com.weatherapp.app01.service.WeatherService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@Validated
@Tag(name = "Weather Api for city", description = "Weather api for city for current temperature ! ")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RateLimiter(name = "basic")
    @GetMapping("/{city}")
    public ResponseEntity<WeatherDTO> getWeather(@PathVariable("city") @CityNameConstraint @NotBlank String cityName) {
        return ResponseEntity.ok(weatherService.getWeather(cityName));
    }

}
