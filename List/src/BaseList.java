import java.util.ConcurrentModificationException;
import java.util.Iterator;

class BaseList<T> implements Iterable<T> {

    private static class Node<T> {

        private T value;
        private Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        T getValue() { return value; }
        void setValue(T value) { this.value = value; }
        Node<T> getNext() { return next; }
        void setNext(Node<T> next) { this.next = next; }
    }

    /* use a sentinel node to retain valid state while the list is empty, which simplifies the implementation */
    private final Node<T> head = new Node<>(null, null);
    /* ensure that no modification of the list occurred while an iterator is moving through it */
    private boolean isModified = false;
    private int size;

    //TODO: requiredIndex -1 -- previousNodeIndex (previous?)

    void insert(T newValue, int insertionIndex) {
        if (insertionIndex < 0  ||  insertionIndex > size)
            throw new IllegalArgumentException("Invalid index.");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < insertionIndex -1; ++currentNodeIndex)
            currentNode = currentNode.getNext();

        Node<T> nextNode = currentNode.getNext();
        Node<T> newNode = new Node<>(newValue, nextNode);
        currentNode.setNext(newNode);
        size++;
        isModified = true;
    }

    int indexOf(final T targetValue) {
        int valueNotFound = -1;
        if (size == 0) return valueNotFound;

        Node<T> currentNode = head.getNext();
        for (int currentNodeIndex = 0; currentNodeIndex < size; ++currentNodeIndex) {
            if (currentNode.getValue().equals(targetValue)) return currentNodeIndex;
            currentNode = currentNode.getNext();
        }
        return valueNotFound;
    }
    //TODO: implement a variant that retains state and use it in iterator and indexOf?
    T valueAt(int requiredIndex) {
        if (requiredIndex < 0  ||  requiredIndex >= size)
            throw new IllegalArgumentException("Invalid index");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < requiredIndex; ++currentNodeIndex)
            currentNode = currentNode.getNext();

        return currentNode.getValue();
    }

    T remove(int requiredIndex) {
        if (requiredIndex >= size)
            throw new IllegalArgumentException("Invalid index");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < requiredIndex -1; ++currentNodeIndex)
            currentNode = currentNode.getNext();

        Node<T> nextNode = currentNode.getNext();
        Node<T> newNextNode = nextNode.getNext();
        currentNode.setNext(newNextNode);
        size--;
        isModified = true;
        return nextNode.getValue();
    }

    int size() { return size; }

    @Override
    public Iterator<T> iterator() {

        isModified = false;  //fails if get iterator, modify, get another iterator

        return new Iterator<T>() {

            private Node<T> currentNode = head;

            @Override
            public boolean hasNext() { return currentNode.getNext() != null; }

            @Override
            public T next() {
                if (isModified)
                    throw new ConcurrentModificationException("Invalid iterator: the list has been modified");
                currentNode = currentNode.getNext();
                return currentNode != null ? currentNode.getValue() : null;
            }
        };
    }

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
        int hashCode = 1;
        for (T value : this)
            hashCode = 31*hashCode + (value==null ? 0 : value.hashCode());
        return hashCode;
    }
}
