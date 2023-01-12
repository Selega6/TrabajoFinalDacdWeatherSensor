package es.ulpgc.dacd.WeatherProject.TemperatureService.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import es.ulpgc.dacd.WeatherProject.Feeder.model.Weather;
import es.ulpgc.dacd.WeatherProject.Datamart.database.Database;
import es.ulpgc.dacd.WeatherProject.Datamart.database.SqliteDatabase;
import es.ulpgc.dacd.WeatherProject.TemperatureService.model.WeatherForAPI;

import java.util.List;

import static spark.Spark.get;

public class WebService {
    private final Database database;

    public WebService(SqliteDatabase database){
        this.database = database;
    }
    public void startGetSparkFunctions() {
        get("/v1/places/with-max-temperature", (req, res) ->
        {
            res.type("application/json");
            String dateFrom = req.queryParams("from");
            String dateTo = req.queryParams("to");
            List<Weather> weatherList = database.tablesGetter().getMaxTemperatureTable().getDataTableUsingDates(dateFrom, dateTo);
            return weatherList.stream().map(this::toWeatherForAPIForMax).toList();
        });
        get("/v1/places/with-min-temperature", (req, res) ->
        {
            res.type("application/json");
            String dateFrom = req.queryParams("from");
            String dateTo = req.queryParams("to");
            List<Weather> weatherList = database.tablesGetter().getMinTemperatureTable().getDataTableUsingDates(dateFrom, dateTo);
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
