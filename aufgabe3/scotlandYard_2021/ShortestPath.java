
// O. Bittel;
// 26.09.2022
import sim.SYSimulation;
import java.util.*;

// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 *
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {

	SYSimulation sim = null;

	Map<V, Double> dist; // Distanz für jeden Knoten
	Map<V, V> pred; // Vorgänger für jeden Knoten
	IndexMinPQ<V, Double> cand; // Kandidaten als PriorityQueue PQ
	DirectedGraph<V> graph;
	Heuristic<V> heuristic;
	V start;
	V ziel;
	Double inf = Double.MAX_VALUE; // "unendlich"

	/**
	 * Konstruiert ein Objekt, das im Graph g kürzeste Wege
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch
	 * mit dem Dijkstra-Verfahren.
	 *
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 *          dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();
		graph = g;
		heuristic = h;
	}

	/**
	 * Diese Methode sollte nur verwendet werden,
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p>
	 * <blockquote>
	 *
	 * <pre>
	 * if (sim != null)
	 * 	sim.visitStation((Integer) v, Color.blue);
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 *
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {
		shortestPath(s, g, graph, dist, pred, cand);
	}

	/**
	 * Hilfsmethode zum Berechnen des kürzesten Weges
	 *
	 * @param s    Startknoten
	 * @param z    Zielknoten
	 * @param g    Graph
	 * @param dist Distanz zu jedem Knoten
	 * @param pred Vorgänger von jedem Knoten
	 * @param cand Kandidatenliste als PQ
	 * @return true, wenn der Knoten mit der minimalen Distanz der Zielknoten ist
	 */
	private boolean shortestPath(V s, V z, DirectedGraph<V> g, Map<V, Double> dist,
			Map<V, V> pred, IndexMinPQ<V, Double> cand) {
		dist.clear();
		cand.clear();
		pred.clear();
		start = s;
		ziel = z;

		for (V v : g.getVertexSet()) {
			dist.put(v, inf); // Distanz unendlich
			pred.put(v, null); // Vorgänger undefiniert
		}

		dist.put(s, 0.0); // Startknoten

		if (heuristic == null) { /* === Dijkstra-Verfahren === */
			cand.add(s, 0.0);
		} else { /* === A*-Verfahren === */
			cand.add(s, heuristic.estimatedCost(s, z));
		}
		while (!cand.isEmpty()) {
			V v = cand.removeMin(); // Knoten mit kleinstem Distanzwert
			System.out.println("Besuchter Knoten " + v + " mit d: " + dist.get(v));
			if (v == z) {
				return true; // Zielknoten z erreicht
			}
			for (V w : g.getPredecessorVertexSet(v)) {
				if (heuristic == null) { /* === Dijkstra-Verfahren === */
					if (dist.get(w) == inf) {
						pred.put(w, v); // p[w] = v;
						dist.put(w, dist.get(v) + g.getWeight(v, w)); // d[w] = d[v] + c(v,w);
						cand.add(w, dist.get(w)); // kl.insert(w, d[w]);
					} else if (dist.get(v) + 0 < dist.get(w)) {
						pred.put(w, v); // p[w] = v;
						dist.put(w, dist.get(v) + g.getWeight(v, w)); // d[w] = d[v] + c(v,w);
						cand.change(w, dist.get(w)); // kl.change(w, d[w]);
					}
				} else { /* === A*-Verfahren === */
					if (dist.get(w) == inf) {
						pred.put(w, v); // p[w] = v;
						dist.put(w, dist.get(v) + g.getWeight(v, w)); // d[w] = d[v] + c(v,w);
						cand.add(w, dist.get(w) + heuristic.estimatedCost(w, z)); // kl.insert(w, d[w] + h(w,z));
					} else if (dist.get(v) + g.getWeight(v, w) < dist.get(w)) {
						pred.put(w, v); // p[w] = v;
						dist.put(w, dist.get(v) + g.getWeight(v, w)); // d[w] = d[v] + c(v,w);
						cand.change(w, dist.get(w) + heuristic.estimatedCost(w, z)); // kl.change(w, d[w] + h(w,z));
					}
				}
			}
		}
		return false;
	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 *
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		LinkedList<V> shortestPath = new LinkedList<>();
		V t = pred.get(ziel);
		shortestPath.add(ziel);
		while (t != start) {
			shortestPath.add(t);
			t = pred.get(t);
		}
		shortestPath.add(start);
		Collections.reverse(shortestPath);
		return shortestPath;
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g
	 * zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 *
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		return dist.get(ziel);
	}

}
