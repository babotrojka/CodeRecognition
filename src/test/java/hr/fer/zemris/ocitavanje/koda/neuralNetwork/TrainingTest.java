package hr.fer.zemris.ocitavanje.koda.neuralNetwork;

import hr.fer.zemris.ocitavanje.koda.Constants;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class TrainingTest {

    @Test
    public void testTraining() {
        new Training(Constants.TRAIN_PATH, new int[]{10}).trainWithRead();
        new Training(Constants.TRAIN_PATH, new int[]{5, 5}).trainWithRead();
        new Training(Constants.TRAIN_PATH, new int[]{20}).trainWithRead();
        new Training(Constants.TRAIN_PATH, new int[]{5, 5, 5}).trainWithRead();
        new Training(Constants.TRAIN_PATH, new int[]{10, 10}).trainWithRead();

    }
}
