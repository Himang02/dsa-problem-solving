
import java.util.Arrays;

class BoundedKnapsack {
    public static void main(String[] args) {
        // test case
        int[] weights = {1, 2, 3, 2};
        int[] values = {10, 15, 40, 20};
        int capacity = 6;
        
        System.out.println(knapsackTopDown(weights, values, capacity));  // expected output: 70
        System.out.println(knapsackBottomUp(weights, values, capacity)); // expected output: 70
        System.out.println(knapsackBottomUpSpaceOptimized(weights, values, capacity)); // expected output: 70

    }

    private static int knapsackTopDown(int[] weights, int[] values, int capacity){
        int[][] dp = new int[capacity + 1][weights.length+1];
        for(int i = 0; i <= capacity; i++) {
            Arrays.fill(dp[i], -1);
        }
        return knapsackTopDown(weights, values, capacity, 0, dp);
    }

    // include-exclude pattern
    // maximum value achievable with given capacity and items
    // state: remaining capacity, remaining items
    private static int knapsackTopDown(int[] weights, int[] values, int capacity, int index, int[][] dp) {
        // memory
        if(dp[capacity][index] != -1) {
            return dp[capacity][index];
        }
        
        // base-case
        if(index == weights.length || capacity <= 0) {
            return dp[capacity][index] = 0;
        }

        // transition
        int exclude = knapsackTopDown(weights, values, capacity, index + 1, dp);
        int include = 0;
        if(capacity - weights[index] >= 0) {
            include = values[index] + knapsackTopDown(weights, values, capacity - weights[index], index + 1, dp);
        }
        return dp[capacity][index] = Math.max(include, exclude);
    }

    
    // bottom-up approach
    private static int knapsackBottomUp(int[] weights, int[] values, int capacity) {
        // memory
        int[][] dp = new int[capacity+1][weights.length];

        // base-case
        for(int i = 0; i <= capacity; i++){
            if(weights[weights.length-1] <= i){
                dp[i][weights.length-1] = values[weights.length-1];
            }
        }

        // transition
        for(int remainingCapacity = 1; remainingCapacity <= capacity; remainingCapacity++){
            for(int index = weights.length - 2; index >= 0; index--){
                dp[remainingCapacity][index] = Math.max((((remainingCapacity >= weights[index]) ? dp[remainingCapacity - weights[index]][index+1] : 0) + values[index]), dp[remainingCapacity][index+1]);
            }
        }
        
        return dp[capacity][0];
    }

    private static int knapsackBottomUpSpaceOptimized(int[] weights, int[] values, int capacity) {
        // memory
        int[] dp = new int[capacity+1];

        // base-case
        for(int i = 0; i <= capacity; i++){
            if(weights[weights.length-1] <= i){
                dp[i] = values[weights.length-1];
            }
        }

        // transition
        for(int index = weights.length - 2; index >= 0; index--){
            for(int remainingCapacity = capacity; remainingCapacity > 0; remainingCapacity--){
                dp[remainingCapacity] = Math.max((((remainingCapacity >= weights[index]) ? dp[remainingCapacity - weights[index]] : 0) + values[index]), dp[remainingCapacity]);
            }
        }
        
        return dp[capacity];
    }
}