import java.util.Arrays;

public class BuyAndSellStocks_3{

    public static void main(String[] args) {
        BuyAndSellStocks_3 solution = new BuyAndSellStocks_3();

        // [3, 3, 5, 0, 0, 3, 1, 4] -> 6 (buy 0 sell 3, then buy 1 sell 4)
        System.out.println(solution.maxProfit(new int[]{3, 3, 5, 0, 0, 3, 1, 4}));

        // [1, 2, 3, 4, 5] -> 4 (one transaction is enough)
        System.out.println(solution.maxProfit(new int[]{1, 2, 3, 4, 5}));

        // [7, 6, 4, 3, 1] -> 0 (prices only fall)
        System.out.println(solution.maxProfit(new int[]{7, 6, 4, 3, 1}));

        // [3, 2, 6, 5, 0, 3] -> 7 (buy 2 sell 6 = 4, then buy 0 sell 3 = 3)
        System.out.println(solution.maxProfit(new int[]{3, 2, 6, 5, 0, 3}));

        // single price [5] -> 0 (cannot sell)
        System.out.println(solution.maxProfit(new int[]{5}));
    }


    public int maxProfit(int[] prices) {
        int[][][] dp = new int[prices.length][3][3];

        for(int[][] arr_2d : dp){
            for(int[] arr : arr_2d){
                Arrays.fill(arr, -1);
            }
        }
        return maxProfit(prices.length - 1, 2, 2, prices, dp);

    }


    private int maxProfit(int i, int remOpen, int remClose, int[] prices, int[][][] dp){
        if(dp[i][remOpen][remClose] != -1){
            return dp[i][remOpen][remClose];
        }

        if(i == 0 || remOpen == 0){
            return dp[i][remOpen][remClose] = 0;
        }

        if(remOpen == remClose){
            return dp[i][remOpen][remClose] = Math.max(maxProfit(i-1, remOpen, remClose, prices, dp), maxProfit(i-1, remOpen, remClose-1, prices, dp) + prices[i] - prices[i-1]);
        }
        else{
            return dp[i][remOpen][remClose] = dp[i][remOpen][remClose] = Math.max(maxProfit(i-1, remOpen-1, remClose, prices, dp), maxProfit(i-1, remOpen, remClose, prices, dp) + prices[i] - prices[i-1]);
        }

    }
}