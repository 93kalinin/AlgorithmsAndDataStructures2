import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

class SortedListTest {

    private static Random random = new Random(System.currentTimeMillis());

    @Test
    @DisplayName("can maintain its elements sorted")
    void sortWorks() {
        SortedList<Integer> actual = new SortedList<>();
        ArrayList<Integer> expected = new ArrayList<>();
        int elementsAmount = random.nextInt(11) +15;    // 15..25

        for (int i = elementsAmount; i > 0; --i) {
            int someValue = random.nextInt();
            actual.append(someValue);
            expected.add(someValue);
        }
        expected.sort(Comparator.naturalOrder());

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("custom comparator")
    void customComparator() {
        Comparator<Integer> someComparator = (int1, int2) ->
                (int1+int2 % 3) -1;
        SortedList<Integer> actual = new SortedList<>(someComparator);
        ArrayList<Integer> expected = new ArrayList<>();
        int elementsAmount = random.nextInt(11) +15;    // 15..25

        for (int i = elementsAmount; i > 0; --i) {
            int someValue = random.nextInt();
            actual.append(someValue);
            expected.add(someValue);
        }
        expected.sort(someComparator);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("sorts MyLinkedList with selection sort")
    void sortsLinkedList() {
        int elementsAmount = random.nextInt(5) +5;    // 5..9
        ArrayList<Integer> expected = new ArrayList<>(elementsAmount);
        MyLinkedList<Integer> myLinkedList = new MyLinkedList<>();
        random.ints()
                .limit(elementsAmount)
                .forEach(expected::add);
        myLinkedList.addAll(expected);

        SortedList<Integer> actual = new SortedList<>(myLinkedList);
        expected.sort(Comparator.naturalOrder());

        Assertions.assertIterableEquals(expected, actual);
    }
}
