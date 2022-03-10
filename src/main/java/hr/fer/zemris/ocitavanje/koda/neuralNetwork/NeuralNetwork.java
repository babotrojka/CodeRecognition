package hr.fer.zemris.ocitavanje.koda.neuralNetwork;

import hr.fer.zemris.ocitavanje.koda.Constants;
import hr.fer.zemris.ocitavanje.koda.data.ImageData;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorPreservingVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Class representing neural network
 */
public class NeuralNetwork {
    /**
     * Input layer
     */
    private List<Neuron> input;

    /**
     * List of hidden layers
     */
    private List<List<Neuron>> hidden;

    /**
     * Output layer
     */
    private List<Neuron> output;

    /**
     * Index of output layer
     */
    private int outputLayerIndex;

    /**
     * Activation function
     */
    private UnaryOperator<Double> activationFunction;

    /**
     * Training coefficient
     */
    private double ETA = Constants.ETA;


    /**
     * Mean squared error of model
     */
    private Double meanSquaredError;

    /**
     * Init with random values for weights
     *
     * @param inputSize
     * @param outputSize
     * @param hiddenSizes
     */
    public NeuralNetwork(UnaryOperator<Double> activationFunction, int inputSize, int outputSize, int... hiddenSizes) {
        this.activationFunction = activationFunction;

        Random r = new Random();

        input = new ArrayList<>();
        for (int i = 0; i < inputSize; i++) {//init input
            input.add(new Neuron(0));
        }

        output = new ArrayList<>();
        for (int i = 0; i < outputSize; i++) //init output
            output.add(new Neuron(r.nextGaussian() * 0.1));

        int nextSize = hiddenSizes.length == 0 ? outputSize : hiddenSizes[0];

        for (int i = 0; i < inputSize; i++) //weights from input
            for (int j = 0; j < nextSize; j++)
                input.get(i).addWeightTo(r.nextGaussian() * 0.1);

        hidden = new ArrayList<>();
        for (int i = 0; i < hiddenSizes.length; i++) {
            hidden.add(new ArrayList<>());
            for (int j = 0; j < hiddenSizes[i]; j++) {
                hidden.get(i).add(new Neuron(r.nextGaussian() * 0.1)); //hidden neuron
                nextSize = i < hiddenSizes.length - 1 ? hiddenSizes[i + 1] : outputSize; //weights from hidden
                for (int k = 0; k < nextSize; k++)
                    hidden.get(i).get(j).addWeightTo(r.nextGaussian() * 0.1);
            }
        }
        outputLayerIndex = hiddenSizes.length + 1;
    }

    public NeuralNetwork(int inputSize, int outputSize, int... hiddenSizes) {
        this(d -> 1 / (1 + Math.exp(-d)), inputSize, outputSize, hiddenSizes);
    }

    public NeuralNetwork(UnaryOperator<Double> activationFunction, List<Double> inputValues, int outputSize, int... hiddenSizes) {
        this(activationFunction, inputValues.size(), outputSize, hiddenSizes);
        setInputValues(inputValues);
    }


    public NeuralNetwork(List<Double> inputValues, int outputSize, int... hiddenSizes) {
        this(d -> 1 / (1 + Math.exp(-d)), inputValues, outputSize, hiddenSizes);
    }

    public NeuralNetwork(NeuralNetwork other) {
        this(other.input.size(), other.output.size(), other.hidden.stream().mapToInt(List::size).toArray());
    }


    /**
     * Sets output of input layer
     * @param inputValues
     */
    public void setInputValues(List<Double> inputValues) {
        if(input.size() != inputValues.size())
            throw new IllegalArgumentException("Invalid size of input values");

        for(int i = 0; i < input.size(); i++)
            input.get(i).setOutput(inputValues.get(i));
    }

