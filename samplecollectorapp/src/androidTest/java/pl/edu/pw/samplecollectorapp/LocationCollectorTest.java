package pl.edu.pw.samplecollectorapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;

import pl.edu.pw.Location;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class LocationCollectorTest {
    private LocationCollector locationCollector;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        locationCollector = new LocationCollector(appContext);
    }

    @Test
    public void startCollecting() throws Exception {
        locationCollector.startCollecting();
        await().atMost(10, SECONDS).until(getLocation(), notNullValue());
    }

    private Callable<Location> getLocation() {
        return new Callable<Location>() {
            public Location call() throws Exception {
                return locationCollector.getLocation();
            }
        };
    }
}
