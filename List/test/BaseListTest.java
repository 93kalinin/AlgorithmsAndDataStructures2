import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BaseListTest {

    @Test
    void validInsertions() {
        BaseList<Integer> actual = new BaseList<>();
        List<Integer> expected = new ArrayList<>();
        expected.add(3);
        expected.add(14);
        expected.add(15);    // expected = { 3, 14, 15 }

        actual.insert(3, 0);
        actual.insert(15, 1);
        actual.insert(14, 1);

        Assertions.assertIterableEquals(expected, actual);
    }
}
