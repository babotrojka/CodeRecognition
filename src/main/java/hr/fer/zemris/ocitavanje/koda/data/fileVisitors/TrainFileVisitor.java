package hr.fer.zemris.ocitavanje.koda.data.fileVisitors;

import hr.fer.zemris.ocitavanje.koda.Constants;
import hr.fer.zemris.ocitavanje.koda.data.BiMap;
import hr.fer.zemris.ocitavanje.koda.data.ImageDataEntry;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * File visitor which goes over character files and loads then into list of ImageDataEntry
 */
public class TrainFileVisitor extends SimpleFileVisitor<Path> {
    /**
     * Used to determine which character we are looking at
     * Output is created using this
     */
    int letterCounter = -1;

    /**
     * Counter for Limit of character entries read
     */
    int readCounter;

    /**
     * Limit for character entries read
     */
    private int limit;

    /**
     * List of character entries
     */
    private List<ImageDataEntry> entries;

    public TrainFileVisitor(int limit, List<ImageDataEntry> entries) {
        this.limit = limit;
        this.entries = entries;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if(dir.getFileName().toString().length() == 1) {
            BiMap.getInstance().put(++letterCounter, dir.getFileName().toString());
            readCounter = 0;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(readCounter < limit) {
            BufferedImage image = ImageIO.read(Files.newInputStream(file));
            List<Double> imageInput = new ArrayList<>();
            for (int i = 0; i < image.getHeight(); i++)
                for (int j = 0; j < image.getWidth(); j++)
                    imageInput.add(image.getRGB(i, j) == -1 ? 0. : 1.);

            List<Double> imageOutput = new ArrayList<>();
            for (int i = 0; i < Constants.NUMBER_CHARS; i++)
                imageOutput.add(i == letterCounter ? 1. : 0.);

            entries.add(new ImageDataEntry(imageInput, imageOutput));
            if(entries.size() %100 == 0) System.out.println(entries.size());
            readCounter++;
        }
        return FileVisitResult.CONTINUE;
    }
}