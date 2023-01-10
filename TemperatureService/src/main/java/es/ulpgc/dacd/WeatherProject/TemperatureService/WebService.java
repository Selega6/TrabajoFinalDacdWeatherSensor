package es.ulpgc.dacd.WeatherProject.TemperatureService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import es.ulpgc.dacd.WeatherProject.DataLakeFeeder.model.Weather;
import es.ulpgc.dacd.WeatherProject.DataMartProvider.DataBase;
import es.ulpgc.dacd.WeatherProject.DataMartProvider.SqliteDataBase;

import java.util.List;

import static spark.Spark.get;

public class WebService {
    private final DataBase database;
    private final String url;

    public WebService(SqliteDataBase database, String url){
        this.database = database;
        this.url = url;
    }
    public void start() {
        get("/v1/places/with-max-temperature", (req, res) ->
        {
            res.type("application/json");
            String dateFrom = req.queryParams("from");
            String dateTo = req.queryParams("to");
            List<Weather> weatherList = database.getDataFromMaxTemperatureTable(url, dateFrom, dateTo);
            return weatherList.stream().map(this::toWeatherForAPIForMax).toList();
        });
        get("/v1/places/with-min-temperature", (req, res) ->
        {
            res.type("application/json");
            String dateFrom = req.queryParams("from");
            String dateTo = req.queryParams("to");
            List<Weather> weatherList = database.getDataFromMinTemperatureTable(url, dateFrom, dateTo);
            return weatherList.stream().map(this::toWeatherForAPIForMin).toList();
        });
    }

    private String toWeatherForAPIForMax(Weather o) {
            WeatherForAPI weatherForAPI = new WeatherForAPI(o.timeStamp(), o.place(), o.station(), o.maxTemperature());
            return fromWeatherForApiToJson(weatherForAPI);
    }
    private String toWeatherForAPIForMin(Weather o) {
        WeatherForAPI weatherForAPI = new WeatherForAPI(o.timeStamp(), o.place(), o.station(), o.minTemperature());
        return fromWeatherForApiToJson(weatherForAPI);
    }


    private String fromWeatherForApiToJson(WeatherForAPI weatherForAPI) {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        try {
            return mapper.writeValueAsString(weatherForAPI) + "\n";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
