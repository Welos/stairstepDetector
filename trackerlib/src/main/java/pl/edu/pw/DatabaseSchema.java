package pl.edu.pw;

public class DatabaseSchema {
    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "samplecollectordb";

    // Location table name
    public static final String TABLE_LOCATION = "loc";
    // Acceleration table name
    public static final String TABLE_ACCELERATION = "acc";
    // Marker table name
    public static final String TABLE_MARKER = "mar";

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_TIMESTAMP = "time";

    // Location Table Columns names
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGITUDE = "longitude";
    public static final String COLUMN_NAME_SPEED = "speed";

    // Acceleration Table Columns names
    public static final String COLUMN_NAME_X = "x";
    public static final String COLUMN_NAME_Y = "y";
    public static final String COLUMN_NAME_Z = "z";

    // Marker Table Columns names
    public static final String COLUMN_NAME_ID_MAR = "id";
    public static final String COLUMN_NAME_VALUE = "value";
}
