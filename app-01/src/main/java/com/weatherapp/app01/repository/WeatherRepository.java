package com.weatherapp.app01.repository;

import com.weatherapp.app01.model.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity,Long> {


    Optional<WeatherEntity> findFirstByRequestedCityNameOrderByUpdatedTimeDesc(String cityName);

}
