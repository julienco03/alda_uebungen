import java.util.Arrays;
import java.util.Iterator;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {

    private Entry<K, V>[] data;
    private int size;
    private static final int DEF_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public SortedArrayDictionary() {
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);

        if (i >= 0) {
            V old = data[i].getValue();
            data[i].setValue(value);
            return old;
        }

        if (data.length == size) {
            data = Arrays.copyOf(data, 2*size);
        }

        int j = size-1;
        while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
            data[j+1] = data[j];
            j--;
        }

        data[j+1] = new Entry<>(key, value);
        size++;
        return null;
    }

    @Override
    public V search(K key) {
        int i = searchKey(key);
        if (i >= 0) {
            return data[i].getValue();
        } else {
            return null;
        }
    }

    private int searchKey(K key) {
        int li = 0;
        int re = size-1;

        while (re >= li) {
            int m = (li+re)/2;
            if (key.compareTo(data[m].getKey()) < 0) re = m-1;
            else if (key.compareTo(data[m].getKey()) > 0) li = m+1;
            else return m;
        }
        return -1;
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1) {
            return null;
        }

        V r = data[i].getValue();
        if (size - 1 - i >= 0) System.arraycopy(data, i + 1, data, i, size - 1 - i);
        data[--size] = null;
        return r;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {

            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            public Entry<K, V> next() {
                return data[current++];
            }
        };
    }
}
