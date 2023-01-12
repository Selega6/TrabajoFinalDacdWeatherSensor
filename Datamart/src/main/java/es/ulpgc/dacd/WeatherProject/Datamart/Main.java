package es.ulpgc.dacd.WeatherProject.Datamart;

import es.ulpgc.dacd.WeatherProject.Datamart.controller.DatamartController;
import es.ulpgc.dacd.WeatherProject.Datamart.database.SqliteDatabase;
import es.ulpgc.dacd.WeatherProject.Feeder.datalake.FileDatalake;


public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:./datamart.db";
        DatamartController controller = new DatamartController(new SqliteDatabase(url), new FileDatalake());
        controller.execute();
    }
}
