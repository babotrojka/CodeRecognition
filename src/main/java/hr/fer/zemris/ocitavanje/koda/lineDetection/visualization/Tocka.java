package hr.fer.zemris.ocitavanje.koda.lineDetection.visualization;

import java.util.Objects;

/**
 * Class representing one point of BufferedImage
 */
public class Tocka {

    /**
     * Coordinate x
     */
    public int x;

    /**
     * Coordinate y
     */
    public int y;

    public Tocka(long x, long y) {
        this.x = (int)x;
        this.y = (int)y;
    }

    @Override
    public String toString() {
        return "Tocka{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tocka)) return false;
        Tocka tocka = (Tocka) o;
        return x == tocka.x && y == tocka.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
