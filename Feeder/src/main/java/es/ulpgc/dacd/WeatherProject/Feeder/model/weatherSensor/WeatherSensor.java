package es.ulpgc.dacd.WeatherProject.Feeder.model.weatherSensor;

import es.ulpgc.dacd.WeatherProject.Feeder.model.Weather;

import java.io.IOException;
import java.util.List;

    public interface WeatherSensor {
        List<Weather> read(Double minLat, Double maxLat, Double minLon, Double maxLon) throws IOException;

    }