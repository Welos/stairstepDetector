package pl.edu.pw;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.util.LinkedList;
import java.util.List;

//TODO Two buffers of samples (capacities as parameters). Implement classification (should return enum or sth) and training logic.
public class Tracker {
    private final static int DEFAULT_CAPACITY = 10;

    private static Tracker instance = new Tracker();
    public static Tracker getInstance() {
        return instance;
    }

    private NeuralNetwork neuralNetwork;
    private ShiftRegister locationRegister;
    private ShiftRegister accelerationRegister;

    private Tracker() {
        neuralNetwork = new MultiLayerPerceptron();
        locationRegister = new ShiftRegister(DEFAULT_CAPACITY);
        accelerationRegister = new ShiftRegister(DEFAULT_CAPACITY);
    }

    public void classify() {
        double[] data = collectRegistersData();
        neuralNetwork.setInput(data);
        neuralNetwork.calculate();
        neuralNetwork.getOutput();
        //TODO Consider what state should be returned.
    }

    private double[] collectRegistersData() {
        List<Double> dataList = new LinkedList<>();
        dataList.addAll(locationRegister.toVector());
        dataList.addAll(accelerationRegister.toVector());

        return unwrap(dataList);
    }

    private double[] unwrap(List<Double> dataList) {
        double[] dataArray = new double[dataList.size()];
        for (int i = 0; i < dataArray.length; ++i) {
            dataArray[i] = dataList.get(i);
        }
        return dataArray;
    }

    public void train() {
        DataSet dataSet = prepareTrainingSet();
    }

    private DataSet prepareTrainingSet() {
        return new DataSet(0);
    }

    public void save(String filePath) {
        neuralNetwork.save(filePath);
    }

    public void load(String filePath) {
        neuralNetwork = NeuralNetwork.createFromFile(filePath);
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public void shiftLocationRegister(Location value) {
        locationRegister.shift(value);
    }

    public void setLocationRegisterCapacity(int capacity) {
        locationRegister = new ShiftRegister(capacity);
    }

    public void shiftAccelerationRegister(Acceleration value) {
        accelerationRegister.shift(value);
    }

    public void setAccelerationRegisterCapacity(int capacity) {
        accelerationRegister = new ShiftRegister(capacity);
    }
}