    /**
     * Gets vector of weights to a particular neuron.
     * Layer 0 is input layer and represents invalid input
     * Position starts from 0
     * @param layer
     * @param position
     * @return
     */
    private RealVector vectorOfWeightsToMe(int layer, int position) {
        if(layer == 0)
            throw new IllegalArgumentException("Layer cant be 0");
        if(layer > hidden.size() + 1)
            throw new IllegalArgumentException("Layer is out of bounds");

        List<Neuron> layerBefore = findLayer(layer - 1);

        if(position < 0 || position >= layerBefore.get(0).getNumberOfWeightsTo())
            throw new IllegalArgumentException("Position is out of bounds");

        double[] weights = new double[layerBefore.size()];
        for(int i = 0; i < layerBefore.size(); i++) {
            weights[i] = layerBefore.get(i).getWeightTo(position);
        }

        return MatrixUtils.createRealVector(weights);
    }

    /**
     * Vector of weights from given neuron
     * @param layer
     * @param position
     * @return
     */
    private RealVector vectorOfWeightsFromMe(int layer, int position) {
        if(layer == outputLayerIndex)
            throw new IllegalArgumentException("Output layer doesnt have out weights");
        if(layer < 0 || layer > outputLayerIndex)
            throw new IllegalArgumentException("Layer is out of bounds");

        List<Neuron> currentLayer = findLayer(layer);

        if(position < 0 || position >= currentLayer.size())
            throw new IllegalArgumentException("Position is out of bounds");

        return realVectorFromListDouble(currentLayer.get(position).getWeights_to());
    }

    /**
     * Sets weights of given neuron
     * @param layer
     * @param position
     * @param weights
     */
    private void setWeightsFromMe(int layer, int position, RealVector weights) {
        if(layer == outputLayerIndex)
            throw new IllegalArgumentException("Output layer doesnt have out weights");
        if(layer < 0 || layer > outputLayerIndex)
            throw new IllegalArgumentException("Layer is out of bounds");

        List<Neuron> currentLayer = findLayer(layer);

        if(position < 0 || position >= currentLayer.size())
            throw new IllegalArgumentException("Position is out of bounds");

        currentLayer.get(position).setWeights_to(Arrays.stream(weights.toArray()).boxed().collect(Collectors.toList()));
    }

    /**
     * Gets vector of outputs from given layer
     * @param layer
     * @return
     */
    private RealVector vectorOfOutputsFromLayer(int layer) {
        return realVectorFromListDouble(findLayer(layer).stream().map(Neuron::getOutput).collect(Collectors.toList()));
    }

    /**
     * Gets vector of weights 0 from given layer
     * @param layer
     * @return
     */
    private RealVector vectorOfWeights0(int layer) {
        return realVectorFromListDouble(findLayer(layer).stream().map(Neuron::getWeight_0).collect(Collectors.toList()));
    }

    /**
     * Sets weight0 of every neuron to given value
     * @param layer
     * @param weights0
     */
    private void setVectorOfWeights0(int layer, RealVector weights0) {
        List<Neuron> currentLayer = findLayer(layer);
        if(currentLayer.size() != weights0.getDimension())
            throw new IllegalArgumentException("Looking for length " + currentLayer.size() + ", provided is " + weights0.getDimension());

        for(int i = 0; i < currentLayer.size(); i++)
            currentLayer.get(i).setWeight_0(weights0.getEntry(i));
    }

    /**
     * Util function to create vector from list of doubles
     * @param nums
     * @return
     */
    public static RealVector realVectorFromListDouble(List<Double> nums) {
        double[] weights = new double[nums.size()];
        for(int i = 0; i < nums.size(); i++)
            weights[i] = nums.get(i);

        return MatrixUtils.createRealVector(weights);
    }

    /**
     * Gets RealMatrix of out weights from given layer to the next one
     * @param layer
     * @return
     */
    private RealMatrix matrixOfOutWeightsFromLayer(int layer) {
        if(layer == hidden.size() + 2)
            throw new IllegalArgumentException("Output layer doesnt have out weights");

        List<Neuron> neuronLayer = findLayer(layer);
        List<Neuron> nextLayer = findLayer(layer + 1);

        RealMatrix matrix = MatrixUtils.createRealMatrix(nextLayer.size(), neuronLayer.size());
        for(int i = 0; i < nextLayer.size(); i++) {
            matrix.setRowVector(i, vectorOfWeightsToMe(layer + 1, i));
        }
        return matrix;
    }


