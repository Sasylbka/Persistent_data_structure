package TreeStructure;

import java.util.ArrayList;

public class Node<T> {
    public final ArrayList<Node<T>> children;
    public final T data;
    public int previousIndex; //-1 or non-negative graph index corresponding to the next element in the linked list
    public int nextIndex; //-1 or non-negative graph index corresponding to the next element in the linked list

    /**
     * constructor for internal (non-leaf) nodes
     */
    public Node(int branchingFactor) {
        this.data = null;
        this.children = new ArrayList<>();
        for (int i = 0; i < branchingFactor; i++) {
            this.children.add(null);
        }
        this.previousIndex = -1;
        this.nextIndex = -1;
    }

    /**
     * constructor for leaf nodes
     *
     * @param data data to be stored in the leaf
     */
    public Node(int branchingFactor, T data) {
        this.data = data;
        this.children = null;
        this.previousIndex = -1;
        this.nextIndex = -1;
    }

    /**
     * get the ith child in the current node
     *
     * @param i index of the needed child
     * @return the ith child
     */
    public Node<T> get(int i) {
        if (this.children.size() <= i) {
            return null;
        }
        return this.children.get(i);
    }

    /**
     * set the ith child
     *
     * @param i index of the needed child
     * @param e new value of the child
     */
    public void set(int i, Node<T> e) {
        this.children.set(i, e);
    }
}
