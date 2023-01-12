package es.ulpgc.dacd.WeatherProject.Datamart.database;

import es.ulpgc.dacd.WeatherProject.Datamart.database.databaseTables.DatabaseTable;

public interface Database {

    DatabaseTable getMaxTemperatureTable();
    DatabaseTable getMinTemperatureTable();

}
