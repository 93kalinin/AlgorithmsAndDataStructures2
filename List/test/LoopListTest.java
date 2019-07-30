import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Iterator;

class LoopListTest {

    private static LoopList<String> list;
    private static StringBuilder actual;

    @BeforeEach
    void prepareList() {
        list = new LoopList<>("second ");
        list.insert(0, "first ");
        list.insert(2, "third ");
        actual = new StringBuilder();
    }

    @Test
    @DisplayName("default loop")
    void allElementsLoop() {
        String expected = "first second third first ";
        Iterator<String> iterator = list.iterator();

        for (int i = list.size +1; i > 0; --i)
            actual.append(iterator.next());

        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    @DisplayName("valid non-default loop")
    void nondefaultLoop() {
        list.loopTailTo(1);
        String expected = "first before_loop_entry_point second after_loop_entry_point third second ";
        list.insert(1, "before_loop_entry_point ");
        list.insert(3, "after_loop_entry_point ");
        Iterator<String> iterator = list.iterator();

        for (int i = list.size +1; i > 0; --i)
            actual.append(iterator.next());

        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    @DisplayName("removal of the only node in the loop throws")
    void invariantThrow() {
        Iterator<String> iterator = list.iterator();

        Executable wrongMove = () -> { while (true) iterator.remove(); };

        Assertions.assertThrows(IllegalStateException.class, wrongMove);
    }

    @Test
    @DisplayName("retracing loop detection")
    void retracingLoopDetection() {
        list.loopTailTo(1);
        Assertions.assertSame(list.tail, list.findTailByRetracing());
    }

    @Test
    @DisplayName("Floyd loop detection")
    void floydLoopDetection() {
        list.loopTailTo(1);
        Assertions.assertSame(list.tail, list.findTailFloyd());
    }
}
