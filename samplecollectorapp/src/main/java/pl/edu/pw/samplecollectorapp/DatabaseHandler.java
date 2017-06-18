package pl.edu.pw.samplecollectorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.edu.pw.Acceleration;
import pl.edu.pw.Location;
import pl.edu.pw.Marker;

import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_ID;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_LATITUDE;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_LONGITUDE;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_SPEED;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_TIMESTAMP;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_VALUE;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_X;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_Y;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_Z;
import static pl.edu.pw.DatabaseSchema.DATABASE_NAME;
import static pl.edu.pw.DatabaseSchema.DATABASE_VERSION;
import static pl.edu.pw.DatabaseSchema.TABLE_ACCELERATION;
import static pl.edu.pw.DatabaseSchema.TABLE_LOCATION;
import static pl.edu.pw.DatabaseSchema.TABLE_MARKER;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_ACCELERATION_TABLE = "CREATE TABLE " + TABLE_ACCELERATION + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_X + " REAL, "
                + COLUMN_NAME_Y + " REAL, "
                + COLUMN_NAME_Z + " REAL, "
                + COLUMN_NAME_TIMESTAMP + " INTEGER);";

        String CREATE_MARKER_TABLE = "CREATE TABLE " + TABLE_MARKER + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_VALUE + " INTEGER, "
                + COLUMN_NAME_TIMESTAMP + " INTEGER);";

        db.execSQL(CREATE_ACCELERATION_TABLE);
        db.execSQL(CREATE_MARKER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCELERATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKER);

        // Create tables again
        onCreate(db);
    }


    // Adding new point to Acceleration
    public void addNewAccelerationPoint(Acceleration acceleration) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_acc = new ContentValues();
        values_acc.put(COLUMN_NAME_X, acceleration.getX());
        values_acc.put(COLUMN_NAME_Y, acceleration.getY());
        values_acc.put(COLUMN_NAME_Z, acceleration.getZ());
        values_acc.put(COLUMN_NAME_TIMESTAMP, acceleration.getTime());

        // Inserting Row
        try {
            db.insert(TABLE_ACCELERATION, null, values_acc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public void addNewMarkerPoint(Marker marker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values_marker = new ContentValues();
        values_marker.put(COLUMN_NAME_VALUE, marker.getValue());
        values_marker.put(COLUMN_NAME_TIMESTAMP, marker.getTime());

        // Inserting Row
        try {
            db.insert(TABLE_MARKER, null, values_marker);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close(); // Closing database connection
    }
}