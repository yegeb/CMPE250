/**
 * Represents a node in a graph, holding its coordinates, type,
 * outgoing edges (neighbors), and state flags (e.g., seen/unseen).
 *
 * Each GraphNode tracks a mapping of other GraphNodes to float costs
 * (travel times), stored in a custom MyHashMap<GraphNode, Float>.
 */
public class GraphNode {

    /**
     * A custom HashMap storing outgoing edges from this node to neighbor nodes,
     * keyed by the neighbor GraphNode and valued by the edge cost/weight.
     */
    private MyHashMap<GraphNode, Float> outgoingNeighbor;

    /** The "type" or category of this node (e.g., "0", "1", some string). */
    private String nodeType;

    /** X coordinate of this node in a 2D grid. */
    private int node_X;

    /** Y coordinate of this node in a 2D grid. */
    private int node_Y;

    /**
     * Distance metric used by pathfinding algorithms (e.g., Dijkstra).
     * Defaults to Float.MAX_VALUE when unknown.
     */
    public float distance = Float.MAX_VALUE;

    /** Flag indicating whether this node has been "seen" (e.g. revealed). */
    private boolean isSeen;

    /**
     * Constructs a new GraphNode with specific coordinates and node type.
     *
     * @param node_X   the X coordinate of the node
     * @param node_Y   the Y coordinate of the node
     * @param nodeType the string representing this node's type
     */
    public GraphNode(int node_X, int node_Y, String nodeType) {
        this.node_X = node_X;
        this.node_Y = node_Y;
        this.nodeType = nodeType;
        this.outgoingNeighbor = new MyHashMap<>();
        this.isSeen = false;
    }

    /**
     * Marks this node as seen/unseen.
     *
     * @param seen true to mark the node as seen, false otherwise
     */
    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    /**
     * Checks if this node is marked as seen.
     *
     * @return true if seen, false otherwise
     */
    public boolean isSeen() {
        return isSeen;
    }

    /**
     * Retrieves the X coordinate of this node.
     *
     * @return the X coordinate
     */
    public int getNode_X() {
        return node_X;
    }

    /**
     * Retrieves the Y coordinate of this node.
     *
     * @return the Y coordinate
     */
    public int getNode_Y() {
        return node_Y;
    }

    /**
     * Retrieves the type of this node (e.g., "0", "1", or other string).
     *
     * @return the node type
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Sets the type of this node.
     *
     * @param nodeType the new node type
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Returns the mapping of outgoing edges from this node.
     * The map keys are neighboring GraphNodes; the values are travel times (float).
     *
     * @return a MyHashMap of neighbor-to-cost entries
     */
    public MyHashMap<GraphNode, Float> getOutgoingNeighbors() {
        return outgoingNeighbor;
    }

    /**
     * Adds a directed edge from this node to the specified neighbor
     * with a given travelTime cost.
     *
     * @param node       the neighbor node
     * @param travelTime the cost or weight of traveling to that neighbor
     */
    public void addNeighbor(GraphNode node, float travelTime) {
        this.outgoingNeighbor.put(node, travelTime);
    }

    /**
     * Provides a string representation of this GraphNode, including its
     * coordinates, type, and seen status.
     *
     * @return a formatted string describing the node
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GraphNode{");
        sb.append("key='").append(node_X).append(" - ").append(node_Y).append('\'');
        sb.append(", type='").append(nodeType).append('\'');
        sb.append(", seen=").append(isSeen);
        sb.append("}}");
        return sb.toString();
    }

    /**
     * Indicates whether another object is "equal to" this one by comparing
     * nodeType, coordinates, and outgoing neighbors.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // Check if the object is the same instance
        if (this == obj) {
            return true;
        }

        // Check if the object is an instance of GraphNode
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Cast the object to GraphNode
        GraphNode otherNode = (GraphNode) obj;

        // Compare the nodeType, coordinates, and outgoingNeighbor
        return this.nodeType.equals(otherNode.nodeType)
                && this.node_X == otherNode.node_X
                && this.node_Y == otherNode.node_Y
                && this.outgoingNeighbor.equals(otherNode.outgoingNeighbor);
    }
}
