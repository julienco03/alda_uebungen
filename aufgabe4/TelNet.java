import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @author Julian Klimek, Dominik Bartsch
 * @since 01.01.2023
 * @version 0.1
 */

/**
 * Klasse zur Verwaltung von Telefonknoten mit (x,y)-Koordinaten und zur
 * Berechnung eines minimal aufspannenden Baums mit dem Algorithmus von Kruskal.
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
     * Berechnet ein optimales Telefonnetz als minimal aufspannenden Baum mit dem
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
            int t1 = forest.find(telMap.get(tel.anfang));
            int t2 = forest.find(telMap.get(tel.ende));
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
     * Zeichnet das gefundene optimale Telefonnetz mit der Größe xMax*yMax in ein
     * Fenster.
     *
     * @param xMax Maximale x-Größe.
     * @param yMax Maximale y-Größe.
     */
    public void drawOptTelNet(int xMax, int yMax) {
        StdDraw.setCanvasSize(512, 512);
        StdDraw.setXscale(0, xMax + 1);
        StdDraw.setYscale(0, yMax + 1);

        for (int i = 0; i < yMax; i++) {
            StdDraw.line(0.5, i + 0.5, yMax + 0.5, i + 0.5);
        }
        for (int i = 0; i < xMax; i++) {
            StdDraw.line(i + 0.5, 0.5, i + 0.5, xMax + 0.5);
        }
        StdDraw.line(0.5, yMax + 0.5, xMax + 0.5, yMax + 0.5);
        StdDraw.line(xMax + 0.5, 0.5, xMax + 0.5, yMax + 0.5);
        StdDraw.setPenColor(StdDraw.RED);

        for (var v : minTree) {
            double x = v.anfang.x;
            double y = v.ende.y;
            StdDraw.line(v.anfang.x, v.anfang.y, x, y);
            StdDraw.line(x, y, v.ende.x, v.ende.y);
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.filledSquare(v.anfang.x, v.anfang.y, 0.5);
            StdDraw.filledSquare(v.ende.x, v.ende.y, 0.5);
            StdDraw.setPenColor(Color.RED);
        }
        StdDraw.show(0);
    }

    /**
     *
     * @param n    Anzahl Telefonknoten
     * @param xMax xMax - Intervallgrenz für x-Koordinate.
     * @param yMax yMax - Intervallgrenz für y-Koordinate.
     */
    public void generateRandomTelNet(int n, int xMax, int yMax) {
        int i = 0;
        while (i < n) {
            int px = (int) (Math.random() * xMax);
            int py = (int) (Math.random() * yMax);
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

    public static void main(String[] args) {
        int max = 1000;
        TelNet tn2 = new TelNet(100);

        tn2.generateRandomTelNet(max, max, max);

        System.out.println(tn2.computeOptTelNet());

        tn2.drawOptTelNet(max, max);
    }
}
