package es.ulpgc.dacd.WeatherProject.DataLakeFeeder.model;

public record Weather(LocalDateTime timeStamp, String place, String station, Double temperature, Double minTemperature,
                      Double maxTemperature) {
}

