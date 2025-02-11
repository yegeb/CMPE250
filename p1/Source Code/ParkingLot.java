/**
 * ParkingLot represents a parking area with separate sections for waiting and ready trucks.
 * The parking lot has a capacity constraint and a truck limit, and uses two queues to
 * manage trucks waiting to be loaded and trucks that are ready.
 */
public class ParkingLot {

    private int capacity_constraint; // The capacity constraint for loading operations
    private int truck_limit; // Maximum number of trucks allowed in the parking lot

    private MyQueue<Truck> waiting_section; // Queue for trucks waiting to be moved to the ready section
    private MyQueue<Truck> ready_section; // Queue for trucks that are ready for loading
    private int size; // Current count of trucks in the parking lot (waiting + ready)

    /**
     * Default constructor for ParkingLot.
     * Creates an empty ParkingLot with no specific constraints.
     */
    public ParkingLot() {}

    /**
     * Constructs a ParkingLot with the specified capacity constraint and truck limit.
     * Initializes the waiting and ready sections as queues with the specified truck limit.
     *
     * @param capacity_constraint The loading capacity constraint for this parking lot.
     * @param truck_limit The maximum number of trucks that can be in the parking lot.
     */
    public ParkingLot(int capacity_constraint, int truck_limit) {
        this.capacity_constraint = capacity_constraint;
        this.truck_limit = truck_limit;
        waiting_section = new MyQueue<>(truck_limit); // Initialize waiting section queue
        ready_section = new MyQueue<>(truck_limit); // Initialize ready section queue
    }

    /**
     * Gets the capacity constraint of the parking lot.
     *
     * @return The capacity constraint of the parking lot.
     */
    public int getCapacity_constraint() {
        return capacity_constraint;
    }

    /**
     * Gets the queue representing the waiting section of the parking lot.
     *
     * @return The waiting section queue.
     */
    public MyQueue<Truck> getWaiting_section() {
        return waiting_section;
    }

    /**
     * Gets the queue representing the ready section of the parking lot.
     *
     * @return The ready section queue.
     */
    public MyQueue<Truck> getReady_section() {
        return ready_section;
    }

    /**
     * Calculates the total number of trucks in the parking lot.
     *
     * @return The number of trucks in both the waiting and ready sections.
     */
    public int size() {
        return waiting_section.size() + ready_section.size();
    }

    /**
     * Checks if the parking lot is full.
     *
     * @return True if the number of trucks equals the truck limit, false otherwise.
     */
    public boolean isFull() {
        return size() == truck_limit;
    }
}
