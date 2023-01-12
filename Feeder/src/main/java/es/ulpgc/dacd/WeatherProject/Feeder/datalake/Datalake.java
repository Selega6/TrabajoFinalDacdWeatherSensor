package es.ulpgc.dacd.WeatherProject.Feeder.datalake;

import es.ulpgc.dacd.WeatherProject.Feeder.model.Weather;

import java.io.IOException;
import java.util.List;

public interface Datalake {
    void save(List<Weather> eventsList) throws IOException;
    List<Weather> get(String date) throws IOException;
}
