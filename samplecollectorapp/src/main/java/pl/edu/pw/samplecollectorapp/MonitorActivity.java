package pl.edu.pw.samplecollectorapp;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pl.edu.pw.Acceleration;
import pl.edu.pw.Location;
import pl.edu.pw.Marker;

import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_TIMESTAMP;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_X;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_Y;
import static pl.edu.pw.DatabaseSchema.COLUMN_NAME_Z;
import static pl.edu.pw.DatabaseSchema.DATABASE_NAME;

public class MonitorActivity extends AppCompatActivity {
    private static final String TAG = "MonitorActivity";
    private static final long UPDATE_DELAY = 1000;

    private Button mButtonExport;
    private EditText mEditTextAcceleration;
    private ProgressBar mProgressBar;

    private CollectorService mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            CollectorService.CollectorBinder binder = (CollectorService.CollectorBinder) service;
            mService = binder.getService();
            updateSensorValuesHandler.postDelayed(updateSensorValues, UPDATE_DELAY);
            Log.i(TAG, "Service connected.");
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            updateSensorValuesHandler.removeCallbacks(updateSensorValues);
            Log.i(TAG, "Service disconnected.");
        }
    };
    private Handler updateSensorValuesHandler = new Handler();
    private Runnable updateSensorValues = new Runnable() {
        @Override
        public void run() {
            Acceleration acceleration = mService.getAcceleration();

            if (acceleration != null) {
                mEditTextAcceleration.setText(acceleration.toString());
            }
            else {
                mEditTextAcceleration.setText(R.string.not_available);
            }

            updateSensorValuesHandler.postDelayed(this, UPDATE_DELAY);
        }
    };

    private class ExportDatabaseTask extends AsyncTask<Void, Integer, Void> {
        protected ExportDatabaseTask() {
            mButtonExport.setText(R.string.exporting);
            mButtonExport.setEnabled(false);

        }

        @Override
        protected Void doInBackground(Void... parameters) {
            mService.stopCollecting();
            exportDatabase();
            mService.startCollecting();
            return null;
        }

        private void exportDatabase() {
            try {
                Log.i(TAG, "Exporting database to external storage.");
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "/data/" + getPackageName() + "/databases/" + DATABASE_NAME;
                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(sd, DATABASE_NAME);

                    copy(currentDB, backupDB);
                }
                else {
                    Log.w(TAG, "Permission denied.");
                    askForPermission();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            mProgressBar.setProgress(0);
        }

        private void copy(File src, File dst) throws IOException {
            if (src.exists()) {
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dst);
                Log.e(TAG,dst.getAbsolutePath());

                byte[] buffer = new byte[1024];
                long sent = 0;
                long file_length = src.length();
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                    sent += length;
                    publishProgress((int) ((float) sent / file_length * 100));
                }

                fos.flush();

                fis.close();
                fos.close();
                Log.i(TAG, "Exported database to external storage.");
                Toast.makeText(MonitorActivity.this, "exported", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.w(TAG, "Database doesn't exist.");
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Void result) {
            mButtonExport.setText(R.string.export_database_to_external_storage);

            mButtonExport.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_monitor);

            mEditTextAcceleration = (EditText) findViewById(R.id.editTextAcceleration);
            mButtonExport = (Button) findViewById(R.id.buttonExport);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

            Intent intent = new Intent(this, CollectorService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            Intent startServiceIntent = new Intent(this, CollectorService.class);
            this.startService(startServiceIntent);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void mButtonExportOnClick(View view) {
        new ExportDatabaseTask().execute();
    }

    public void walk_buttonOnClick(View view){
        Marker marker = new Marker(1);
        CollectorService.getDatabaseHandler().addNewMarkerPoint(marker);
    }
    public void run_buttonOnClick(View view){
        Marker marker = new Marker(2);
        CollectorService.getDatabaseHandler().addNewMarkerPoint(marker);
    }
    public void up_buttonOnClick(View view){
        Marker marker = new Marker(3);
        CollectorService.getDatabaseHandler().addNewMarkerPoint(marker);
    }
    public void down_buttonOnClick(View view){
        Marker marker = new Marker(4);
        CollectorService.getDatabaseHandler().addNewMarkerPoint(marker);
    }
    public void end_buttonOnClick(View view){
        Marker marker = new Marker(0);
        CollectorService.getDatabaseHandler().addNewMarkerPoint(marker);

    }

    private void askForPermission() {
        PermissionEverywhere.getPermission(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                0,
                "SampleCollector",
                "This app needs an external storage permission",
                R.drawable.ic_directions_bus_red_a700_24dp)
                .enqueue(new PermissionResultCallback() {
                    @Override
                    public void onComplete(PermissionResponse permissionResponse) {}
                });
    }
}
