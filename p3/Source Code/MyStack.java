/**
 * A simple stack implementation using a custom MyLinkedList as the underlying data structure.
 * Follows Last-In-First-Out (LIFO) semantics.
 *
 * @param <E> the type of elements stored in the stack
 */
public class MyStack<E> {

    /** A custom singly-linked list used to store the stack elements. */
    private MyLinkedList<E> list;

    /** Tracks the current number of elements in the stack. */
    private int size;

    /**
     * Constructs an empty MyStack.
     */
    public MyStack() {
        list = new MyLinkedList<>();
        size = 0;
    }

    /**
     * Pushes an element onto the top of the stack.
     *
     * @param element the element to be pushed
     */
    public void push(E element) {
        // Add at the front (head) so pop() becomes O(1)
        list.addFirst(element);
        size++;
    }

    /**
     * Removes and returns the element at the top of the stack.
     *
     * @return the top element, or null if the stack is empty
     */
    public E pop() {
        if (isEmpty()) {
            return null;
        }
        E poppedElement = list.removeFirst();  // remove from the head
        size--;
        return poppedElement;
    }

    /**
     * Returns (but does not remove) the element at the top of the stack.
     *
     * @return the top element, or null if the stack is empty
     */
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return list.first();  // get the head element without removing
    }

    /**
     * Checks whether the stack is empty.
     *
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return the number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        list.clear();
        size = 0;
    }

    @Override
    public String toString() {
        // You could iterate through list to build a string from top to bottom
        StringBuilder sb = new StringBuilder("MyStack: [");
        // If MyLinkedList has an iterator, you can iterate from head to tail:
        // The top of the stack is the head (first element).
        boolean firstElement = true;
        for (E element : list) {
            if (!firstElement) {
                sb.append(", ");
            }
            sb.append(element);
            firstElement = false;
        }
        sb.append("] (top at first element)");
        return sb.toString();
    }
}
