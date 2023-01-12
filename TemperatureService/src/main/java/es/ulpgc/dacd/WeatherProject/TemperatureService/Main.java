package es.ulpgc.dacd.WeatherProject.TemperatureService;

import es.ulpgc.dacd.WeatherProject.Datamart.database.SqliteDatabase;
import es.ulpgc.dacd.WeatherProject.TemperatureService.view.WebService;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:./datamart.db";
        WebService webService = new WebService(new SqliteDatabase(url));
        webService.start();
    }
}