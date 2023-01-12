package es.ulpgc.dacd.WeatherProject.Feeder.controller;

import es.ulpgc.dacd.WeatherProject.Feeder.datalake.FileDatalake;
import es.ulpgc.dacd.WeatherProject.Feeder.model.weatherSensor.AemetSensor;
import es.ulpgc.dacd.WeatherProject.Feeder.model.Weather;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private final AemetSensor aemetSensor;
    private final FileDatalake fileDataLake;

    public Controller(AemetSensor aemetSensor, FileDatalake fileDataLake) {
        this.aemetSensor = aemetSensor;
        this.fileDataLake = fileDataLake;
    }

    public void execute(){
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> programExecution(aemetSensor, fileDataLake), 0, 1, TimeUnit.HOURS);
    }

    private static void programExecution(AemetSensor aemetSensor, FileDatalake fileDataLake) {
        List<Weather> weathersList;
        try {
            weathersList = aemetSensor.read( 27.5, 28.4,  (double) -16, (double) -15);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileDataLake.save(weathersList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Datalake Done");
    }
}
