import java.util.Iterator;

/**
 * Allows for indefinite iteration over a subset of its elements. The loop goes through all of them by default.
 * tail is always pointing at some other element E or itself. E can be swapped for another node
 * at any moment. If E is removed, the tail will point to the next element relative to E (the loop will become shorter).
 * If there is only one element in the loop (E.next() == E), an attempt to remove it will throw an IllegalStateException.
 */
public class LoopList<T> extends BasicList<T> {

    private Node<T> tail = head;

    class LoopListIterator<E> extends BasicListIterator<T> {

        @Override
        public boolean hasNext() { return true; }

        @Override
        public void remove() {
            /* if there is only one node in the loop then throw exception */
            if (currentNode == previousNode)
                throw new IllegalStateException("The loop has only one element which can't be removed");
            /* if removing the node to which the tail points to then contract the loop */
            if (currentNode == tail.getNext()) tail.setNext(currentNode.getNext());
            /* if removing the tail (but it's not the only node in the loop) mark previous as the tail */
            if (currentNode == tail) tail = previousNode;
            /* remove the current node */
            super.remove();
        }
    }

    LoopList() { head.setNext(head); }

    @Override
    void insert(int insertionIndex, T newValue) {
        super.insert(insertionIndex, newValue);
        /* inserting a node in front of the tail makes it the new tail */
        if (insertionIndex == size -1) tail = tail.getNext();
    }

    void loopTo(int index) {
        checkIndex(index);
        Node<T> newLoopEntryPoint = getPreviousNode(index).getNext();
        tail.setNext(newLoopEntryPoint);
    }

    @Override
    public Iterator<T> iterator() { return new LoopListIterator<T>(); }
}