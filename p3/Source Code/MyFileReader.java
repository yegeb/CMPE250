import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A utility class that reads various file formats to build or modify a Land object.
 * It parses nodes, edges, and objectives, constructing both GraphNodes
 * and the relationships between them.
 */
public class MyFileReader {

    /**
     * An instance of PathFinding to perform pathfinding operations during file parsing.
     */
    private PathFinding pathFinding;

    /**
     * Constructs a new MyFileReader, initializing the internal PathFinding object.
     */
    public MyFileReader() {
        this.pathFinding = new PathFinding();
    }

    /**
     * Parses a file that defines nodes for the Land.
     * The first line indicates the gridSize: "width height".
     * Subsequent lines define individual nodes: "x y nodeType".
     *
     * @param fileName the name (or path) of the node definition file
     * @param land     the Land object to populate with nodes
     * @throws IOException if there's an error reading the file
     */
    public void nodeFileParser(String fileName, Land land) throws IOException {
        // Use BufferedReader for efficient file reading
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        String[] words;

        // Read the first line to get grid dimensions (e.g. "10 8")
        line = reader.readLine();
        words = line.split(" ");
        int gridSize_X = Integer.parseInt(words[0]);
        int gridSize_Y = Integer.parseInt(words[1]);

        // Set the Land's grid size, initializing the 2D array
        land.setGridSize(new int[]{gridSize_X, gridSize_Y});

        // Read subsequent lines, each describing a node
        while ((line = reader.readLine()) != null) {
            words = line.split(" ");
            int node_X = Integer.parseInt(words[0]);
            int node_Y = Integer.parseInt(words[1]);
            String nodeType = words[2];

            // Create a GraphNode and add it to the Land
            GraphNode node = new GraphNode(node_X, node_Y, nodeType);
            land.addNode(node);         // Place node in the 2D grid
            land.addNodesByType(node);  // Also categorize it by nodeType
        }
    }

    /**
     * Parses a file that defines edges between nodes in the Land.
     * Each line: "Node1,Node2 Weight"
     * Where Node1 and Node2 are "x-y" coordinates, and Weight is a float.
     *
     * @param fileName the file specifying edges
     * @param land     the Land object containing the nodes
     * @throws IOException if there's an error reading the file
     */
    public void edgeFileParser(String fileName, Land land) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        // Read each line defining an edge
        while ((line = reader.readLine()) != null) {
            String[] words = line.split(" ");
            // e.g., words[0] = "0-0,0-1", words[1] = "1.5"
            String[] inputEdge = words[0].split(",");
            float travelTime = Float.parseFloat(words[1]);

            // Parse coordinates for the first node
            String[] xy1 = inputEdge[0].split("-");
            int x1 = Integer.parseInt(xy1[0]);
            int y1 = Integer.parseInt(xy1[1]);

            // Parse coordinates for the second node
            String[] xy2 = inputEdge[1].split("-");
            int x2 = Integer.parseInt(xy2[0]);
            int y2 = Integer.parseInt(xy2[1]);

            // Retrieve node references from the Land grid
            GraphNode node1 = land.getGrid()[x1][y1];
            GraphNode node2 = land.getGrid()[x2][y2];

            // Add bidirectional edges between the two nodes
            node1.addNeighbor(node2, travelTime);
            node2.addNeighbor(node1, travelTime);
        }
    }

    /**
     * Parses an objectives file. The first line contains the visibility radius (int).
     * The second line has the starting node coordinates (e.g. "startX startY").
     * Each subsequent line defines an objective node ("objX objY") and optionally
     * includes wizard help offers as extra tokens.
     *
     * @param fileName the file specifying objectives and (optionally) wizard help offers
     * @param land     the Land object containing the grid and GraphNodes
     * @param logger   a Logger object for recording progress or events
     * @throws IOException if there's an error reading the file
     */
    public void objFileParser(String fileName, Land land, Logger logger) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        String[] words;

        // Read radius
        line = reader.readLine();
        words = line.split("\\s+");
        int radius = Integer.parseInt(words[0]);

        // Read the starting node coordinates
        line = reader.readLine();
        words = line.split(" ");
        int startX = Integer.parseInt(words[0]);
        int startY = Integer.parseInt(words[1]);
        GraphNode startingNode = land.getGrid()[startX][startY];

        // Prepare to accumulate wizard "help" offers
        ArrayList<String> helpOffers = new ArrayList<>();
        GraphNode currentNode = startingNode;
        int i = 1;

        // Create a Wizard to handle help offers
        Wizard wizard = new Wizard(pathFinding, logger, land);

        // Read each objective line
        while ((line = reader.readLine()) != null) {
            words = line.split(" ");
            int objX = Integer.parseInt(words[0]);
            int objY = Integer.parseInt(words[1]);
            GraphNode objectiveNode = land.getGrid()[objX][objY];

            // If help offers exist, let the Wizard choose and apply them
            if (!helpOffers.isEmpty()) {
                wizard.wizardHelp(helpOffers, currentNode, objectiveNode, radius);
            }

            // Move from currentNode to objectiveNode
            // Pathfinding loop to reach each objective sequentially
            while (currentNode != objectiveNode) {
                pathFinding.dijkstra(land, currentNode, objectiveNode, "0", radius);
                currentNode = pathFinding.move(currentNode, land, objectiveNode, radius, logger);
            }
            logger.log("Objective " + i + " reached!");
            i++;

            // Parse any wizard help offers following the objective coordinates
            if (words.length > 2) {
                for (int j = 2; j < words.length; j++) {
                    helpOffers.add(words[j]);
                }
            }
        }
    }
}
