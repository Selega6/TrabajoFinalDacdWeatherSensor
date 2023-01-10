package es.ulpgc.dacd.WeatherProject.DataMartProvider;

import java.sql.Connection;

public interface DataBase {
    static Connection getConnection(String url) {
        return null;
    }

    DataBaseTable getMaxTemperatureTable();
    DataBaseTable getMinTemperatureTable();

}
