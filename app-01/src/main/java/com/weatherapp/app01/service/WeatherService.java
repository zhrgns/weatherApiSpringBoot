package com.weatherapp.app01.service;


import com.weatherapp.app01.model.WeatherEntity;
import com.weatherapp.app01.dtos.WeatherDTO;
import com.weatherapp.app01.dtos.WeatherResponse;
import com.weatherapp.app01.repository.WeatherRepository;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.weatherapp.app01.constants.Constants.*;

@Service
@CacheConfig(cacheNames = {"weathers"})
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;


    public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
    }

    @Cacheable(key = "#cityName")
    public WeatherDTO getWeather(String cityName) {

        Optional<WeatherEntity> entityOptional = weatherRepository.findFirstByRequestedCityNameOrderByUpdatedTimeDesc(cityName);

        return entityOptional.map(entity -> {
            if (entityOptional.get().getUpdatedTime().isBefore(LocalDateTime.now().minusMinutes(30))) {
                logger.info(String.format("Creating a new city weather from api for %s ", cityName));
                return createCityWeather(cityName);
            }
            logger.info(String.format("Getting weather from database for %s", cityName));
            return WeatherDTO.convert(entity);
        }).orElseGet(() -> createCityWeather(cityName));
    }

    @CachePut(key = "cityName")
    public WeatherDTO createCityWeather(String cityName) {

        logger.info("Requesting weather api for city: " + cityName);
        String url = getWeatherStackUrl(cityName);
        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response.getLocation() == null) {
            throw new RuntimeException("There may not be such a city!");
        } else return WeatherDTO.convert(saveWeatherEntity(cityName, response));

    }

    //clears cache every 10 seconds !
    @CacheEvict(allEntries = true)
    @PostConstruct
    @Scheduled(fixedRateString = "10000")
    public void clearCache() {
        logger.info("Cache is cleared !");
    }

    private WeatherEntity saveWeatherEntity(String cityName, WeatherResponse response) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        WeatherEntity entity = new WeatherEntity(cityName,
                response.getLocation().getName(),
                response.getLocation().getCountry(),
                response.getCurrent().getTemperature(),
                LocalDateTime.now(),
                LocalDateTime.parse(response.getLocation().getLocaltime(), formatter));

        return weatherRepository.save(entity);
    }

    private String getWeatherStackUrl(String cityName) {
        return WEATHER_STACK_API_BASE_URL +
                WEATHER_STACK_API_ACCESS_KEY_PARAM +
                API_KEY +
                WEATHER_STACK_API_QUERY_PARAM +
                cityName;
    }

}
