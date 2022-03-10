package hr.fer.zemris.ocitavanje.koda.lineDetection.visualization;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Component which draws lines of input table
 */
public class LineDetectionComponent extends JComponent {
    /**
     * List of starting points
     */
    private List<Tocka> starts;

    /**
     * List of ending points
     */
    private List<Tocka> ends;

    /**
     * Total size
     */
    private int SIZE;
    public LineDetectionComponent(List<Tocka> starts, List<Tocka> ends) {
        if(starts.size() != ends.size()) throw new IllegalArgumentException("Starts and ends must be same size!");

        SIZE = starts.size();
        this.starts = starts;
        this.ends = ends;
    }

    @Override
    protected void paintComponent(Graphics g) {
        for (int i = 0; i < SIZE; i++)
            g.drawLine(starts.get(i).x, starts.get(i).y, ends.get(i).x, ends.get(i).y);
    }
}
