import java.util.Comparator;
import java.util.Iterator;

/**
 * A binomial-heap-based implementation of a priority queue. It is not optimal but it works.
 * Null values are not allowed.
 */
//TODO: it would be substantially faster to store empty trees in the heap and just change them as required during
// heaps merging. They can be sorted right after the merging is complete. Iterator? equals(), hashCode()
public class PriorityQueue<T extends Comparable<T>> {

    private static class Node<E extends Comparable<E>> {

        final E value;
        final LinkedList<Node<E>> children;

        Node(E value) {
            this.value = value;
            this.children = new LinkedList<>();
        }
    }

    private class Tree implements Comparable<Tree> {

        final Node<T> root;

        Tree(Node<T> root)
        { this.root = root; }

        int getOrder()
        { return root.children.size; }

        boolean isEmpty()
        { return this.root == null; }

        @Override
        public int compareTo(Tree that)
        { return this.getOrder() - that.getOrder(); }

        /**
         * This method allows to just meld multiple trees without trying to find out which of them is nonempty.
         * Any amount of them can be passed to it via method call chaining.
         */
        Tree meldWith(Tree that) {
            if (that.isEmpty())
                return this;
            if (this.isEmpty())
                return that;
            if (this.getOrder() != that.getOrder())
                throw new IllegalArgumentException("An attempt to meld trees of different orders has been made");

            Tree smaller = (comparator.compare(this.root.value, that.root.value) <= 0) ? this : that;
            Tree bigger = (this == smaller) ? that : this;
            smaller.root.children.add(bigger.root);
            return smaller;
        }

        @Override
        public String toString()
        { return this.isEmpty() ? "empty" : this.root.value.toString(); }
    }

    /**
     * The merging operation is only defined for trees of the same order. This iterator exposes the next tree,
     * thereby allowing to get its order and decide what to do.
     */
    private class HeapIterator implements Iterator<Tree> {

        Tree nextTree;
        Iterator<Tree> iterator;

        HeapIterator(Iterator<Tree> iterator) {
            this.iterator = iterator;
            nextTree = (iterator.hasNext()) ? iterator.next() : emptyTree;
        }

        @Override
        public boolean hasNext()
        { return iterator.hasNext(); }

        @Override
        public Tree next() {
            Tree returnValue = nextTree;
            nextTree = (iterator.hasNext()) ? iterator.next() : emptyTree;
            return returnValue;
        }
    }

    public final Comparator<T> comparator;
    private SortedList<Tree> heap = new SortedList<>();
    private final Tree emptyTree;

    PriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
        emptyTree = new Tree(null) {
            @Override
            int getOrder()
            { return Integer.MAX_VALUE; }    // this property is used in meldHeaps() to pick currentOrder correctly
        };
    }

    public void offer(PriorityQueue<T> that) {
        HeapIterator thisIter = new HeapIterator(this.heap.iterator());
        HeapIterator thatIter = new HeapIterator(that.heap.iterator());
        this.heap = new SortedList<>();
        meldHeaps(thisIter, thatIter, emptyTree);
    }

    public void offer(T item) {
        Tree tree = new Tree(new Node<T>(item));
        PriorityQueue<T> queue = new PriorityQueue<>(comparator);
        queue.heap.add(tree);
        offer(queue);
    }

    public T poll() {
        if (this.isEmpty())
            throw new IllegalStateException("This queue is empty");

        int smallestRootValueIndex = 0;
        T smallestRootValue = this.heap.peek(smallestRootValueIndex).root.value;
        Iterator<Tree> thisHeapIterator = this.heap.iterator();
        for (int currentIndex = 0; thisHeapIterator.hasNext(); ++currentIndex) {
            T currentValue = thisHeapIterator.next().root.value;
            if (comparator.compare(currentValue, smallestRootValue) < 0) {
                smallestRootValueIndex = currentIndex;
                smallestRootValue = currentValue;
            }
        }
        Tree target = this.heap.remove(smallestRootValueIndex);
        LinkedList<Tree> forest = new LinkedList<>();
        for (Node<T> node : target.root.children)
            forest.add(new Tree(node));
        SortedList<Tree> newHeap = new SortedList<>(forest);
        PriorityQueue<T> targetTreeChildren = new PriorityQueue<>(this.comparator);
        targetTreeChildren.heap = newHeap;
        offer(targetTreeChildren);
        return smallestRootValue;
    }

    public boolean isEmpty()
    { return heap.size == 0; }

    /**
     * Merges two heaps into one. The amount of trees in each binomial heap is not more than log2(N), where N is
     * the number of elements in the heap, so the recursion here is shallow.
     * @param previousTree -- a tree obtained at the previous level of recursion.
     */
    private void meldHeaps(HeapIterator heap1, HeapIterator heap2, Tree previousTree) {
        int currentOrder = min(heap1.nextTree.getOrder(), heap2.nextTree.getOrder(), previousTree.getOrder());
        Tree tree1 = (heap1.nextTree.getOrder() == currentOrder) ? heap1.next() : emptyTree;
        Tree tree2 = (heap2.nextTree.getOrder() == currentOrder) ? heap2.next() : emptyTree;

        int nonemptyTreesCount = 0;
        if (!tree1.isEmpty()) nonemptyTreesCount++;
        if (!tree2.isEmpty()) nonemptyTreesCount++;
        if (!previousTree.isEmpty()) nonemptyTreesCount++;

        switch (nonemptyTreesCount) {
            case 1: Tree nonemptyTree = tree1.meldWith(tree2).meldWith(previousTree);
                    this.heap.add(nonemptyTree);
                    meldHeaps(heap1, heap2, emptyTree);
                    break;

            case 2: Tree higherOrderTree1 = tree1.meldWith(tree2).meldWith(previousTree);
                    meldHeaps(heap1, heap2, higherOrderTree1);
                    break;

            case 3: this.heap.add(previousTree);
                    Tree higherOrderTree2 = tree1.meldWith(tree2);
                    meldHeaps(heap1, heap2, higherOrderTree2);
                    break;
        }
    }

    private int min(int... numbers) {
        if (numbers.length == 0)
            throw new IllegalArgumentException("No arguments passed to min()");

        int smallest = Integer.MAX_VALUE;
        for (int number : numbers)
            if (number < smallest)
                smallest = number;
        return smallest;
    }
}