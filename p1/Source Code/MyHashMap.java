/**
 * MyHashMap is a custom implementation of a HashMap that uses an array for storage.
 * This implementation supports basic operations such as put, remove, get, and containsKey.
 *
 * @param <E> The type of elements stored in the map.
 */
public class MyHashMap<E> {

    private E[] array; // Array to store values mapped by their integer keys
    private static final int CAPACITY = 500001; // Fixed capacity of the array
    private int size; // Tracks the number of unique entries in the map

    /**
     * Constructs a new MyHashMap with a fixed capacity.
     * Initializes the array to hold objects of type E and sets the size to zero.
     */
    public MyHashMap() {
        array = (E[]) new Object[CAPACITY]; // Initialize array with specified capacity
        size = 0; // Initialize size to zero
    }

    /**
     * Inserts a key-value pair into the map.
     * If the key already exists, the value is updated. If it is a new key, size is incremented.
     *
     * @param key   The integer key associated with the value.
     * @param value The value to store in the map.
     */
    public void put(int key, E value) {
        if (array[key] == null) {
            size++; // Increase size only if it's a new entry
        }
        array[key] = value; // Insert or update the value at the given key index
    }

    /**
     * Removes the value associated with the specified key.
     * If the key exists, the entry is set to null, and the size is decremented.
     *
     * @param key The key for the entry to be removed.
     */
    public void remove(int key) {
        if (array[key] != null) {
            array[key] = null; // Set entry to null, removing it from the map
            size--; // Decrement size as an entry has been removed
        }
    }

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key The key for the entry to retrieve.
     * @return The value associated with the key, or null if the key is not present.
     */
    public E get(int key) {
        return array[key]; // Return the value at the specified key index
    }

    /**
     * Checks if the map contains an entry for the specified key.
     *
     * @param key The key to check for existence in the map.
     * @return True if the map contains the key, false otherwise.
     */
    public boolean containsKey(int key) {
        return array[key] != null; // Return true if entry at key index is not null
    }
}
