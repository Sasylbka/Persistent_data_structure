package PersistentDataStructure;



import TreeStructure.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class PersistentArray<T> {

    private final PersistentArray<T> latestVersion;
    private PersistentArray<T> futureVersion;
    final Node<T> root;
    final int numberOfChild;
    final int depth;
    final int base; //BF ^ (depth - 1)
    final int size;

    /**
     * package-private constructor for the persistent array
     *
     * @param root a designated/initial vertex in a graph
     * @param numberOfChild number of children at each node
     * @param depth maximum number of edges in the paths from the root to any node
     * @param base numberOfChild ^ (depth - 1)
     * @param size number of leaves in the graph or elements in the persistent array
     */
    PersistentArray(Node<T> root, int numberOfChild, int depth, int base, int size,PersistentArray<T> latestVersion) {
        this.root = root;
        this.numberOfChild = numberOfChild;
        this.depth = depth;
        this.base = base;
        this.size = size;
        this.latestVersion=latestVersion;
    }
    PersistentArray(PersistentArray<T> thisVersion,PersistentArray<T> futureVersion){
        this.root=thisVersion.root;
        this.numberOfChild=thisVersion.numberOfChild;
        this.base=thisVersion.base;
        this.size=thisVersion.size;
        this.depth=thisVersion.depth;
        this.latestVersion=thisVersion.latestVersion;
        this.futureVersion=futureVersion;

    }

    /**
     * constructor for the persistent array
     *
     * @param powerOfNumberOfChild the branching factor will be equals to
     * 2^powerOfNumberOfChild
     */
    public PersistentArray(int powerOfNumberOfChild) {
        int numberOfChild = 1;
        for (int i = 0; i < powerOfNumberOfChild; i++) {
            numberOfChild *= 2;
        }

        this.numberOfChild = numberOfChild;
        this.root = new Node<>(numberOfChild);
        this.depth = 1;
        this.base = 1;
        this.size = 0;
        this.latestVersion = this;
        this.futureVersion = null;
    }

    private class TraverseData {

        Node<T> currentNode;
        Node<T> currentNewNode;
        Node<T> newRoot;
        int index;
        int base;

        public TraverseData(Node<T> currentNode, Node<T> currentNewNode, Node<T> newRoot, int index,
                            int base) {
            this.currentNode = currentNode;
            this.currentNewNode = currentNewNode;
            this.newRoot = newRoot;
            this.index = index;
            this.base = base;
        }
    }

    /**
     * traverse one level in the graph
     *
     * @param data metadata before this level
     * @return metadata after this level
     */
    private TraverseData traverseOneLevel(TraverseData data) {
        Node<T> currentNode = data.currentNode;
        Node<T> currentNewNode = data.currentNewNode;
        int nextBranch = data.index / data.base;

        currentNewNode.set(nextBranch, new Node<>(numberOfChild));
        for (int anotherBranch = 0; anotherBranch < numberOfChild; anotherBranch++) {
            if (anotherBranch == nextBranch) {
                continue;
            }
            currentNewNode.set(anotherBranch, currentNode.get(anotherBranch));
        }

        //down
        currentNode = currentNode.get(nextBranch);
        currentNewNode = currentNewNode.get(nextBranch);
        return new TraverseData(currentNode, currentNewNode, data.newRoot, data.index % data.base,
                data.base);
    }

    /**
     * traverse the old structure while creating the new one and copying data into it
     *
     * @param index destination index in the array
     * @return metadata of traversing
     */
    private TraverseData traverse(int index) {
        Node<T> newRoot = new Node<>(numberOfChild);
        Node<T> currentNode = this.root;
        Node<T> currentNewNode = newRoot;

        for (int b = base; b > 1; b = b / numberOfChild) {
            TraverseData data = traverseOneLevel(
                    new TraverseData(currentNode, currentNewNode, newRoot, index, b));
            currentNode = data.currentNode;
            currentNewNode = data.currentNewNode;
            index = data.index;
        }
        return new TraverseData(currentNode, currentNewNode, newRoot, index, 1);
    }

    /**
     * Returns the element at the specified position in this list
     *
     * @param index index of the element to be returned
     * @return the element at the specified index in the given list
     */
    public T get(int index) {
        Node<T> currentNode = this.root;

        for (int b = base; b > 1; b = b / numberOfChild) {
            int nextBranch = index / b;

            //down
            currentNode = currentNode.get(nextBranch);
            index = index % b;
        }
        return currentNode.get(index).data;

    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element
     *
     * @param index index of the element to replace
     * @param data element to be stored at the specified position
     * @return new version of the persistent array
     */
    public PersistentArray<T> set(int index, T data) {
        int newSize = this.size;
        if (newSize == index) {
            newSize++;
        }

        TraverseData traverseData = traverse(index);

        traverseData.currentNewNode.set(traverseData.index, new Node<>(numberOfChild, data));
        for (int i = 0; i < numberOfChild; i++) {
            if (i == traverseData.index) {
                continue;
            }
            traverseData.currentNewNode.set(i, traverseData.currentNode.get(i));
        }
        this.futureVersion= new PersistentArray<>(traverseData.newRoot, this.numberOfChild, this.depth,
                this.base, newSize, this);
        return new PersistentArray<>(traverseData.newRoot, this.numberOfChild, this.depth,
                this.base, newSize,this);
    }

    /**
     * Append a specified element to the end of a list
     *
     * @param data The element to be appended to this list
     * @return new version of the persistent array
     */
    public PersistentArray<T> add(T data) {
        //there's still space in the latest element
        if (this.size == 0 || this.size % numberOfChild != 0) {
            return set(this.size, data);
        }

        //there's still space for the new data
        if (this.base * numberOfChild > this.size) {
            Node<T> newRoot = new Node<>(numberOfChild);

            Node<T> currentNode = this.root;
            Node<T> currentNewNode = newRoot;

            int index = this.size;
            int b;
            for (b = base; b > 0; b = b / numberOfChild) {
                TraverseData traverseData = traverseOneLevel(
                        new TraverseData(currentNode, currentNewNode, newRoot, index, b));
                currentNode = traverseData.currentNode;
                currentNewNode = traverseData.currentNewNode;
                index = traverseData.index;

                if (currentNode == null) {
                    b = b / numberOfChild;
                    break;
                }
            }

            while (b > 1) {
                currentNewNode.set(0, new Node<>(numberOfChild));
                currentNewNode = currentNewNode.get(0);
                index = index % b;
                b = b / numberOfChild;
            }
            currentNewNode.set(0, new Node<>(numberOfChild, data));

            return new PersistentArray<>(newRoot, this.numberOfChild, this.depth, this.base,
                    this.size + 1,this);
        }

        //root overflow
        Node<T> newRoot = new Node<>(numberOfChild);
        newRoot.set(0, this.root);
        newRoot.set(1, new Node<>(numberOfChild));
        //newRoot[2..]=null
        Node<T> currentNewNode = newRoot.get(1);

        int b = base;
        while (b > 1) {
            currentNewNode.set(0, new Node<>(numberOfChild));
            currentNewNode = currentNewNode.get(0);
            b = b / numberOfChild;
        }
        currentNewNode.set(0, new Node<>(numberOfChild, data));
        this.futureVersion=new PersistentArray<>(newRoot, this.numberOfChild, this.depth + 1,
                this.base * numberOfChild, this.size + 1,latestVersion);
        return new PersistentArray<>(newRoot, this.numberOfChild, this.depth + 1,
                this.base * numberOfChild, this.size + 1,latestVersion);
    }

    /**
     * Removes the last element in this list
     *
     * @return new version of the persistent array
     */
    public PersistentArray<T> pop() {
        //the latest element won't become empty
        int index = this.size - 1;
        Node<T> newRoot = new Node<>(numberOfChild);

        Node<T> currentNode = this.root;
        Node<T> currentNewNode = newRoot;

        ArrayList<Node<T>> newNodes = new ArrayList<>();
        newNodes.add(newRoot);
        ArrayList<Integer> newNodesIndices = new ArrayList<>();

        for (int b = base; b > 1; b = b / numberOfChild) {
            TraverseData traverseData = traverseOneLevel(
                    new TraverseData(currentNode, currentNewNode, newRoot, index, b));
            currentNode = traverseData.currentNode;
            currentNewNode = traverseData.currentNewNode;
            newNodes.add(currentNewNode);
            newNodesIndices.add(index / b);
            index = traverseData.index;
        }
        newNodesIndices.add(index);

        for (int i = 0; i < numberOfChild && i < index; i++) {
            currentNewNode.set(i, currentNode.get(i));
        }
        currentNewNode.set(index, null);

        if (index == 0 && newNodes.size() >= 2) {
            int latestIndex = newNodes.size() - 2;
            newNodes.get(latestIndex).set(newNodesIndices.get(latestIndex), null);

            for (int i = latestIndex; i > 0; i--) {
                if (newNodesIndices.get(i) == 0) {
                    newNodes.get(i - 1).set(newNodesIndices.get(i - 1), null);
                } else {
                    break;
                }
            }
        }

        if (newNodes.size() > 1) {
            int nonNullChildren = 0;
            for (Node<T> child : newRoot.children) {
                if (child != null) {
                    nonNullChildren++;
                }
            }
            if (nonNullChildren == 1) { //need new root
                newRoot = newRoot.get(0);
                return new PersistentArray<>(newRoot, this.numberOfChild, this.depth - 1,
                        this.base / numberOfChild,
                        this.size - 1,this);
            }
        }
        this.futureVersion=new PersistentArray<>(newRoot, this.numberOfChild, this.depth, this.base,
                this.size - 1,this);
        return new PersistentArray<>(newRoot, this.numberOfChild, this.depth, this.base,
                this.size - 1,this);
    }

    /**
     * convert the structure to PersistentLinkedList sharing the same data
     * @return PersistentLinkedList
     */
    public PersistentLinkedList<T> toPersistentLinkedList() {
        PersistentLinkedList<T> out = new PersistentLinkedList<>(this.root, this.numberOfChild, this.depth, this.base,
                this.size, new TreeSet<>(), 0, this.size);
        for (int i = -1; i < size; i++) {
            out.setLinks(i, i + 1);
        }
        return out;
    }

    /**
     * @return persistent array size
     */
    public int size() {
        return this.size;
    }

    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return get(currentIndex++);
            }
        };

        return it;
    }
    public PersistentArray<T> undo() {
        if (latestVersion == null) {
            return null;
        }
        return new PersistentArray<T>(latestVersion, this);
    }
    public PersistentArray<T> redo() {
        return futureVersion;
    }
    /**
     * recursive function returning the string representation of the current subgraph
     *
     * @param node root node for the current subgraph
     * @param curDepth depth left till the leaf level
     * @return string representation of the current subgraph
     */
    private String toStringHelper(Node<T> node, int curDepth) {
        if (node.data != null) {
            return node.data.toString();
        }
        if (node.children == null) {
            return "_";
        }

        StringBuilder outString = new StringBuilder();
        for (int i = 0; i < numberOfChild; i++) {
            if (node.get(i) == null) {
                outString.append("_");
                //break;
            } else {
                if (curDepth == 0) {
                    outString.append(node.get(i).toString());

                } else {
                    outString.append(toStringHelper(node.get(i), curDepth - 1));
                }
            }

            if (i + 1 != numberOfChild) {
                outString.append(", ");
            }
        }
        return "(" + outString + ")";
    }

    @Override
    public String toString() {
        return toStringHelper(this.root, this.depth);
    }
}

