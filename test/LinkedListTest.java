import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.*;

class LinkedListTest {

    private static Random random = new Random(System.currentTimeMillis());

    @Test
    @DisplayName("random insertions and removals work fine")
    void randomInsertionsAndRemovals() {
        LinkedList<Integer> testList = new LinkedList<>();
        java.util.LinkedList stdList = new java.util.LinkedList();
        int insertionsCount = random.nextInt(11) + 15;    // 15..25

        for (int i = insertionsCount; i > 0; --i) {
            int validIndex = getValidRandomIndex(testList.size());
            int someValue = random.nextInt();
            testList.insert(validIndex, someValue);
            stdList.add(validIndex, someValue);
        }

        Iterator<Integer> testIter = testList.iterator();
        Iterator<Integer> stdIter = stdList.iterator();
        while (testIter.hasNext()  &&  stdIter.hasNext()) {
            testIter.next();
            stdIter.next();
            if (random.nextBoolean()) {
                testIter.remove();
                stdIter.remove();
            }
        }
        Assertions.assertIterableEquals(stdList, testList);
    }

    @Test
    @DisplayName("iterator throws if the list is modified during iteration")
    void iteratorThrowsIfModified() {
        LinkedList<String> testList = new LinkedList<>();
        testList.add("need at least 2 items");
        testList.add("one more");

        Executable wrongMove = () -> { for (String ignored : testList) testList.add("woops!"); };
        Assertions.assertThrows(ConcurrentModificationException.class, wrongMove);
    }

    @Test
    @DisplayName("iterator throws if remove() is called before next()")
    void iteratorThrowsOnEarlyRemove() {
        LinkedList<String> testList = new LinkedList<>();
        testList.add("something to remove");
        Iterator<String> iter = testList.iterator();

        Assertions.assertThrows(IllegalStateException.class, iter::remove);
    }

    @Test
    @DisplayName("iterator throws if remove() is called twice in a row")
    void iteratorThrowsOnDoubleRemove() {
        LinkedList<String> list = new LinkedList<>();
        list.add("nonempty list");
        list.add("one more");
        Iterator<String> iter = list.iterator();

        iter.next();
        iter.remove();

        Executable wrongMove = iter::remove;
        Assertions.assertThrows(IllegalStateException.class, wrongMove);
    }

    private static int getValidRandomIndex(int boundInclusive) {
        if (boundInclusive < 0)
            throw new IllegalArgumentException("Index is a natural number");
        if (boundInclusive == 0) return 0;
        return random.nextInt(boundInclusive +1);
    }
}