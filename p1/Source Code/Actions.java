import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Actions class provides methods to manage parking lots and trucks, including creating and deleting parking lots,
 * adding trucks to waiting sections, preparing trucks in ready sections, loading trucks, and counting trucks
 * in parking lots. The class interacts with AVL Trees and HashMaps to efficiently manage capacity constraints.
 */
public class Actions {

    // AVL Trees to manage parking lot capacities and track availability for various operations.
    MyAVLTree parkingLotCapacities = new MyAVLTree();
    MyAVLTree addTruckAvailable = new MyAVLTree();
    MyAVLTree readyAvailable = new MyAVLTree();
    MyAVLTree loadAvailable = new MyAVLTree();

    // HashMap to store parking lots based on their capacity constraints.
    MyHashMap<ParkingLot> parkingLots = new MyHashMap<>();

    // BufferedWriter to write output to a file.
    BufferedWriter writer;

    /**
     * Constructs an Actions object and initializes a BufferedWriter for outputting results to a specified file.
     *
     * @param outputFile The name of the file where results will be written.
     * @throws IOException If an I/O error occurs while opening or creating the file.
     */
    public Actions(String outputFile) throws IOException {
        writer = new BufferedWriter(new FileWriter(outputFile));
    }


    /**
     * Creates a new parking lot with the specified capacity constraint and truck limit.
     * @param capacity_constraint The capacity constraint of the parking lot.
     * @param truck_limit The truck limit for the parking lot.
     */
    public void create_parking_lot(int capacity_constraint, int truck_limit) {
        ParkingLot parkingLot = new ParkingLot(capacity_constraint, truck_limit);
        // Add parking lot if it doesn't already exist.
        if (!parkingLots.containsKey(capacity_constraint)) {
            parkingLots.put(capacity_constraint, parkingLot);
            parkingLotCapacities.insert(capacity_constraint);
            addTruckAvailable.insert(capacity_constraint);
        }
    }

    /**
     * Deletes an existing parking lot by its capacity constraint.
     * @param capacity_constraint The capacity constraint of the parking lot to delete.
     */
    public void delete_parking_lot(int capacity_constraint) {
        if (parkingLots.containsKey(capacity_constraint)) {
            parkingLots.remove(capacity_constraint);
            parkingLotCapacities.delete(capacity_constraint);
            addTruckAvailable.delete(capacity_constraint);
            readyAvailable.delete(capacity_constraint);
            loadAvailable.delete(capacity_constraint);
        }
    }

