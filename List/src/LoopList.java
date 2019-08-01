import java.util.Iterator;

/**
 * Use it to demonstrate some loop finding algorithms via tests.
 * Allows for indefinite iteration over a subset of its elements. The loop goes through all of them by default.
 * tail is always pointing at some other element E or itself. E can be swapped for another node at any moment.
 * If E is removed, the tail will point to the next element relative to E (the loop will become shorter).
 * If there is only one element in the loop (E.next() == E), an attempt to remove it will throw an IllegalStateException.
 */
public class LoopList<T> extends BasicList<T> {

    Node<T> tail;
    boolean loopIsDefault = true;

    class LoopListIterator<E> extends BasicListIterator<T> {

        @Override
        public boolean hasNext() { return size != 0; }

        @Override
        public void remove() {
            /* if there is only one node in the loop then throw exception */
            if (currentNode == tail.getNext()  &&  tail.getNext() == tail)
                throw new IllegalStateException("The loop has only one element which can't be removed");
            /* if removing the node to which the tail points to then contract the loop */
            if (currentNode == tail.getNext()) tail.setNext(currentNode.getNext());
            /* if removing the tail (but it's not the only node in the loop) mark previous as the tail */
            if (currentNode == tail) tail = previousNode;
            /* remove the current node */
            super.remove();
        }
    }

    LoopList(T firstValue) {
        Node<T> firstNode = new Node<>(firstValue, null);
        firstNode.setNext(firstNode);
        head.setNext(firstNode);
        tail = firstNode;
        size++;
    }

    @Override
    public Iterator<T> iterator() { return new LoopListIterator<T>(); }

    @Override
    void insert(int insertionIndex, T newValue) {
        super.insert(insertionIndex, newValue);
        /* inserting a node in front of the tail makes it the new tail */
        if (insertionIndex == size -1) tail = tail.getNext();
        if (loopIsDefault) tail.setNext(head.getNext());
    }

    void loopTailTo(int index) {
        Node<T> newLoopEntryPoint = getNodePrecedingTo(index).getNext();
        tail.setNext(newLoopEntryPoint);
        loopIsDefault = false;
    }

    Node<T> findTailByRetracing() {
        if (size == 1) return head.getNext();
        Node<T> leader = head.getNext();
        Node<T> tracer = head.getNext();
        while (true) {
            while (leader != tracer) {
                if (leader.getNext() == tracer) return leader;
                tracer = tracer.getNext();
            }
            leader = leader.getNext();
        }
    }

    Node<T> findTailFloyd() {
        if (size == 1) return head.getNext();
        Node<T> hare = head.getNext().getNext();
        Node<T> tortoise = head.getNext();

        while (hare != tortoise) {
            hare = hare.getNext().getNext();
            tortoise = tortoise.getNext();
        }
        hare = head;
        while (hare != tortoise) {
            hare = hare.getNext();
            tortoise = tortoise.getNext();
        }
        /* at this point mare and tortoise are both at the start of the loop */
        while (tortoise.getNext() != hare)
            tortoise = tortoise.getNext();

        return tortoise;
    }
}