import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A minimal viable implementation of a list.
 * Search and removal can be done via its iterator.
 */
class BasicList<T> implements Iterable<T> {

    static class Node<T> {

        private T value;
        private Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        T getValue() { return value; }
        Node<T> getNext() { return next; }
        void setNext(Node<T> next) { this.next = next; }
    }

    protected final Node<T> head = new Node<>(null, null);
    protected int modCount;
    protected int size;

    void insert(T newValue, int insertionIndex) {
        ListUtils.checkForNull(newValue, "Null values are not supported");
        if (insertionIndex < 0  ||  insertionIndex > size)
            throw new IllegalArgumentException("Invalid index");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < insertionIndex -1; ++currentNodeIndex)
            currentNode = currentNode.getNext();

        Node<T> nextNode = currentNode.getNext();
        Node<T> newNode = new Node<>(newValue, nextNode);
        currentNode.setNext(newNode);
        size++;
        modCount++;
    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<>() {

            private Node<T> currentNode = head;
            private Node<T> previousNode;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() { return currentNode.getNext() != null; }

            @Override
            public T next() {
                if (modCount != expectedModCount)
                    throw new ConcurrentModificationException("This iterator is invalid. " +
                            "The list must not be changed during iteration by means other than this iterator");
                if (!hasNext())
                    throw new NoSuchElementException("The iteration has no more elements");

                previousNode = currentNode;
                currentNode = currentNode.getNext();
                return currentNode.getValue();
            }

            @Override
            public void remove() {
                if (currentNode == head)
                    throw new IllegalStateException("next() must be called at least once before remove() can be called");
                if (currentNode == previousNode)
                    throw new IllegalStateException("One element has already been removed since the last next() call");

                Node<T> nextNode = currentNode.getNext();
                previousNode.setNext(nextNode);
                currentNode = previousNode;
                size--;
            }
        };
    }

    int size() { return size; }
}
