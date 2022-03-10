package hr.fer.zemris.ocitavanje.koda.data;

import hr.fer.zemris.ocitavanje.koda.Constants;
import hr.fer.zemris.ocitavanje.koda.data.fileVisitors.DirectoryFileVisitor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing information about character directories
 */
public class DirectoryData {
    /**
     * Array of names of char directories
     */
    private File[] charDirectories;

    /**
     * List of arrays of names of character files
     */
    private List<File[]> filePictures;

    /**
     * Root file
     */
    private File source;

    public DirectoryData(Path source) {
        this.source = source.toFile();
    }

    /**
     * Using source, fills charDirectories, filePictures and BiMap
     */
    public void load() {
        charDirectories = source.listFiles();
        filePictures = new ArrayList<>();
        for(int i = 0; i < charDirectories.length; i++) {
            //System.out.println(charDirectories[i].getName() + " " + charDirectories[i].listFiles().length);
            filePictures.add(charDirectories[i].listFiles());
            BiMap.getInstance().put(i, charDirectories[i].getName());
        }
    }


    /**
     * Util method which creates list of doubles from character image
     * @param file
     * @return
     */
    public List<Double> getListFromImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Files.newInputStream(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(image == null) return null;
        List<Double> imageInput = new ArrayList<>();
        for (int j = 0; j < image.getHeight(); j++)
            for (int i = 0; i < image.getWidth(); i++)
                imageInput.add(image.getRGB(i, j) == -1 ? 0. : 1.);

        return imageInput;
    }


    /**
     * Util method which creates output list from integer
     * List consists of all 0s, except on place n
     * @param n
     * @return
     */
    public List<Double> getOutputListFromInteger(int n) {
        List<Double> imageOutput = new ArrayList<>();
        for (int i = 0; i < charDirectories.length; i++)
            imageOutput.add(i == n ? 1. : 0.);

        return imageOutput;
    }

    /**
     * Gets Character directories
     * @return
     */
    public File[] getCharDirectories() {
        return charDirectories;
    }

    /**
     * Gets file pictures
     * @return
     */
    public List<File[]> getFilePictures() {
        return filePictures;
    }
}
