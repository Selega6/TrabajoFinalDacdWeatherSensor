package es.ulpgc.dacd.WeatherProject.TemperatureService;

import java.time.LocalDateTime;

public record WeatherForAPI(LocalDateTime timeStamp, String place, String station, Double temperature) {
}
