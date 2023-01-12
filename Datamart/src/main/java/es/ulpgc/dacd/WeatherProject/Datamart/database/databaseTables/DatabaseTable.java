package es.ulpgc.dacd.WeatherProject.Datamart.database.databaseTables;

import es.ulpgc.dacd.WeatherProject.Feeder.model.Weather;

import java.util.List;

public interface DatabaseTable {
    void createTable();

    void insertIntoTable(Weather weather);

    List<Weather> getDataTableUsingDates(String dateFrom, String dateTo);
}
