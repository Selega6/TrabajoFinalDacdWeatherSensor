package es.ulpgc.dacd.WeatherProject.DataMartProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteDataBase implements DataBase{
    private final SqliteMaxTemperatureTable MaxTemperatureTable;
    private final SqliteMinTemperatureTable MinTemperatureTable;

    public SqliteDataBase(String url) throws SQLException {
        this.MaxTemperatureTable = new SqliteMaxTemperatureTable(url);
        this.MinTemperatureTable = new SqliteMinTemperatureTable(url);
    }

    public static Connection getConnection(String url) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url);
            } catch (SQLException ex) {
                Logger.getLogger(SqliteDataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        return connection;
    }


    public DataBaseTable getMinTemperatureTable() {
        return MinTemperatureTable;
    }

    public DataBaseTable getMaxTemperatureTable() {
        return MaxTemperatureTable;
    }
}