    /**
     * Adds a truck to a parking lot based on its capacity. If an exact match for the capacity
     * is available in the list of parking lots that can add trucks, the truck is added to that
     * parking lot. If an exact match is not available, the method tries to find the nearest
     * smaller capacity parking lot with availability.
     *
     * @param truck_id The ID of the truck to be added.
     * @param capacity The capacity constraint for the truck.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    public void add_truck(int truck_id, int capacity) throws IOException {
        // Create a new truck object with the provided ID and capacity.
        Truck truck = new Truck(truck_id, capacity);

        // Check if there's an exact matching parking lot for the truck capacity in the AVL tree.
        if (addTruckAvailable.searchNode(addTruckAvailable.getRoot(), capacity)) {
            // Retrieve the parking lot with the exact capacity.
            ParkingLot parkingLot = parkingLots.get(capacity);

            // Add the truck to the waiting section of the parking lot.
            parkingLot.getWaiting_section().enqueue(truck);

            // If the parking lot is now full after adding the truck, remove it from the list of
            // available parking lots for adding more trucks.
            if (parkingLot.isFull()) {
                addTruckAvailable.delete(capacity);
            }

            // Insert this capacity into the readyAvailable tree, indicating there is a truck
            // ready to move from waiting to ready section for this capacity.
            readyAvailable.insert(capacity);

            // Write the capacity to the output file as feedback for the successful addition.
            writer.write(String.valueOf(capacity));
            writer.newLine();
            return; // Exit the method after successfully adding the truck.
        }

        // If an exact capacity match isn't available, find the predecessor capacity parking lot
        // that still has availability for adding trucks.
        MyTreeNode nextNode = addTruckAvailable.findGreatestSmallerThan(addTruckAvailable.getRoot(), capacity);

        if (nextNode != null) {
            // Get the element (capacity) of the next available smaller parking lot.
            int nextCapacity = nextNode.element;
            ParkingLot parkingLot = parkingLots.get(nextCapacity);

            // Add the truck to the waiting section of this smaller capacity parking lot.
            parkingLot.getWaiting_section().enqueue(truck);

            // If this parking lot becomes full after adding the truck, remove it from the list of
            // parking lots available for adding trucks.
            if (parkingLot.isFull()) {
                addTruckAvailable.delete(nextCapacity);
            }

            // Insert this capacity into the readyAvailable tree as there’s a truck ready for this capacity.
            readyAvailable.insert(nextCapacity);

            // Write the capacity of the parking lot used to the output file.
            writer.write(String.valueOf(nextCapacity));
            writer.newLine();
            return; // Exit the method after successfully adding the truck.
        }

        // If no suitable parking lot is found, write "-1" to the output file to indicate failure.
        writer.write("-1");
        writer.newLine();
    }



    /**
     * Moves a truck from the waiting section to the ready section in a parking lot
     * with the specified capacity. If no exact match for the capacity is found,
     * it tries to find the nearest larger capacity that has availability.
     *
     * @param capacity The capacity constraint for preparing the truck.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    public void ready(int capacity) throws IOException {
        // Check if there's an exact matching parking lot with trucks in the waiting section.
        if (readyAvailable.searchNode(readyAvailable.getRoot(), capacity)) {
            // Retrieve the parking lot with the exact capacity.
            ParkingLot parkingLot = parkingLots.get(capacity);

            // Dequeue (remove) the truck from the waiting section to move it to the ready section.
            Truck truck = parkingLot.getWaiting_section().dequeue();

            // If the waiting section becomes empty after removing the truck,
            // delete this capacity from the readyAvailable tree to indicate it has no more waiting trucks.
            if (parkingLot.getWaiting_section().isEmpty()) {
                readyAvailable.delete(capacity);
            }

            // Add the truck to the ready section of this parking lot.
            parkingLot.getReady_section().enqueue(truck);

            // Insert this capacity into the loadAvailable tree, indicating there is now a truck ready for loading.
            loadAvailable.insert(capacity);

            // Write the truck ID and capacity to the output file to confirm the move to ready section.
            writer.write(truck.getTruck_id() + " " + capacity);
            writer.newLine();
            return; // Exit the method as the truck has been successfully moved to ready.
        }

        // If an exact capacity match is not available, find the successor capacity with availability.
        MyTreeNode nextNode = readyAvailable.findSmallestGreaterThan(readyAvailable.getRoot(), capacity);

        // Check if a larger capacity parking lot with availability was found.
        if (nextNode != null) {
            int nextCapacity = nextNode.element;

            // Retrieve the parking lot with the nearest larger capacity.
            ParkingLot parkingLot = parkingLots.get(nextCapacity);

            // Dequeue a truck from the waiting section to move it to the ready section.
            Truck truck = parkingLot.getWaiting_section().dequeue();

            // If the waiting section becomes empty after removing the truck,
            // delete this capacity from the readyAvailable tree to indicate it has no more waiting trucks.
            if (parkingLot.getWaiting_section().isEmpty()) {
                readyAvailable.delete(nextCapacity);
            }

            // Move the truck to the ready section of this parking lot.
            parkingLot.getReady_section().enqueue(truck);

            // Insert this capacity into the loadAvailable tree, indicating a truck is ready for loading.
            loadAvailable.insert(nextCapacity);

            // Write the truck ID and new capacity to the output file.
            writer.write(truck.getTruck_id() + " " + nextCapacity);
            writer.newLine();
            return; // Exit the method as the truck has been successfully moved to ready.
        }

        // If no suitable parking lot is found, write "-1" to the output file to indicate failure.
        writer.write("-1");
        writer.newLine();
    }


    /**
     * Loads trucks from the ready section of parking lots based on the specified capacity constraint and load amount.
     * Trucks are loaded until the required load amount is met or no more suitable trucks are available.
     *
     * @param capacityConstraint The starting capacity constraint for loading trucks.
     * @param loadAmount The amount to be loaded across trucks.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    public void load(int capacityConstraint, int loadAmount) throws IOException {
        ArrayList<int[]> outputs = new ArrayList<>(); // Store truck load operations for output.
        int currentCapacity = capacityConstraint; // Initialize the current capacity constraint for loading.

        // Continue loading until loadAmount is depleted or no suitable trucks are available.
        while (loadAmount > 0) {
            // Check if there's a matching parking lot with trucks in the ready section.
            if (loadAvailable.searchNode(loadAvailable.getRoot(), currentCapacity)) {
                ParkingLot parkingLot = parkingLots.get(currentCapacity);

                // If the parking lot is initially full, now there'll be new slots to make it available for adding new trucks.
                if (parkingLot.isFull()) {
                    addTruckAvailable.insert(currentCapacity);
                }

                // Dequeue (remove) a truck from the ready section to load it.
                Truck truck = parkingLot.getReady_section().dequeue();

                // If the ready section is now empty, remove this capacity from loadAvailable.
                if (parkingLot.getReady_section().isEmpty()) {
                    loadAvailable.delete(currentCapacity);
                }

                // Check if the remaining load amount is less than the parking lot's capacity constraint.
                if (loadAmount < parkingLot.getCapacity_constraint()) {
                    // Load the truck with the remaining load amount.
                    truck.setLoad(truck.getLoad() + loadAmount);
                    loadAmount = 0; // All load amount has been allocated.

                    // Relocate the truck after loading and store the result for output.
                    int lastCapacityConstraint = relocate_truck(truck, truck.getCapacity() - truck.getLoad());
                    outputs.add(new int[]{truck.getTruck_id(), lastCapacityConstraint});
                } else {
                    // Load the truck up to the parking lot's capacity constraint.
                    truck.setLoad(truck.getLoad() + parkingLot.getCapacity_constraint());
                    loadAmount -= parkingLot.getCapacity_constraint(); // Deduct from total load amount.

                    // If truck is fully loaded, reset its load.
                    if (truck.getCapacity() == truck.getLoad()) {
                        truck.setLoad(0);
                    }

                    // Relocate the truck after loading and store the result for output.
                    int lastCapacityConstraint = relocate_truck(truck, truck.getCapacity() - truck.getLoad());
                    outputs.add(new int[]{truck.getTruck_id(), lastCapacityConstraint});
                }
            } else {
                // If there's no truck in the ready section of that parking lot,
                // find the next larger capacity with ready trucks.
                MyTreeNode nextNode = loadAvailable.findSmallestGreaterThan(loadAvailable.getRoot(), currentCapacity);
                if (nextNode != null) {
                    currentCapacity = nextNode.element; // Update to the next available larger capacity.
                } else {
                    break; // Exit if no more trucks are available for loading.
                }
            }
        }

        // Write the output results based on loaded trucks.
        if (!outputs.isEmpty()) {
            for (int i = 0; i < outputs.size(); i++) {
                int[] output = outputs.get(i);
                writer.write(output[0] + " " + output[1]); // Write truck ID and final capacity.
                if (i != outputs.size() - 1) {
                    writer.write(" - ");
                }
            }
        } else {
            writer.write("-1"); // Indicate no trucks were available for loading.
        }
        writer.newLine();
    }


    /**
     * Relocates a truck to a parking lot based on its remaining capacity.
     * If an exact match for the parking lot capacity is found, the truck is added to that lot.
     * If no exact match is available, the method tries to find the nearest smaller capacity lot.
     *
     * @param truck The truck to be relocated.
     * @param capacity The remaining capacity the truck can accommodate after loading.
     * @return The capacity constraint of the parking lot the truck was relocated to, or -1 if no suitable lot was found.
     * @throws IOException If an I/O error occurs.
     */
    public int relocate_truck(Truck truck, int capacity) throws IOException {
        // Check if there's an exact match for a parking lot with the required capacity in the AVL tree.
        if (addTruckAvailable.searchNode(addTruckAvailable.getRoot(), capacity)) {
            // Retrieve the parking lot with the exact matching capacity.
            ParkingLot parkingLot = parkingLots.get(capacity);

            // Enqueue the truck in the waiting section of the matching parking lot.
            parkingLot.getWaiting_section().enqueue(truck);

            // If the parking lot is now full after adding the truck, remove it from addTruckAvailable.
            if (parkingLot.isFull()) {
                addTruckAvailable.delete(capacity);
            }

            // Insert this capacity into the readyAvailable tree, indicating that a truck is ready in this lot.
            readyAvailable.insert(capacity);

            // Return the capacity constraint of the parking lot where the truck was relocated.
            return capacity;
        }

        // If an exact match is unavailable, look for the nearest smaller capacity parking lot.
        MyTreeNode nextNode = addTruckAvailable.findGreatestSmallerThan(addTruckAvailable.getRoot(), capacity);

        // Check if a suitable smaller capacity parking lot was found.
        if (nextNode != null) {
            int nextCapacity = nextNode.element;

            // Retrieve the parking lot with the nearest smaller capacity.
            ParkingLot parkingLot = parkingLots.get(nextCapacity);

            // Enqueue the truck in the waiting section of this parking lot.
            parkingLot.getWaiting_section().enqueue(truck);

            // If this parking lot becomes full after adding the truck, remove it from addTruckAvailable.
            if (parkingLot.isFull()) {
                addTruckAvailable.delete(nextCapacity);
            }

            // Insert this capacity into the readyAvailable tree, as there is now a truck ready in this lot.
            readyAvailable.insert(nextCapacity);

            // Return the capacity constraint of the parking lot where the truck was relocated.
            return nextCapacity;
        }

        // Return -1 to indicate that no suitable parking lot was found for the truck.
        return -1;
    }


