import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * A generic binary min-heap data structure, storing (key, value) pairs
 * where the value is a Comparable used to determine the heap order.
 *
 * @param <K> the type of keys
 * @param <V> the type of comparable values
 */
public class BinaryHeap<K, V extends Comparable<V>> {

    /**
     * Internal array list representing the binary heap.
     * The root (minimum element) is stored at index 0.
     */
    private ArrayList<Entry<K, V>> heap;

    /**
     * A custom HashMap that tracks the current index of each key in the heap,
     * enabling O(log n) decreaseKey operations.
     */
    private MyHashMap<K, Integer> indexMap;

    /**
     * Constructs an empty BinaryHeap with no initial elements.
     * Uses ArrayList for the underlying heap storage and a custom MyHashMap
     * to track the positions (indices) of keys in the heap.
     */
    public BinaryHeap() {
        heap = new ArrayList<>();
        indexMap = new MyHashMap<>();
    }

    /**
     * Represents an entry (key, value) in the binary heap.
     *
     * @param <K> the type of the key
     * @param <V> the type of the comparable value
     */
    public static class Entry<K, V extends Comparable<V>> {
        K key;      // The key associated with this entry
        V value;    // The comparable value used to maintain heap order

        /**
         * Constructs a new Entry object with the specified key and value.
         *
         * @param key   the key
         * @param value the value
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key for this entry.
         *
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value for this entry.
         *
         * @return the value
         */
        public V getValue() {
            return value;
        }

        /**
         * Sets a new value for this entry.
         *
         * @param value the new value
         */
        public void setValue(V value) {
            this.value = value;
        }
    }

    /**
     * Inserts a new (key, value) pair into the heap, then "bubbles up" if needed.
     * Also updates the indexMap to record the new index of this key.
     *
     * @param key   the key
     * @param value the value (must be Comparable)
     */
    public void insert(K key, V value) {
        // Create a new entry
        Entry<K, V> entry = new Entry<>(key, value);
        heap.add(entry);           // Append at the end
        int index = heap.size() - 1;
        indexMap.put(key, index);  // Map the key to its index
        bubbleUp(index);           // Restore heap order if violated
    }

    /**
     * Extracts (and removes) the minimum entry (root) from the heap.
     * Then places the last entry at the root and "bubbles down" if needed.
     *
     * @return the Entry<K, V> with the smallest value
     * @throws NoSuchElementException if the heap is empty
     */
    public Entry<K, V> extractMin() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        // The root (min) is at index 0
        Entry<K, V> minEntry = heap.get(0);
        // Remove the last entry in the heap array
        Entry<K, V> lastEntry = heap.remove(heap.size() - 1);
        indexMap.remove(minEntry.key);

        // If the heap is not empty after removal, move lastEntry to root and bubble down
        if (!heap.isEmpty()) {
            heap.set(0, lastEntry);
            indexMap.put(lastEntry.key, 0);
            bubbleDown(0);
        }
        return minEntry;
    }

    /**
     * Decreases the value associated with the specified key to newValue, if newValue is smaller.
     * "Bubbles up" the entry to restore the min-heap order.
     *
     * @param key      the key whose value is being decreased
     * @param newValue the new (smaller) value
     * @throws NoSuchElementException    if the key is not found in the heap
     * @throws IllegalArgumentException  if newValue is greater than the current value
     */
    public void decreaseKey(K key, V newValue) {
        Integer index = indexMap.get(key);
        if (index == null) {
            throw new NoSuchElementException("Key not found in the heap: " + key);
        }

        Entry<K, V> entry = heap.get(index);
        // Ensure the new value is actually smaller (heap is for min-values)
        if (newValue.compareTo(entry.value) > 0) {
            throw new IllegalArgumentException("New value is greater than the current value");
        }
        entry.setValue(newValue);
        bubbleUp(index);
    }

    /**
     * Returns (but does not remove) the entry at the root (minimum value).
     *
     * @return the minimum entry in the heap
     * @throws NoSuchElementException if the heap is empty
     */
    public Entry<K, V> peekMin() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return heap.get(0);
    }

    /**
     * Checks whether the heap has any elements.
     *
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Swaps entries upwards until the heap property is restored.
     * Starting at 'index', compare with parent until the parent's value is no longer greater.
     *
     * @param index the index of the entry that may need to move up
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).value.compareTo(heap.get(parentIndex).value) >= 0) {
                break;
            }
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    /**
     * Swaps entries downwards until the heap property is restored.
     * Starting at 'index', compare with children until both children have larger values or no children exist.
     *
     * @param index the index of the entry that may need to move down
     */
    private void bubbleDown(int index) {
        int size = heap.size();
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            // Compare left child
            if (leftChild < size && heap.get(leftChild).value.compareTo(heap.get(smallest).value) < 0) {
                smallest = leftChild;
            }
            // Compare right child
            if (rightChild < size && heap.get(rightChild).value.compareTo(heap.get(smallest).value) < 0) {
                smallest = rightChild;
            }

            // If the smallest index is still 'index', no more bubble-down is required
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    /**
     * Swaps two entries in the heap's internal ArrayList and updates their indices in the indexMap.
     *
     * @param i the first index
     * @param j the second index
     */
    private void swap(int i, int j) {
        Entry<K, V> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        // Update indexMap for the swapped entries
        indexMap.put(heap.get(i).key, i);
        indexMap.put(heap.get(j).key, j);
    }

    /**
     * Returns a string representation of the heap, listing all (key, value) pairs.
     *
     * @return a textual representation of the BinaryHeap
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BinaryHeap: ");
        for (Entry<K, V> entry : heap) {
            sb.append("(").append(entry.key).append(", ").append(entry.value).append(") ");
        }
        return sb.toString();
    }
}
