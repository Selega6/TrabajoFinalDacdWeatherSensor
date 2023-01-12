package es.ulpgc.dacd.WeatherProject.Datamart.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteDatabase implements Database {
    private static String url;
    private final DatabaseTablesGetter databaseTables;

    public SqliteDatabase(String url) {
        SqliteDatabase.url = url;
        this.databaseTables = new DatabaseTablesGetter(url);
    }


    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(SqliteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public DatabaseTablesGetter tablesGetter() {
        return databaseTables;
    }
}
