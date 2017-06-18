package pl.edu.pw.samplecollectorapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import pl.edu.pw.Acceleration;
import pl.edu.pw.Location;

public class CollectorService extends Service {
    private static final String TAG = "CollectorService";
    private final IBinder mBinder = new CollectorBinder();

    private static DatabaseHandler sDatabaseHandler;
    private AccelerationCollector mAccelerationCollector;


    public class CollectorBinder extends Binder {
        CollectorService getService() {
            // Return this instance of LocalService so clients can call public methods
            return CollectorService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("CollectorService", "Bound.");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            sDatabaseHandler = new DatabaseHandler(this);

            mAccelerationCollector = new AccelerationCollector(this);

            startCollecting();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCollecting();
    }

    public static DatabaseHandler getDatabaseHandler() {
        return sDatabaseHandler;
    }

    public void startCollecting() {
        if (mAccelerationCollector == null ) {
            Log.w(TAG, "Collector service is not available...");
            return;
        }
        mAccelerationCollector.startCollecting();
        Log.i(TAG, "Started.");
    }

    public void stopCollecting() {
        if (mAccelerationCollector == null) {
            Log.w(TAG, "Collector service is not available...");
            return;
        }
        mAccelerationCollector.stopCollecting();
        Log.i(TAG, "Stopped.");
    }

    public Acceleration getAcceleration() {
        if (mAccelerationCollector == null) return null;
        return mAccelerationCollector.getAcceleration();
    }

}