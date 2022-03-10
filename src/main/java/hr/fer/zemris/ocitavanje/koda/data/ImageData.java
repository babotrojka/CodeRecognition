package hr.fer.zemris.ocitavanje.koda.data;

import hr.fer.zemris.ocitavanje.koda.Constants;
import hr.fer.zemris.ocitavanje.koda.data.fileVisitors.TrainFileVisitor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class containing list of information about character pictures
 */
public class ImageData {

    /**
     * List of ImageDataEntries
     */
    private List<ImageDataEntry> entries = new ArrayList<>();

    /**
     * Root
     */
    private Path source;

    /**
     * Limit for number of pictures read
     */
    private int limit;

    public ImageData(Path source, int limit) {
        this.source = source;
        this.limit = limit;
    }

    public ImageData(){ 
        limit = 10000;
    }

    /**
     * Uses TrainFileVisitor to load entries
     */
    public void load() {
        try {
            Files.walkFileTree(source, new TrainFileVisitor(limit, entries));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(entries);
    }

    /**
     * Gets the size of data
     * @return
     */
    public int getDataSize() {
        return entries.size();
    }

    /**
     * Gets number of input features
     * @return
     */
    public int getNumberOfFeatures() {
        return entries.get(0).getInput().size();
    }

    /**
     * Gets number of Output features
     * @return
     */
    public int getNumberOfOutputFeatures() {
        return entries.get(0).getOutput().size();
    }

    /**
     * Gets input at index index
     * @param index
     * @return
     */
    public List<Double> getInputAt(int index) {
        return entries.get(index).getInput();
    }

    /**
     * Gets output at index index
     * @param index
     * @return
     */
    public List<Double> getOutputAt(int index) {
        return entries.get(index).getOutput();
    }

    /**
     * Gets source
     * @return
     */
    public Path getSource() {
        return source;
    }

    /**
     * Sets source
     * @param source
     */
    public void setSource(Path source) {
        this.source = source;
    }
}
