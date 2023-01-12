package es.ulpgc.dacd.WeatherProject.Feeder.model;

import java.time.LocalDateTime;

public record Weather(LocalDateTime timeStamp, String place, String station, Double temperature, Double minTemperature,
                      Double maxTemperature) {
}

