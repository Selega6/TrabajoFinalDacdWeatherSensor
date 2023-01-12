package es.ulpgc.dacd.WeatherProject.TemperatureService;

import es.ulpgc.dacd.WeatherProject.TemperatureService.Control.Controller;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:./datamart.db";
        Controller controller = new Controller(url);
        controller.execute();
    }
}