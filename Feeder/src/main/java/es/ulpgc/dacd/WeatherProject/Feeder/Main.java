package es.ulpgc.dacd.WeatherProject.Feeder;

import es.ulpgc.dacd.WeatherProject.Feeder.controller.Controller;
import es.ulpgc.dacd.WeatherProject.Feeder.datalake.FileDataLake;
import es.ulpgc.dacd.WeatherProject.Feeder.model.weatherSensor.AemetSensor;

public class Main {
    public static void main(String[] args) {
        String apiKey = args[0];
        Controller controller = new Controller(new AemetSensor(apiKey), new FileDataLake());
        controller.execute();
    }
}
