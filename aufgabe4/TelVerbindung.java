/**
 * @author Julian Klimek, Dominik Bartsch
 * @since 01.01.2023
 * @version 0.1
 */

public class TelVerbindung {
    TelKnoten u;
    TelKnoten v;
    int c;

    /**
     * Legt eine neue Telefonverbindung von u nach v mit Verbindungskosten c an.
     *
     * @param u Anfangsknoten
     * @param v Endknoten
     * @param c Verbindungskosten
     */
    public TelVerbindung(TelKnoten u, TelKnoten v, int c) {
        this.u = u;
        this.v = v;
        this.c = c;
    }

    @Override
    public String toString() {
        return "Anfangsknoten: " + this.u + " Endknoten: " + this.v + " Kosten: " + this.c;
    }
}
