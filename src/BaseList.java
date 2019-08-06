import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A basic implementation of a singly linked list with sentinel nodes at the beginning and the end.
 * It can be used as a standalone class or extended by other classes. Null values are not allowed.
 */
public class BaseList<T> implements Iterable<T> {

    protected final Node<T> tail = new Node<>(null, null);
    protected final Node<T> head = new Node<>(null, tail);
    protected int modCountForCachedHash;
    protected int cachedHash;
    protected int modCount;
    protected int size;

    protected static class Node<T> {

        private final T value;
        private Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return (this.next == null) ? "tail"
                    : (this.value == null) ? "head"
                    : this.value.toString();
        }
    }

    protected class BasicListIterator<E> implements Iterator<T> {

        Node<T> currentNode = head;
        Node<T> previousNode;
        int expectedModCount = modCount;

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

            previousNode = currentNode;
            currentNode = currentNode.next;
            return currentNode.value;
        }

        @Override
        public void remove() {
            if (currentNode == head)
                throw new IllegalStateException("next() must be called at least once before remove() can be called");
            if (currentNode == previousNode)
                throw new IllegalStateException("One element has already been removed since the last next() call");

            previousNode.next = currentNode.next;
            currentNode = previousNode;
            size--;
        }
    }

    @Override
    public Iterator<T> iterator()
    { return new BasicListIterator<T>(); }

    @Override
    public boolean equals(Object that) {
        if (that == this) return true;
        if (!(that instanceof BaseList)) return false;
        BaseList otherBaseList = (BaseList) that;
        if (otherBaseList.size() != this.size()) return false;

        Iterator thisIterator = this.iterator();
        Iterator thatIterator = otherBaseList.iterator();
        while (thisIterator.hasNext()  &&  thatIterator.hasNext())
            if (!thisIterator.next().equals(thatIterator.next())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        if (modCountForCachedHash == modCount)
            return cachedHash;
        for (T value : this)
            cachedHash += value.hashCode();
        modCountForCachedHash = modCount;
        return cachedHash;
    }

    public void insert(int insertionIndex, T newValue) {
        Objects.requireNonNull(newValue, "Null values are prohibited");
        Node<T> left = getNodePrecedingTo(insertionIndex);
        Node<T> right = left.next;
        left.next = new Node<>(newValue, right);
        size++;
        modCount++;
    }

    public T remove(int index) {
        Node<T> left = getNodePrecedingTo(index);
        Node<T> removedNode = left.next;
        Node<T> right = removedNode.next;
        left.next = right;
        size--;
        modCount++;
        return removedNode.value;
    }

    public void add(T newValue)
    { insert(0, newValue); }

    public void addAll(Iterable<T> collection)
    { collection.forEach(this::add); }

    public int size()
    { return size; }

    protected Node<T> getNodePrecedingTo(int index) {
        if (index < 0  ||  index > size)
            throw new IllegalArgumentException("Invalid index");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < index -1; ++currentNodeIndex)
            currentNode = currentNode.next;
        return currentNode;
    }
}