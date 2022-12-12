// O. Bittel;
// 26.09.22

//package directedGraph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class TopologicalSort<V> {
    private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge
	// ...

	/**
	 * Führt eine topologische Sortierung für g durch.
	 * @param g gerichteter Graph.
	 */
	public TopologicalSort(DirectedGraph<V> graph) {
        List<V> result = new LinkedList<>();
		Map<V, Integer> inDegree = new TreeMap<>();
		Queue<V> q = new ArrayDeque<>();

		for (V v : graph.getVertexSet()) {
			inDegree.put(v, graph.getInDegree(v));
			if (inDegree.get(v) == 0) {
				q.add(v);
			}
		}

		while (!q.isEmpty()) {
			V v = q.remove();
			result.add(v);
			for (V w : graph.getSuccessorVertexSet(v)) {
				inDegree.put(w, inDegree.get(w) - 1);
 				if (inDegree.get(w) == 0) {
					q.add(w);
				}
			}
		}

		// speichere die Topologische Sortierung in ts nur, wenn keine Zyklen existieren
		if (result.size() == graph.getNumberOfVertexes()) {
			ts.addAll(result);
		}
    }
    
	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück,
	 * die topologisch sortiert ist.
	 * @return topologisch sortierte Liste
	 */
	public List<V> topologicalSortedList() {
        return Collections.unmodifiableList(ts);
    }
    

	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1, 3);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		g.addEdge(4, 6);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		System.out.println(g);

		TopologicalSort<Integer> ts1 = new TopologicalSort<>(g);
		
		System.out.println(ts1.topologicalSortedList()); // [1, 2, 3, 4, 5, 6, 7]

		System.out.println("\nMorgenroutine:");
		DirectedGraph<String> morningRoutine = new AdjacencyListDirectedGraph<>();
		morningRoutine.addEdge("Socken", "Schuhe");
		morningRoutine.addEdge("Unterhose", "Hose");
		morningRoutine.addEdge("Unterhemd", "Hemd");
		morningRoutine.addEdge("Hose", "Schuhe");
		morningRoutine.addEdge("Hose", "Gürtel");
		morningRoutine.addEdge("Hemd", "Pulli");
		morningRoutine.addEdge("Schuhe", "Handschuhe");
		morningRoutine.addEdge("Gürtel", "Mantel");
		morningRoutine.addEdge("Pulli", "Mantel");
		morningRoutine.addEdge("Mantel", "Schal");
		morningRoutine.addEdge("Schal", "Handschuhe");
		morningRoutine.addEdge("Mütze", "Handschuhe");
		// Wenn eine Hose nur mit einem Schal angezogen werden darf, entsteht ein Zyklus und die Topologische Sortierung schlägt fehl
		// morningRoutine.addEdge("Schal", "Hose");
		System.out.println(morningRoutine);

		TopologicalSort<String> ts2 = new TopologicalSort<>(morningRoutine);

		System.out.println(ts2.topologicalSortedList());
	}
}
