/**
 * A custom implementation of a minimum binary heap.
 * The heap stores elements of type {@code Post} and ensures the smallest element is always at the root.
 *
 * @param <E> the type of elements stored in the heap, extending {@code Comparable}
 */
public class MyMinHeap<E extends Comparable<? super Post>> {

    /** The heap array storing elements */
    private Post[] array;

    /** The maximum capacity of the heap */
    private int capacity;

    /** The current number of elements in the heap */
    public int currentSize;

    /**
     * Constructs a minimum heap with the specified initial capacity.
     *
     * @param capacity the initial capacity of the heap
     */
    @SuppressWarnings("unchecked")
    public MyMinHeap(int capacity) {
        this.array = new Post[capacity + 1]; // Array index 0 is reserved
        this.currentSize = 0;
        this.capacity = capacity;
    }

    /**
     * Inserts a new element into the heap.
     * Resizes the heap if the capacity is exceeded.
     *
     * @param x the element to insert
     */
    public void insert(Post x) {
        // Resize if the heap is full
        if (currentSize == array.length - 1) {
            enlargeArray(array.length * 2 + 1);
        }

        // Percolate up to maintain heap order
        int hole = ++currentSize;
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2) {
            array[hole] = array[hole / 2]; // Move parent down
        }
        array[hole] = x; // Place the new element in the correct position
    }

    /**
     * Retrieves the smallest element in the heap without removing it.
     *
     * @return the smallest element in the heap
     * @throws RuntimeException if the heap is empty
     */
    public Post findMin() {
        if (isEmpty()) {
            throw new RuntimeException("Heap is empty");
        }
        return array[1]; // Minimum element is always at index 1
    }

    /**
     * Removes and returns the smallest element in the heap.
     * Restores heap order after removal.
     *
     * @return the smallest element in the heap
     * @throws RuntimeException if the heap is empty
     */
    public Post deleteMin() {
        if (isEmpty()) {
            throw new RuntimeException("Heap is empty");
        }

        Post minItem = findMin(); // Get the minimum element
        array[1] = array[currentSize--]; // Replace root with the last element
        percolateDown(1); // Restore heap order
        return minItem;
    }

    /**
     * Checks if the heap is empty.
     *
     * @return {@code true} if the heap is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Removes all elements from the heap.
     */
    @SuppressWarnings("unchecked")
    public void makeEmpty() {
        for (int i = 0; i <= currentSize; i++) {
            array[i] = null; // Nullify all elements
        }
        this.currentSize = 0;
    }

    /**
     * Restores the heap order by percolating down from a given index.
     *
     * @param hole the index to percolate down from
     */
    private void percolateDown(int hole) {
        int child;
        Post tmp = array[hole]; // Temporarily hold the element being moved

        while (hole * 2 <= currentSize) {
            child = hole * 2; // Left child

            // Select the smaller of the two children
            if (child != currentSize && array[child + 1].compareTo(array[child]) < 0) {
                child++;
            }

            // If the smaller child is smaller than the temp, move it up
            if (array[child].compareTo(tmp) < 0) {
                array[hole] = array[child];
            } else {
                break; // Heap order is restored
            }
            hole = child;
        }
        array[hole] = tmp; // Place the temp element in its correct position
    }

    /**
     * Builds the heap by percolating down all non-leaf nodes.
     * Useful when converting an arbitrary array into a heap.
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    /**
     * Resizes the heap array to a new size.
     *
     * @param newSize the new size of the array
     */
    @SuppressWarnings("unchecked")
    private void enlargeArray(int newSize) {
        Post[] oldArray = array; // Reference to the old array
        this.array = (Post[]) new Object[newSize]; // Create a new larger array
        System.arraycopy(oldArray, 0, this.array, 0, currentSize + 1); // Copy existing elements
    }

    /**
     * Sorts the elements in the heap and returns them as a sorted array.
     * The original heap remains unaltered.
     *
     * @return a sorted array of heap elements
     */
    public Post[] heapSort() {
        int size = currentSize; // Store the original size
        Post[] sortedArray = new Post[currentSize];

        // Perform heap sort
        for (int i = size - 1; i >= 0; i--) {
            Post minItem = deleteMin(); // Extract the minimum element
            sortedArray[i] = minItem; // Place it at the end of the array
        }

        return sortedArray;
    }
}
