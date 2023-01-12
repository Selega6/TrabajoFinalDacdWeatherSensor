package es.ulpgc.dacd.WeatherProject.Datamart.controller;

import es.ulpgc.dacd.WeatherProject.Datamart.database.Database;
import es.ulpgc.dacd.WeatherProject.Feeder.datalake.FileDataLake;
import es.ulpgc.dacd.WeatherProject.Feeder.model.Weather;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DatamartController {
    private final Database dataBase;
    private final FileDataLake fileDataLake;

    public DatamartController(Database dataBase, FileDataLake fileDataLake){
        this.dataBase = dataBase;
        this.fileDataLake = fileDataLake;
    }
    public void execute() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            programExecution(dataBase, fileDataLake);
            System.out.println("Datamart Done");
        },16,3600,TimeUnit.SECONDS);
    }

    private static void programExecution(Database dataBase, FileDataLake fileDataLake) {
        initializingTables(dataBase);
        File directoryPath = new File("./datalake");
        File[] filesList = directoryPath.listFiles();
        assert filesList != null;
        List<Weather> maxWeathersList = new ArrayList<>();
        List<Weather> minWeathersList = new ArrayList<>();
        for (File file : filesList) {
            try {
                getMaxAndMinTemperatureToList(fileDataLake, maxWeathersList, minWeathersList, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        addingMaxAndMinTemperatureToDataBase(dataBase, maxWeathersList, minWeathersList);
    }


    private static void addingMaxAndMinTemperatureToDataBase(Database dataBase, List<Weather> maxWeathersList, List<Weather> minWeathersList) {
        minWeathersList.forEach(o -> dataBase.getMinTemperatureTable().insertIntoTable(o));
        maxWeathersList.forEach(o -> dataBase.getMaxTemperatureTable().insertIntoTable(o));
    }

    private static void getMaxAndMinTemperatureToList(FileDataLake fileDataLake, List<Weather> maxWeathersList, List<Weather> minWeathersList, File file) throws IOException {
        String fileName = file.getName();
        String date = fileName.replace(".events", "");
        List<Weather> weathersList = fileDataLake.get(date);
        maxWeathersList.add(maxTempWeatherFinder(weathersList));
        minWeathersList.add(minTempWeatherFinder(weathersList));
    }

    private static void initializingTables(Database dataBase) {
        dataBase.getMaxTemperatureTable().createTable();
        dataBase.getMinTemperatureTable().createTable();
    }

    private static Weather minTempWeatherFinder(List<Weather> weathersList) {
        Weather MinTempWeather = weathersList.get(0);
        for (Weather weather : weathersList) {
            if (weather.minTemperature() < MinTempWeather.minTemperature()) {
                MinTempWeather = weather;
            }
        }
        return MinTempWeather;
    }

    private static Weather maxTempWeatherFinder(List<Weather> weathersList) {
        Weather MaxTempWeather = weathersList.get(0);
        for (Weather weather : weathersList) {
            if (weather.maxTemperature() > MaxTempWeather.maxTemperature()) {
                MaxTempWeather = weather;
            }
        }
        return MaxTempWeather;
    }
}

