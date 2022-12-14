// O. Bittel
// 22.09.2022

import java.util.Iterator;

/**
 * Implementation of the Dictionary interface as AVL tree.
 * <p>
 * The entries are ordered using their natural ordering on the keys,
 * or by a Comparator provided at set creation time, depending on which constructor is used.
 * <p>
 * An iterator for this dictionary is implemented by using the parent node reference.
 *
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if (root != null) {
            root.parent = null;
        }
        return oldValue;  // liefere den alten Wert zurück
    }

    private Node<K, V> insertR(K key, V value, Node<K, V> p) {
        if (p == null) {  // Knoten kommt nicht vor, füge neuen Knoten ein
            p = new Node<>(key, value);
            oldValue = null;
            size++;
        } else if (key.compareTo(p.key) < 0) {  // key ist kleiner als Knoten, suche links weiter
            p.left = insertR(key, value, p.left);
            if (p.left != null) {
                p.left.parent = p;
            }
        } else if (key.compareTo(p.key) > 0) {  // key ist größer als Knoten, suche rechts weiter
            p.right = insertR(key, value, p.right);
            if (p.right != null) {
                p.right.parent = p;
            }
        } else {  // speichere alten Wert und aktualisiere den Knoten mit neuem Wert
            oldValue = p.value;
            p.value = value;
        }
        p = balance(p);  // balanciere den Baum, sodass er ein AVL-Baum bleibt
        return p;  // liefere den Baum zurück
    }

    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    private V searchR(K key, Node<K, V> p) {
        if (p == null) {  // Knoten kommt nicht vor
            return null;
        } else if (key.compareTo(p.key) < 0) {  // key ist kleiner als Knoten, suche links weiter
            return searchR(key, p.left);
        } else if (key.compareTo(p.key) > 0) {  // key ist größer als Knoten, suche rechts weiter
            return searchR(key, p.right);
        } else {  // key gefunden, liefere den Wert zurück
            return p.value;
        }
    }

    @Override
    public V remove(K key) {
        root = removeR(key, root);
        return oldValue;  // liefere alten Wert zurück, standardmäßig null
    }

    private Node<K, V> removeR(K key, Node<K, V> p) {
        if (p == null) {  // Knoten kommt nicht vor, tue nichts
            oldValue = null;
        } else if (key.compareTo(p.key) < 0) {  // key ist kleiner als Knoten, suche links weiter
            p.left = removeR(key, p.left);
        } else if (key.compareTo(p.key) > 0) {  // key ist größer als Knoten, suche rechts weiter
            p.right = removeR(key, p.right);
        } else if (p.left == null || p.right == null) {  // Knoten hat genau ein Kind und wird ausgehängt
            oldValue = p.value;
            p = (p.left != null) ? p.left : p.right;
            size--;
        } else {  // Knoten hat zwei Kinder, ersetze p durch kleinsten Knoten im rechten Teilbaum
            MinEntry<K, V> min = new MinEntry<>();
            p.right = getRemMinR(p.right, min);
            oldValue = p.value;
            p.key = min.key;
            p.value = min.value;
            size--;
        }
        p = balance(p);  // balanciere den Baum, sodass er ein AVL-Baum bleibt
        return p;  // liefere den Baum zurück
    }

    private Node<K, V> getRemMinR(Node<K, V> p, MinEntry<K, V> min) {
        assert p != null;  // stelle sicher, dass der Baum nicht leer ist
        if (p.left == null) {  // kleinsten Wert gefunden, initialisiere 'min' mit 'p'
            min.key = p.key;
            min.value = p.value;
            p = p.right;
        } else {  // linker Teilbaum ist leer, suche rechts weiter
            p.left = getRemMinR(p.left, min);
        }
        p = balance(p);  // balanciere den Baum, sodass er ein AVL-Baum bleibt
        return p;  // liefere den Baum zurück
    }

    @Override
    public int size() {
        return size;
    }

    public int getHeight(Node<K, V> p) {
        if (p == null) {  // leerer Teilbaum hat Höhe -1
            return -1;
        } else {
            return p.height;
        }
    }

    public int getBalance(Node<K, V> p) {
        if (p == null) {  // Knoten mit jeweils leeren Teilbäumen hat Höhenunterschied 0
            return 0;
        } else {  //
            return getHeight(p.right) - getHeight(p.left);  // Höhe rechts minus Höhe links
        }
    }

    private Node<K, V> balance(Node<K, V> p) {
        if (p == null) {  // leerer Teilbaum, tue nichts
            return null;
        }
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;  // Höhe aktualisieren
        if (getBalance(p) == -2) {
            if (getBalance(p.left) <= 0) {
                p = rotateRight(p);  // Fall A1
            } else {
                p = rotateLeftRight(p);  // Fall A2
            }
        } else if (getBalance(p) == 2) {
            if (getBalance(p.right) >= 0) {
                p = rotateLeft(p);  // Fall B1
            } else {
                p = rotateRightLeft(p);  // Fall B2
            }
        }
        return p;
    }

    private Node<K, V> rotateRight(Node<K, V> p) {
        assert p.left != null;
        Node<K, V> q = p.left;
        p.left = q.right;
        if (p.left != null)
            p.left.parent = p;
        q.right = p;
        if (q.right != null)
            q.right.parent = q;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }
    private Node<K, V> rotateLeftRight(Node<K, V> p) {
        assert p.left != null;
        p.left = rotateLeft(p.left);
        return rotateRight(p);
    }
    private Node<K, V> rotateLeft(Node<K, V> p) {
        assert p.right != null;
        Node<K, V> q = p.right;
        p.right = q.left;
        if (p.right != null)
            p.right.parent = p;
        q.left = p;
        if (q.left != null)
            q.left.parent = q;
        p.height = Math.max(getHeight(p.right), getHeight(p.left)) + 1;
        q.height = Math.max(getHeight(q.right), getHeight(q.left)) + 1;
        return q;
    }
    private Node<K, V> rotateRightLeft(Node<K, V> p) {
        assert p.right != null;
        p.right = rotateRight(p.right);
        return rotateLeft(p);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<Entry<K, V>>() {

            private Node<K, V> current = null;

            @Override
            public boolean hasNext() {
                if (current == null) {  // Knoten hat keine Kinder
                    current = leftMostDescendant(root);
                } else if (current.right != null) {  // Knoten hat rechtes Kind
                    current = leftMostDescendant(current.right);
                } else {
                    current = parentOfLeftMostAncestor(current);
                }
                return current == null ? false : true;
            }

            @Override
            public Entry<K, V> next() {
                Entry<K, V> element = new Entry<K, V>(current.key, current.value);
                return element;
            }
        };
    }

    private Node<K,V> leftMostDescendant(Node<K, V> p) {
        assert p != null;
        while (p.left != null) {
            p = p.left;
        }
        return p;
    }
    private Node<K,V> parentOfLeftMostAncestor(Node<K, V> p) {
        assert p != null;
        while (p.parent != null && p.parent.right == p) {
            p = p.parent;
        }
        return p.parent;
    }

    private static class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }
    }

    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }

    private Node<K, V> root = null;
    private int size = 0;
    private V oldValue;  // Ausgabeparameter

	/**
	 * Pretty prints the tree
	 */
	public void prettyPrint() {
        printR(0, root);
    }

    private void printR(int level, Node<K, V> p) {
        printLevel(level);
        if (p == null) {
            System.out.println("#");
        } else {
            System.out.println(p.key + " " + p.value + "^" + ((p.parent == null) ? "null" : ""+p.parent.key));
            if (p.left != null || p.right != null) {
                printR(level + 1, p.left);
                printR(level + 1, p.right);
            }
        }
    }

    private static void printLevel(int level) {
        if (level == 0) {
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   ");
        }
        System.out.print("|__");
    }
}
