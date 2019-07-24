import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Serves as a base class for more sophisticated types of lists.
 */
class SimpleList<T> implements Iterable<T> {

    static class Node<T> {

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
    private boolean isModifiedDuringIteration = false;
    private int size;

    //TODO: requiredIndex -1 -- previousNodeIndex (previous?)

    public void insertAt(T newValue, int insertionIndex) {

        if (newValue == null)
            throw new IllegalArgumentException("Null values are not allowed.");
        if (insertionIndex < 0 || insertionIndex > size)
            throw new IllegalArgumentException("Invalid index.");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < insertionIndex -1; ++currentNodeIndex)
            currentNode = currentNode.getNext();

        Node<T> nextNode = currentNode.getNext();
        Node<T> newNode = new Node<>(newValue, nextNode);
        currentNode.setNext(newNode);
        size++;
        isModifiedDuringIteration = true;
    }

    public int indexOf(T targetValue) {

        int valueNotFound = -1;
        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < size; ++currentNodeIndex) {
            currentNode = currentNode.getNext();
            if (currentNode.getValue() == targetValue) return currentNodeIndex;
        }
        return valueNotFound;
    }

    public T valueOf(int requiredIndex) {  //TODO: implement a variant that retains state and use it in iterator and indexOf?

        if (requiredIndex >= size)
            throw new IllegalArgumentException("Invalid index");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < requiredIndex; ++currentNodeIndex)
            currentNode = currentNode.getNext();

        return currentNode.getValue();
    }

    public T remove(int requiredIndex) {

        if (requiredIndex >= size)
            throw new IllegalArgumentException("Invalid index");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex < requiredIndex -1; ++currentNodeIndex)
            currentNode = currentNode.getNext();

        Node<T> nextNode = currentNode.getNext();
        Node<T> newNextNode = nextNode.getNext();
        currentNode.setNext(newNextNode);
        size--;
        isModifiedDuringIteration = true;
        return nextNode.getValue();
    }

    @Override
    public Iterator<T> iterator() {

        isModifiedDuringIteration = false;

        return new Iterator<T>() {

            private Node<T> currentNode = head;

            @Override
            public boolean hasNext() { return currentNode.getNext() != null; }

            @Override
            public T next() {

                if (isModifiedDuringIteration)
                    throw new ConcurrentModificationException("Invalid iterator: the list has been modified");

                currentNode = currentNode.getNext();
                return hasNext() ? currentNode.getValue() : null;
            }
        };
    }
}
