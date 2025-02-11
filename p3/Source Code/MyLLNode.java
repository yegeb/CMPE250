/**
 * MyLLNode represents a single node in a singly linked list.
 * Each node contains an element and a reference to the next node in the list.
 *
 * @param <E> The type of element stored in the node.
 */
public class MyLLNode<E> {

    private E element; // The data element stored in this node
    private MyLLNode<E> next; // Reference to the next node in the linked list

    /**
     * Constructs a new node with the specified element and next node reference.
     *
     * @param element The element to store in this node.
     * @param next The next node in the linked list (null if this is the last node).
     */
    public MyLLNode(E element, MyLLNode<E> next) {
        this.element = element;
        this.next = next;
    }

    /**
     * Gets the next node in the linked list.
     *
     * @return The next node, or null if this is the last node.
     */
    public MyLLNode<E> getNext() {
        return next;
    }

    /**
     * Sets the next node in the linked list.
     *
     * @param next The node to set as the next node.
     */
    public void setNext(MyLLNode<E> next) {
        this.next = next;
    }

    /**
     * Gets the element stored in this node.
     *
     * @return The element stored in this node.
     */
    public E getElement() {
        return element;
    }

    /**
     * Returns a string representation of the node, showing its element.
     *
     * @return A string describing the element in this node.
     */
    @Override
    public String toString() {
        return "Element is: " + element;
    }
}
