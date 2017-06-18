package pl.edu.pw;

import java.util.LinkedList;
import java.util.List;


/**
 * Class simulating shift register. It contains buffer of elements
 * which implement Vectorizable interface. Used as adapter from measured samples from sensors
 * to Neuroph input values.
 */
public class ShiftRegister extends Register implements Vectorizable {
    /**
     * Size of elements buffer.
     */
    protected int capacity;

    public ShiftRegister(int capacity) {
        super();
        this.capacity = capacity;
    }

    /**
     * It adds an element to the end of the buffer and shifts all the other elements.
     * @param element element to add to the buffer
     */
    @Override
    public void shift(Vectorizable element) {
        if (elements.size() == capacity) {
            elements.remove();
        }
        elements.add(element);
    }

    @Override
    public List<Double> toVector() {
        List<Double> result = new LinkedList<>();

        for (Vectorizable element: elements) {
            result.addAll(element.toVector());
        }

        return result;
    }
}