    /**
     * Constructs a vector of all weights in this neural network
     * For each layer, for each neuron, we append vectorOfWeightsFromMe
     * Then we add weights0 from next layer
     * @return
     */
    public RealVector vectorOfAllWeights() {
        RealVector v = MatrixUtils.createRealVector(new double[0]);
        for(int i = 0; i < outputLayerIndex; i++) {
            List<Neuron> currentLayer = findLayer(i);
            for(int j = 0; j < currentLayer.size(); j++)
                v = v.append(vectorOfWeightsFromMe(i, j));
            v = v.append(vectorOfWeights0(i + 1));
        }

        return v;
    }

    /**
     * Sets weights of this nn from given vector
     * @param weights
     */
    public void setWeightsFromVector(RealVector weights) {
        int weightsRead = 0;
        for(int i = 0; i < outputLayerIndex; i++) {
            int currentLayerSize = findLayer(i).size();
            int nextLayerSize = findLayer(i + 1).size();
            for(int j = 0; j < currentLayerSize; j++) {
                RealVector weightsFromMe = weights.getSubVector(weightsRead, nextLayerSize);
                weightsRead += nextLayerSize;
                setWeightsFromMe(i, j, weightsFromMe);
            }

            RealVector weightsOfNextLayer = weights.getSubVector(weightsRead, nextLayerSize);
            weightsRead += nextLayerSize;
            setVectorOfWeights0(i + 1, weightsOfNextLayer);
        }
    }

    /**
     * Sets output of given layer to vector values
     * @param vector
     * @param layer
     */
    private void setOutput(RealVector vector, int layer) {
        List<Neuron> neurons = findLayer(layer);
        if(neurons.size() != vector.getDimension())
            throw new IllegalArgumentException();

        for(int i = 0; i < neurons.size(); i++) {
            neurons.get(i).setOutput(vector.getEntry(i));
        }
    }

    /**
     * Performs activation function on every entry of RealVector
     * @param input
     */
    private void performActivationFunctionOnVector(RealVector input) {
        for(int i = 0; i < input.getDimension(); i++)
            input.setEntry(i, activationFunction.apply(input.getEntry(i)));
    }


    /**
     * Based on weights and output of previous layer, calculates output of given layer
     * @param layer
     * @return
     */
    private RealVector calculateAndSetOutputOfLayer(int layer) {
        if(layer == 0)
            return realVectorFromListDouble(input.stream().map(Neuron::getOutput).collect(Collectors.toList()));

        RealMatrix weights = matrixOfOutWeightsFromLayer(layer - 1);
        RealVector outputs = vectorOfOutputsFromLayer(layer - 1);

        RealVector weights0 = vectorOfWeights0(layer);

        RealVector net = weights.operate(outputs).add(weights0);
        //if(layer != outputLayerIndex)
            performActivationFunctionOnVector(net);

        setOutput(net, layer);

        return net;
    }


    /**
     * Performs calculation
     * @return
     */
    public RealVector calculate() {
        for(int i = 0; i < hidden.size(); i++) //Setting outputs of hidden layers
            calculateAndSetOutputOfLayer(i + 1);

        calculateAndSetOutputOfLayer(outputLayerIndex);

        return realVectorFromListDouble(output.stream().map(Neuron::getOutput).collect(Collectors.toList()));
    }

    /**
     * Calculates mean squared error of the output of this network and given data
     * @param data
     */
    public void calculateMeanSquaredErrorFromData(ImageData data) {
        double meanError = 0;
        for(int i = 0; i < data.getDataSize(); i++) {
            this.setInputValues(data.getInputAt(i));
            RealVector result = this.calculate();
            meanError += sumOfVector(result.subtract(realVectorFromListDouble(data.getOutputAt(i))));//t
        }

        meanSquaredError = meanError / data.getDataSize();

    }

