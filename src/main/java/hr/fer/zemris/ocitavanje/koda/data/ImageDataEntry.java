package hr.fer.zemris.ocitavanje.koda.data;

import java.util.List;

/**
 * Class representing one entry for ImageData
 * Entry represents character picture stored into list of doubles
 */
public class ImageDataEntry {
    /**
     * List representing input
     */
    private List<Double> input;

    /**
     * List representing output
     */
    private List<Double> output;

    public ImageDataEntry(List<Double> input, List<Double> output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Gets input
     * @return
     */
    public List<Double> getInput() {
        return input;
    }

    /**
     * Gets output
     * @return
     */
    public List<Double> getOutput() {
        return output;
    }

    /**
     * Sets input
     * @param input
     */
    public void setInput(List<Double> input) {
        this.input = input;
    }

    /**
     * Sets output
     * @param output
     */
    public void setOutput(List<Double> output) {
        this.output = output;
    }
}
