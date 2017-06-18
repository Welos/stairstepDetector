package pl.edu.pw;

import java.util.Arrays;
import java.util.List;

/**
 * Wrapper for GPS data. Can be transformed into Neuroph compatible vector.
 */
public class Location implements Vectorizable {
    private int id;
    private double latitude;
    private double longitude;
    private long time;
    private float speed;


    public Location() {}

    @Override
    public List<Double> toVector() {
        return Arrays.asList(longitude, latitude);
    }

    @Override
    public String toString() {
        return "(" + latitude +
                ", " + longitude + ")";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
