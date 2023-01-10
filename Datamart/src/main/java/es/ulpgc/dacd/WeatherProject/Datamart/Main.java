package es.ulpgc.dacd.WeatherProject.DataMartProvider;

import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.Storage.FileDataLake;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        String url = "jdbc:sqlite:./datamart.db";
        DataMartController controller = new DataMartController(new SqliteDataBase(url), new FileDataLake());
        controller.execute();
    }
}
