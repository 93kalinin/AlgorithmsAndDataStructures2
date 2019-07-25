import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class BaseListTest {

    /* utilize throughout the tests to make sure its state remains valid after some use */
    private static BaseList<Integer> usedBaseList = new BaseList<>();
    /* use for comparison to ensure proper function of insertion and removal */
    private static List<Integer> referenceLinkedList = new LinkedList<>();
    private static Random random = new Random(System.currentTimeMillis());
    private static final int MIN_SIZE = 15;
    private static final int MAX_SIZE = 25;

    /* make random nonempty BaseList and LinkedList with identical contents and reasonable sizes */
    @BeforeAll
    static void simulateRandomWork() {
        int insertionsCount = MAX_SIZE - MIN_SIZE + random.nextInt(MIN_SIZE +1);
        boolean removalIsPicked = false;    // removals < insertions in any case, so size > 0
        int someValue = random.nextInt();
        usedBaseList.insert(someValue, 0);
        referenceLinkedList.add(0, someValue);

        /* insert items in each iteration and remove items in some iterations */
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
    }

    @Test
    void insertion_at_invalid_index_throws() {
        Executable wrongMove = () -> usedBaseList.insert(0, MAX_SIZE);
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
        Executable wrongMove = () -> usedBaseList.valueAt(MAX_SIZE);
        Assertions.assertThrows(IllegalArgumentException.class, wrongMove);
    }
}
