// O. Bittel;
// 26.09.22

//package directedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 * @author Oliver Bittel
 * @since 02.03.2020
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {
	// comp speichert fuer jede Komponente die zughörigen Knoten.
    // Die Komponenten sind numeriert: 0, 1, 2, ...

	private final Map<Integer,Set<V>> comp = new TreeMap<>();
	private final Set<V> besucht = new TreeSet<>();
	private int comp_counter = 0;

	/**
	 * Ermittelt alle strengen Komponenten mit
	 * dem Kosaraju-Sharir Algorithmus.
	 * @param g gerichteter Graph.
	 */
	public StrongComponents(DirectedGraph<V> g) {
		kosarajuSharirAlgorithm(g);
	}

	/* Ermittelt die invertierte PostOrder-Reihenfolge mittels einer Tiefensuche */
	public List<V> inversePostOrder(DirectedGraph<V> g) {
		DepthFirstOrder<V> d = new DepthFirstOrder<>(g);
		List<V> postOrder = new LinkedList<>(d.postOrder());
		Collections.reverse(postOrder);
		return postOrder;
	}

	/** Macht eine Tiefensuche über die Knoten der obersten Tiefensuchebene in
		der invertierten Post-Order-Reihenfolge */
	public void kosarajuSharirAlgorithm(DirectedGraph<V> g) {
		List<V> p_inverse = inversePostOrder(g);
		DirectedGraph<V> g_inverse = g.invert();

		for (V v : p_inverse) {
			if (!besucht.contains(v)) {
				comp.put(comp_counter, new TreeSet<>());
				comp.get(comp_counter).add(v);
				besucht.add(v);
				kosarajuSharirAlgorithmR(v, g_inverse, comp_counter);
				comp_counter++;
			}
		}
	}

	public void kosarajuSharirAlgorithmR(V v, DirectedGraph<V> g, int comp_counter) {
		for (var neighbour : g.getSuccessorVertexSet(v)) {
			if (!besucht.contains(neighbour)) {
				comp.get(comp_counter).add(neighbour);
				besucht.add(neighbour);
				kosarajuSharirAlgorithmR(neighbour, g, comp_counter);
			}
		}
	}

	/* Anzahl der starken Komponenten. */
	public int numberOfComp() {
		return comp.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for (var v : comp.entrySet()) {
			sb.append("Component ").append(v.getKey()).append(": ");
			for (var e : v.getValue())
				sb.append(e.toString()).append(", ");
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Liest einen gerichteten Graphen von einer Datei ein.
	 * @param fn Dateiname.
	 * @return gerichteter Graph.
	 * @throws FileNotFoundException
	 */
	public static DirectedGraph<Integer> readDirectedGraph(File fn) throws FileNotFoundException {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		Scanner sc = new Scanner(fn);
		sc.nextLine();
        sc.nextLine();
		while (sc.hasNextInt()) {
			int v = sc.nextInt();
			int w = sc.nextInt();
			g.addEdge(v, w);
		}
		sc.close();
		return g;
	}

	private static void test1() {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(1,3);
		g.addEdge(2,1);
		g.addEdge(2,3);
		g.addEdge(3,1);

		g.addEdge(1,4);
		g.addEdge(5,4);

		g.addEdge(5,7);
		g.addEdge(6,5);
		g.addEdge(7,6);

		g.addEdge(7,8);
		g.addEdge(8,2);

		StrongComponents<Integer> sc = new StrongComponents<>(g);

		System.out.println(sc.numberOfComp());  // 4

		System.out.println(sc);
			// Component 0: 5, 6, 7,
        	// Component 1: 8,
            // Component 2: 1, 2, 3,
            // Component 3: 4,
	}

	private static void test2() throws FileNotFoundException {
		DirectedGraph<Integer> g = readDirectedGraph(new File("aufgabe2\\graph\\mediumDG.txt"));
		System.out.println(g.getNumberOfVertexes());
		System.out.println(g.getNumberOfEdges());
		System.out.println(g);

		System.out.println("");

		StrongComponents<Integer> sc = new StrongComponents<>(g);
		System.out.println(sc.numberOfComp());  // 10
		System.out.println(sc);

	}

	public static void main(String[] args) throws FileNotFoundException {
		test1();
		test2();
	}
}
