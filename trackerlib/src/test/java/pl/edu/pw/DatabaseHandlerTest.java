package pl.edu.pw;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DatabaseHandlerTest {
    @Test
    public void selectRideLocationSamples() throws Exception {
        List<Location> locations = DatabaseHandler.getInstance().selectRideLocationSamples("trackerlib/src/test/res/test.db");

        int speedCounter = 0;
        for (Location location: locations) {
            if (location.getSpeed() > 0) {
                ++speedCounter;
            }
        }

        Assert.assertTrue(speedCounter >= locations.size() - speedCounter);
    }

    @Test
    public void selectNonRideLocationSamples() throws Exception {
        List<Location> locations = DatabaseHandler.getInstance().selectNonRideLocationSamples("trackerlib/src/test/res/test.db");

        int speedCounter = 0;
        for (Location location: locations) {
            if (location.getSpeed() > 0) {
                ++speedCounter;
            }
        }

        Assert.assertTrue(speedCounter <= locations.size() - speedCounter);
    }

    @Test
    public void selectNearestAccelerationSamples() throws Exception {
        String pathToDb = "trackerlib/src/test/res/test.db";
        long range = 1000;
        List<Location> locations = DatabaseHandler.getInstance().selectRideLocationSamples(pathToDb);

        for (int i = 0; i < locations.size(); ++i) {
            if (i % 50 == 0) {
                Location l = locations.get(i);
                List<Acceleration> accelerations =
                        DatabaseHandler.getInstance().selectNearestAccelerationSamples(
                                pathToDb,
                                l,
                                range);

                for (Acceleration a: accelerations) {
                    Assert.assertTrue(a.getTime() <= l.getTime() + range && a.getTime() >= l.getTime() - range);
                }
            }
        }
    }
}