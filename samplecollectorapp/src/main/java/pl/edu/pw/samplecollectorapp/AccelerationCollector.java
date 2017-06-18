package pl.edu.pw.samplecollectorapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import pl.edu.pw.Acceleration;

import static android.content.Context.SENSOR_SERVICE;

public class AccelerationCollector implements SensorEventListener {
    private static final String TAG = "AccelerationCollector";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Acceleration mAcceleration;

    public AccelerationCollector(Context context) {
        Log.i(TAG, "Initializing...");
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.i(TAG, "Initialized.");
    }

    public void startCollecting() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        Log.i(TAG, "Collecting started.");
    }

    public void stopCollecting() {
        mSensorManager.unregisterListener(this);
        Log.i(TAG, "Collecting stopped.");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        mAcceleration = new Acceleration(event.values);

        CollectorService.getDatabaseHandler().addNewAccelerationPoint(mAcceleration);
        //Log.i(TAG, mAcceleration.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public Acceleration getAcceleration() {
        return mAcceleration;
    }
}