    /**
     * Performs backpropagation
     * @param input
     * @param output
     */
    public void backpropagation(List<Double> input, List<Double> output) {
        this.setInputValues(input);
        RealVector trueOutputs = realVectorFromListDouble(output);

        RealVector outputVector = this.calculate();

        double[] onesArray = new double[outputVector.getDimension()];
        Arrays.fill(onesArray, 1);
        RealVector ones = MatrixUtils.createRealVector(onesArray);
        RealVector errorVector = outputVector.ebeMultiply(ones.subtract(outputVector)).ebeMultiply(trueOutputs.subtract(outputVector));
        //System.out.println("output error: " + errorVector);
        for(int i = 0; i < this.output.size(); i++)
            this.output.get(i).setWeight_0(this.output.get(i).getWeight_0() + this.ETA * errorVector.getEntry(i));

        for(int layer = hidden.size(); layer >= 0; layer--) {
            List<Neuron> currentLayer = findLayer(layer);
            double[] errors = new double[currentLayer.size()];
            for(int pos = 0; pos < currentLayer.size(); pos++) {
                Neuron currentNeuron = currentLayer.get(pos);
                //System.out.println("weights from me " + layer + " " + pos + " " + vectorOfWeightsFromMe(layer, pos));
                //System.out.println("after mul " + this.vectorOfWeightsFromMe(layer, pos).ebeMultiply(errorVector));
                double downstreamSum = sumOfVector(this.vectorOfWeightsFromMe(layer, pos).ebeMultiply(errorVector));
                //System.out.println("downSum " + downstreamSum);
                //System.out.println("current output: " + currentNeuron.getOutput());
                double currentError = currentNeuron.getOutput() * (1 - currentNeuron.getOutput()) * downstreamSum; //Racunanje errora
                errors[pos] = currentError;
                //System.out.println("cur err: " + currentError);

                if(layer > 0) currentNeuron.setWeight_0(currentNeuron.getWeight_0() + this.ETA * currentError);
                for(int i = 0; i < currentNeuron.getNumberOfWeightsTo(); i++) { //mijenjanje weightsa
                    //System.out.printf("(%d, %d) to %d old weight %f%n", layer, pos, i, currentNeuron.getWeightTo(i));
                    //System.out.printf("(%d, %d) to %d output %f%n", layer, pos, i, currentNeuron.getOutput());
                    currentNeuron.setWeightTo(i, currentNeuron.getWeightTo(i) + this.ETA * currentNeuron.getOutput() * errorVector.getEntry(i));
                    //System.out.printf("(%d, %d) to %d new weight %f%n", layer, pos, i, currentNeuron.getWeightTo(i));
                }
            }
            errorVector = MatrixUtils.createRealVector(errors);
        }
    }

    /**
     * Gets mean squared error
     * @return
     */
    public Double getMeanSquaredError() {
        return meanSquaredError;
    }

    /**
     * Util function for sum of vector
     * @param v
     * @return
     */
    public double sumOfVector(RealVector v) {
        return v.walkInDefaultOrder(new RealVectorPreservingVisitor() {
            double sum;

            @Override
            public void start(int i, int i1, int i2) {
                sum = 0;
            }

            @Override
            public void visit(int i, double v) {
                sum += v;
            }

            @Override
            public double end() {
                return sum;
            }
        });
    }

    /**
     * Util method to get the reference to layer from index
     * @param layer
     * @return
     */
    private List<Neuron> findLayer(int layer) {
        if(layer < 0 || layer > hidden.size() + 2)
            throw new IllegalArgumentException("Layer is out of bounds");

        List<Neuron> neuronlayer = null;
        if(layer == 0)
            neuronlayer = input;
        else if(layer == hidden.size() + 1)
            neuronlayer = output;
        else
            neuronlayer = hidden.get(layer - 1);

        return neuronlayer;
    }


}
