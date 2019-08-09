import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * This class keeps its elements sorted.
 * The order is natural by default but can be changed via comparators.
 * SortedList can be based on a MyLinkedList. Depending on the size of the LinkedList, either selection sort or heapsort
 * will be employed. NOTE: the original MyLinkedList's nodes will be removed from it and reused in the new instance
 * of SortedList.
 */
public final class SortedList<T extends Comparable<T>> extends MyLinkedList<T> {

    private final Comparator<T> comparator;
    public static final int HEAPSORT_WORTHY_SIZE = 10;

    SortedList()
    { this.comparator = Comparator.naturalOrder(); }

    SortedList(Comparator<T> comparator)
    { this.comparator = comparator; }

    SortedList(MyLinkedList<T> list) {
        this();
        sortAndAdd(list);
    }

    SortedList(MyLinkedList<T> list, Comparator<T> comparator) {
        this(comparator);
        sortAndAdd(list);
    }

    @Override
    public void push(T newValue) {
        int insertionIndex = 0;
        for (T value : this) {
            if (comparator.compare(newValue, value) <= 0) break;
            insertionIndex++;
        }
        super.insert(insertionIndex, newValue);
    }

    @Override
    public void append(T newValue)
    { this.push(newValue); }

    @Override
    public void insert(int insertionIndex, T newValue)
    { throw new UnsupportedOperationException("Random insertion on a sorted list is prohibited. Use append()"); }

    private void sortAndAdd(MyLinkedList<T> list) {
        if (list.size <= 1) {
            // the list is already sorted
        }
        else if (list.size < HEAPSORT_WORTHY_SIZE) {
            ArrayList<T> array = new ArrayList<>(list.size);
            for (T value : list)
                array.add(value);

            for (int unsortedPartIndex = 0; unsortedPartIndex < array.size(); ++unsortedPartIndex) {
                T smallestValue = array.get(unsortedPartIndex);
                int smallestValueIndex = unsortedPartIndex;
                for (int currentIndex = unsortedPartIndex +1; currentIndex < array.size(); ++currentIndex) {
                    T currentValue = array.get(currentIndex);
                    if (comparator.compare(currentValue, smallestValue) < 0) {
                        smallestValue = currentValue;
                        smallestValueIndex = currentIndex;
                    }
                }
                array.set(smallestValueIndex, array.get(unsortedPartIndex));
                array.set(unsortedPartIndex, smallestValue);
            }
            copyArrayToThatList(array, list);
        }
        else {
            ArrayList<T> heap = makeHeap(list);
            for (int heapSize = list.size; heapSize >= 0; --heapSize) {
                this.append(heap.get(0));
                fixHeap(heap, heapSize);
            }
            copyArrayToThatList(heap, list);
        }
        /* original list's nodes are reused at this point and no longer belong to it */
        this.head.next = list.head.next;
        list.head.next.previous = this.head;
        this.tail.previous = list.tail.previous;
        list.tail.previous.next = this.tail;
        list.head.next = null;
        list.tail.previous = null;
    }

    private void copyArrayToThatList(ArrayList<T> array, MyLinkedList<T> list) {
        Iterator<T> heapIterator = array.iterator();
        Node<T> currentNode = list.head.next;
        while (heapIterator.hasNext()) {
            currentNode.value = heapIterator.next();
            currentNode = currentNode.next;
        }
    }

    private ArrayList<T> makeHeap(MyLinkedList<T> list) {
        Iterator<T> iterator = list.iterator();
        ArrayList<T> heap = new ArrayList<>(list.size);

        for (int currentIndex = 0; currentIndex < list.size; ++currentIndex) {
            heap.set(currentIndex, iterator.next());
            int newItemIndex = currentIndex;
            while (newItemIndex != 0) {
                int parentIndex = (newItemIndex -1) / 2;
                T newItem = heap.get(newItemIndex);
                T parent = heap.get(parentIndex);

                if (comparator.compare(newItem, parent) <= 0)
                    break;
                heap.set(newItemIndex, parent);
                heap.set(parentIndex, newItem);
                newItemIndex = parentIndex;
            }
        }
        return heap;
    }

    private void fixHeap(ArrayList<T> heap, int heapSize) {
        T lastItem = heap.get(heapSize -1);
        heap.set(0, lastItem);
        int parentIndex = 0;
        T parent = heap.get(parentIndex);

        while (true) {
            int child1Index = (2*parentIndex +1 >= heapSize) ? parentIndex : 2*parentIndex +1;
            int child2Index = (2*parentIndex +2 >= heapSize) ? parentIndex : 2*parentIndex +2;
            T child1 = heap.get(child1Index);
            T child2 = heap.get(child2Index);
            T largerChild = (comparator.compare(child1, child2) <= 0) ? child2 : child1;
            int largerChildIndex = heap.indexOf(largerChild);

            if (comparator.compare(child1, parent) <= 0  &&  comparator.compare(child2, parent) <= 0)
                break;
            heap.set(parentIndex, largerChild);
            heap.set(largerChildIndex, parent);
            parentIndex = largerChildIndex;
        }
    }
}