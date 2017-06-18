package pl.edu.pw;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Register {
    protected Queue<Vectorizable> elements;

    public Register() {
        elements = new LinkedList<>();
    }

    public abstract void shift(Vectorizable element);
}
