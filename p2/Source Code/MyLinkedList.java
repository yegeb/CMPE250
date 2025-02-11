import java.util.Iterator;

/**
 * A custom implementation of a singly linked list that supports basic operations like adding,
 * removing, and clearing elements. Implements the {@code Iterable} interface to allow
 * iteration over its elements.
 *
 * @param <E> the type of elements held in this list
 */
public class MyLinkedList<E> implements Iterable<E> {

    /** The head (first node) of the linked list */
    private Node<E> head;

    /** The number of elements in the linked list */
    private int size;

    /**
     * A static nested class to represent a node in the linked list.
     *
     * @param <E> the type of element held in the node
     */
    private static class Node<E> {
        E data; // Data held by the node
        Node<E> next; // Reference to the next node in the list

        /**
         * Constructs a new Node with the given data.
         *
         * @param data the data to store in the node
         */
        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Constructs an empty linked list.
     */
    public MyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Adds an element to the end of the linked list.
     *
     * @param data the element to add
     */
    public void add(E data) {
        Node<E> newNode = new Node<>(data); // Create a new node
        if (head == null) { // If the list is empty, set the new node as the head
            head = newNode;
        } else { // Traverse to the end of the list and add the new node
            Node<E> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++; // Increment the size of the list
    }

    /**
     * Removes the first occurrence of the specified element from the linked list.
     *
     * @param data the element to remove
     * @return true if the element was removed, false if it was not found
     */
    public boolean remove(E data) {
        if (head == null) { // If the list is empty, nothing to remove
            return false;
        }

        if (head.data.equals(data)) { // If the element is at the head, update the head
            head = head.next;
            size--;
            return true;
        }

        Node<E> current = head;
        // Traverse the list to find the element
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }

        if (current.next != null) { // If the element is found, remove it
            current.next = current.next.next;
            size--;
            return true;
        }

        return false; // Element not found
    }

    /**
     * Returns the number of elements in the linked list.
     *
     * @return the size of the linked list
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the linked list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the linked list.
     */
    public void clear() {
        head = null; // Set the head to null to clear the list
        size = 0; // Reset the size
    }

    /**
     * Returns an iterator to traverse the linked list.
     *
     * @return an {@code Iterator} for the linked list
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            /** The current node being iterated */
            private Node<E> current = head;

            /**
             * Checks if there are more elements to traverse.
             *
             * @return true if there are more elements, false otherwise
             */
            @Override
            public boolean hasNext() {
                return current != null;
            }

            /**
             * Returns the next element in the list.
             *
             * @return the next element
             */
            @Override
            public E next() {
                E data = current.data; // Get the data of the current node
                current = current.next; // Move to the next node
                return data;
            }
        };
    }
}
