package es.ulpgc.dacd.WeatherProject.DataLakeFeeder;

import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.Controller.Controller;
import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.Storage.FileDataLake;
import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.WeatherSensor.AemetSensor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Controller controller = new Controller(new AemetSensor(), new FileDataLake());
        controller.execute();
    }
}
