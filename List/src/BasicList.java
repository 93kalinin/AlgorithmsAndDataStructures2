import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A minimal viable implementation of a list.
 * Search and removal can be done via its iterator.
 * Allows to store null references.
 */
class BasicList<T> implements Iterable<T> {

    final Node<T> head = new Node<>(null, null);
    int modCount;
    int size;

    static class Node<T> {

        final T value;
        private Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        Node<T> getNext() { return next; }
        void setNext(Node<T> next) { this.next = next; }
    }

    class BasicListIterator<E> implements Iterator<T> {

        Node<T> currentNode = head;
        Node<T> previousNode;
        int expectedModCount = modCount;

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
            return currentNode.value;
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
    }

    BasicList() {}
    BasicList(Iterable<T> collection) { addAll(collection); }
    void add(T newValue) { insert(0, newValue); }
    void addAll(Iterable<T> collection) { collection.forEach(this::add); }
    int size() { return size; }

    void insert(int insertionIndex, T newValue) {
        Node<T> left = getPreviousNode(insertionIndex);
        Node<T> right = left.getNext();
        Node<T> middle = new Node<>(newValue, right);
        left.setNext(middle);
        size++;
        modCount++;
    }

    @Override
    public Iterator<T> iterator() { return new BasicListIterator<T>(); }

    Node<T> getPreviousNode(int index) {
        checkIndex(index);
        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < index -1; ++currentNodeIndex)
            currentNode = currentNode.getNext();
        return currentNode;
    }

    void checkIndex(int index) {
        if (index < 0  ||  index > size)
            throw new IllegalArgumentException("Invalid index");
    }
}