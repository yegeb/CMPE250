import java.util.Iterator;

/**
 * A generic queue implementation using a custom linked list as the underlying data structure.
 * This queue has a dynamic capacity that doubles whenever the current size reaches the limit.
 *
 * @param <E> the type of elements held in this queue
 */
public class MyQueue<E> implements Iterable<E> {

    /** The maximum number of elements allowed before the queue enlarges. */
    private int capacity;

    /** The underlying linked list for storing queue elements. */
    private MyLinkedList<E> myList;

    /** The current number of elements in the queue. */
    private int size;

    /**
     * Constructs a MyQueue with a default initial capacity of 10.
     */
    public MyQueue() {
        this(10); // Set a default capacity
    }

    /**
     * Constructs a MyQueue with a specified initial capacity.
     *
     * @param capacity the initial capacity of the queue
     */
    public MyQueue(int capacity) {
        myList = new MyLinkedList<>();
        this.capacity = capacity;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return myList.isEmpty();
    }

    /**
     * Checks if the queue is at full capacity.
     *
     * @return true if the queue cannot accept more elements without enlarging, false otherwise
     */
    public boolean isFull() {
        return size >= capacity;
    }

    /**
     * Adds an element to the end of the queue. If the queue is full (size == capacity),
     * the capacity is doubled (via enlarge()) before enqueueing the new element.
     *
     * @param element the element to add
     */
    public void enqueue(E element) {
        if (size == capacity) { // Ensures size does not exceed capacity
            enlarge();
        }
        myList.addLast(element);
        size++;
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return the front element of the queue, or null if the queue is empty
     */
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        E removedElement = myList.removeFirst();
        if (removedElement != null) {
            size--; // Decrement size only if an element was actually removed
        }
        return removedElement;
    }

    /**
     * Retrieves, but does not remove, the front element of the queue.
     *
     * @return the first element of the queue, or null if the queue is empty
     */
    public E peek() {
        if (isEmpty()) {
            return null; // Return null if the queue is empty
        }
        return myList.first(); // Get the first element without removing it
    }

    /**
     * Returns the number of elements currently in the queue.
     *
     * @return the number of elements in the queue
     */
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in this queue, in proper sequence (front to back).
     *
     * @return an Iterator<E> for traversing the queue elements
     */
    @Override
    public Iterator<E> iterator() {
        return myList.iterator();
    }

    /**
     * Returns a string representation of this queue and its elements.
     *
     * @return a string listing the queue elements in order
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MyQueue: [");

        // Use the iterator to traverse the queue elements
        Iterator<E> iterator = myList.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * Indicates whether some other object is "equal to" this queue.
     * Two MyQueue objects are considered equal if they have the same size
     * and contain the same elements in the same order.
     *
     * @param obj the object to compare
     * @return true if this queue is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // Check if the object is the same instance
        if (this == obj) {
            return true;
        }

        // Check if the object is an instance of MyQueue
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Cast the object to MyQueue
        MyQueue<?> otherQueue = (MyQueue<?>) obj;

        // First, compare the size of both queues
        if (this.size != otherQueue.size) {
            return false;
        }

        // Now, compare the elements in the queues
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = otherQueue.iterator();

        // Check if all elements are the same in sequence
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E thisElement = thisIterator.next();
            Object otherElement = otherIterator.next();

            // If any corresponding elements differ, return false
            if (!thisElement.equals(otherElement)) {
                return false;
            }
        }

        // If both iterators have finished simultaneously, the queues are equal
        return !thisIterator.hasNext() && !otherIterator.hasNext();
    }

    /**
     * Doubles the capacity of the queue.
     * Called internally by enqueue() if the queue is full.
     */
    public void enlarge() {
        // Double the capacity
        int newCapacity = this.capacity * 2;

        // Update the capacity field
        this.capacity = newCapacity;
        // Optionally, you can add a log statement or some message:
        // System.out.println("Queue capacity enlarged to " + newCapacity);
    }
}
