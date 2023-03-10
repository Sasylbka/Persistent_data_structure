package TreeStructure;

import java.util.ArrayList;

public class NodeMap<K, V> {

    public final ArrayList<NodeMap<K, V>> children;
    public ArrayList<V> values;
    public ArrayList<K> keys;
    public int previousIndex; //-1 or non-negative graph index corresponding to the next element in the linked list
    public int nextIndex; //-1 or non-negative graph index corresponding to the next element in the linked list

    /**
     * constructor for internal (non-leaf) nodes
     */
    public NodeMap(int branchingFactor) {
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
     * @param value value to be stored in the leaf
     */
   public NodeMap(NodeMap<K, V> oldNode, K key, V value) {
        this.keys = new ArrayList<K>();
        this.values = new ArrayList<V>();
        if (oldNode != null) {
            if (oldNode.keys.size() == 0) {
                this.keys.add(key);
                this.values.add(value);
            } else {
                boolean isIn = oldNode.keys.contains(key);
                this.keys.addAll(oldNode.keys);
                this.values.addAll(oldNode.values);
                if (!isIn) {
                    this.keys.add(key);
                    this.values.add(value);
                } else if (value == null) {
                    int position = oldNode.keys.indexOf(key);
                    this.keys.remove(position);
                    this.values.remove(position);
                }
            }
        } else {
            this.keys.add(key);
            this.values.add(value);
        }
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
    public NodeMap<K, V> get(int i) {
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
    public void set(int i, NodeMap<K, V> e) {
        this.children.set(i, e);
    }
}
