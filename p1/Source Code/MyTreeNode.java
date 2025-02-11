/**
 * MyTreeNode represents a node in a binary tree, specifically for use in an AVL tree.
 * Each node contains an integer element, references to left and right child nodes,
 * and a height value for balancing in AVL trees.
 */
public class MyTreeNode {

    protected int element; // The integer value stored in this node
    protected MyTreeNode right; // Reference to the right child node
    protected MyTreeNode left; // Reference to the left child node
    protected int height; // Height of the node for balancing in AVL trees

    /**
     * Constructs a new tree node with the specified element.
     * Initializes the node's height to 1, and left and right child nodes to null.
     *
     * @param number The integer element to store in this node.
     */
    public MyTreeNode(int number) {
        element = number; // Set the element of the node
        height = 1; // Initial height of a new node is 1
        left = null; // Left child is initially null
        right = null; // Right child is initially null
    }

    /**
     * Gets the element stored in this node.
     *
     * @return The integer element of this node.
     */
    public int getElement() {
        return element;
    }
}
