/**
 * MyAVLTree is a custom implementation of an AVL Tree, a self-balancing binary search tree.
 * This class provides methods for inserting, deleting, searching, and finding nodes in the tree,
 * ensuring that it remains balanced after each modification.
 */
public class MyAVLTree {

    private MyTreeNode root; // Root node of the AVL tree
    private int size; // Number of nodes in the AVL tree

    /**
     * Default constructor for MyAVLTree Class.
     */
    public MyAVLTree() {}


    /**
     * Gets the root node of the AVL tree.
     *
     * @return The root node of the AVL tree.
     */
    public MyTreeNode getRoot() {
        return root;
    }

    /**
     * Calculates the height of a node in the AVL tree.
     *
     * @param node The node for which the height is calculated.
     * @return The height of the node, or 0 if the node is null.
     */
    public int height(MyTreeNode node) {
        if (node == null)
            return 0;
        return node.height;
    }

    /**
     * Right rotates the subtree rooted with a given node.
     *
     * @param subRoot The root of the subtree to be right-rotated.
     * @return The new root of the rotated subtree.
     */
    public MyTreeNode rightRotate(MyTreeNode subRoot) {
        MyTreeNode newSubRoot = subRoot.left;
        MyTreeNode temp = newSubRoot.right;

        // Perform rotation
        newSubRoot.right = subRoot;
        subRoot.left = temp;

        // Update heights
        subRoot.height = Math.max(height(subRoot.left), height(subRoot.right)) + 1;
        newSubRoot.height = Math.max(height(newSubRoot.left), height(newSubRoot.right)) + 1;

        // Return new root
        return newSubRoot;
    }

    /**
     * Left rotates the subtree rooted with a given node.
     *
     * @param subRoot The root of the subtree to be left-rotated.
     * @return The new root of the rotated subtree.
     */
    public MyTreeNode leftRotate(MyTreeNode subRoot) {
        MyTreeNode newSubRoot = subRoot.right;
        MyTreeNode temp = newSubRoot.left;

        // Perform rotation
        newSubRoot.left = subRoot;
        subRoot.right = temp;

        // Update heights
        subRoot.height = Math.max(height(subRoot.left), height(subRoot.right)) + 1;
        newSubRoot.height = Math.max(height(newSubRoot.left), height(newSubRoot.right)) + 1;

        // Return new root
        return newSubRoot;
    }

    /**
     * Gets the balance factor of a node in the AVL tree.
     *
     * @param node The node for which the balance factor is calculated.
     * @return The balance factor, where a positive value indicates a left-heavy node
     *         and a negative value indicates a right-heavy node.
     */
    public int getBalance(MyTreeNode node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    /**
     * Public method to insert a element into the AVL tree. This method calls the
     * private recursive insert method to update the tree.
     *
     * @param element The element to insert into the AVL tree.
     */
    public void insert(int element) {
        root = insert(root, element); // Update the root after insertion
    }

    /**
     * Private helper method to recursively insert a element into the AVL tree.
     * Balances the tree if it becomes unbalanced after insertion.
     *
     * @param node The root of the subtree where the element will be inserted.
     * @param element  The element to insert.
     * @return The new root of the subtree after insertion.
     */
    private MyTreeNode insert(MyTreeNode node, int element) {
        // Perform the standard BST insertion
        if (node == null) {
            size++;
            return new MyTreeNode(element);
        }

        if (element < node.element) {
            node.left = insert(node.left, element);
        } else if (element > node.element) {
            node.right = insert(node.right, element);
        } else {
            // Duplicate keys not allowed
            return node;
        }

        // Update height of this ancestor node
        node.height = Math.max(height(node.left), height(node.right)) + 1;

        // Get the balance factor of this node to check if it is unbalanced
        int balance = getBalance(node);

        // If this node becomes unbalanced, there are 4 cases

        // Left Left Case
        if (balance > 1 && element < node.left.element) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && element > node.right.element) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && element > node.left.element) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && element < node.right.element) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // Return the unchanged node pointer
        return node;
    }

    /**
     * Finds the node with the minimum key value in a given subtree.
     *
     * @param node The root of the subtree.
     * @return The node with the minimum key in the subtree.
     */
    public MyTreeNode minValueNode(MyTreeNode node) {
        MyTreeNode current = node;

        // Traverse to the leftmost leaf node
        while (current.left != null)
            current = current.left;

        return current;
    }

    /**
     * Public method to delete a element from the AVL tree. This method calls the
     * private recursive deleteNode method to update the tree.
     *
     * @param element The element to delete from the AVL tree.
     */
    public void delete(int element) {
        root = deleteNode(root, element); // Update the root after deletion
    }

    /**
     * Private helper method to recursively delete a element from the AVL tree.
     * Balances the tree if it becomes unbalanced after deletion.
     *
     * @param root The root of the subtree where the element will be deleted.
     * @param element  The element to delete.
     * @return The new root of the subtree after deletion.
     */
    private MyTreeNode deleteNode(MyTreeNode root, int element) {
        // Perform standard BST delete
        if (root == null)
            return root;

        // Traverse the tree to find the node to delete
        if (element < root.element)
            root.left = deleteNode(root.left, element);
        else if (element > root.element)
            root.right = deleteNode(root.right, element);
        else {
            // Node with one or zero children
            if (root.left == null || root.right == null) {
                MyTreeNode temp = root.left != null ? root.left : root.right;

                // No child case
                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    root = temp; // One child case
                }
                size--; // Decrement size as a node is deleted
            } else {
                // Node with two children
                MyTreeNode temp = minValueNode(root.right); // Get the inorder successor
                root.element = temp.element; // Copy the inorder successor's data
                root.right = deleteNode(root.right, temp.element); // Delete the inorder successor
            }
        }

        // If the tree had only one node, return null
        if (root == null)
            return root;

        // Update height of the current node
        root.height = Math.max(height(root.left), height(root.right)) + 1;

        // Get the balance factor to check if this node became unbalanced
        int balance = getBalance(root);

        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root; // Return the modified root
    }

    /**
     * Searches for a element in the AVL tree.
     *
     * @param root The root node of the subtree.
     * @param element  The element to search for.
     * @return True if the element is found, false otherwise.
     */
    public boolean searchNode(MyTreeNode root, int element) {
        if (root == null)
            return false;
        else if (root.element == element)
            return true;
        else if (root.element > element)
            return searchNode(root.left, element);
        else
            return searchNode(root.right, element);
    }

    /**
     * Finds the greatest node with a value smaller than the given value.
     *
     * @param node  The root of the subtree.
     * @param value The value to compare.
     * @return The node with the greatest value less than the given value, or null if none exists.
     */
    public MyTreeNode findGreatestSmallerThan(MyTreeNode node, int value) {
        if (node == null) {
            return null;
        }
        if (node.element >= value) {
            return findGreatestSmallerThan(node.left, value);
        } else {
            MyTreeNode rightResult = findGreatestSmallerThan(node.right, value);
            return (rightResult != null) ? rightResult : node;
        }
    }

    /**
     * Finds the smallest node with a value greater than the given value.
     *
     * @param node  The root of the subtree.
     * @param value The value to compare.
     * @return The node with the smallest value greater than the given value, or null if none exists.
     */
    public MyTreeNode findSmallestGreaterThan(MyTreeNode node, int value) {
        if (node == null) {
            return null;
        }
        if (node.element <= value) {
            return findSmallestGreaterThan(node.right, value);
        } else {
            MyTreeNode leftResult = findSmallestGreaterThan(node.left, value);
            return (leftResult != null) ? leftResult : node;
        }
    }

}
