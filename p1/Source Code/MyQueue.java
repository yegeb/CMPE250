
public class MyQueue<E> {

    private int capacity;
    private MyLinkedList<E> myList;
    private int size;

    public MyQueue(){
        this(10); // Set a default capacity
    }

    public MyQueue(int capacity){
        myList = new MyLinkedList<>();
        this.capacity = capacity;
    }

    public boolean isEmpty(){
        return myList.isEmpty();
    }

    public boolean isFull() {
        return size >= capacity;
    }

    public boolean enqueue(E element) {
        if (size < capacity) {  // Ensures size does not exceed capacity
            myList.addLast(element);
            size++;
            return true;
        }
        return false;  // Queue is full, so return false
    }

    public E dequeue(){
        if (isEmpty())
            return null;
        E removedElement = myList.removeFirst();
        if (removedElement != null) {
            size--; // Decrement size only if an element was removed
        }
        return removedElement;
    }

    public E peek() {
        if (isEmpty()) {
            return null; // Return null if the queue is empty
        }
        return myList.first(); // Get the first element without removing it
    }

    public int size(){
        return size;
    }

    public void deleteAll() {
        myList.clear(); // Assuming clear() method is implemented
        size = 0; // Reset the size to 0
    }

    public int getCapacity(){
        return capacity;
    }
}

