package pl.edu.pw.samplecollectorapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;

import pl.edu.pw.Acceleration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class AccelerationCollectorTest {
    private AccelerationCollector accelerationCollector;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        accelerationCollector = new AccelerationCollector(appContext);
    }

    @Test
    public void startCollecting() throws Exception {
        accelerationCollector.startCollecting();
        await().atMost(2, SECONDS).until(getAcceleration(), notNullValue());
    }

    private Callable<Acceleration> getAcceleration() {
        return new Callable<Acceleration>() {
            public Acceleration call() throws Exception {
                return accelerationCollector.getAcceleration();
            }
        };
    }
}
