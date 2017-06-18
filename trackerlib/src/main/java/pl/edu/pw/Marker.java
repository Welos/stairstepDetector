package pl.edu.pw;

public class Marker {
    private int id;
    private int value;
    private long time;

    public Marker() {
    }

    public Marker(boolean value) {
        if (value)
            this.value = 1;
        else
            this.value = 0;

        this.time = System.currentTimeMillis();
    }
    public Marker(int value) {

        this.value = value;
        this.time = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
