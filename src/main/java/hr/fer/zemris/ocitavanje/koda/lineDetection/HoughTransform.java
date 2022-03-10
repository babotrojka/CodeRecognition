package hr.fer.zemris.ocitavanje.koda.lineDetection;

import hr.fer.zemris.ocitavanje.koda.lineDetection.visualization.Tocka;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class used to perform Hough Transform on images
 */
public class HoughTransform {
    private BufferedImage image;

    private int THETA_NUM;
    private int RHO_NUM;

    /**
     * 2D Array used for representing Hough Space
     */
    private int[][] houghSpace;

    /**
     * Array representing all possible theta values in THETA_RANGE
     */
    private int[] thetas;

    private double[] sinThetas;
    private double[] cosThetas;

    /**
     * Array representing all possible theta values in THETA_RANGE
     */
    private double[] rhos;

    /**
     * Diagonal of the image. Used for calcualting rho parameters
     */
    double d;

    /**
     * Based on this threshold, it will be determined whether it is a line or not
     */
    private int threshold;

    /**
     * Contains starting points for all lines
     */
    private List<Tocka> starts = new ArrayList<>();

    /**
     * Contains ending points for all lines
     */
    private List<Tocka> ends = new ArrayList<>();

    /**
     * Representing numerical info about each cell.
     * Gets filled up after transform
     */
    private CellInfo cellInfo;


    public HoughTransform(String filename, int theta_num, int rho_num, int threshold) {
        try {
            InputStream is = getClass().getResourceAsStream(filename);
            image = ImageIO.read(is); //promijeni u jfilechooser
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.d = Math.hypot(image.getWidth(), image.getHeight());
        double ro_step = 2 * d / rho_num;

        THETA_NUM = theta_num;
        RHO_NUM = rho_num;
        this.threshold = threshold;

        //fill houghSpace to an array of zeros
        houghSpace = new int[THETA_NUM + 1][RHO_NUM];

        //fill thetas with values [0, 180], sinThetas, cosThetas
        thetas = new int[THETA_NUM + 1];
        sinThetas = new double[THETA_NUM + 1];
        cosThetas = new double[THETA_NUM + 1];
        for(int i = 0; i < THETA_NUM + 1; i++) {
            thetas[i] = i;
            sinThetas[i] = Math.sin(Math.toRadians(thetas[i]));
            cosThetas[i] = Math.cos(Math.toRadians(thetas[i]));
        }

        //fill rhos with values in range [-d, d] with step d2
        rhos = new double[RHO_NUM + 1];
        rhos[0] = -d;
        for(int i = 1; i < RHO_NUM + 1; i++) rhos[i] = rhos[i - 1] + ro_step;

    }

    public HoughTransform(String filename) {
        this(filename, 180, 180, 220);
    }

    public HoughTransform(String filename, int threshold) {
        this(filename, 180, 180, threshold);
    }
    public void transform() {
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                if(image.getRGB(x, y) != -1) {
                    for (int theta = 0; theta < THETA_NUM + 1; theta++) {
                        double ro = x * cosThetas[theta] + y * sinThetas[theta];//ro = x * cos + y * sin
                        int rhoID = 0;
                        for(; rhoID < rhos.length; rhoID++) if(ro < rhos[rhoID]) break;
                        houghSpace[theta][rhoID-1]++;
                    }
                }
            }
        }

        for(int theta = 0; theta < houghSpace.length; theta++) {
            for(int rho_id = 0; rho_id < houghSpace[0].length; rho_id++) {
                if(houghSpace[theta][rho_id] > threshold) {
                    double a = Math.cos(Math.toRadians(theta));
                    double b = Math.sin(Math.toRadians(theta));

                    long x0 = Math.round(a * rhos[rho_id]);
                    long y0 = Math.round(b * rhos[rho_id]);

                    long x1 = Math.round(x0 + d * (-b));
                    long y1 = Math.round(y0 + d * a);

                    long x2 = Math.round(x0 - d * (-b));
                    long y2 = Math.round(y0 - d * a);

                    if((x0 == x2 || y0 == y2) && y2 > 0) {
                        starts.add(new Tocka(x0, y0));
                        ends.add(new Tocka(x2, y2));
                        //System.out.printf("Tocke: (%d, %d) i (%d, %d)\n", x0, y0, x2, y2);
                    }

                    //System.out.printf("x0: %d, y0: %d\n", x0, y0);

                }
            }
        }
    }

    /**
     * Creates a cell info object based on transform() results
     * @return
     */
    public void fillCellInfo() {
        List<Tocka> verticals = starts.stream()
                .filter(t -> t.y == 0)
                .sorted((t1, t2) -> Integer.compare(t1.x, t2.x))
                .collect(Collectors.toList());

        List<Tocka> horizontals = starts.stream()
                .filter(t -> t.x == 0)
                .sorted((t1, t2) -> Integer.compare(t1.y, t2.y))
                .collect(Collectors.toList());

        cellInfo = new CellInfo();
        cellInfo.setStartLeft(verticals.get(0).x);
        cellInfo.setStartTop(horizontals.get(0).y);
        cellInfo.setEndRight(verticals.get(verticals.size() - 1).x);
        cellInfo.setEndBottom(horizontals.get(horizontals.size() - 1).y);

        int w = findMostCommonDifference(verticals.stream().map(t -> t.x).collect(Collectors.toList()));
        cellInfo.setWidth(w);

        int h = findMostCommonDifference(horizontals.stream().map(t-> t.y).collect(Collectors.toList()));
        cellInfo.setHeight(h);

        cellInfo.setNoRows((int)Math.round((cellInfo.getEndBottom() - cellInfo.getStartTop()) / (double) cellInfo.getHeight()));
        cellInfo.setNoColumns((int) Math.round((cellInfo.getEndRight() - cellInfo.getStartLeft()) / (double) cellInfo.getWidth()));
        //System.out.println(cellInfo);
    }

    /**
     * Util method finding the most common difference between lines
     * @param starts
     * @return
     */
    private int findMostCommonDifference(List<Integer> starts) {
        Map<Integer, Integer> mostCommon = new HashMap<>();
        for(int i = 1; i < starts.size(); i++) {
            //System.out.println("Diff: " + (starts.get(i) - starts.get(i - 1)));
            int diff = starts.get(i) - starts.get(i - 1);
            if(mostCommon.containsKey(diff))
                mostCommon.put(diff, mostCommon.get(diff) + 1);
            else if(mostCommon.containsKey(diff - 1))
                mostCommon.put(diff - 1, mostCommon.get(diff - 1) + 1);
            else if(mostCommon.containsKey(diff + 1))
                mostCommon.put(diff + 1, mostCommon.get(diff + 1) + 1);
            else
                mostCommon.put(diff, 0);
        }

        int width = -1, max = -1;
        for(Map.Entry<Integer, Integer> me : mostCommon.entrySet())
            if(me.getValue() > max) {
                max = me.getValue();
                width = me.getKey();
            }

        return width;
    }

    public void extractAndSaveFirstCell() {
        List<Tocka> verticals = starts.stream()
                .filter(t -> t.y == 0)
                .sorted((t1, t2) -> Integer.compare(t1.x, t2.x))
                .collect(Collectors.toList());
}

    /**
     * Returns list of starting points
     * @return starts list
     */
    public List<Tocka> getStarts() {
        return starts;
    }

    /**
     * Returns list of ending points
     * @return ends list
     */
    public List<Tocka> getEnds() {
        return ends;
    }

    public CellInfo getCellInfo() {
        return cellInfo;
    }
}
