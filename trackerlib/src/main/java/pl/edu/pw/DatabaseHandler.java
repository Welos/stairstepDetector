package pl.edu.pw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_LATITUDE;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_LONGITUDE;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_SPEED;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_TIMESTAMP;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_X;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_Y;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_Z;

public class DatabaseHandler {
    private static final DatabaseHandler ourInstance = new DatabaseHandler();

    static DatabaseHandler getInstance() {
        return ourInstance;
    }

    private DatabaseHandler() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Location> selectRideLocationSamples(String databaseFileName) {
        return selectLocationSamplesUsingQuery(databaseFileName,
                "SELECT * FROM (" +
                        "SELECT * FROM loc l " +
                        "INNER JOIN mar m ON l.time < m.time " +
                        "GROUP BY l.time " +
                        "HAVING MIN(m.time)) " +
                        "WHERE value == 0;");
    }

    public List<Location> selectNonRideLocationSamples(String databaseFileName) {
        return selectLocationSamplesUsingQuery(databaseFileName,
                "SELECT * FROM (" +
                        "SELECT * FROM loc l " +
                        "INNER JOIN mar m ON l.time < m.time " +
                        "GROUP BY l.time " +
                        "HAVING MIN(m.time)) " +
                        "WHERE value == 1;");
    }

    private List<Location> selectLocationSamplesUsingQuery(String databaseFileName, String query) {
        List<Location> locations = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFileName);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            Location location;
            while (resultSet.next()) {
                location = new Location();
                location.setLatitude(resultSet.getDouble(COLUMN_NAME_LATITUDE));
                location.setLongitude(resultSet.getDouble(COLUMN_NAME_LONGITUDE));
                location.setTime(resultSet.getLong(COLUMN_NAME_TIMESTAMP));
                location.setSpeed(resultSet.getFloat(COLUMN_NAME_SPEED));

                locations.add(location);
            }

            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return locations;
    }

    public List<Acceleration> selectNearestAccelerationSamples(String databaseFileName, Location location, long accelerationSamplesTimeRange) {
        List<Acceleration> accelerations = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFileName);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM acc " +
                    "WHERE time BETWEEN " + (location.getTime() - accelerationSamplesTimeRange) +
                    " AND " + (location.getTime() + accelerationSamplesTimeRange) + ";");
            Acceleration acceleration;
            while (resultSet.next()) {
                acceleration = new Acceleration();
                acceleration.setX(resultSet.getFloat(COLUMN_NAME_X));
                acceleration.setY(resultSet.getFloat(COLUMN_NAME_Y));
                acceleration.setZ(resultSet.getFloat(COLUMN_NAME_Z));
                acceleration.setTime(resultSet.getLong(COLUMN_NAME_TIMESTAMP));
                accelerations.add(acceleration);
            }

            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return accelerations;
    }
}
