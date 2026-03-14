package Kadane;

import java.util.Arrays;

public class KadaneDp {
    public static void main(String[] args){
        int passed = 0;

        // --- All positives: entire array is the answer ---
        passed += check("All positives",                new int[]{1, 2, 3, 4, 5},                15);

        // --- All negatives: best single element ---
        passed += check("All negatives",                new int[]{-3, -1, -4, -2},               -1);

        // --- Single negative element ---
        passed += check("Single negative",              new int[]{-7},                            -7);

        // --- Single positive element ---
        passed += check("Single positive",              new int[]{5},                              5);

        // --- Classic mixed case (Kadane's canonical test) ---
        passed += check("Classic mixed",                new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6);

        // --- Best subarray is in the middle ---
        passed += check("Peak in middle",               new int[]{-5, 3, 6, -2, -4},              9);

        // --- Best subarray is at the start ---
        passed += check("Peak at start",                new int[]{10, -1, -100, 2},               10);

        // --- Best subarray is at the end ---
        passed += check("Peak at end",                  new int[]{-100, -1, 5, 8},                13);

        // --- Single zero ---
        passed += check("Single zero",                  new int[]{0},                              0);

        // --- All zeros ---
        passed += check("All zeros",                    new int[]{0, 0, 0, 0},                     0);

        // --- Mixed with zeros acting as bridges ---
        passed += check("Zero bridge",                  new int[]{-1, 0, -2},                      0);

        // --- Positive island surrounded by negatives ---
        passed += check("Positive island",              new int[]{-10, -5, 7, -8, -3},             7);

        // --- Two equal-sum candidate subarrays ---
        passed += check("Two equal peaks",              new int[]{3, -10, 3},                      3);

        // --- Negative then large positive ---
        passed += check("Neg then large pos",           new int[]{-100, 200},                    200);

        // --- Alternating signs, net positive ---
        passed += check("Alternating net positive",     new int[]{5, -1, 5, -1, 5},              13);

        // --- Alternating signs, net negative ---
        passed += check("Alternating net negative",     new int[]{-5, 1, -5, 1, -5},              1);

        // --- Large negative followed by large positive followed by large negative ---
        passed += check("Sandwich",                     new int[]{-1000, 999, -1000},            999);

        // --- Integer overflow boundary (large values) ---
        passed += check("Large values",                 new int[]{Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2}, Integer.MAX_VALUE - 1);

        // --- Single large negative (Integer.MIN_VALUE) ---
        passed += check("INT_MIN element",              new int[]{Integer.MIN_VALUE},   Integer.MIN_VALUE);

        // --- Subarray of length 1 is optimal among negatives and one zero ---
        passed += check("Negatives with zero",          new int[]{-3, 0, -2},                      0);

        // --- All elements the same negative value ---
        passed += check("Same negative repeated",       new int[]{-4, -4, -4},                    -4);

        // --- All elements the same positive value ---
        passed += check("Same positive repeated",       new int[]{6, 6, 6},                       18);

        System.out.println("\n=============================");
        System.out.printf("  getMaxSum  : %d passed, %d failed%n", passed, 22 - passed);
        System.out.println("=============================");

        // ── getMaxSumSubArrayIndices tests ──────────────────────────────────
        // checkIndices verifies: valid bounds + sum(arr[start..end]) == expectedSum.
        // checkIndicesExact additionally asserts the exact [start, end] pair
        // (only used when the answer is provably unique).
        System.out.println();
        int idxPassed = 0;

        // Unique-answer cases → check exact indices AND sum
        idxPassed += checkIndicesExact("All positives",           new int[]{1, 2, 3, 4, 5},                    15,  0, 4);
        idxPassed += checkIndicesExact("All negatives",           new int[]{-3, -1, -4, -2},                   -1,  1, 1);
        idxPassed += checkIndicesExact("Single negative",         new int[]{-7},                               -7,  0, 0);
        idxPassed += checkIndicesExact("Single positive",         new int[]{5},                                 5,  0, 0);
        idxPassed += checkIndicesExact("Classic mixed",           new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4},    6,  3, 6);
        idxPassed += checkIndicesExact("Peak in middle",          new int[]{-5, 3, 6, -2, -4},                 9,  1, 2);
        idxPassed += checkIndicesExact("Peak at start",           new int[]{10, -1, -100, 2},                 10,  0, 0);
        idxPassed += checkIndicesExact("Peak at end",             new int[]{-100, -1, 5, 8},                  13,  2, 3);
        idxPassed += checkIndicesExact("Single zero",             new int[]{0},                                 0,  0, 0);
        idxPassed += checkIndicesExact("Zero bridge (only zero)", new int[]{-1, 0, -2},                         0,  1, 1);
        idxPassed += checkIndicesExact("Positive island",         new int[]{-10, -5, 7, -8, -3},               7,  2, 2);
        idxPassed += checkIndicesExact("Neg then large pos",      new int[]{-100, 200},                       200,  1, 1);
        idxPassed += checkIndicesExact("Alternating net positive",new int[]{5, -1, 5, -1, 5},                 13,  0, 4);
        idxPassed += checkIndicesExact("Sandwich",                new int[]{-1000, 999, -1000},               999,  1, 1);
        idxPassed += checkIndicesExact("Large values",            new int[]{Integer.MAX_VALUE/2, Integer.MAX_VALUE/2}, Integer.MAX_VALUE-1, 0, 1);
        idxPassed += checkIndicesExact("INT_MIN element",         new int[]{Integer.MIN_VALUE},      Integer.MIN_VALUE,  0, 0);
        idxPassed += checkIndicesExact("Negatives with zero",     new int[]{-3, 0, -2},                         0,  1, 1);
        idxPassed += checkIndicesExact("Same positive repeated",  new int[]{6, 6, 6},                          18,  0, 2);

