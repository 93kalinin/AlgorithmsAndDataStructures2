import java.util.*;

/**
 * A doubly linked list with sentinel nodes at the beginning and the end.
 * It can be used as a standalone class or extended by other classes. Null values are not allowed.
 */
//TODO: use bounded wildcard? (Bloch, item 31). implement ListIterator?
public class LinkedList<T> implements Iterable<T> {

    protected final Node<T> head = new Node<>(null);
    protected final Node<T> tail = new Node<>(null);
    { head.next = tail;  tail.previous = head; }
    protected int modCountForCachedHash;
    protected int cachedHash;
    protected int modCount;
    protected int size;

    protected static class Node<E> {

        protected E value;
        protected Node<E> next;
        protected Node<E> previous;

        Node(E value)
        { this.value = value; }

        @Override
        public String toString() {
            return (this.next == null) ? " tail"
                    : (this.previous == null) ? "head "
                    : value.toString();
        }
    }

    protected class ListIterator implements Iterator<T> {

        Node<T> currentNode = head;
        int expectedModCount = modCount;
        boolean removalAlreadyOccurred;

        @Override
        public boolean hasNext()
        { return currentNode.next.value != null; }

        @Override
        public T next() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException("This iterator is invalid. The list must not be changed " +
                        "during iteration by means other than this iterator");
            if (!hasNext())
                throw new NoSuchElementException("This iteration has no more elements");

            removalAlreadyOccurred = false;
            currentNode = currentNode.next;
            return currentNode.value;
        }

        @Override
        public void remove() {
            if (currentNode == head)
                throw new IllegalStateException("next() must be called at least once before remove() can be called");
            if (removalAlreadyOccurred)
                throw new IllegalStateException("One element has already been removed since the last next() call");
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException("This iterator is invalid. The list must not be changed " +
                        "during iteration by means other than this iterator");

            currentNode.previous.next = currentNode.next;
            currentNode.next.previous = currentNode.previous;
            removalAlreadyOccurred = true;
            expectedModCount++;
            modCount++;
            size--;
        }
    }

    @Override
    public Iterator<T> iterator()
    { return new ListIterator(); }

    @Override
    public boolean equals(Object that) {
        if (that == this) return true;
        if (!(that instanceof LinkedList)) return false;
        LinkedList otherLinkedList = (LinkedList) that;
        if (otherLinkedList.size() != this.size()) return false;

        Iterator thisIterator = this.iterator();
        Iterator thatIterator = otherLinkedList.iterator();
        while (thisIterator.hasNext()  &&  thatIterator.hasNext())
            if (!thisIterator.next().equals(thatIterator.next())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        if (modCountForCachedHash == modCount)
            return cachedHash;
        cachedHash = 1;
        for (T value : this)
            cachedHash = 31*cachedHash + value.hashCode();
        modCountForCachedHash = modCount;
        return cachedHash;
    }

    public void insert(int index, T newValue) {
        Objects.requireNonNull(newValue, "Null values are prohibited");
        if (index < 0  ||  index > size)
            throw new IllegalArgumentException("Invalid index: " + index);

        Node<T> right = getNodeByIndex(index);
        Node<T> left = right.previous;
        Node<T> newNode = new Node<>(newValue);

        left.next = newNode;
        newNode.previous = left;
        right.previous = newNode;
        newNode.next = right;
        size++;
        modCount++;
    }

    public T removeAndReturn(int index) {
        Node<T> removed = getNodeByIndex(index);
        Node<T> left = removed.previous;
        Node<T> right = removed.next;

        left.next = right;
        right.previous = left;
        size--;
        modCount++;
        return removed.value;
    }

    public T get(int index)
    { return getNodeByIndex(index).value; }

    public void add(T newValue)
    { insert(size, newValue); }

    public void addAll(Iterable<T> collection)
    { collection.forEach(this::add); }

    public int size()
    { return size; }

    protected Node<T> getNodeByIndex(int index) {
        if (index < 0  ||  index >= size)
            throw new IllegalArgumentException("Invalid index: " + index);

        Node<T> currentNode;
        if (index < size/2) {
            currentNode = head;
            for (int currentNodeIndex = -1; currentNodeIndex < index; ++currentNodeIndex)
                currentNode = currentNode.next;
        } else {
            currentNode = tail;
            for (int currentNodeIndex = size; currentNodeIndex > index; --currentNodeIndex)
                currentNode = currentNode.previous;
        }
        return currentNode;
    }
}