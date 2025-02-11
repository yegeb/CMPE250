import java.util.ArrayList;
import java.util.Iterator;

/**
 * A custom implementation of a HashMap-like data structure.
 * This implementation uses separate chaining with linked lists to handle collisions.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class MyHashMap<K, V> {

    /** Default initial capacity of the hash table */
    private static final int DEFAULT_CAPACITY = 13;

    /** Load factor threshold for resizing the hash table */
    private static final float LOAD_FACTOR = 1f;

    /** Array of custom linked lists (buckets) to store entries */
    private MyLinkedList<Entry<K, V>>[] table;

    /** Number of key-value pairs in the hash map */
    private int size;

    /**
     * Default constructor that initializes the hash table with the default capacity.
     */
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new MyLinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Constructor that initializes the hash table with a specified capacity.
     *
     * @param capacity the initial capacity of the hash table
     */
    @SuppressWarnings("unchecked")
    public MyHashMap(int capacity) {
        table = new MyLinkedList[capacity];
        size = 0;
    }

    /**
     * Entry class representing a key-value pair in the hash map.
     */
    public static class Entry<K, V> {
        K key;    // The key of the entry
        V value;  // The value associated with the key

        Entry(K k, V v) {
            key = k;
            value = v;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    /**
     * Computes the hash index for a given key.
     *
     * @param key the key to hash
     * @return the computed index in the hash table
     */
    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    /**
     * Associates the specified value with the specified key in the hash map.
     * If the key already exists, updates its value.
     */
    public void put(K key, V value) {
        int index = hash(key);

        // Initialize the bucket if it doesn't exist
        if (table[index] == null) {
            table[index] = new MyLinkedList<>();
        }

        MyLinkedList<Entry<K, V>> bucket = table[index];

        // Update value if the key already exists
        for (Entry<K, V> entry : bucket) {
            if (equalsKey(entry.key, key)) {
                entry.value = value;
                return;
            }
        }
        // Add new entry if key does not exist
        bucket.addLast(new Entry<>(key, value));
        size++;

        // Resize the table if the load factor threshold is exceeded
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
    }

    /**
     * Retrieves the value associated with the specified key.
     */
    public V get(K key) {
        int index = hash(key);
        MyLinkedList<Entry<K, V>> bucket = table[index];

        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (equalsKey(entry.key, key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the hash map contains the specified key.
     */
    public boolean containsKey(K key) {
        int index = hash(key);
        MyLinkedList<Entry<K, V>> bucket = table[index];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (equalsKey(entry.key, key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes the key-value pair associated with the specified key.
     */
    public void remove(K key) {
        int index = hash(key);
        MyLinkedList<Entry<K, V>> bucket = table[index];

        if (bucket != null) {
            Iterator<Entry<K, V>> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (equalsKey(entry.key, key)) {
                    bucket.remove(entry);
                    size--;
                    return;
                }
            }
        }
    }

    /**
     * Resizes the hash table when the load factor exceeds the threshold.
     * Doubles the capacity and rehashes all existing entries.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        MyLinkedList<Entry<K, V>>[] oldTable = table;
        table = new MyLinkedList[oldTable.length * 2];
        size = 0;

        for (MyLinkedList<Entry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }

    /**
     * Utility method to check key equality, handling nulls.
     */
    private boolean equalsKey(K key1, K key2) {
        return (key1 == null && key2 == null) || (key1 != null && key1.equals(key2));
    }

    /**
     * Returns a list of all values in the hash map.
     */
    public ArrayList<V> values() {
        ArrayList<V> valuesList = new ArrayList<>();
        for (MyLinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    valuesList.add(entry.value);
                }
            }
        }
        return valuesList;
    }

    /**
     * Returns the number of key-value pairs in the hash map.
     */
    public int size() {
        return size;
    }

    /**
     * Removes all key-value pairs from the hash map.
     */
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    /**
     * Checks if the hash map is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an Iterable over all Entry<K, V> objects in the hash map,
     * allowing for "for-each" usage like:
     * for (MyHashMap.Entry<K, V> e : myHashMap.entrySet()) { ... }
     */
    public Iterable<Entry<K, V>> entrySet() {
        return new Iterable<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new MyHashMapIterator();
            }
        };
    }

    /**
     * A private iterator class that scans each bucket in MyHashMap's underlying table,
     * yielding all Entry<K, V> objects in sequence. This iterator advances from left
     * to right through the table, and from front to back within each bucket's linked list.
     */
    private class MyHashMapIterator implements Iterator<Entry<K, V>> {

        /** The current bucket index in the hash table array. */
        int bucketIndex = 0;

        /** An iterator over the current bucket's entries. */
        Iterator<Entry<K, V>> bucketIterator = null;

        /**
         * Constructs a MyHashMapIterator and immediately advances to the first
         * non-empty bucket, if any.
         */
        MyHashMapIterator() {
            advanceToNextNonEmptyBucket();
        }

        /**
         * Checks if there is at least one more Entry<K, V> available in the hash map.
         *
         * @return true if another entry is found, false otherwise
         */
        @Override
        public boolean hasNext() {
            // We're valid only if we haven't run off the end of the table
            // and the current bucketIterator still has a next element.
            return bucketIndex < table.length && bucketIterator != null && bucketIterator.hasNext();
        }

        /**
         * Returns the next Entry<K, V> in the hash map iteration.
         * This will pull the next Entry from the current bucket.
         * If that bucket becomes empty, we advance to the next non-empty bucket.
         *
         * @return the next Entry<K, V>
         */
        @Override
        public Entry<K, V> next() {
            // Retrieve the next entry from the current bucket
            Entry<K, V> nextEntry = bucketIterator.next();

            // If we've exhausted this bucket, move on to the next one
            if (!bucketIterator.hasNext()) {
                bucketIndex++;
                advanceToNextNonEmptyBucket();
            }
            return nextEntry;
        }

        /**
         * Advances bucketIndex to the next bucket that is non-empty, and sets
         * bucketIterator to iterate over that bucket. If no more buckets exist,
         * bucketIterator remains null and hasNext() returns false.
         */
        private void advanceToNextNonEmptyBucket() {
            // Scan forward until we find a non-empty bucket or exhaust the table
            while (bucketIndex < table.length) {
                // If this bucket exists and has entries, set up the iterator
                if (table[bucketIndex] != null && !table[bucketIndex].isEmpty()) {
                    bucketIterator = table[bucketIndex].iterator();
                    break;
                }
                bucketIndex++;
            }
        }
    }

}
