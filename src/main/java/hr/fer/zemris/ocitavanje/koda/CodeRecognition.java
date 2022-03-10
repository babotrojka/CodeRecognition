package hr.fer.zemris.ocitavanje.koda;

import hr.fer.zemris.ocitavanje.koda.data.BiMap;
import hr.fer.zemris.ocitavanje.koda.lineDetection.CellInfo;
import hr.fer.zemris.ocitavanje.koda.neuralNetwork.NeuralNetwork;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CodeRecognition {

    /**
     * Image of table with code
     */
    private BufferedImage codeImage;

    /**
     * Info about cell location
     */
    private CellInfo cellInfo;

    /**
     * Trained neural network
     */
    private NeuralNetwork network;

    /**
     * Output writer
     */
    private BufferedWriter writer;


    /**
     * Margin in each cell
     */
    private final int MARGIN = 10;

    /**
     * List of images to read
     */
    private List<BufferedImage> images = new ArrayList<>();

    public CodeRecognition(String imagePath, CellInfo cellInfo) {
        try {
            this.codeImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.cellInfo = cellInfo;
        fillImages();

        try {
            writer = Files.newBufferedWriter(Constants.OUTPUT_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> lines = null;
        try {
            lines = Files.readAllLines(Constants.NETWORK_OUTPUT_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*String[] hiddenS = lines.get(0).split(",");
        int[] hidden = new int[hiddenS.length];*/
        network = new NeuralNetwork(Constants.TARGET_HEIGHT * Constants.TARGET_WIDTH, Constants.NUMBER_CHARS, 10, 10);

        /*String[] weightsS = lines.get(1).split(",");
        List<Double> weights = new ArrayList<>();
        for(String w : weightsS) weights.add(Double.parseDouble(w));

        network.setWeightsFromVector(NeuralNetwork.realVectorFromListDouble(weights));*/
    }

    /**
     * Extracts images with characters from table
     */
    private void fillImages() {
        int w = cellInfo.getWidth();
        int h = cellInfo.getHeight();

        for(int row = 0; row < cellInfo.getNoRows(); row++) {
            for(int column = 0; column < cellInfo.getNoColumns(); column++) {
                BufferedImage image = new BufferedImage(w - 2 * MARGIN, h - 2 * MARGIN, BufferedImage.TYPE_INT_RGB);
                for(int y = 0; y < image.getHeight(); y++)
                    for(int x = 0; x < image.getWidth(); x++)
                        image.setRGB(x, y, codeImage.getRGB(cellInfo.getStartLeft() + column * w + MARGIN + x,
                                                            cellInfo.getStartTop() + row * h + MARGIN + y));

                images.add(resizeImage(image, Constants.TARGET_WIDTH, Constants.TARGET_HEIGHT));
            }
        }
    }

    /**
     * Performs text recognition and writes to output
     */
    public void perform() {
        for(int i = 0; i < images.size(); i++) {
            List<Double> input = getListFromImage(images.get(i));
            network.setInputValues(input);
            String r = BiMap.getInstance().get(network.calculate().getMaxIndex());
            try {
                writer.write(r);
                if((i + 1) % cellInfo.getNoColumns() == 0) writer.write("\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Makes list out of image
     * @param image
     * @return
     */
    private List<Double> getListFromImage(BufferedImage image) {
        List<Double> imageInput = new ArrayList<>();
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++)
                imageInput.add(image.getRGB(i, j) == -1 ? 0. : 1.);

        return imageInput;
    }

    /**
     * Resizes image to match target width and height of network
     * @param originalImage
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight){
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }
}
