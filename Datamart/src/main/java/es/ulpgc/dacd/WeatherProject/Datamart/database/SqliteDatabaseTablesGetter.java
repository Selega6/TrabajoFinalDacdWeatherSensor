package es.ulpgc.dacd.WeatherProject.Datamart.database;

import es.ulpgc.dacd.WeatherProject.Datamart.database.databaseTables.SqliteMaxTemperatureTable;
import es.ulpgc.dacd.WeatherProject.Datamart.database.databaseTables.SqliteMinTemperatureTable;

public class SqliteDatabaseTablesGetter implements DatabaseTablesGetter{
    private final SqliteMaxTemperatureTable MaxTemperatureTable;
    private final SqliteMinTemperatureTable MinTemperatureTable;

    public SqliteDatabaseTablesGetter(String url) {
        this.MaxTemperatureTable = new SqliteMaxTemperatureTable(url);
        this.MinTemperatureTable = new SqliteMinTemperatureTable(url);
    }

    public SqliteMinTemperatureTable getMinTemperatureTable() {
        return MinTemperatureTable;
    }

    public SqliteMaxTemperatureTable getMaxTemperatureTable() {
        return MaxTemperatureTable;
    }
}