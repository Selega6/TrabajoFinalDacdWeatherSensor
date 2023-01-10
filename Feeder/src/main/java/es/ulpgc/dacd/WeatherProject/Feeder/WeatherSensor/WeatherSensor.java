package es.ulpgc.dacd.WeatherProject.DataLakeFeeder.WeatherSensor;

import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.model.Weather;

import java.io.IOException;
import java.util.List;

    public interface WeatherSensor {
        List<Weather> read(Double minLat, Double maxLat, Double minLon, Double maxLon) throws IOException;

    }