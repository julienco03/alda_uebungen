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

        if (search(key) != null) {
            for (var e : data[adr]) {
                if (e.getKey().equals(key)) {
                    V old = e.getValue();
                    e.setValue(value);
                    return old;
                }
            }
        }

        if (size == data.length) { ensureCapacity(); }
        if (data[adr] == null) {
            data[adr] = new LinkedList<>();
            data[adr].add(new Entry<K, V>(key, value));
            size++;
        } else {
            data[adr].add(new Entry<K, V>(key, value));
            size++;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        size = 0;
        int newload = load * 2;

        while (!isPrime(newload)) {
            ++newload;
        }

        HashDictionary<K, V> newdata = new HashDictionary<>(newload);
        for (var v : data) {
            if (v == null) continue;
            for (Entry<K, V> e : v) {
                newdata.insert(e.getKey(), e.getValue());
            }
        }

        data = new LinkedList[newload];
        load = newload;

        for (var v : newdata.data) {
            if (v == null) continue;
            for (Entry<K, V> e : v) {
                insert(e.getKey(), e.getValue());
            }
        }
    }

    private int searchAdr(K key) {
        int adr = 0;
        int hash = key.hashCode() > 0 ? adr : -adr;  // handle hashcode overflow
        // not sure if this always works fine: adr = hash % (data.length - 1);
        adr =  hash - (int) Math.floor(hash / data.length) * data.length;  // mathematical modulo

        return adr;
    }

    @Override
    public V search(K key) {
        int adr = searchAdr(key);
        if (data[adr] != null) {
            for (var e : data[adr]) {
                if (e.getKey().equals(key)) {
                    return e.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int adr = searchAdr(key);

        for (var e : data[adr]) {
            if (e.getKey().equals(key)) {
                V old = e.getValue();
                data[adr].remove(e);
                size--;
                return old;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashDictionaryIterator();
    }

    private class HashDictionaryIterator implements Iterator<Entry<K, V>> {

        Iterator<Entry<K, V>> it;
        int index = -1;

        @Override
        public boolean hasNext() {
            if (it != null && it.hasNext()) {
                return true;
            }
            while (++index < data.length) {
                if (data[index] != null) {
                    it = data[index].iterator();
                    return it.hasNext();
                }
            }
            return false;
        }

        @Override
        public Entry<K, V> next() {
            return it.next();
        }

    }
}
