public class SortList<T extends Comparable<T>> extends BasicList<T> {

    @Override
    void add(T newValue) {
        int insertionIndex = 0;
        for (T value : this) {
            if (value.compareTo(newValue) >= 0) break;
            insertionIndex++;
        }
        super.insert(insertionIndex, newValue);
    }

    @Override
    void insert(int insertionIndex, T newValue) {
        throw new UnsupportedOperationException("Random insertion on a sorted list is prohibited. Use add()");
    }
}
