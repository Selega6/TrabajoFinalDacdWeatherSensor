package es.ulpgc.dacd.WeatherProject.Datamart.database.databaseTables;

import es.ulpgc.dacd.WeatherProject.Datamart.database.SqliteDatabase;
import es.ulpgc.dacd.WeatherProject.Feeder.model.Weather;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SqliteMaxTemperatureTable implements DatabaseTable {

    public final String url;

    public SqliteMaxTemperatureTable(String url) {
        this.url = url;
    }

    @Override
    public void createTable() {
        final String SQL = "CREATE TABLE IF NOT EXISTS MaxTemperatureTable (Date TEXT PRIMARY KEY, Time TEXT, Place TEXT, Station Text, MaxTemperature REAL);";
        try (Connection con = SqliteDatabase.getConnection(); Statement statement = con.createStatement()) {
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertIntoTable(Weather weather) {
        final String SQL = "INSERT OR REPLACE INTO MaxTemperatureTable VALUES(date(?), ?, ?, ?, ?)";
        LocalDate date = LocalDate.of(weather.timeStamp().getYear(), weather.timeStamp().getMonth(), weather.timeStamp().getDayOfYear());
        LocalTime time = LocalTime.of(weather.timeStamp().getHour(), weather.timeStamp().getMinute(), weather.timeStamp().getSecond());
        try (Connection con = SqliteDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {
            prepareStatementsForInsert(weather, date, time, ps);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<es.ulpgc.dacd.WeatherProject.Feeder.model.Weather> getDataTableUsingDates(String dateFrom, String dateTo) {
        String sql = "SELECT Date, Time, Place, Station, MaxTemperature FROM MaxTemperatureTable WHERE Date BETWEEN date(?) AND date(?)";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate dateFromFormatted = LocalDate.parse(dateFrom, formatter);
        LocalDate dateToFormatted = LocalDate.parse(dateTo, formatter);
        List<Weather> result = new ArrayList<>();
        try (Connection conn = SqliteDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            prepareTimeForSelection(pstmt, dateFromFormatted.toString(), dateToFormatted.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                addingWeathersToResult(result, rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    private static void addingWeathersToResult(List<Weather> result, ResultSet rs) throws SQLException {
        LocalDate time = LocalDate.parse(rs.getString("Date"));
        LocalTime time2 = LocalTime.parse(rs.getString("Time"));
        LocalDateTime finalTime = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time2.getHour(), time2.getMinute(), time2.getSecond());
        result.add(new Weather(finalTime, rs.getString("Place"), rs.getString("Station"), 0.0, 0.0, rs.getDouble("MaxTemperature")));
    }

    private static void prepareTimeForSelection(PreparedStatement pstmt, String dateFromFormatted, String dateToFormatted) throws SQLException {
        pstmt.setString(1, dateFromFormatted);
        pstmt.setString(2, dateToFormatted);
    }

    private static void prepareStatementsForInsert(Weather weather, LocalDate date, LocalTime time, PreparedStatement ps) throws SQLException {
        ps.setString(1, date.toString());
        ps.setString(2, String.valueOf(time));
        ps.setString(3, weather.place());
        ps.setString(4, weather.station());
        ps.setDouble(5, weather.maxTemperature());
    }

}
