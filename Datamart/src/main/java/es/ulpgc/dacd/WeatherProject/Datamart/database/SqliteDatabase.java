package es.ulpgc.dacd.WeatherProject.Datamart.database;

import es.ulpgc.dacd.WeatherProject.Datamart.database.databaseTables.DatabaseTable;
import es.ulpgc.dacd.WeatherProject.Datamart.database.databaseTables.SqliteMaxTemperatureTable;
import es.ulpgc.dacd.WeatherProject.Datamart.database.databaseTables.SqliteMinTemperatureTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteDatabase implements Database {
    private final SqliteMaxTemperatureTable MaxTemperatureTable;
    private final SqliteMinTemperatureTable MinTemperatureTable;

    public SqliteDatabase(String url) {
        this.MaxTemperatureTable = new SqliteMaxTemperatureTable(url);
        this.MinTemperatureTable = new SqliteMinTemperatureTable(url);
    }


    public DatabaseTable getMinTemperatureTable() {
        return MinTemperatureTable;
    }

    public static Connection getConnection(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public DatabaseTable getMaxTemperatureTable() {
        return MaxTemperatureTable;
    }
}
