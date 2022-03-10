package hr.fer.zemris.ocitavanje.koda.lineDetection;

/**
 * Class containg info about input table
 */
public class CellInfo {
    /**
     * Left starting point
     */
    private int startLeft;

    /**
     * Top starting point
     */
    private int startTop;

    /**
     * Right ending point
     */
    private int endRight;

    /**
     * Bottom ending point
     */
    private int endBottom;

    /**
     * Width of cell
     */
    private int width;
    /**
     * Height of cell
     */
    private int height;

    /**
     * Number of rows in table
     */
    private int noRows;
    /**
     * Number of columns in table
     */
    private int noColumns;

    public CellInfo(int startLeft, int startTop, int endRight, int endBottom, int width, int height, int noRows, int noColumns) {
        this.startLeft = startLeft;
        this.startTop = startTop;
        this.endRight = endRight;
        this.endBottom = endBottom;
        this.width = width;
        this.height = height;
        this.noRows = noRows;
        this.noColumns = noColumns;
    }

    public CellInfo() {}

    public int getStartLeft() {
        return startLeft;
    }

    public void setStartLeft(int startLeft) {
        this.startLeft = startLeft;
    }

    public int getStartTop() {
        return startTop;
    }

    public void setStartTop(int startTop) {
        this.startTop = startTop;
    }

    public int getEndRight() {
        return endRight;
    }

    public void setEndRight(int endRight) {
        this.endRight = endRight;
    }

    public int getEndBottom() {
        return endBottom;
    }

    public void setEndBottom(int endBottom) {
        this.endBottom = endBottom;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNoColumns() {
        return noColumns;
    }

    public int getNoRows() {
        return noRows;
    }

    public void setNoColumns(int noColumns) {
        this.noColumns = noColumns;
    }

    public void setNoRows(int noRows) {
        this.noRows = noRows;
    }

    @Override
    public String toString() {
        return "CellInfo{" +
                "startLeft=" + startLeft +
                ", startTop=" + startTop +
                ", endRight=" + endRight +
                ", endBottom=" + endBottom +
                ", width=" + width +
                ", height=" + height +
                ", noRows=" + noRows +
                ", noColumns=" + noColumns +
                '}';
    }
}
