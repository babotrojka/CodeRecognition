package hr.fer.zemris.ocitavanje.koda.lineDetection;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HoughTransformTest {

    @Test
    public void test_1() {
        HoughTransform ht = new HoughTransform("test_1.png");
        ht.transform();
    }
}
