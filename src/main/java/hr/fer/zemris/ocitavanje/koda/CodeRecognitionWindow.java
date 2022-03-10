package hr.fer.zemris.ocitavanje.koda;

import hr.fer.zemris.ocitavanje.koda.lineDetection.HoughTransform;
import hr.fer.zemris.ocitavanje.koda.lineDetection.visualization.LineDetectionComponent;

import javax.swing.*;
import java.awt.*;

public class CodeRecognitionWindow extends JFrame {

    public CodeRecognitionWindow() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ocitavanje rukom pisanog koda");
        setLocation(300, 20);
        setSize(900, 1000);
        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        HoughTransform ht = new HoughTransform(Constants.INPUT_PICTURE, 400);
        ht.transform();
        ht.fillCellInfo();

        //CodeRecognition codeRecognition = new CodeRecognition(Constants.INPUT_PICTURE, ht.getCellInfo());

        JComponent lineDetectionComponent = new LineDetectionComponent(ht.getStarts(), ht.getEnds());
        cp.add(lineDetectionComponent, BorderLayout.CENTER);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new CodeRecognitionWindow();
            frame.setVisible(true);
        });
    }
}
