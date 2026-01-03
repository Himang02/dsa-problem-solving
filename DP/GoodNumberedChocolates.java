import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Problem:
 * You have a bag of N chocolates that you intend to eat. Eating chocolates makes you happy,
 * but different numbers of chocolates eaten in one sitting give different amounts of
 * happiness. Formally, eating i chocolates gives you h_i amount of happiness.
 *
 * Given the amounts of happiness you gain by eating i chocolates for all 1 <= i <= N,
 * determine the maximum total happiness you can obtain from the N chocolates.
 *
 * You are allowed to eat the same number of chocolates i multiple times as long as the
 * remaining number of chocolates is at least i.
 *
 * Input:
 * - The first line contains a single integer t (1 ≤ t ≤ 1000), the number of test cases.
 * - For each test case:
 *   - First line contains a single integer N (1 ≤ N ≤ 1000), the total number of chocolates.
 *   - Next line contains N space-separated integers, the i-th number denoting h_i (1 ≤ h_i ≤ 10^5).
 * - It is guaranteed that the sum of N across all test cases does not exceed 1000.
 *
 * Output:
 * - For each test case, output a single integer: the maximum amount of happiness obtainable from N chocolates.
 */

// PATTERN : include / exclude DP

public class GoodNumberedChocolates {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine().trim());

        for (int testCase = 0; testCase < t; testCase++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int N = Integer.parseInt(st.nextToken());
            int[] hi = new int[N];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++) {
                hi[i] = Integer.parseInt(st.nextToken());
            }

            int[][] dp = new int[N + 1][N];
            for (int[] arr : dp) {
                Arrays.fill(arr, -1);
            }
            System.out.println("Top-Down: " + maxHappinessTopDown(N, N - 1, hi, dp));

            System.out.println("Bottom-Up: " + maxHappinessBottomUp(hi));

        }
    }

    private static int maxHappinessTopDown(int remainingChocolates, int index, int[] hi, int[][] dp) {
        // memory
        if (dp[remainingChocolates][index] != -1) return dp[remainingChocolates][index];

        // base case
        if (remainingChocolates == 0) {
            return dp[remainingChocolates][index] = 0;
        }

        // transition
        int include = 0, exclude = 0;
        if (index < remainingChocolates) {
            include = maxHappinessTopDown(remainingChocolates - index - 1, index, hi, dp) + hi[index];
        }

        if (index > 0) {
            exclude = maxHappinessTopDown(remainingChocolates, index - 1, hi, dp);
        }
        return dp[remainingChocolates][index] = Math.max(include, exclude);
    }

    private static int maxHappinessBottomUp(int[] hi) {
        // memory
        int[][] dp = new int[hi.length + 1][hi.length];

        // base cases
        for (int i = 1; i <= hi.length; i++) {
            dp[i][0] = i * hi[0];
        }

        // transition
        for (int remainingChocolates = 1; remainingChocolates <= hi.length; remainingChocolates++) {
            for (int index = 1; index < hi.length; index++) {
                int include = 0;
                if (remainingChocolates - index - 1 >= 0) {
                    include = dp[remainingChocolates - index - 1][index] + hi[index];
                }
                dp[remainingChocolates][index] = Math.max(include, dp[remainingChocolates][index - 1]);
            }
        }

        return dp[hi.length][hi.length - 1];
    }
}
