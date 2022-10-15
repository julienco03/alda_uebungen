import java.util.Iterator;
import java.util.LinkedList;
import java.lang.Math;

public class HashDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private LinkedList<Entry<K, V>>[] data;
    private int size;
    private int load;

    @SuppressWarnings("unchecked")
    public HashDictionary(int load) {
        this.size = 0;
        this.load = load;
        this.data = new LinkedList[load];
    }

    private static boolean isPrime(int n) {
        for (int i = 2; i*i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public V insert(K key, V value) {
        int adr = searchAdr(key);
        if (adr != -1) {
            // key already exists
            // TODO: alte daten ersetzen
            //Entry<K,V> old = data[adr].
            // return old;
        } else {
            data[adr].addFirst(new Entry<K, V>(key, value));
        }
    }

    @Override
    public V search(K key) {
        int adr = searchAdr(key);
        if (adr != -1) {
            return data[adr].value;
        } else {
            return null;
        }
    }

    private int searchAdr(K key) {
        int adr, j = 0;
        do {
            int hash = (key.hashCode() > 0 ? adr : -adr);  // handle hashcode overflow
            adr =  hash - (int) Math.floor(hash / size) * size;  // mathematical modulo

        } while (data[adr] != null && data[adr].key != key);

        if (/** letzter knoten != null*/) {
            return adr;
        } else {
            return -1;
        }
    }

    @Override
    public V remove(K key) {
        int adr = searchAdr(key);
        if (adr != -1) {
            // TODO: knoten k aus liste löschen
            // return daten
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {
        return null;
    }
}
