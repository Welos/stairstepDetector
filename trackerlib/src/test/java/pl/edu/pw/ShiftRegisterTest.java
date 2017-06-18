package pl.edu.pw;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ShiftRegisterTest {
    private class Integer implements Vectorizable {
        private int value = 0;

        public Integer(int value) {
            this.value = value;
        }

        @Override
        public List<Double> toVector() {
            return Collections.singletonList((double) value);
        }

        public int getValue() {
            return value;
        }
    }

    @Test
    public void shift() throws Exception {
        int capacity = 10;
        ShiftRegister shiftRegister = new ShiftRegister(capacity);
        Integer wrapper = new Integer(0);

        shiftRegister.shift(wrapper);
        List<Double> expected = Collections.singletonList((double) wrapper.getValue());
        List<Double> actual = shiftRegister.toVector();

        assertEquals(expected, actual);

        for (int i = 1; i < 10; i++) {
            wrapper = new Integer(i);
            shiftRegister.shift(wrapper);
        }

        expected = Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        actual = shiftRegister.toVector();
        assertEquals(expected, actual);
    }

    @Test
    public void toVector() throws Exception {
        emptyArrayTest();
        oneElementTest();
        multipleElementsTest(20, 100);
    }

    private void emptyArrayTest() {
        int capacity = 10;
        ShiftRegister shiftRegister = new ShiftRegister(capacity);

        List<Double> expected = new ArrayList<>(capacity);
        List<Double> actual = shiftRegister.toVector();

        assertEquals(expected, actual);
    }

    private void oneElementTest() {
        ShiftRegister shiftRegister = new ShiftRegister(10);
        int value = 0;
        shiftRegister.shift(new Integer(value));

        List<Double> expected = Collections.singletonList((double) value);
        List<Double> actual = shiftRegister.toVector();

        assertEquals(expected, actual);
    }

    private void multipleElementsTest(int capacity, int shifts) {
        ShiftRegister shiftRegister = new ShiftRegister(capacity);
        for (int i = 0; i < shifts; i++) {
            shiftRegister.shift(new Integer(i));

            List<Double> actual = shiftRegister.toVector();

            if (i >= capacity) {
                assertEquals(Double.valueOf(i), actual.get(capacity - 1));
                assertEquals(Double.valueOf(i + 1 - capacity), actual.get(0));
            }
        }
    }
}