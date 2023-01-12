package es.ulpgc.dacd.WeatherProject.Feeder.datalake;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import es.ulpgc.dacd.WeatherProject.Feeder.model.Weather;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileDataLake implements DataLake {
    @Override
    public void save(List<Weather> eventsList) throws IOException {
        createDirectory();
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        String validatedDateToday = LocalDate.now().format(formatter);
        String validatedDateYesterday = LocalDate.now().minusDays(1).format(formatter);
        BufferedWriter bufferedWriterForToday = new BufferedWriter(new FileWriter(String.format("datalake/" + validatedDateToday + ".events", validatedDateToday), true));
        BufferedWriter bufferedWriterForYesterday = new BufferedWriter(new FileWriter(String.format("datalake/" + validatedDateYesterday + ".events", validatedDateToday), true));
        Set<Weather> weathersOfTodaySet = eventsOnListToSet(validatedDateToday);
        Set<Weather> weathersOfYesterdaySet = eventsOnListToSet(validatedDateYesterday);
        eventsList.stream()
                .filter(o -> localDateForComparison(o).equals(LocalDate.now()))
                .filter(o -> isIn(o, weathersOfTodaySet))
                .forEach(o -> eventFile(o, bufferedWriterForToday));
        bufferedWriterForToday.close();
        eventsList.stream()
                .filter(o -> localDateForComparison(o).equals(LocalDate.now().minusDays(1)))
                .filter(o -> isIn(o, weathersOfYesterdaySet))
                .forEach(o -> eventFile(o, bufferedWriterForYesterday));
        bufferedWriterForYesterday.close();
    }

    @Override
    public List<Weather> get(String date) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(String.format("datalake/" + date + ".events", date)));
        String st;
        List<Weather> weathersList = new ArrayList<>();
        while ((st = br.readLine()) != null) {
            Weather weather = weatherObjectConverter(st);
            weathersList.add(weather);
        }
        return weathersList;
    }

    private Set<Weather> eventsOnListToSet(String date) throws IOException {
        return new HashSet<>(get(date));
    }

    private boolean isIn(Weather weather, Set<Weather> weathers) {
        return !weathers.contains(weather);
    }

    public LocalDate localDateForComparison(Weather weather) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDateTime timeFromClass = weather.timeStamp();
        return LocalDate.parse(LocalDate.from(timeFromClass).format(formatter));
    }

    private void eventFile(Weather weather, BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.append(jsonConverterWeather(weather));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String jsonConverterWeather(Weather weather) {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        try {
            return mapper.writeValueAsString(weather) + "\n";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void createDirectory() {
        File directory = new File("./datalake");
        boolean fileDirectory = directory.mkdir();
        if (fileDirectory) {
            System.out.println("The directory has been created.");
        } else {
            System.out.println("The directory already exists.");
        }
    }

    private Weather weatherObjectConverter(String st) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        return mapper.readValue(st, Weather.class);
    }
}
