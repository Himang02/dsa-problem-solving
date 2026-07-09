package StacksAndQueues;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/*
You have an empty sequence, and you will be given N queries. Each query is one
of these three types:
    1 x  - Store the element x into the array (push).
    2    - Delete the most recently stored element (pop).
    3    - Print the maximum element in the array.
For each type-3 query, output the maximum element currently in the stack.
It is guaranteed that each query is valid (no pop / max on an empty stack).

Implement processQueries: given the queries, return the list of answers
produced by the type-3 queries, in order.

Each query is encoded as an int[]:
    type 1 -> {1, x}
    type 2 -> {2}
    type 3 -> {3}
*/
public class MaxStack {
    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        // push 4, push 9, max -> 9
        total++; passed += check(
            new int[][]{{1, 4}, {1, 9}, {3}},
            new int[]{9}) ? 1 : 0;

        // max stays at the largest even after smaller elements are pushed/popped
        // push 5,3,8 -> max 8 ; pop 8 -> max 5 ; pop 3 -> max 5
        total++; passed += check(
            new int[][]{{1, 5}, {1, 3}, {1, 8}, {3}, {2}, {3}, {2}, {3}},
            new int[]{8, 5, 5}) ? 1 : 0;

        // single element
        total++; passed += check(
            new int[][]{{1, 7}, {3}},
            new int[]{7}) ? 1 : 0;

        // descending pushes: max is set by the first element and never changes
        total++; passed += check(
            new int[][]{{1, 10}, {3}, {1, 8}, {3}, {1, 6}, {3}, {2}, {3}, {2}, {3}},
            new int[]{10, 10, 10, 10, 10}) ? 1 : 0;

        // ascending pushes: max grows, then shrinks back as we pop
        total++; passed += check(
            new int[][]{{1, 1}, {3}, {1, 5}, {3}, {1, 3}, {3}, {2}, {3}, {2}, {3}},
            new int[]{1, 5, 5, 5, 1}) ? 1 : 0;

        // duplicate maximum: popping one copy must keep the max
        total++; passed += check(
            new int[][]{{1, 4}, {1, 4}, {3}, {2}, {3}},
            new int[]{4, 4}) ? 1 : 0;

        // pop the current max, exposing an older larger element underneath
        // push 6,9,2 -> max 9 ; pop 2 -> max 9 ; pop 9 -> max 6
        total++; passed += check(
            new int[][]{{1, 6}, {1, 9}, {1, 2}, {3}, {2}, {3}, {2}, {3}},
            new int[]{9, 9, 6}) ? 1 : 0;

        // negative numbers
        total++; passed += check(
            new int[][]{{1, -5}, {1, -2}, {3}, {2}, {3}},
            new int[]{-2, -5}) ? 1 : 0;

        // rebuild after emptying: push, pop everything, push again
        total++; passed += check(
            new int[][]{{1, 3}, {2}, {1, 7}, {1, 1}, {3}, {2}, {3}},
            new int[]{7, 7}) ? 1 : 0;

        // no type-3 queries at all -> no output
        total++; passed += check(
            new int[][]{{1, 1}, {1, 2}, {2}},
            new int[]{}) ? 1 : 0;

        System.out.println("--------------------------------------");
        System.out.println("Passed " + passed + " / " + total + " tests");
    }

    // Runs processQueries and reports whether the produced answers match the expected ones.
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

    public static List<Integer> processQueries(int[][] queries) {
        Deque<Integer> stack = new ArrayDeque<>();
        Deque<Integer> maxTillNow = new ArrayDeque<>();

        List<Integer> list = new ArrayList<>();

        for(int q = 0; q < queries.length; q++){
            if(queries[q][0] == 1){
                stack.push(queries[q][1]);
                if(maxTillNow.isEmpty() || maxTillNow.peek() <= queries[q][1]){
                    maxTillNow.push(queries[q][1]);
                }
            }
            else if(queries[q][0] == 2){
                int last = stack.pop();
                if(!maxTillNow.isEmpty() && last == maxTillNow.peek()){
                    maxTillNow.pop();
                }
            }
            else{
                list.add(maxTillNow.peek());
            }
        }
        return list;

    }
}
