import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Arrays;

/*
Problem: Equal Partition (partition array into two subsets with equal sum)

You have N candies with weights a[i]. Determine if you can split them into two sets S1 and S2
such that sum(S1) == sum(S2).

Input:
- t: number of test cases (1 ≤ t ≤ 200)
For each test case:
- N: number of chocolates (1 ≤ N ≤ 200)
- next line: N integers a[i] (1 ≤ a[i] ≤ 100)

Constraints:
- Sum of N over all test cases ≤ 200.

Output:
- For each test case print "YES" if such a partition exists, otherwise "NO".

Approach (used in this file):
- Equivalent to checking whether total sum is even and whether a subset exists with sum = total/2.
- The implementation below uses recursion with memoization. The dp table dp[s1Weight][index]
    stores visited states that are determined to be impossible (so a true entry means this state
    leads to no valid partition and can be pruned).
*/

public class EqualPartition {

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int t = Integer.parseInt(br.readLine().trim());
    for (int testCase = 0; testCase < t; testCase++) {
      int N = Integer.parseInt(br.readLine().trim());

      StringTokenizer st = new StringTokenizer(br.readLine());
      int[] weights = new int[N];
      int totalWeight = 0;
      for (int i = 0; i < weights.length; i++) {
        weights[i] = Integer.parseInt(st.nextToken());
        totalWeight += weights[i];
      }

      boolean[][] dp = new boolean[totalWeight + 1][weights.length];

    //   System.out.println(isPossibleTopDown(0, 0, totalWeight, weights, dp) ? "YES" : "NO");
      System.out.println(isPossibleBottomUp(totalWeight, weights) ? "YES" : "NO");
    }

  }

  private static boolean isPossibleTopDown(int s1Weight, int index, int totalWeight, int[] weights, boolean[][] dp) {
    // System.out.println(s1Weight +", "+ index);

    // base case
    if (s1Weight * 2 == totalWeight) {
      return true;
    }
    if (index == weights.length) {
      return false;
    }

    // memory
    if (dp[s1Weight][index]) return false;

    // transition
    if ((s1Weight + weights[index] <= totalWeight && isPossibleTopDown(s1Weight + weights[index], index + 1, totalWeight, weights, dp)) ||
        (isPossibleTopDown(s1Weight, index + 1, totalWeight, weights, dp))) {
      return true;
    }

    dp[s1Weight][index] = true;
    return false;
  }

  private static boolean isPossibleBottomUp(int totalWeight, int[] weights) {
    boolean[][] dp = new boolean[totalWeight + 1][weights.length];

    if (totalWeight % 2 != 0) {
      return false;
    }

    for (int i = 1; i <= totalWeight; i++) {
      dp[i][weights.length - 1] = ((i + weights[weights.length - 1]) * 2 == totalWeight);
    }
    for (int i = 0; i < weights.length; i++) {
      dp[totalWeight / 2][i] = true;
    }

    for (int s1Weight = totalWeight / 2 - 1; s1Weight >= 0; s1Weight--) {
      for (int index = weights.length - 2; index >= 0; index--) {
        dp[s1Weight][index] = ((s1Weight + weights[index] <= (totalWeight)) && dp[s1Weight + weights[index]][index + 1]) || dp[s1Weight][index + 1];
      }
    }

    return dp[0][0];
  }
}
