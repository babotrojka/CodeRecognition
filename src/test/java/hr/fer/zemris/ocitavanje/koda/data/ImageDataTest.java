package hr.fer.zemris.ocitavanje.koda.data;

import hr.fer.zemris.ocitavanje.koda.Constants;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class ImageDataTest {

    @Test
    public void testLoad() {
        ImageData imageData = new ImageData(Constants.TRAIN_PATH, Constants.TRAINING_READ_NUMBER);
        imageData.load();
    }
}
