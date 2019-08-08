import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * This class keeps its elements sorted.
 * The order is natural by default but can be changed via comparators.
 * Its instances can be based on a LinkedList. Heapsort will be employed to sort LinkedList's elements.
 */
public final class SortedList<T extends Comparable<T>> extends LinkedList<T> {

    private final Comparator<T> comparator;

    SortedList()
    { this.comparator = Comparator.naturalOrder(); }

    SortedList(Comparator<T> comparator)
    { this.comparator = comparator; }

    SortedList(LinkedList<T> list) {
        this();
        sortAndAddAll(list);
    }

    SortedList(LinkedList<T> list, Comparator<T> comparator) {
        this(comparator);
        sortAndAddAll(list);
    }

    @Override
    public void add(T newValue) {
        int insertionIndex = 0;
        for (T value : this) {
            if (comparator.compare(newValue, value) <= 0) break;
            insertionIndex++;
        }
        super.insert(insertionIndex, newValue);
    }

    @Override
    public void insert(int insertionIndex, T newValue)
    { throw new UnsupportedOperationException("Random insertion on a sorted list is prohibited. Use add()"); }

    private void sortAndAddAll(LinkedList<T> list) {
        ArrayList<T> heap = buildHeap(list);
        for (int heapSize = list.size; heapSize >= 0; --heapSize) {
            this.add(heap.get(0));
            fixHeap(heap, heapSize);
        }
    }

    private ArrayList<T> buildHeap(LinkedList<T> list) {
        Iterator<T> iterator = list.iterator();
        ArrayList<T> heap = new ArrayList<>(list.size());

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