    /**
     * Counts the total number of trucks in the ready and waiting sections of parking lots
     * that have a capacity greater than the specified capacity.
     *
     * @param capacity The capacity constraint to start counting from.
     * @throws IOException If an I/O error occurs while writing to the output file.
     */
    public void count(int capacity) throws IOException {
        // Find the smallest capacity greater than the given capacity in the parking lot AVL tree.
        MyTreeNode nextNode = parkingLotCapacities.findSmallestGreaterThan(parkingLotCapacities.getRoot(), capacity);
        int count = 0; // Initialize the count to zero.

        // Iterate over all parking lots with capacities greater than the specified capacity.
        while (nextNode != null) {
            int nextCapacity = nextNode.element; // Get the capacity constraint of the current node.

            // Retrieve the parking lot with the specified capacity.
            ParkingLot parkingLot = parkingLots.get(nextCapacity);

            // Add the number of trucks in both ready and waiting sections of this parking lot to the count.
            count += parkingLot.getReady_section().size() + parkingLot.getWaiting_section().size();

            // Move to the next parking lot with a capacity greater than the current capacity.
            nextNode = parkingLotCapacities.findSmallestGreaterThan(parkingLotCapacities.getRoot(), nextCapacity);
        }

        // Write the total count of trucks to the output file.
        writer.write(String.valueOf(count));
        writer.newLine(); // Move to the next line in the output file.
    }


