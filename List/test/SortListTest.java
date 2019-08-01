import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class SortListTest {

    private static Random random = new Random(System.currentTimeMillis());

    @Test
    @DisplayName("values are sorted correctly")
    void sortingWorks() {
        SortList<Integer> testList = new SortList<>();
        LinkedList<Integer> referenceList = new LinkedList<>();

        for (int ignore = random.nextInt(15) + 10; ignore > 0; --ignore) {
            int nextInt = random.nextInt();
            testList.add(nextInt);
            referenceList.add(nextInt);
        }
        referenceList.sort(Comparator.naturalOrder());

        Assertions.assertIterableEquals(referenceList, testList);
    }
}