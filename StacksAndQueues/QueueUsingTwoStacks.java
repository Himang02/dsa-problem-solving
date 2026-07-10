package StacksAndQueues;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/*
Implement a FIFO queue using only two stacks. The queue must support:
    enqueue(x) - add x to the back of the queue
    dequeue()  - remove and return the element at the front
    front()    - return (without removing) the element at the front
    isEmpty()  - whether the queue has no elements

You may only use standard stack operations (push / pop / peek / isEmpty) on the
two internal stacks. Fill in the Queue class below.

The harness drives your Queue with a list of queries encoded as int[]:
    type 1 -> {1, x}  enqueue x
    type 2 -> {2}     dequeue, recording the removed element
    type 3 -> {3}     front, recording the front element
It is guaranteed dequeue/front are never called on an empty queue.
*/
public class QueueUsingTwoStacks {

    // ==== YOUR CODE: implement these four methods using the two stacks ====
    static class Queue {
        private final Deque<Integer> inStack = new ArrayDeque<>();
        private final Deque<Integer> outStack = new ArrayDeque<>();

        public void enqueue(int x) {
            while(!inStack.isEmpty()){
                outStack.push(inStack.pop());
            }
            inStack.push(x);
            while(!outStack.isEmpty()){
                inStack.push(outStack.pop());
            }
        }

        public int dequeue() {
            return inStack.pop();
        }

        public int front() {
            return inStack.peek();
        }

        public boolean isEmpty() {
            return inStack.isEmpty();
        }
    }
    // =====================================================================

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        // enqueue 10,20 ; front->10, dequeue->10, front->20, dequeue->20
        total++; passed += check(
            new int[][]{{1, 10}, {1, 20}, {3}, {2}, {3}, {2}},
            new int[]{10, 10, 20, 20}) ? 1 : 0;

        // interleaved: enq1, deq->1, enq2, enq3, deq->2, deq->3 (FIFO preserved)
        total++; passed += check(
            new int[][]{{1, 1}, {2}, {1, 2}, {1, 3}, {2}, {2}},
            new int[]{1, 2, 3}) ? 1 : 0;

        // single element: front then dequeue
        total++; passed += check(
            new int[][]{{1, 5}, {3}, {2}},
            new int[]{5, 5}) ? 1 : 0;

        // forces a transfer while inStack still has newer items queued behind
        // enq1,2, deq->1, enq3,4, deq->2, deq->3, deq->4
        total++; passed += check(
            new int[][]{{1, 1}, {1, 2}, {2}, {1, 3}, {1, 4}, {2}, {2}, {2}},
            new int[]{1, 2, 3, 4}) ? 1 : 0;

        // front must NOT change when new items are enqueued behind it
        // enq7, front->7, enq8, front->7, deq->7, front->8
        total++; passed += check(
            new int[][]{{1, 7}, {3}, {1, 8}, {3}, {2}, {3}},
            new int[]{7, 7, 7, 8}) ? 1 : 0;

        // refill after the queue drains empty
        // enq1, deq->1, enq9, enq4, deq->9, front->4, deq->4
        total++; passed += check(
            new int[][]{{1, 1}, {2}, {1, 9}, {1, 4}, {2}, {3}, {2}},
            new int[]{1, 9, 4, 4}) ? 1 : 0;

        // duplicate values keep correct FIFO order
        total++; passed += check(
            new int[][]{{1, 3}, {1, 3}, {2}, {2}},
            new int[]{3, 3}) ? 1 : 0;

        // negatives and a longer run
        total++; passed += check(
            new int[][]{{1, -1}, {1, -2}, {1, -3}, {2}, {3}, {2}, {2}},
            new int[]{-1, -2, -2, -3}) ? 1 : 0;

        // no dequeue/front queries -> no output
        total++; passed += check(
            new int[][]{{1, 1}, {1, 2}},
            new int[]{}) ? 1 : 0;

        System.out.println("--------------------------------------");
        System.out.println("Passed " + passed + " / " + total + " tests");
    }

    // Drives the Queue with the queries and collects outputs of dequeue/front.
    private static List<Integer> processQueries(int[][] queries) {
        Queue q = new Queue();
        List<Integer> out = new ArrayList<>();
        for (int[] query : queries) {
            if (query[0] == 1) {
                q.enqueue(query[1]);
            } else if (query[0] == 2) {
                out.add(q.dequeue());
            } else {
                out.add(q.front());
            }
        }
        return out;
    }

    private static boolean check(int[][] queries, int[] expected) {
        List<Integer> actualList = processQueries(queries);
        int[] actual = actualList == null
            ? new int[]{}
            : actualList.stream().mapToInt(Integer::intValue).toArray();
        boolean ok = Arrays.equals(actual, expected);
        System.out.println(
            (ok ? "PASS" : "FAIL") +
            " | queries=" + Arrays.deepToString(queries) +
            " | expected=" + Arrays.toString(expected) +
            " actual=" + Arrays.toString(actual));
        return ok;
    }
}
