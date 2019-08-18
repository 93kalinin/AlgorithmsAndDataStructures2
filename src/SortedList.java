import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class keeps its elements sorted.
 * The order is natural by default but can be changed via comparators.
 * SortedList can be based on a MyLinkedList. Depending on the size of the LinkedList, either selection sort or
 * heapsort will be employed.
 */
public class SortedList<T extends Comparable<T>> extends LinkedList<T> {

    public final Comparator<T> comparator;
    public static final int HEAPSORT_WORTHY_SIZE = 10;

    SortedList()
    { this.comparator = Comparator.naturalOrder(); }

    SortedList(Comparator<T> comparator)
    { this.comparator = comparator; }

    SortedList(LinkedList<T> list) {
        this();
        sortAndAdd(list);
    }

    SortedList(LinkedList<T> list, Comparator<T> comparator) {
        this(comparator);
        sortAndAdd(list);
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

    private void sortAndAdd(LinkedList<T> list) {
        ArrayList<T> unsortedArray = new ArrayList<>(list.size);
        for (T value : list)
            unsortedArray.add(value);

        ArrayList<T> sortedArray = (list.size <= 1) ? unsortedArray
                : (list.size < HEAPSORT_WORTHY_SIZE) ? selectionSort(unsortedArray)
                : heapSort(unsortedArray);
        this.addAll(sortedArray);
    }

    private ArrayList<T> selectionSort(ArrayList<T> array) {
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
        return array;
    }

    private ArrayList<T> heapSort(ArrayList<T> array) {
        for (int heapLastNodeIndex = 0; heapLastNodeIndex < array.size(); ++heapLastNodeIndex) {
            int newItemIndex = heapLastNodeIndex;
            while (newItemIndex != 0) {
                int parentIndex = (newItemIndex -1) /2;
                T newItem = array.get(newItemIndex);
                T parent = array.get(parentIndex);

                if (comparator.compare(newItem, parent) <= 0)
                    break;
                array.set(newItemIndex, parent);
                array.set(parentIndex, newItem);
                newItemIndex = parentIndex;
            }
        }
        for (int heapEndIndex = array.size() -1; heapEndIndex > 0; --heapEndIndex) {
            T biggestValue = array.get(0);
            fixHeap(array, heapEndIndex);
            array.set(heapEndIndex, biggestValue);
        }
        return array;
    }

    private void fixHeap(ArrayList<T> heap, int heapLastNodeIndex) {
        T lastItem = heap.get(heapLastNodeIndex);
        heap.set(0, lastItem);
        int parentIndex = 0;
        T parent = heap.get(parentIndex);

        while (true) {
            int child1Index = (2*parentIndex +1 >= heapLastNodeIndex) ? parentIndex : 2*parentIndex +1;
            int child2Index = (2*parentIndex +2 >= heapLastNodeIndex) ? parentIndex : 2*parentIndex +2;
            T child1 = heap.get(child1Index);
            T child2 = heap.get(child2Index);
            if (comparator.compare(child1, parent) <= 0  &&  comparator.compare(child2, parent) <= 0)
                break;

            T largerChild = (comparator.compare(child1, child2) <= 0) ? child2 : child1;
            int largerChildIndex = (largerChild == child1) ? child1Index : child2Index;
            heap.set(parentIndex, largerChild);
            heap.set(largerChildIndex, parent);
            parentIndex = largerChildIndex;
        }
    }
}