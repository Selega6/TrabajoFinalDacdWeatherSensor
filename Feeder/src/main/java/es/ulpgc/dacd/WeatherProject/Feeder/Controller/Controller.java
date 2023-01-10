package es.ulpgc.dacd.WeatherProject.DataLakeFeeder.Controller;

import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.Storage.FileDataLake;
import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.WeatherSensor.AemetSensor;
import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.model.Weather;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private final AemetSensor aemetSensor;
    private final FileDataLake fileDataLake;

    public Controller(AemetSensor aemetSensor, FileDataLake fileDataLake) {
        this.aemetSensor = aemetSensor;
        this.fileDataLake = fileDataLake;
    }

    public void execute() throws IOException {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                programExecution(aemetSensor, fileDataLake);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static void programExecution(AemetSensor aemetSensor, FileDataLake fileDataLake) {
        List<Weather> weathersList = null;
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
        System.out.println("Done");
    }
}
