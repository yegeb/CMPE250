
/**
 * MyLinkedList is a custom implementation of a singly linked list.
 * It supports basic operations such as adding an element to the end of the list,
 * removing the first element, and retrieving the first element.
 *
 * @param <E> The type of elements stored in the linked list.
 */
public class MyLinkedList<E> {

    private MyLLNode<E> head; // Head node of the linked list
    private MyLLNode<E> tail; // Tail node of the linked list
    private int size; // Number of elements in the linked list

    /**
     * Constructs an empty linked list.
     */
    public MyLinkedList() {
        // Initialize an empty linked list
    }

    /**
     * Adds an element to the end of the linked list.
     *
     * @param element The element to add.
     */
    public void addLast(E element) {
        // Create a new node with the given element
        MyLLNode<E> newNode = new MyLLNode<>(element, null);

        // If the list is empty, set head and tail to the new node
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            // Otherwise, link the new node as the next of the current tail and update the tail
            tail.setNext(newNode);
            tail = newNode;
        }

        size++; // Increase the size of the list
    }

    /**
     * Removes and returns the first element from the linked list.
     *
     * @return The element at the head of the list, or null if the list is empty.
     */
    public E removeFirst() {
        // If the list is empty, return null
        if (isEmpty()) return null;

        // Store the element of the current head
        E previousFirst = head.getElement();

        // Move the head to the next node
        head = head.getNext();
        size--; // Decrease the size of the list

        // If the list is now empty, set the tail to null as well
        if (isEmpty()) {
            tail = null;
        }

        return previousFirst; // Return the removed element
    }

    /**
     * Returns the first element in the linked list without removing it.
     *
     * @return The element at the head of the list, or null if the list is empty.
     */
    public E first() {
        // Return the head element or null if the list is empty
        return isEmpty() ? null : head.getElement();
    }

    /**
     * Checks if the linked list is empty.
     *
     * @return True if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        // Return true if the size is zero
        return size == 0;
    }

    /**
     * Clears the linked list by removing all elements.
     */
    public void clear() {
        // Remove references to all nodes to clear the list
        head = null;
        tail = null;
        size = 0; // Reset size to 0
    }

    /**
     * Returns a string representation of the linked list.
     *
     * @return A string representation of the list in the format "(element1 -> element2 -> ...)".
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("("); // Start with an opening parenthesis
        MyLLNode<E> currentNode = head; // Start from the head node

        // Traverse the list to build the string
        while (currentNode != null) {
            string.append(currentNode.getElement()); // Append the element of the current node
            currentNode = currentNode.getNext(); // Move to the next node
            if (currentNode != null) {
                string.append(" -> "); // Append separator if there's another element
            }
        }
        string.append(")"); // End with a closing parenthesis
        return string.toString();
    }
}


