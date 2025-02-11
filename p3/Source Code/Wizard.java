import java.util.ArrayList;

/**
 * The Wizard class is responsible for choosing the best "help" type
 * (among a list of help offers) by evaluating shortest-path distances
 * with those help types enabled. It then modifies the Land to make
 * nodes of that help type walkable (nodeType = "0").
 */
public class Wizard {

    /** A PathFinding instance for computing distances (via Dijkstra). */
    PathFinding pathFinding;

    /** A Logger for reporting decisions and progress. */
    Logger logger;

    /** The Land object (2D grid of GraphNodes) in which the wizard operates. */
    Land land;

    /**
     * Constructs a Wizard with the specified pathfinding system, logger, and land.
     *
     * @param pathFinding a PathFinding object for distance calculations
     * @param logger      a Logger to record wizard actions
     * @param land        the Land object containing GraphNodes and type mappings
     */
    public Wizard(PathFinding pathFinding, Logger logger, Land land) {
        this.pathFinding = pathFinding;
        this.logger = logger;
        this.land = land;
    }

    /**
     * Chooses the best help type from a list of offers by comparing Dijkstra distances
     * for each offer. The chosen help type's nodes are converted to type "0" (walkable).
     *
     * @param helpOffers   a list of possible help offers (node types to be unlocked)
     * @param currentNode  the current GraphNode from which distances are measured
     * @param objectiveNode the target GraphNode to reach
     * @param radius       visibility or search radius used in pathfinding
     */
    public void wizardHelp(ArrayList<String> helpOffers, GraphNode currentNode, GraphNode objectiveNode, int radius) {

        // Use a custom BinaryHeap instead of a standard PriorityQueue
        BinaryHeap<String, Float> distanceWithHelp = new BinaryHeap<>();

        // For each help offer, run Dijkstra to find the distance from currentNode to objectiveNode
        // (with that help's node type considered walkable), then insert into the min-heap.
        for (String help : helpOffers) {
            float distance = pathFinding.dijkstra(land, currentNode, objectiveNode, help, radius);
            distanceWithHelp.insert(help, distance);
        }

        // Extract the entry with the smallest distance => best help type
        BinaryHeap.Entry<String, Float> bestEntry = distanceWithHelp.extractMin();
        String bestHelp = bestEntry.getKey();

        // Log the choice
        logger.log("Number " + bestHelp + " is chosen!");

        // Convert all nodes of this best help type to nodeType "0" (walkable)
        MyHashMap<String, ArrayList<GraphNode>> nodesByType = land.getNodesByType();
        ArrayList<GraphNode> bestTypeNodes = nodesByType.get(bestHelp);
        if (bestTypeNodes != null) {
            for (GraphNode node : bestTypeNodes) {
                node.setNodeType("0");
            }
        }

        // Clear the offers since we've consumed them
        helpOffers.clear();
    }
}
