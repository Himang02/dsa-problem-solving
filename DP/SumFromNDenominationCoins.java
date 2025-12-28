import java.util.Arrays;


public class SumFromNDenominationCoins {
    public static void main(String[] args) {
        int n = 5;
        int[] denominations = {1, 2, 3};
        int ways = countWaysToSum(n, denominations);
        System.out.println("Number of ways to sum up to " + n + " : " + ways);
        // Top-Down DP
        ways = countWaysToSumTopDown(n, denominations);
        System.out.println("Number of ways to sum up to " + n + " (Top-Down DP): " + ways);
        // Bottom-Up DP
        ways = countWaysToSumBottomUp(n, denominations);
        System.out.println("Number of ways to sum up to " + n + " (Bottom-Up DP): " + ways);

        // edge test cases
        n = 0;
        ways = countWaysToSum(n, denominations); // expected 1
        System.out.println("Number of ways to sum up to " + n + " : " + ways);
        ways = countWaysToSumTopDown(n, denominations); // expected 1
        System.out.println("Number of ways to sum up to " + n + " (Top-Down DP): " + ways);
        ways = countWaysToSumBottomUp(n, denominations); // expected 1
        System.out.println("Number of ways to sum up to " + n + " (Bottom-Up DP): " + ways);

        n = 7;
        denominations = new int[]{2, 4, 6};
        ways = countWaysToSum(n, denominations); // expected 0
        System.out.println("Number of ways to sum up to " + n + " : " + ways);
        ways = countWaysToSumTopDown(n, denominations); // expected 0
        System.out.println("Number of ways to sum up to " + n + " (Top-Down DP): " + ways);
        ways = countWaysToSumBottomUp(n, denominations); // expected 0
        System.out.println("Number of ways to sum up to " + n + " (Bottom-Up DP): " + ways);

        n = 10;
        denominations = new int[]{2, 5, 3, 6};
        ways = countWaysToSum(n, denominations); // expected 5
        System.out.println("Number of ways to sum up to " + n + " : " + ways);
        ways = countWaysToSumTopDown(n, denominations); // expected 5
        System.out.println("Number of ways to sum up to " + n + " (Top-Down DP): " + ways);
        ways = countWaysToSumBottomUp(n, denominations); // expected 5
        System.out.println("Number of ways to sum up to " + n + " (Bottom-Up DP): " + ways);
    }

    public static int countWaysToSum(int n, int[] denominations) {
        return countWaysToSum(n, denominations.length, denominations);
    }

    public static int countWaysToSumTopDown(int n, int[] denominations){
        int[][] dp = new int[n+1][denominations.length+1];
        for(int[] row : dp) {
            Arrays.fill(row, -1);
        }
        return countWaysToSumTopDown(n, denominations.length, denominations, dp);
    }

    // state: remainingSum, remainingDenominations: number of ways to form remainingSum using last remainingDenominations coins
    public static int countWaysToSum(int remainingSum, int remainingDenominations, int[] denominations) {
        
        // base case
        if(remainingDenominations == 0){
            return 0;
        }
        if(remainingSum == 0){
            return 1;
        }
        if(remainingSum < 0){
            return 0;
        }

        // transition
        return countWaysToSum(remainingSum - denominations[denominations.length - remainingDenominations], remainingDenominations, denominations) +
               countWaysToSum(remainingSum, remainingDenominations - 1, denominations);
    }

    private static int countWaysToSumTopDown(int remainingSum, int remainingDenominations, int[] denominations, int[][] dp) {
        // base case
        if(remainingDenominations == 0){
            return 0;
        }
        if(remainingSum == 0){
            return dp[0][remainingDenominations] = 1;
        }
        if(remainingSum < 0){
            return 0;
        }

        // check if already computed
        if(dp[remainingSum][remainingDenominations] != -1) return dp[remainingSum][remainingDenominations];

        // transition
        dp[remainingSum][remainingDenominations] = countWaysToSumTopDown(remainingSum - denominations[denominations.length - remainingDenominations], remainingDenominations, denominations, dp) +
                                                  countWaysToSumTopDown(remainingSum, remainingDenominations - 1, denominations, dp);
        return dp[remainingSum][remainingDenominations];
    }

    private static int countWaysToSumBottomUp(int n, int[] denominations) {
        // memory
        int[][] dp = new int[n+1][denominations.length+1];

        // base case
        for(int i = 1; i <= denominations.length; i++) {
            dp[0][i] = 1;
        }

        // transition
        System.out.println("Denominations: " + Arrays.toString(denominations));
        for(int denom = 1; denom <= denominations.length; denom++) {
            for(int sum = 1; sum <=n; sum++){
                dp[sum][denom] = dp[sum][denom -1];
                if(sum - denominations[denominations.length - denom] >= 0){
                    dp[sum][denom] += dp[sum - denominations[denominations.length - denom]][denom];
                }
                
            }


            // for(int sum = 1; sum <= n; sum++) {
            //     if(denominations[denom - 1] <= sum) {
            //         dp[sum][denom] = dp[sum - denominations[denom - 1]][denom] + dp[sum][denom - 1];
            //     } else {
            //         dp[sum][denom] = dp[sum][denom - 1];
            //     }
            // }
        }
        // print dp
        for(int[] row : dp) {
            System.out.println(Arrays.toString(row));
        }   
        return dp[n][denominations.length];
    }
}