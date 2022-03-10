package hr.fer.zemris.ocitavanje.koda.lineDetection;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class OcitavanjeKodaTest {
    @Test
    public void imageReadTest() {
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(Path.of("imageTest.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage paintImg = null;
        BufferedImage scanImg = null;
        try {
            paintImg = ImageIO.read(getClass().getResourceAsStream("test_5.png"));
            scanImg = ImageIO.read(getClass().getResourceAsStream("test_5_empty_800.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for (int j = 0; j < paintImg.getHeight(); j++) {
                for (int i = 0; i < paintImg.getWidth(); i++) {
                    //System.out.print(paintImg.getRGB(i, j));
                    writer.write("" + paintImg.getRGB(i, j));
                }
                writer.write("\n");
                writer.flush();
            }

            writer.write("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");


            for (int j = 0; j < scanImg.getHeight(); j++) {
                for (int i = 0; i < scanImg.getWidth(); i++) {
                    //System.out.print(scanImg.getRGB(i, j));
                    writer.write("" + scanImg.getRGB(i, j));
                }
                writer.write("\n");
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
