package es.ulpgc.dacd.WeatherProject.TemperatureService.Control;

import es.ulpgc.dacd.WeatherProject.Datamart.database.SqliteDatabase;
import es.ulpgc.dacd.WeatherProject.TemperatureService.view.WebServiceSparkGetFunctions;

public class Controller {
    private final String url;

    public Controller(String url) {
        this.url = url;
    }

    public void execute(){
        WebServiceSparkGetFunctions webService = new WebServiceSparkGetFunctions(new SqliteDatabase(url));
        webService.startGetSparkFunctions();
    }
}
