import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Arrays;

/**
 * You find yourself on the number-line and you start at the origin (x = 0).
 * In a single jump, you can advance by one of the N distinct jump lengths a_i.
 * From coordinate x, choosing jump-length a_i moves you to x + a_i.
 *
 * Problem:
 * Count the number of combinations of jumps (order does not matter; combinations
 * are defined by the number of times each jump-length is used) that land exactly
 * at coordinate S. Each a_i may be used any number of times (including 0).
 * Output the answer modulo 1e9+7 (1000000007). The answer may be 0 if S cannot
 * be reached.
 *
 * Input:
 * - t (1 ≤ t ≤ 300): number of test cases.
 * For each test case:
 *   - N (1 ≤ N ≤ 300) and S (1 ≤ S ≤ 5000)
 *   - N distinct integers a_i (1 ≤ a_i ≤ 5000): possible jump lengths.
 *
 * Constraints:
 * It is guaranteed that the sum of N across all test cases ≤ 300 and the
 * sum of S across all test cases ≤ 5000.
 *
 * Output:
 * For each test case, output a single integer: the number of combinations to
 * travel a distance of S modulo 1000000007.
 */
public class WaysofJump {
  static int MOD = 1000000007;
  
  public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int t = Integer.parseInt(br.readLine().trim());
    for (int testCase = 0; testCase < t; testCase++) {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int S = Integer.parseInt(st.nextToken());
        
        st = new StringTokenizer(br.readLine());
        int[] jumpLengths = new int[N];
        
        for(int i = 0; i < jumpLengths.length; i++){
          jumpLengths[i] = Integer.parseInt(st.nextToken());
        }
        
        System.out.println(getCounts(jumpLengths, S));
    }

  }
  
  private static int getCounts(int[] jumpLengths, int S){
    int[][] dp = new int[S+1][jumpLengths.length];
    
    for(int i = 0; i < jumpLengths.length; i++){
      dp[0][i] = 1;
    }
    for(int i = 1; i<= S; i++){
      dp[i][jumpLengths.length-1] = (i%jumpLengths[jumpLengths.length-1]==0) ? 1 : 0;
    }
    
    for(int remainingDistance = 1; remainingDistance <= S; remainingDistance++){
      for(int index = jumpLengths.length-2; index>=0; index--){
        dp[remainingDistance][index] = dp[remainingDistance][index+1]%MOD;
        if(remainingDistance-jumpLengths[index]>=0){
          dp[remainingDistance][index]=(dp[remainingDistance][index]+(dp[remainingDistance-jumpLengths[index]][index])%MOD)%MOD;
        }
      }
    }
    
    return dp[S][0];
  }
}