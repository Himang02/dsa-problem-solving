import java.util.Arrays;

// You have an infinite supply of 2 x 1 dimension tiles.
// You are allowed to rotate the tile to orient them horizontally or vertically.
// You want to tile a 2 x N dimension corridor using 2 x 1 tiles.
// Find number of unique ways of tiling the 2 x N dimension corridor modulo 1000000007.
// 1≤N≤105

// Input : Single integer N denoting length of corridor.
// Output: Print a single integer - number of ways to tile the corridor modulo 1000000007.

public class TilePermutations{
    public static void main(String[] args) {
        for(int n = 1; n<=10; n++){
            int arrangements = countTileArrangements(n);
            System.out.println("Number of ways to arrange tiles of length "+ n + " : " + arrangements);
        }

        int n = 10;
        int[] dp = new int[n+1];
        Arrays.fill(dp, -1);
        int arrangements = countTileArrangementsTopDown(n, dp);
        System.out.println("Number of ways to arrange tiles of length "+ n + " : " + arrangements);


        arrangements = countTileArrangementsBottomUp(n);
        System.out.println("Number of ways to arrange tiles of length "+ n + " : " + arrangements);
    }

    // state: n: number of ways to arrange tiles in n length
    public static int countTileArrangements(int n) {
        // base case
        if(n <= 1){
            return 1;
        }
        else if(n == 2){
            return 2;
        }

        // transition
        return countTileArrangements(n-1) + countTileArrangements(n-2);  
    }


    private static int countTileArrangementsTopDown(int n, int[] dp) {
        // check if already computed
        if(dp[n] != -1) return dp[n];

        // base case
        if(n <= 1){
            return dp[n] = 1;
        }
        else if(n == 2){
            return dp[n] = 2;
        }

        // transition
        return dp[n] = countTileArrangementsTopDown(n-1, dp) + countTileArrangementsTopDown(n-2, dp);
    }

    private static int countTileArrangementsBottomUp(int n) {
        int[] dp = new int[n];

        // base case
        dp[0] = 1;
        dp[1] = 2;

        for(int i = 2; i < n; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n-1];
    }
}