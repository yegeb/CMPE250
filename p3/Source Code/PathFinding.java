/**
 * This class performs pathfinding (Dijkstra) on a grid (Land) of GraphNodes,
 * reconstructing the path, and facilitating movement from one node to another.
 */
public class PathFinding {

    /**
     * A queue representing the current path of GraphNodes from a start to end node.
     * It is reconstructed each time Dijkstra is completed.
     */
    private MyQueue<GraphNode> path;

    /**
     * Constructs a PathFinding object. Initializes no data since path is built dynamically.
     */
    public PathFinding() {
    }

    /**
     * Performs Dijkstra's Algorithm from startNode to endNode, considering impassable nodes.
     * Uses a custom BinaryHeap as a priority queue and a custom MyHashMap for neighbor iteration.
     *
     * @param land       The Land object containing the 2D grid of GraphNodes.
     * @param startNode  The node from which to start the pathfinding.
     * @param endNode    The target node to reach.
     * @param help       A nodeType that should be considered passable (in addition to "0").
     * @param radius     Visibility radius to mark nodes as seen.
     * @return           The distance to endNode (Float.MAX_VALUE if unreachable).
     */
    public Float dijkstra(Land land, GraphNode startNode, GraphNode endNode, String help, int radius) {
        // Create a 2D array (previousNode) to store the predecessor of each node on the path
        GraphNode[][] previousNode = new GraphNode[land.getGridSize()[0]][land.getGridSize()[1]];
        // Mark the startNode as "seen" within the given radius
        seeNode(startNode, land, radius);

        // Initialize a BinaryHeap (min-heap) for selecting the next closest node
        BinaryHeap<GraphNode, Float> priorityQueue = new BinaryHeap<>();

        // Store how we reached startNode. Null indicates no predecessor for the start
        previousNode[startNode.getNode_X()][startNode.getNode_Y()] = null; // or startNode, but typically null

        // Retrieve the grid dimensions and the actual 2D GraphNode array
        GraphNode[][] grid = land.getGrid();
        int[] size = land.getGridSize();
        int width = size[0];
        int height = size[1];

        // Initialize all GraphNodes' distance to Float.MAX_VALUE before Dijkstra begins
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GraphNode node = grid[x][y];
                if (node == null) continue;

                node.distance = Float.MAX_VALUE;
            }
        }
        // The start node has distance zero to itself
        startNode.distance = 0; //NOT
        // Insert the startNode into the min-heap with distance 0
        priorityQueue.insert(startNode, startNode.distance);

        // Keep track of visited nodes in a 2D boolean array
        boolean[][] visited = new boolean[land.getGridSize()[0]][land.getGridSize()[1]];

        // Dijkstra's main loop: keep extracting the node with the smallest distance
        while (!priorityQueue.isEmpty()) {
            BinaryHeap.Entry<GraphNode, Float> entry = priorityQueue.extractMin();
            GraphNode current = entry.getKey();

            // If the endNode is extracted from the heap, we've found the shortest path
            if (current == endNode) {
                // Reconstruct the path from endNode back to startNode
                reconstructPath(previousNode, startNode, endNode);
                // Return the final distance stored in endNode
                return endNode.distance;
            }

            // If current node has already been visited, skip processing
            if (visited[current.getNode_X()][current.getNode_Y()]) {
                continue;
            }
            visited[current.getNode_X()][current.getNode_Y()] = true;

            // Skip if current node is impassable (not "0" or not the special 'help' type)
            if (!current.getNodeType().equals("0") && !current.getNodeType().equals(help)) {
                if (current.getNodeType().equals("1") || current.isSeen()) {
                    // It's impassable, skip
                    continue;
                }
            }

            // Relax edges to each neighbor (perform the Dijkstra relaxation step)
            for (MyHashMap.Entry<GraphNode, Float> neighborEntry : current.getOutgoingNeighbors().entrySet()) {
                GraphNode neighbor = neighborEntry.getKey();
                float edgeWeight = neighborEntry.getValue();

                // Also skip impassable neighbors
                if (!neighbor.getNodeType().equals("0") && !neighbor.getNodeType().equals(help)) {
                    if (neighbor.getNodeType().equals("1") || neighbor.isSeen()) {
                        continue;
                    }
                }

                // Calculate a new distance (current.distance + edge cost)
                float newDist = current.distance + edgeWeight;
                // If this new distance is better than neighbor's current distance, update
                if (newDist < neighbor.distance) {
                    neighbor.distance = newDist;
                    // Insert neighbor back into the heap with the updated smaller distance
                    priorityQueue.insert(neighbor, newDist);
                    // Record how we reached 'neighbor' from 'current'
                    previousNode[neighbor.getNode_X()][neighbor.getNode_Y()] = current;
                }
            }
        }

        // If the queue is empty and we haven't returned, endNode was unreachable
        reconstructPath(previousNode, startNode, endNode); // This might produce an empty or partial path
        return endNode.distance;
    }


    /**
     * Moves from the current node along the path queue until reaching the objective node,
     * or until the path is blocked by an impassable node. Uses the 'radius' to see which
     * path nodes might be blocked (type != 0).
     *
     * @param startNode      The current node to start moving from.
     * @param land           The Land object for visibility checks.
     * @param objectiveNode  The target node to reach.
     * @param radius         Visibility radius for marking nodes as seen.
     * @param logger         Logger for recording events.
     * @return               The final node reached (could be objective or earlier if blocked).
     */
    public GraphNode move(GraphNode startNode, Land land, GraphNode objectiveNode, int radius, Logger logger) {
        // Begin movement from the specified startNode
        GraphNode currentNode = startNode;

        // While there is more than one node left in the path queue (meaning we haven't yet reached the end)
        while (path.size() > 1) {
            // Reveal nodes within 'radius' from the currentNode in the grid
            seeNode(currentNode, land, radius);

            // Check each node in the path, but only those within 'radius' of currentNode
            for (GraphNode node : path) {
                // If the node's type is "0", it's walkable; skip checks
                if(node.getNodeType().equals("0")){
                    continue;
                }
                // If within radius, see if it's blocked (seen & type != 0)
                if (isWithinRadius(currentNode, node, radius)) {
                    // If this node is seen and not "0", the path is blocked
                    if (node.isSeen() && !node.getNodeType().equals("0")) {
                        logger.log("Path is impassable!");
                        // Return the last successfully reached node
                        return currentNode;
                    }
                }
            }

            // Move to the next node in the path queue
            path.dequeue();           // Discard the currentNode from the queue
            currentNode = path.peek(); // The new front of the queue becomes currentNode
            logger.log("Moving to " + currentNode.getNode_X() + "-" + currentNode.getNode_Y());
        }

        // If the loop ends, we've either reached the objectiveNode or the path is exhausted
        return currentNode;
    }


    /**
     * Marks nodes within 'radius' of 'center' as seen. Uses a square bounding box
     * then checks actual distance to ensure a circle-like region of visibility.
     *
     * @param center   The node from which visibility is calculated.
     * @param land     The Land object containing the grid.
     * @param radius   The radius within which nodes are seen.
     */
    public void seeNode(GraphNode center, Land land, int radius) {
        // Mark the center node as seen
        center.setSeen(true);

        // Pre-calculate the square of the radius for circle-based distance checks
        int rSquared = radius * radius;

        // Retrieve the grid and its boundaries
        GraphNode[][] grid = land.getGrid();
        int[] landBoundaries = land.getGridSize();
        int width = landBoundaries[0];
        int height = landBoundaries[1];

        // Get the center node's coordinates
        int center_X = center.getNode_X();
        int center_Y = center.getNode_Y();

        // Reveal nodes within the specified radius using a square bounding box
        for (int node_X = center_X - radius; node_X <= center_X + radius; node_X++) {
            // Skip if outside horizontal bounds
            if (node_X < 0 || node_X >= width) continue;

            for (int node_Y = center_Y - radius; node_Y <= center_Y + radius; node_Y++) {
                // Skip if outside vertical bounds
                if (node_Y < 0 || node_Y >= height) continue;

                // Calculate squared distance to (node_X, node_Y)
                int dx = node_X - center_X;
                int dy = node_Y - center_Y;
                int distanceSquared = dx * dx + dy * dy;

                // Only reveal nodes whose squared distance <= rSquared (i.e., inside the circle)
                if (distanceSquared <= rSquared) {
                    GraphNode node = grid[node_X][node_Y];
                    if (node != null) {
                        node.setSeen(true);
                    }
                }
            }
        }
    }


    /**
     * Checks if 'candidate' is within the specified 'radius' of 'center', using Euclidean distance squared.
     *
     * @param center    The reference node.
     * @param candidate The node to check.
     * @param radius    The visibility radius.
     * @return          true if candidate is within radius of center, false otherwise.
     */
    private boolean isWithinRadius(GraphNode center, GraphNode candidate, int radius) {
        int dx = center.getNode_X() - candidate.getNode_X();
        int dy = center.getNode_Y() - candidate.getNode_Y();
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    /**
     * Reconstructs the path from endNode back to startNode using the 'previousNode' matrix.
     * The reconstructed path is stored in 'this.path' (a MyQueue), reversing the stack of nodes.
     *
     * @param previousNode A 2D array tracking the predecessor of each node on the shortest path.
     * @param startNode    The starting node of the path.
     * @param endNode      The end node for which the path is reconstructed.
     */
    private void reconstructPath(GraphNode[][] previousNode, GraphNode startNode, GraphNode endNode) {
        // Clear any old path
        this.path = new MyQueue<>();

        MyStack<GraphNode> stack = new MyStack<>();

        // Start from endNode and work backwards
        GraphNode current = endNode;
        while (current != null && current != startNode) {
            stack.push(current);
            current = previousNode[current.getNode_X()][current.getNode_Y()];
        }
        // Finally, push the startNode
        if (startNode != null) {
            stack.push(startNode);
        }

        // Pop from stack to get start-to-end order
        while (!stack.isEmpty()) {
            this.path.enqueue(stack.pop());
        }
    }
}
