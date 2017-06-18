package pl.edu.pw;

import java.util.ArrayList;
import java.util.List;

public class DynamicRegister extends Register {
    @Override
    public void shift(Vectorizable element) {
        elements.add(element);
    }

    public List<Vectorizable> flush() {
        return new ArrayList<>(elements);
    }
}