        // Tie cases → only verify bounds + sum (any valid index pair accepted)
        idxPassed += checkIndices("All zeros (tie)",              new int[]{0, 0, 0, 0},                        0);
        idxPassed += checkIndices("Two equal peaks (tie)",        new int[]{3, -10, 3},                         3);
        idxPassed += checkIndices("Alternating net neg (tie)",    new int[]{-5, 1, -5, 1, -5},                  1);
        idxPassed += checkIndices("Same negative (tie)",          new int[]{-4, -4, -4},                       -4);

        System.out.println("\n=============================");
        System.out.printf("  getMaxSumSubArrayIndices: %d passed, %d failed%n", idxPassed, 22 - idxPassed);
        System.out.println("=============================");
    }

    // ── sum-only check (valid bounds + correct sum, any valid pair) ──────────
    private static int checkIndices(String label, int[] arr, int expectedSum){
        int[] idx = getMaxSumSubArrayIndices(arr);
        String reason = validateIndices(arr, idx, expectedSum);
        boolean ok = reason == null;
        System.out.printf("[%s] %-35s | arr=%-35s | expectedSum=%-6d | indices=%s%s%n",
            ok ? "PASS" : "FAIL", label, Arrays.toString(arr), expectedSum,
            idx == null ? "null" : Arrays.toString(idx),
            ok ? "" : "  REASON: " + reason);
        return ok ? 1 : 0;
    }

    // ── exact-index check (bounds + sum + exact [start,end]) ─────────────────
    private static int checkIndicesExact(String label, int[] arr, int expectedSum, int expStart, int expEnd){
        int[] idx = getMaxSumSubArrayIndices(arr);
        String reason = validateIndices(arr, idx, expectedSum);
        if (reason == null && (idx[0] != expStart || idx[1] != expEnd))
            reason = String.format("expected [%d,%d] got [%d,%d]", expStart, expEnd, idx[0], idx[1]);
        boolean ok = reason == null;
        System.out.printf("[%s] %-35s | arr=%-35s | expectedSum=%-6d | indices=%s%s%n",
            ok ? "PASS" : "FAIL", label, Arrays.toString(arr), expectedSum,
            idx == null ? "null" : Arrays.toString(idx),
            ok ? "" : "  REASON: " + reason);
        return ok ? 1 : 0;
    }

    // Returns null on valid, or an error string describing the failure
    private static String validateIndices(int[] arr, int[] idx, int expectedSum){
        if (idx == null || idx.length != 2)       return "returned null or wrong length";
        if (idx[0] < 0 || idx[1] >= arr.length)  return "indices out of bounds";
        if (idx[0] > idx[1])                      return "start > end";
        int sum = 0;
        for (int i = idx[0]; i <= idx[1]; i++) sum += arr[i];
        if (sum != expectedSum)                   return String.format("subarray sum=%d, expected=%d", sum, expectedSum);
        return null;
    }

    // Returns 1 on pass, 0 on fail (also prints result)
    private static int check(String label, int[] arr, int expected){
        int got = getMaxSumSubarrayHelper(arr);
        boolean ok = got == expected;
        System.out.printf("[%s] %-30s | arr=%-40s | expected=%-12d | got=%-12d%n",
            ok ? "PASS" : "FAIL",
            label,
            Arrays.toString(arr),
            expected,
            got);
        return ok ? 1 : 0;
    }

    public static int getMaxSumSubarrayHelper(int[] arr){

        int[] dp = new int[arr.length];
        Arrays.fill(dp, Integer.MIN_VALUE);
        getMaxSum(0, arr, dp);
        int maxValue = Integer.MIN_VALUE;
        for(int i = 0; i < dp.length; i++){
            maxValue = Math.max(maxValue, dp[i]);
        }
        return maxValue;
    }

    private static int getMaxSum(int index, int[] arr, int[] dp){
        // memory
        if(dp[index] != Integer.MIN_VALUE){
            return dp[index];
        }

        // base case
        if(index == arr.length-1){
            return dp[index] = arr[index];
        }

        // transition
        int maxSum = Math.max(arr[index], arr[index] + getMaxSum(index+1, arr, dp));
        

        return dp[index] = maxSum;
    }

    public static int[] getMaxSumSubArrayIndices(int[] arr){
        int[] dp = new int[arr.length];
        Arrays.fill(dp, Integer.MIN_VALUE);
        getMaxSum(0, arr, dp);

        int maxValueIndex = 0;
        for(int i = 0; i < dp.length; i++){
            if(dp[maxValueIndex] < dp[i]){
                maxValueIndex = i;
            }
        }

        int sum = arr[maxValueIndex], index = maxValueIndex+1;
        while(sum < dp[maxValueIndex] && index < arr.length){
            sum+=arr[index];
            index++;
        }
        return new int[]{maxValueIndex, index-1};
    }
}
