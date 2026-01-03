import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Arrays;

/**
 * Problem: Ways Of Building
 * Given N types of blocks with heights hi, count the number of combinations
 * (order does NOT matter, each type can be used any number of times) that sum
 * to the target height S. Output the answer modulo 1_000_000_007.
 *
 * Input: t test cases. For each test case:
 *  - N S
 *  - N distinct integers hi
 *
 * Approach: Use dynamic programming (unbounded knapsack / coin-change combinations).
 * dp[remainingHeight][index] = number of ways to form remainingHeight using
 * block types from index..N-1. The DP filling code follows this header.
 */

public class WaysOfBuilding {
    static int MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int t = Integer.parseInt(br.readLine().trim());
        for (int testCase = 0; testCase < t; testCase++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int S = Integer.parseInt(st.nextToken());

            int[] hi = new int[N];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++) {
                hi[i] = Integer.parseInt(st.nextToken());
            }

            System.out.println(getCombinations(hi, S));
        }
    }

    /**
     * Compute number of combinations to reach total height S using block heights hi.
     * @param hi array of block heights
     * @param S  target height
     * @return number of combinations modulo MOD
     *
     * Note: The DP implementation that initializes and fills the dp table follows.
     */
    private static int getCombinations(int[] hi, int S) {
    // memory
    int[][] dp = new int[S+1][hi.length];
    
    // base case
    for(int i = 0; i < hi.length; i++){
      dp[0][i] = 1;
    }
    
    for(int i = 1; i <= S; i++){
      if(i%hi[hi.length-1]==0){
        dp[i][hi.length-1] = 1;
      }
    }
    
    // transtion
    for(int remainingHeight = 1; remainingHeight <= S; remainingHeight++){
      for(int index = hi.length-2; index >=0; index--){
        dp[remainingHeight][index] =  dp[remainingHeight][index+1]%MOD;
        if(remainingHeight - hi[index] >= 0){
          dp[remainingHeight][index] = (dp[remainingHeight][index] + (dp[remainingHeight - hi[index]][index])%MOD)%MOD;
        }
      }
    }
    return dp[S][0];
  }
}