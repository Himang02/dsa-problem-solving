package StacksAndQueues;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/*
Given a non-empty array A, for each element A[i] compute how many subarrays have
A[i] as their MINIMUM element. Return the counts as an array (result[i] = count
for A[i]).

Constraints: N <= 1e5, A[i] <= 1e5.

Idea (monotonic stack):
  For index i, count = (# choices of left boundary) * (# choices of right boundary),
  where A[i] stays the minimum. That is:
      left[i]  = i - (index of previous element that is STRICTLY smaller)
      right[i] = (index of next element that is smaller-OR-EQUAL) - i
      count[i] = left[i] * right[i]
  Using strict-less on one side and less-or-equal on the other is what prevents
  double-counting subarrays that contain equal minimum values.

NOTE: a single count can reach ~ (N/2)^2 ~ 2.5e9, which overflows int, so the
result is long[].
*/
public class SubarrayMinCount {

    public static long[] countAsMinimum(int[] A) {

        int[] left = new int[A.length];
        int[] right = new int[A.length];

        Deque<int[]> stack = new ArrayDeque<>();
        for(int i = A.length - 1; i >= 0; i--){
            int[] lastPopped = new int[2];
            while(!stack.isEmpty() && stack.peek()[0] >= A[i]){
                lastPopped = stack.pop();
            }
            if(stack.isEmpty()){
                right[i] = A.length - i;
            }
            else{
                right[i] = stack.peek()[1] - i;
            }
            if(stack.isEmpty() || lastPopped == null || lastPopped[0] != A[i]){
                stack.push(new int[]{A[i], i});
            }
            else{
                stack.push(lastPopped);
            }
        }

        stack = new ArrayDeque<>();
        for(int i = 0; i < A.length; i++){
            int[] lastPopped = new int[2];
            while(!stack.isEmpty() && stack.peek()[0] >= A[i]){
                lastPopped = stack.pop();
            }
            if(stack.isEmpty()){
                left[i] = i;
            }
            else{
                left[i] = i - stack.peek()[1] - 1;
            }
            if(stack.isEmpty() || lastPopped == null || lastPopped[0] != A[i]){
                stack.push(new int[]{A[i], i});
            }
            else{
                stack.push(lastPopped);
            }
        }

        long[] ans = new long[A.length];
        
        for(int i = 0; i < A.length; i++){
            ans[i] = right[i] * (left[i] + 1); 
        }

        return ans;
    }

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        // [3] alone; 1 is the global min and dominates the middle
        total++; passed += check(new int[]{3, 1, 2}, new long[]{1, 4, 1}) ? 1 : 0;

        // all equal: attribution walks left-to-right -> 1,2,3 (sum = 6 = 3*4/2)
        total++; passed += check(new int[]{2, 2, 2}, new long[]{1, 2, 3}) ? 1 : 0;

        // strictly increasing: first element is min of the most subarrays
        total++; passed += check(new int[]{1, 2, 3, 4}, new long[]{4, 3, 2, 1}) ? 1 : 0;

        // strictly decreasing: last element is min of the most subarrays
        total++; passed += check(new int[]{4, 3, 2, 1}, new long[]{1, 2, 3, 4}) ? 1 : 0;

        // single element
        total++; passed += check(new int[]{5}, new long[]{1}) ? 1 : 0;

        // valley shape with duplicate walls around a single global min
        total++; passed += check(new int[]{2, 1, 2}, new long[]{1, 4, 1}) ? 1 : 0;

        // mixed peaks and valleys
        total++; passed += check(new int[]{1, 3, 2, 4}, new long[]{4, 1, 4, 1}) ? 1 : 0;

        // tie-heavy: equal values on BOTH sides of the min (double-count stress test)
        // sum = 15 = 5*6/2
        total++; passed += check(new int[]{2, 2, 1, 2, 2}, new long[]{1, 2, 9, 1, 2}) ? 1 : 0;

        // two equal elements
        total++; passed += check(new int[]{7, 7}, new long[]{1, 2}) ? 1 : 0;

        System.out.println("--------------------------------------");
        System.out.println("Passed " + passed + " / " + total + " tests");
    }

    private static boolean check(int[] A, long[] expected) {
        long[] actual = countAsMinimum(A);
        boolean ok = Arrays.equals(actual, expected);
        long sum = 0;
        for (long v : actual) sum += v;
        long n = A.length;
        System.out.println(
            (ok ? "PASS" : "FAIL") +
            " | A=" + Arrays.toString(A) +
            " | expected=" + Arrays.toString(expected) +
            " actual=" + Arrays.toString(actual) +
            " | sum=" + sum + " (should be " + (n * (n + 1) / 2) + ")");
        return ok;
    }
}
