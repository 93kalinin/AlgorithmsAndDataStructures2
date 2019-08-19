import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class PriorityQueueTest {

    private static Random random = new Random(System.currentTimeMillis());

    @Test
    @DisplayName("offering and polling")
    void enqueueDequeue() {
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(Integer::intValue));
        ArrayList<Integer> expected = new ArrayList<>(25);
        ArrayList<Integer> actual = new ArrayList<>(25);

        random.ints(random.nextInt(11) +15)
                .peek(expected::add)
                .forEach(queue::offer);
        expected.sort(Comparator.comparingInt(Integer::intValue));
        while (!queue.isEmpty())
            actual.add(queue.poll());

        Assertions.assertIterableEquals(actual, expected);
    }

    @Test
    @DisplayName("merging queues")
    void merge() {
        PriorityQueue<Integer> queue1 = new PriorityQueue<>(Comparator.comparingInt(Integer::intValue));
        PriorityQueue<Integer> queue2 = new PriorityQueue<>(Comparator.comparingInt(Integer::intValue));
        ArrayList<Integer> expected = new ArrayList<>(50);
        ArrayList<Integer> actual = new ArrayList<>(50);

        random.ints(random.nextInt(11) +15)
                .peek(expected::add)
                .forEach(queue1::offer);
        random.ints(random.nextInt(11) +15)
                .peek(expected::add)
                .forEach(queue2::offer);
        expected.sort(Comparator.comparingInt(Integer::intValue));
        queue1.offer(queue2);
        while (!queue1.isEmpty())
            actual.add(queue1.poll());

        Assertions.assertIterableEquals(actual, expected);
    }

    @Test
    @DisplayName("merging with empty queue")
    void mergeEmpty() {
        PriorityQueue<Integer> nonemptyQueue = new PriorityQueue<>(Comparator.comparingInt(Integer::intValue));
        PriorityQueue<Integer> emptyQueue = new PriorityQueue<>(Comparator.comparingInt(Integer::intValue));
        ArrayList<Integer> expected = new ArrayList<>(50);
        ArrayList<Integer> actual = new ArrayList<>(50);

        random.ints(random.nextInt(11) +15)
                .peek(expected::add)
                .forEach(nonemptyQueue::offer);
        expected.sort(Comparator.comparingInt(Integer::intValue));

        nonemptyQueue.offer(emptyQueue);
        while (!nonemptyQueue.isEmpty())
            actual.add(nonemptyQueue.poll());

        Assertions.assertIterableEquals(actual, expected);
    }
}
