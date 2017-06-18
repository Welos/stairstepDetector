package pl.edu.pw;

import java.util.Arrays;
import java.util.List;


/**
 * Wrapper for accelerometer data. Can be transformed into Neuroph compatible vector.
 */
public class Acceleration implements Vectorizable {
    private int id;
    private float x;
    private float y;
    private float z;
    private long time;

    public Acceleration() {
    }

    public  Acceleration(float[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
        time = System.currentTimeMillis();
    }

    @Override
    public List<Double> toVector() {
        return Arrays.asList((double) x, (double) y, (double) z);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ')';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
