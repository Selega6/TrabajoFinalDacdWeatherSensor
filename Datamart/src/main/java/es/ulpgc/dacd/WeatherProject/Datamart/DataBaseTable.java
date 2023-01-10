package es.ulpgc.dacd.WeatherProject.DataMartProvider;

import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.model.Weather;

import java.util.List;

public interface DataBaseTable {
    void createTable(String url);
    void insertIntoTable(String url, Weather weather);
    void dropTable(String url);
    List<Weather> getDataTableUsingDates(String url, String dateFrom, String dateTo);
}
