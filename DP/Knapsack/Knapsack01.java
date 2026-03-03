import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Knapsack01{

    public static void main(String[] args){
        int[] weights = {1, 2, 3};
        int[] values = {10, 15, 40};
        int capacity = 6;
        System.out.println(helper(weights, values, capacity));  
        System.out.println(getMaxValueBottomUp(weights, values, capacity));  
        System.out.println(getMaxValueBottomUpSpaceOptimized(weights, values, capacity));  
        // more test cases
        weights = new int[]{1, 2, 3, 2};
        values = new int[]{10, 15, 40, 20};
        System.out.println(helper(weights, values, 5));
        System.out.println(getMaxValueBottomUp(weights, values, 5));  
        System.out.println(getMaxValueBottomUpSpaceOptimized(weights, values, 5));

        // generate more test cases that test proper edge cases
        weights = new int[]{1, 2, 3, 4, 5};
        values = new int[]{10, 15, 40, 30, 50};
        System.out.println(helper(weights, values, 10));
        System.out.println(getMaxValueBottomUp(weights, values, 10));  
        System.out.println(getMaxValueBottomUpSpaceOptimized(weights, values, 10));


    }


    private static int helper(int[] weights, int[] values, int capacity){
        int[][] dp = new int[weights.length+1][capacity+1];
        for(int[] arr: dp){
            Arrays.fill(arr, -1);
        }
        int maxValue = getMaxValueTopDown(0, capacity, weights, values, dp);

        /* Get maximum value- item indices */
        int index = 0, cap = capacity;
        List<Integer> takenIndices = new ArrayList<>();
        while (index < weights.length && capacity > 0){
            if(dp[index][cap] != dp[index+1][cap]){
                takenIndices.add(index);
                cap -= weights[index];
                index++;
            }
            else{
                index++;
            }

        }

        System.out.println("Selected items: " + takenIndices);
        return maxValue;
    }

    /* Get maximum value */
    // Top-down approach
    // include-exclude pattern
    // state: index, remaining capacity
    private static int getMaxValueTopDown(int index, int remainingCapacity, int[] weights, int[] values, int[][] dp){
        // memory
        if(dp[index][remainingCapacity] != -1){
            return dp[index][remainingCapacity];
        }
        // base case
        if(index == weights.length){
            return dp[index][remainingCapacity] = 0;
        }
        if(remainingCapacity == 0){
            return dp[index][remainingCapacity] = 0;
        }

        // transition
        int maxValue = getMaxValueTopDown(index+1, remainingCapacity, weights, values, dp);
        if(remainingCapacity >= weights[index]){
            maxValue = Math.max(getMaxValueTopDown(index+1, remainingCapacity - weights[index], weights, values, dp) + values[index], maxValue);
        }
        return dp[index][remainingCapacity] = maxValue;
    }

    /* Get maximum value */
    // Bottum-up approach
    // state: index, capacity
    private static int getMaxValueBottomUp(int[] weights, int[] values, int capacity){
        // memory
        int[][] dp = new int[capacity+1][weights.length+1];

        // base case - already set

        // transition
        for(int cap = 1; cap <= capacity; cap++){
            for(int index = weights.length-1; index >=0; index--){
                dp[cap][index] = (cap-weights[index] >= 0) ? Math.max(dp[cap][index+1], values[index] + dp[cap-weights[index]][index+1]) : dp[cap][index+1];
            }
        }

        /* Get maximum value- item indices */
        int index = 0, cap = capacity;
        List<Integer> takenIndices = new ArrayList<>();
        while (index < weights.length && capacity > 0){
            if(dp[cap][index] != dp[cap][index+1]){
                takenIndices.add(index);
                cap -= weights[index];
                index++;
            }
            else{
                index++;
            }

        }

        System.out.println("Selected items: " + takenIndices);
        return dp[capacity][0];
    }

    /* Get maximum value */
    // Bottum-up approach - space optimized
    // state: index, capacity
    private static int getMaxValueBottomUpSpaceOptimized(int[] weights, int[] values, int capacity){
        int[] dp = new int[capacity+1];
        for(int index = weights.length-1; index >=0; index--){
            for(int cap = capacity; cap >= 0; cap--){
                if(cap-weights[index] >= 0){
                    dp[cap] = Math.max(dp[cap], values[index] + dp[cap-weights[index]]);
                }
            }
        }
        return dp[capacity];
    }

    /* Get maximum value- item indices */

}