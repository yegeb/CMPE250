import java.util.ArrayList;

/**
 * A container class for a 2D grid of GraphNodes and a registry
 * of nodes indexed by their nodeType.
 */
public class Land {

    /**
     * 2D array (grid) to hold GraphNodes by their (x, y) coordinates.
     * gridSize[0] = width, gridSize[1] = height.
     */
    private GraphNode[][] grid;

    /**
     * A mapping from nodeType (e.g., "0", "1", "help", etc.) to a list of GraphNodes of that type.
     * This is a custom map that associates each string nodeType with an ArrayList of GraphNodes.
     */
    private MyHashMap<String, ArrayList<GraphNode>> nodesByType;

    /**
     * An int array specifying the size of the grid: [width, height].
     */
    private int[] gridSize;

    /**
     * Constructs a Land object without knowing its dimensions yet.
     * The grid is initialized later in setGridSize().
     */
    public Land() {
        nodesByType = new MyHashMap<>();
    }

    /**
     * Sets the dimensions of the grid and initializes the 2D array.
     *
     * @param gridSize an int[] where gridSize[0] = width, gridSize[1] = height
     */
    public void setGridSize(int[] gridSize) {
        this.gridSize = gridSize;
        // Create the 2D array now that the dimension is known
        this.grid = new GraphNode[gridSize[0]][gridSize[1]];
    }

    /**
     * Adds a single GraphNode to the internal 2D grid structure,
     * using the node's (x, y) coordinates.
     *
     * @param node the GraphNode to add to the grid
     */
    public void addNode(GraphNode node) {
        int x = node.getNode_X();
        int y = node.getNode_Y();
        this.grid[x][y] = node;
    }


    /**
     * Adds a GraphNode to the mapping of nodeType -> ArrayList<GraphNode>.
     * If the nodeType does not exist in the map, create a new ArrayList.
     *
     * @param node the GraphNode to categorize by its nodeType
     */
    public void addNodesByType(GraphNode node) {
        String nodeType = node.getNodeType();
        if (!nodesByType.containsKey(nodeType)) {
            nodesByType.put(nodeType, new ArrayList<>());
        }
        nodesByType.get(nodeType).add(node);
    }

    /**
     * Returns the underlying 2D grid of GraphNodes.
     *
     * @return a 2D array of GraphNodes
     */
    public GraphNode[][] getGrid() {
        return grid;
    }

    /**
     * Returns the map from nodeType to list of GraphNodes of that type.
     *
     * @return a custom MyHashMap<String, ArrayList<GraphNode>>
     */
    public MyHashMap<String, ArrayList<GraphNode>> getNodesByType() {
        return nodesByType;
    }

    /**
     * Returns the gridSize array, which contains [width, height].
     *
     * @return an int[] of the form [width, height]
     */
    public int[] getGridSize() {
        return gridSize;
    }
}
