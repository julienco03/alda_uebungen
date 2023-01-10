import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.awt.*;

/**
 * @author Julian Klimek, Dominik Bartsch
 * @since 01.01.2023
 * @version 0.1
 */

/**
 * Klasse zur Verwaltung von Telefonknoten mit (x,y)-Koordinaten und zur
 * Berechnung eines minimal aufspannvn Baums mit dem Algorithmus von Kruskal.
 * Kantengewichte sind durch den Manhattan-Abstand definiert.
 */
public class TelNet {
    Map<TelKnoten, Integer> telMap;
    List<TelVerbindung> minTree;
    int lbg;
    int size;

    /**
     * Legt ein neues Telefonnetz mit dem Leitungsbegrenzungswert lbg an.
     *
     * @param lbg Leistungsbegrenzungswert
     */
    public TelNet(int lbg) {
        telMap = new HashMap<>();
        minTree = new LinkedList<>();
        this.lbg = lbg;
    }

    /**
     * Fügt einen neuen Telefonknoten mit Koordinate (x,y) dazu.
     *
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @return true, falls die Koordinate neu ist, sonst false
     */
    public boolean addTelKnoten(int x, int y) {
        TelKnoten tk = new TelKnoten(x, y);
        if (!telMap.containsKey(tk)) {
            telMap.put(tk, size++);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Berechnet ein optimales Telefonnetz als minimal aufspannvn Baum mit dem
     * Algorithmus von Kruskal.
     *
     * @return true, falls es einen minimal aufspannden Baum gibt, sonst false
     */
    public boolean computeOptTelNet() {
        UnionFind forest = new UnionFind(size);
        PriorityQueue<TelVerbindung> edges = new PriorityQueue<>(size, Comparator.comparing(x -> x.c));

        // befülle die PriorityQueue
        for (var v : telMap.entrySet()) {
            for (var w : telMap.entrySet()) {
                if (v.equals(w))
                    continue;

                int cost = (Math.abs(v.getKey().x - w.getKey().x) + Math.abs(v.getKey().y - w.getKey().y));
                if (cost <= lbg) {
                    edges.add(new TelVerbindung(v.getKey(), w.getKey(), cost));
                }
            }
        }

        while (forest.size() != 1 && !edges.isEmpty()) {
            TelVerbindung tel = edges.poll();
            int t1 = forest.find(telMap.get(tel.u));
            int t2 = forest.find(telMap.get(tel.v));
            if (t1 != t2) {
                forest.union(t1, t2);
                minTree.add(tel);
            }
        }
        if (edges.isEmpty() && forest.size() != 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Liefert ein optimales Telefonnetz als Liste von Telefonverbindungen zurück.
     *
     * @return Liste von Telefonverbindungen.
     */
    public java.util.List<TelVerbindung> getOptTelNet() {
        return minTree;
    }

    /**
     * Liefert die Gesamtkosten eines optimalen Telefonnetzes zurück.
     *
     * @return Gesamtkosten eines optimalen Telefonnetzes.
     */
    public int getOptTelNetKosten() {
        int cost = 0;
        for (var v : minTree) {
            cost += v.c;
        }
        return cost;
    }

    /**
     * Zeichnet das gefundene optimale Telefonnetz mit der Größe xlbg*ylbg in ein
     * Fenster.
     *
     * @param xlbg lbgimale x-Größe.
     * @param ylbg lbgimale y-Größe.
     */
    public void drawOptTelNet(int xlbg, int ylbg) {
        StdDraw.setCanvasSize(512, 512);
        StdDraw.setXscale(0, xlbg + 1);
        StdDraw.setYscale(0, ylbg + 1);

        // Raster einzeichnen
        for (int i = 0; i < ylbg; i++) {
            StdDraw.line(0.5, i + 0.5, ylbg + 0.5, i + 0.5);
        }
        for (int i = 0; i < xlbg; i++) {
            StdDraw.line(i + 0.5, 0.5, i + 0.5, xlbg + 0.5);
        }
        StdDraw.line(0.5, ylbg + 0.5, xlbg + 0.5, ylbg + 0.5);
        StdDraw.line(xlbg + 0.5, 0.5, xlbg + 0.5, ylbg + 0.5);

        // minimal aufspannenden Baum einzeichnen
        StdDraw.setPenColor(StdDraw.RED);
        for (var v : minTree) {
            double x = v.u.x;
            double y = v.v.y;
            StdDraw.line(v.u.x, v.u.y, x, y);
            StdDraw.line(x, y, v.v.x, v.v.y);
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.filledSquare(v.u.x, v.u.y, 0.5);
            StdDraw.filledSquare(v.v.x, v.v.y, 0.5);
            StdDraw.setPenColor(Color.RED);
        }
        StdDraw.show(0);
    }

    /**
     *
     * @param n    Anzahl Telefonknoten
     * @param xlbg xlbg - Intervallgrenz für x-Koordinate.
     * @param ylbg ylbg - Intervallgrenz für y-Koordinate.
     */
    public void generateRandomTelNet(int n, int xlbg, int ylbg) {
        int i = 0;
        while (i < n) {
            int px = (int) (Math.random() * xlbg);
            int py = (int) (Math.random() * ylbg);
            if (this.addTelKnoten(px, py)) {
                i++;
            }
        }
    }

    /**
     * Liefert die Anzahl der Knoten des Telefonnetzes zurück.
     *
     * @return Anzahl der Knoten des Telefonnetzes.
     */
    public int size() {
        return size;
    }

    /* === TESTS === */
    private static void test1() {
        TelNet telnet = new TelNet(5);
        telnet.addTelKnoten(1, 1);
        telnet.addTelKnoten(3, 1);
        telnet.addTelKnoten(4, 2);
        telnet.addTelKnoten(3, 4);
        telnet.addTelKnoten(7, 6);
        telnet.addTelKnoten(2, 6);
        telnet.addTelKnoten(4, 7);
        System.out.println(
                "Gibt es einen minimal aufspannenden Baum? " + (telnet.computeOptTelNet() ? "Ja!" : "Nein!"));
        System.out.println("Gesamtkosten: " + telnet.getOptTelNetKosten());
        telnet.drawOptTelNet(7, 7);
    }

    private static void test2() {
        int lbg = 1000;
        TelNet telnet2 = new TelNet(100);

        telnet2.generateRandomTelNet(lbg, lbg, lbg);
        System.out.println(
                "Gibt es einen minimal aufspannenden Baum? " + (telnet2.computeOptTelNet() ? "Ja!" : "Nein!"));
        telnet2.drawOptTelNet(lbg, lbg);
    }

    public static void main(String[] args) {
        test1();
        // test2();
    }
}
