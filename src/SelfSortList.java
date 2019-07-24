import java.util.Iterator;
/**
 * Frequently searched items in this list are put closer to the beginning of it,
 * thereby minimizing the average search time.
 * Null values are not allowed.  TODO: why?
 */
class SelfSortList<T> implements SimpleList<T> {

    /* use a sentinel node to retain valid state while the list is empty, which simplifies the implementation */
    private final Node<T> head = new Node<>(null, null);
    private int size;

    public void insertAt(T newValue, int insertionIndex) {

        if (newValue == null)
            throw new IllegalArgumentException("Null values are not allowed.");
        if (insertionIndex < 0 || insertionIndex > size)
            throw new IllegalArgumentException("Invalid index.");

        Node<T> currentNode = head;
        for (int currentNodeIndex = -1; currentNodeIndex == insertionIndex -1; ++currentNodeIndex)
            currentNode = currentNode.getNext();

        Node<T> nextNode = currentNode.getNext();
        Node<T> newNode = new Node<>(newValue, nextNode);
        currentNode.setNext(newNode);
        size++;
    }

    public int indexOf(T targetValue) {
        return 0;
    }

    public T valueOf(int index) {
        return null;
    }

    public T remove(int index) {
        return null;
    }


    public Iterator<T> iterator() {
        return null;
    }
}
