package es.ulpgc.dacd.WeatherProject.DataLakeFeeder.Storage;

import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.model.Weather;

import java.io.IOException;
import java.util.List;

public interface DataLake {
    void save(List<Weather> eventsList) throws IOException;
    List<Weather> get(String date) throws IOException;
}
