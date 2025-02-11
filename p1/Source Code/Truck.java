/**
 * Truck represents a truck with a unique ID, capacity, and current load.
 * The class provides methods to retrieve and modify these properties.
 */
public class Truck {

    private int truck_id; // Unique identifier for the truck
    private int capacity; // Maximum load capacity of the truck
    private int load; // Current load on the truck

    /**
     * Constructs a new Truck with the specified ID and capacity.
     * Initializes the load to zero.
     *
     * @param truck_id The unique identifier for the truck.
     * @param capacity The maximum load capacity of the truck.
     */
    public Truck(int truck_id, int capacity) {
        this.truck_id = truck_id;
        this.capacity = capacity;
        this.load = 0; // Initialize load to 0 as the truck is initially empty
    }

    /**
     * Gets the unique ID of the truck.
     *
     * @return The truck's ID.
     */
    public int getTruck_id() {
        return truck_id;
    }

    /**
     * Gets the maximum capacity of the truck.
     *
     * @return The capacity of the truck.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the current load on the truck.
     *
     * @return The current load of the truck.
     */
    public int getLoad() {
        return load;
    }

    /**
     * Sets the current load of the truck.
     *
     * @param load The new load to set on the truck.
     */
    public void setLoad(int load) {
        this.load = load;
    }
}
