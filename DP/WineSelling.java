import java.util.Arrays;


// You have a collection of N wines placed next to each other on a shelf. Let's number the wines from left to right as they are standing on the shelf with integers from 1 to N, respectively.
// The price of the ith wine is Pi (prices of different wines can be different). Because the wines get better every year, supposing today is the year 1, 
// on year Y the price of the ith wine will be Y∗Pi, that is Y times the value of wine that current year.
// You want to sell all the wines you have, but you want to sell exactly one wine per year, starting this year. 
// Each year you are allowed to sell only either the leftmost or the rightmost wine on the shelf and you are not allowed to reorder the wines on the shelf 
// (i.e. they must stay in the same order as they are in the beginning).
// You want to find out, what is the maximum profit you can get, if you sell the wines in optimal order.

// Input: The first line contains a single integer N - number of wine bottles on the shelf. 
// The next line contains N space separated integers Pi, for 1≤i≤N.

// Output: Print a single integer - the maximum profit you can get by selling the wines in optimal order.

public class WineSelling{
    public static void main(String[] args) {
        int[] prices = {2,3,5,1,4};
        int n = prices.length;
        int maxProfit = getMaxProfit(prices, 0, n-1, 1);    // expected ans is 50
        System.out.println("Maximum profit from selling wines: " + maxProfit);
        

        int[][] dp = new int[n][n];
        for(int[] row : dp) {
            Arrays.fill(row, -1);
        }
        maxProfit = getMaxProfitTopDown(prices, 0, n-1, dp);
        System.out.println("Maximum profit from selling wines (Top-Down DP): " + maxProfit);
        // print dp
        for(int[] row : dp) {
            System.out.println(Arrays.toString(row));
        }

        maxProfit = getMaxProfitBottomUp(prices);
        System.out.println("Maximum profit from selling wines (Bottom-Up DP): " + maxProfit);
    }

    // state: left, right: maximum profit from selling wines in prices[left..right] in current year
    public static int getMaxProfit(int[] prices, int left, int right, int year) {
        //base case
        if(left == right){
            return year * prices[left];
        }

        // transition
        int maxProfit = Math.max(
            prices[left] * year + getMaxProfit(prices, left + 1, right, year + 1),
            prices[right] * year + getMaxProfit(prices, left, right - 1, year + 1)
        );
        return maxProfit;
    }
    
    public static int getMaxProfitTopDown(int[] prices, int left, int right, int[][] dp) {
        if(dp[left][right] != -1) return dp[left][right];

        //base case
        if(left == right){
            dp[left][right] = (prices.length-right+left) * prices[left];
            return dp[left][right];
        }
    
        // transition
        int maxProfit = Math.max(
            prices[left] * (prices.length-right+left) + getMaxProfitTopDown(prices, left + 1, right, dp),
            prices[right] * (prices.length-right+left) + getMaxProfitTopDown(prices, left, right - 1, dp)
        );
        dp[left][right] = maxProfit;
        return maxProfit;
    }
    
    public static int getMaxProfitBottomUp(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n][n];

        // base case:
        for(int i = 0; i < n; i++) {
            dp[i][i] = n * prices[i];
        }

        // transition:
        for(int i = 1; i < n; i++){
            int left = 0, right = i;
            int year = n - i;
            while(right < n){
                dp[left][right] = Math.max(prices[left]*year + dp[left+1][right],
                                          prices[right]*year + dp[left][right-1]);
                left++;
                right++;
            }
        }

        // print dp
        for(int[] row : dp) {
            System.out.println(Arrays.toString(row));
        }

        return dp[0][n-1];
    }

}