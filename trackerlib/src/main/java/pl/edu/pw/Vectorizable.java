package pl.edu.pw;

import java.util.List;

/**
 * Adapter interface for Neuroph input.
 */
public interface Vectorizable {
    List<Double> toVector();
}
