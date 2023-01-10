import java.util.Objects;

/**
 * @author Julian Klimek, Dominik Bartsch
 * @since 01.01.2023
 * @version 0.1
 */

public class TelKnoten {
    int x, y;

    /**
     * Legt einen neuen Telefonknoten mit den Koordinaten (x,y) an.
     *
     * @param x x-Koordinate
     * @param y y-Koordinate
     */
    public TelKnoten(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TelKnoten) {
            if (this.x == ((TelKnoten) obj).x && this.y == ((TelKnoten) obj).y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return ("x: " + this.x + " y: " + this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
