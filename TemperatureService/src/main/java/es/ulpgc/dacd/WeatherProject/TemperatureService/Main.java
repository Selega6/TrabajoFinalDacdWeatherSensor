package es.ulpgc.dacd.WeatherProject.TemperatureService;

import es.ulpgc.dacd.WeatherProject.DataMartProvider.SqliteDataBase;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:./datamart.db";
        WebService webService = new WebService(new SqliteDataBase(url), url);
        webService.start();
    }
}