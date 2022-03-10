package hr.fer.zemris.ocitavanje.koda;

import hr.fer.zemris.ocitavanje.koda.data.DirectoryData;
import hr.fer.zemris.ocitavanje.koda.lineDetection.HoughTransform;

public class Driver {
    public static void main(String[] args) {
        HoughTransform ht = new HoughTransform(Constants.INPUT_PICTURE, 400);
        ht.transform();
        ht.fillCellInfo();

        DirectoryData dd = new DirectoryData(Constants.TRAIN_PATH);
        dd.load();

        CodeRecognition codeRecognition = new CodeRecognition(Constants.INPUT_PICTURE, ht.getCellInfo());
        codeRecognition.perform();
    }
}
