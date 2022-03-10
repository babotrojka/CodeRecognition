package hr.fer.zemris.ocitavanje.koda.neuralNetwork;

import hr.fer.zemris.ocitavanje.koda.Constants;
import hr.fer.zemris.ocitavanje.koda.data.BiMap;
import hr.fer.zemris.ocitavanje.koda.data.DirectoryData;
import hr.fer.zemris.ocitavanje.koda.data.ImageData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class used for training of neural network
 */
public class Training {

    /**
     * Reference to neural network
     */
    private NeuralNetwork network;

    /**
     * Source of files
     */
    private Path source;

    /**
     * Sizes of hidden layers of network
     */
    private int[] hiddenSizes;

    /**
     * Writer used for results of network
     */
    private BufferedWriter writer;

    public Training(Path source, int[] hiddenSizes) {
        this.source = source;
        this.hiddenSizes = hiddenSizes;

        try {
            writer = Files.newBufferedWriter(Constants.NETWORK_OUTPUT_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uses Image Data to read then train
     */
    public void readThenTrain() {
        System.out.println("Training started. Loading...");
        ImageData imageData = new ImageData(Constants.TRAIN_PATH, Constants.TRAINING_READ_NUMBER);
        imageData.load();
        System.out.println("Data loaded. Backpropagation stared");

        network = new NeuralNetwork(imageData.getNumberOfFeatures(), imageData.getNumberOfOutputFeatures(), 5);

        for(int i = 0; i < imageData.getDataSize(); i++) {
            network.backpropagation(imageData.getInputAt(i), imageData.getOutputAt(i));
            if(i % 100 == 0) System.out.printf("%dth entry finished!\n", i);
        }
    }

    public void trainWithRead() {
        System.out.println("Training started. Loading...");
        /*try {
            writer.write("Training started. Loading...\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        DirectoryData dd = new DirectoryData(source);
        dd.load();

        File[] charDirectories = dd.getCharDirectories();
        List<File[]> filePictures = dd.getFilePictures();

        List<Double> input = dd.getListFromImage(filePictures.get(0)[0]);
        List<Double> output = dd.getOutputListFromInteger(0);

        network = new NeuralNetwork(input.size(), output.size(), 20);

        Random r = new Random();
        for(int i = 0; i < Constants.TOTAL_TRAINING_COUNTER; i++) {
            int file = r.nextInt(charDirectories.length);
            int pic = r.nextInt(filePictures.get(file).length);
            //if(i % 100 == 0) System.out.println("Chosen dir " + charDirectories[file].getName() + ", pic " + filePictures.get(file)[pic].getName());
            if(i % 4000 == 0) {
                System.out.println(i + "th entry. Going on...");
           /*     try {
                    writer.write("i + \"th entry. Going on...\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
            input = dd.getListFromImage(filePictures.get(file)[pic]);
            output = dd.getOutputListFromInteger(file);

            network.backpropagation(input, output);
        }

        testWithRead();
        try {
            StringBuilder sb = new StringBuilder();
            for(int h : hiddenSizes) sb.append(h).append(",");
            writer.write(sb.substring(0, sb.length() - 1));
            writer.write(network.vectorOfAllWeights().toString().substring(1, network.vectorOfAllWeights().toString().length() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testWithRead() {
        System.out.println("Testing started...");
        /*try {
            writer.write("Testing started\n");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        DirectoryData dd = new DirectoryData(Constants.TEST_PATH);
        dd.load();

        File[] charDirectories = dd.getCharDirectories();
        List<File[]> filePictures = dd.getFilePictures();

        Random r = new Random();

        int wrongs = 0;
        for (int i = 0; i < Constants.TOTAL_TEST_COUNTER; i++) {
            int file = r.nextInt(charDirectories.length);
            int pic = r.nextInt(filePictures.get(file).length);
            //if (i % 1000 == 0) System.out.println("Chosen dir " + charDirectories[file].getName() + ", pic " + filePictures.get(file)[pic].getName());

            List<Double> input = dd.getListFromImage(filePictures.get(file)[pic]);
            if(input == null) continue;
            List<Double> output = dd.getOutputListFromInteger(file);

            network.setInputValues(input);
            int res = network.calculate().getMaxIndex();

            if (file != res) {
                //System.out.printf("[ERROR]: Network predicted %s. Correct is %s\n", BiMap.getInstance().get(res), charDirectories[file].getName());
                wrongs++;
            }
        }
        System.out.print(Arrays.toString(hiddenSizes) + ": ");
        System.out.println("After testing percentage of bad is " + wrongs / (double) Constants.TOTAL_TEST_COUNTER);

        /*try {
            writer.write("Arrays.toString(hiddenSizes) + \": \"");
            writer.write("After testing percentage of bad is " + wrongs / (double) Constants.TOTAL_TEST_COUNTER);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
