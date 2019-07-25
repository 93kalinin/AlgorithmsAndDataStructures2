import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class BaseListTest {

    // utilize throughout the tests to make sure its state remains valid after some use
    private static BaseList<Integer> usedBaseList = new BaseList<>();
    // use for comparison to ensure proper function of insertion and removal
    private static List<Integer> referenceLinkedList = new LinkedList<>();
    private static BaseList<Integer> emptyBaseList = new BaseList<>();
    private static Random random = new Random(System.currentTimeMillis());

    // make random nonempty BaseList and LinkedList with identical contents and reasonable sizes
    @BeforeAll
    static void simulateRandomWork() {
        int insertionsCount = random.nextInt(11) + 15;    // 15..25
        boolean removalIsPicked = false;    // removals < insertions in any case, so size > 0
        int someValue = random.nextInt();
        usedBaseList.insert(someValue, 0);
        referenceLinkedList.add(0, someValue);

        // insert items in each iteration and remove items in some iterations
        while (insertionsCount != 0) {
            int validIndex = random.nextInt(usedBaseList.size());
            someValue = random.nextInt();

            usedBaseList.insert(someValue, validIndex);
            referenceLinkedList.add(validIndex, someValue);
            insertionsCount--;

            if (removalIsPicked  &&  usedBaseList.size() != 0) {
                usedBaseList.remove(validIndex);
                referenceLinkedList.remove(validIndex);
            }
            removalIsPicked = random.nextBoolean();
        }
    }

    @Test
    void insertions_and_removals_work_fine() {
        Assertions.assertIterableEquals(referenceLinkedList, usedBaseList);
        for (int i : usedBaseList) System.out.print(i + " ");
        System.out.println();
        for (int i : referenceLinkedList) System.out.print(i + " ");

    }

    @Test
    void insertion_at_invalid_index_throws() {
        Executable wrongMove = () -> usedBaseList.insert(0, usedBaseList.size() +1);
        Assertions.assertThrows(IllegalArgumentException.class, wrongMove);
    }

    @Test
    void can_find_present_value() {
        int expectedIndex = 0;
        Integer presentValue = referenceLinkedList.get(expectedIndex);
        Assertions.assertEquals(expectedIndex, usedBaseList.indexOf(presentValue));
    }

    @Test
    void cant_find_absent_value() {
        int expectedIndex = -1;
        int absentValue = Integer.MAX_VALUE;    // an extremely low chance of a false fail
        Assertions.assertEquals(expectedIndex, usedBaseList.indexOf(absentValue));
    }

    @Test
    void can_find_value_by_index() {
        Assertions.assertEquals(referenceLinkedList.get(0), usedBaseList.valueAt(0));
    }

    @Test
    void throws_if_search_index_is_invalid() {
        Executable wrongMove = () -> usedBaseList.valueAt(usedBaseList.size());
        Assertions.assertThrows(IllegalArgumentException.class, wrongMove);
    }

    @Test
    void cant_find_value_if_empty() {
        int expectedIndex = -1;
        Assertions.assertEquals(expectedIndex, emptyBaseList.indexOf(0));
    }

    @Test
    void throws_if_empty_and_search_index_is_invalid() {
        Executable wrongMove = () -> emptyBaseList.valueAt(emptyBaseList.size());
        Assertions.assertThrows(IllegalArgumentException.class, wrongMove);
    }

    @Test
    void insertion_at_invalid_index_throws_if_empty() {
        Executable wrongMove = () -> emptyBaseList.insert(0, emptyBaseList.size() +1);
        Assertions.assertThrows(IllegalArgumentException.class, wrongMove);
    }

    @Test
    void insertion_of_null_throws() {
        Executable wrongMove = () -> emptyBaseList.insert(null, emptyBaseList.size());
        Assertions.assertThrows(IllegalArgumentException.class, wrongMove);
    }

    @Test
    void removal_returns_removed_value() {
        Assertions.assertEquals(usedBaseList.remove(0), referenceLinkedList.remove(0));
    }

    @Test
    void iterator_throws_if_modify_list() {
        BaseList<String> newList = new BaseList<>();
        newList.insert("first", 0);
        newList.insert("second", 0);
        Executable wrongMove = () -> {
            for (String ignored : newList) newList.remove(0);
        };
        Assertions.assertThrows(ConcurrentModificationException.class, wrongMove);
    }
}