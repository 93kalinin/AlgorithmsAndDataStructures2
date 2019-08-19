import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.*;

class LinkedListTest {

    private static Random random = new Random(System.currentTimeMillis());

    @Test
    @DisplayName("random insertions and iterator removals")
    void iteratorRemoval() {
        LinkedList<Integer> actual = new LinkedList<>();
        java.util.LinkedList expected = new java.util.LinkedList();
        int insertionsCount = random.nextInt(11) +15;    // 15..25

        for (int i = insertionsCount; i > 0; --i) {
            int validIndex = getRandomInsertionIndex(actual.size());
            int someValue = random.nextInt();
            actual.insert(validIndex, someValue);
            expected.add(validIndex, someValue);
        }

        Iterator<Integer> testIter = actual.iterator();
        Iterator<Integer> stdIter = expected.iterator();
        while (testIter.hasNext()  &&  stdIter.hasNext()) {
            testIter.next();
            stdIter.next();
            if (random.nextBoolean()) {
                testIter.remove();
                stdIter.remove();
            }
        }
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("random insertions and removals")
    void removals() {
        LinkedList<Integer> actual = new LinkedList<>();
        java.util.LinkedList expected = new java.util.LinkedList();
        int insertionsCount = random.nextInt(11) +15;    // 15..25

        for (int i = insertionsCount; i > 0; --i) {
            int validIndex = getRandomInsertionIndex(actual.size());
            int someValue = random.nextInt();
            actual.insert(validIndex, someValue);
            expected.add(validIndex, someValue);
            if (random.nextBoolean()) {
                validIndex = getRandomRemovalIndex(actual.size());
                actual.remove(validIndex);
                expected.remove(validIndex);
            }
        }
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("iterator throws if the list is modified during iteration")
    void iteratorThrowsIfModified() {
        LinkedList<String> testList = new LinkedList<>();
        testList.add("need at least 2 items");
        testList.add("one more");

        Executable wrongMove = () -> {
            for (String ignored : testList)
                testList.add("woops!");
        };

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

    @Test
    @DisplayName("if iterator is currently on the item B, then after list.remove(indexOfB) iterator.remove() throws")
    void iteratorThrowsIfRemoveItemUnderneathIt() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("gone");
        Iterator<String> iterator = linkedList.iterator();
        iterator.next();
        linkedList.remove(0);

        Executable wrongMove = iterator::remove;

        Assertions.assertThrows(ConcurrentModificationException.class, wrongMove);
    }

    private static int getRandomInsertionIndex(int boundInclusive) {
        if (boundInclusive == 0) return 0;
        return random.nextInt(boundInclusive +1);
    }

    private static int getRandomRemovalIndex(int boundExclusive) {
        if (boundExclusive == 0)
            throw new IllegalStateException("A removal attempt on an empty list");
        return random.nextInt(boundExclusive);
    }
}