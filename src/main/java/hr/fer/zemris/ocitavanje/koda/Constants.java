package hr.fer.zemris.ocitavanje.koda;

import java.nio.file.Path;

public class Constants {
    /**
     * Path to train data
     */
    public static final Path TRAIN_PATH = Path.of("../dataset/train_big");
    /**
     * Path to test data
     */
    public static final Path TEST_PATH = Path.of("../dataset/test");

    /**
     * Number of training data to read
     */
    public static final int TRAINING_READ_NUMBER = 400;

    /**
     * Number of test data to read
     */
    public static final int TEST_READ_NUMBER = 30;

    /**
     * Number of total training data to go through
     */
    public static final int TOTAL_TRAINING_COUNTER = 5000 * 40;

    /**
     * Number of total test data to go through
     */
    public static final int TOTAL_TEST_COUNTER = 200;

    /**
     * Path to output network info
     */
    public static final Path NETWORK_OUTPUT_PATH = Path.of("network.txt");

    /**
     * Path to output of file
     */
    public static final Path OUTPUT_PATH = Path.of("output.txt");

    /**
     * Training adjust
     */
    public static final double ETA = 0.1;

    /**
     * Input to read from
     */
    public static final String INPUT_PICTURE = "test_7_code_1.jpg";

    /**
     * Target width
     */
    public static final int TARGET_WIDTH = 128;
    /**
     * Target height
     */
    public static final int TARGET_HEIGHT = 128;

    public static final int NUMBER_CHARS = 36;
}
