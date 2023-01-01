/**
 * @author Julian Klimek, Dominik Bartsch
 * @date 01.01.2023
 */

/**
 * Klasse für Union-Find-Strukturen. Unterstützt die effiziente Verwaltung einer
 * Partionierung (disjunkte Zerlegung) der Grundmenge {0, 1, 2, ..., n-1}.
 * union benötigt O(1) und find benötigt O(log(n)).
 */
public class UnionFind {
    int[] p;
    int size;

    /**
     * Legt eine neue Union-Find-Struktur mit der Partitionierung {{0}, {1}, ...,
     * {n-1}} an.
     *
     * @param n Größe der Grundmenge.
     */
    public UnionFind(int n) {
        p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = -1;
        }
        size = n;
    }

    /**
     * Liefert den Repräsentanten der Menge zurück, zu der e gehört.
     *
     * @param e Element
     * @return Repräsentant der Menge, zu der e gehört.
     */
    public int find(int e) {
        while (p[e] >= 0) {
            e = p[e];
        }
        return e;
    }

    /**
     * Vereinigt die beiden Menge s1 und s2. s1 und s2 müssen Repräsentanten der
     * jeweiligen Menge sein. Die Vereinigung wird nur durchgeführt, falls s1 und s2
     * unterschiedlich sind. Es wird union-by-height durchgeführt.
     *
     * @param s1 Element, das eine Menge repräsentiert.
     * @param s2 Element, das eine Menge repräsentiert.
     */
    public void union(int s1, int s2) {
        if (p[s1] >= 0 || p[s2] >= 0) {
            return;
        }
        if (s1 == s2) {
            return;
        }
        if (-p[s1] < -p[s2]) {
            p[s1] = s2;
        } else {
            if (-p[s1] == -p[s2])
                p[s1]--;
            p[s2] = s1;
        }
        size--;
    }

    /**
     * Liefert die Anzahl der Mengen in der Partitionierung zurück.
     *
     * @return Anzahl der Mengen.
     */
    public int size() {
        return size;
    }

    public static void main(String[] args) {
        UnionFind uf = new UnionFind(10);

        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(0, 3);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(0, 4);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(3, 1);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(2, 8);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(9, 8);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(5, 1);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(5, 6);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(6, 9);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(4, 8);
        System.out.println("Anzahl Partitionierungen: " + uf.size);
        uf.union(8, 3);
        System.out.println("Anzahl Partitionierungen: " + uf.size);

        System.out.println();

        for (int i = 0; i < 10; i++) {
            System.out.println(i + " ist enthalten in " + uf.find(i));
        }
    }
}