    /**
     * Reads and executes commands from a file, processing actions based on parsed input.
     * Each line in the file should contain a command with appropriate parameters,
     * which this method interprets and executes.
     *
     * @param fileName The name of the file containing commands.
     * @throws IOException If an I/O error occurs during file operations.
     */
    public void fileReader(String fileName) throws IOException {
        // Create a File object from the file name and open a Scanner to read it.
        File file = new File(fileName);
        Scanner inputFile = new Scanner(file);
         // Instantiate an Actions object to execute commands.

        // Read the file line by line until there are no more lines.
        while (inputFile.hasNextLine()) {
            String line = inputFile.nextLine(); // Read the next line from the file.
            String[] words = line.split(" "); // Split the line into words to identify the command and parameters.

            try {
                // Determine the command based on the first word in the line and execute the corresponding action.
                switch (words[0]) {
                    case "create_parking_lot":
                        // Parse capacity and truck limit parameters and create a parking lot.
                        int capacity_constraint = Integer.parseInt(words[1]);
                        int truck_limit = Integer.parseInt(words[2]);
                        create_parking_lot(capacity_constraint, truck_limit);
                        break;

                    case "delete_parking_lot":
                        // Parse capacity parameter and delete the specified parking lot.
                        capacity_constraint = Integer.parseInt(words[1]);
                        delete_parking_lot(capacity_constraint);
                        break;

                    case "add_truck":
                        // Parse truck ID and capacity parameters and add a truck to a parking lot.
                        int truck_id = Integer.parseInt(words[1]);
                        int capacity = Integer.parseInt(words[2]);
                        add_truck(truck_id, capacity);
                        break;

                    case "ready":
                        // Parse capacity parameter and prepare a truck in the specified parking lot.
                        capacity = Integer.parseInt(words[1]);
                        ready(capacity);
                        break;

                    case "load":
                        // Parse capacity and load amount parameters and load trucks as needed.
                        capacity = Integer.parseInt(words[1]);
                        int load_amount = Integer.parseInt(words[2]);
                        load(capacity, load_amount);
                        break;

                    case "count":
                        // Parse capacity parameter and count trucks in all parking lots with a greater capacity.
                        capacity = Integer.parseInt(words[1]);
                        count(capacity);
                        break;

                    default:
                        System.out.println("Unknown command: " + words[0]);
                        break;
                }
            } catch (NumberFormatException e) {
                // Handle any errors that occur when parsing integers from the input line.
                System.out.println("Error parsing integer from line: " + line);
            }
        }

        // Close the writer to ensure all outputs are saved and release resources.
        writer.close();
        // Close the input file to release resources.
        inputFile.close();
    }

}
