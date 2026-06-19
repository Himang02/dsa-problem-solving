// https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/?envType=problem-list-v2&envId=dynamic-programming

import java.util.Arrays;

public class BuyAndSellStocks {

    public static void main(String[] args) {
        BuyAndSellStocks solution = new BuyAndSellStocks();

        // [7, 1, 5, 3, 6, 4] -> 5 (buy at 1, sell at 6)
        System.out.println(solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));

        // [7, 6, 4, 3, 1] -> 0 (prices only fall, no profit)
        System.out.println(solution.maxProfit(new int[]{7, 6, 4, 3, 1}));

        // [1, 2, 3, 4, 5] -> 4 (buy at 1, sell at 5)
        System.out.println(solution.maxProfit(new int[]{1, 2, 3, 4, 5}));

        // [2, 4, 1] -> 2 (buy at 2, sell at 4)
        System.out.println(solution.maxProfit(new int[]{2, 4, 1}));

        // single price [5] -> 0 (cannot sell)
        System.out.println(solution.maxProfit(new int[]{5}));
    }



    public int maxProfit(int[] prices) {
        int[] dp = new int[prices.length];
        Arrays.fill(dp, -1);
        maxProfit(prices.length - 1, prices, dp);
        int max = 0;
        for(int i = 0; i < prices.length; i++){
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    // if sold on index-th day
    private int maxProfit(int index, int[] prices, int[] dp){
        if(dp[index] != -1){
            return dp[index];
        } 

        // base case
        if(index == 0){
            return dp[index] = 0;
        }

        // transition
        int prevProfit = maxProfit(index-1, prices, dp);
        return dp[index] = (prevProfit + (prices[index] - prices[index-1]) > 0) ? (prevProfit + (prices[index] - prices[index-1])) : 0;
    }
}
