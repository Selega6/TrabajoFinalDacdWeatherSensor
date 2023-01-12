package es.ulpgc.dacd.WeatherProject.TemperatureService.Control;

import es.ulpgc.dacd.WeatherProject.Datamart.database.SqliteDatabase;
import es.ulpgc.dacd.WeatherProject.TemperatureService.view.WebService;

public class Controller {
    private final String url;

    public Controller(String url) {
        this.url = url;
    }

    public void execute(){
        WebService webService = new WebService(new SqliteDatabase(url));
        webService.startGetSparkFunctions();
    }
}
