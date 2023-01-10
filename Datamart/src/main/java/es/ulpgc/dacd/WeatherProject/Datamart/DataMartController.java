package es.ulpgc.dacd.WeatherProject.DataMartProvider;

import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.Storage.FileDataLake;
import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.model.Weather;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataMartController {
    private final DataBase dataBase;
    private final FileDataLake fileDataLake;

    public DataMartController(DataBase dataBase, FileDataLake fileDataLake){
        this.dataBase = dataBase;
        this.fileDataLake = fileDataLake;
    }
    public void execute() throws IOException {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                programExecution(dataBase, fileDataLake);
                System.out.println("Done");
            }
        },0,1,TimeUnit.HOURS);
    }

    private static void programExecution(DataBase dataBase, FileDataLake fileDataLake) {
        String url = "jdbc:sqlite:./datamart.db";
        inicializingTables(dataBase, url);
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
        addingMaxAndMinTemperatureToDataBase(dataBase, url, maxWeathersList, minWeathersList);
    }


    private static void addingMaxAndMinTemperatureToDataBase(DataBase dataBase, String url, List<Weather> maxWeathersList, List<Weather> minWeathersList) {
        minWeathersList.forEach(o -> dataBase.getMinTemperatureTable().insertIntoTable(url, o));
        maxWeathersList.forEach(o -> dataBase.getMaxTemperatureTable().insertIntoTable(url, o));
    }

    private static void getMaxAndMinTemperatureToList(FileDataLake fileDataLake, List<Weather> maxWeathersList, List<Weather> minWeathersList, File file) throws IOException {
        String fileName = file.getName();
        String date = fileName.replace(".events", "");
        List<Weather> weathersList = fileDataLake.get(date);
        maxWeathersList.add(maxTempWeatherFinder(weathersList));
        minWeathersList.add(minTempWeatherFinder(weathersList));
    }

    private static void inicializingTables(DataBase dataBase, String url) {
        dataBase.getMaxTemperatureTable().dropTable(url);
        dataBase.getMinTemperatureTable().dropTable(url);
        dataBase.getMaxTemperatureTable().createTable(url);
        dataBase.getMinTemperatureTable().createTable(url);
    }

    private static Weather minTempWeatherFinder(List<Weather> weathersList) {
        Weather MinTempWeather = weathersList.get(0);
        for (int i = 0; i < weathersList.size(); i++) {
            if (weathersList.get(i).minTemperature() < MinTempWeather.minTemperature()) {
                MinTempWeather = weathersList.get(i);
            }
        }
        return MinTempWeather;
    }

    private static Weather maxTempWeatherFinder(List<Weather> weathersList) {
        Weather MaxTempWeather = weathersList.get(0);
        for (int i = 0; i < weathersList.size(); i++) {
            if (weathersList.get(i).maxTemperature() > MaxTempWeather.maxTemperature()) {
                MaxTempWeather = weathersList.get(i);
            }
        }
        return MaxTempWeather;
    }
}

