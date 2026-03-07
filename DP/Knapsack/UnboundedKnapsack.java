import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class UnboundedKnapsack{

    public static void main(String[] args){

        // test with proper edge cases
        // test case 1
        int[] weights = {1, 2, 3, 2};
        int[] values = {10, 15, 40, 20};
        int capacity = 6;

        System.out.println(helper(weights, values, capacity));  // expected output: 80
        System.out.println(getMaxValueBottomUp(weights, values, capacity)); // expected output: 80
        System.out.println(getMaxValueBottomUpSpaceOptimized(weights, values, capacity)); // expected output: 80

        // test case 2
        weights = new int[]{1, 2, 3, 4, 5};
        values = new int[]{10, 15, 40, 30, 50};
        capacity = 10;

        System.out.println(helper(weights, values, capacity));  // expected output: 130
        System.out.println(getMaxValueBottomUp(weights, values, capacity)); // expected output: 130
        System.out.println(getMaxValueBottomUpSpaceOptimized(weights, values, capacity)); // expected output: 130

        new UnboundedKnapsackTest().testAll();
    }



    // Time: O(N × W) | Space: O(N × W) dp table + O(N + W) stack
    static int helper(int[] weights, int[] values, int capacity){
        int[][] dp = new int[weights.length+1][capacity+1];
        for(int[] arr: dp){
            Arrays.fill(arr, -1);
        }
        int maxValue = getMaxValueTopDown(0, capacity, weights, values, dp);

        /* Get maximum value- item indices */
        int index = 0, cap = capacity;
        List<Integer> takenIndices = new ArrayList<>();
        while (index < weights.length && cap > 0){
            if(dp[index][cap] != dp[index+1][cap]){
                takenIndices.add(index);
                cap -= weights[index];
                // index++;
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
    // Time: O(N × W) | Space: O(N × W) dp table + O(N + W) stack
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
            maxValue = Math.max(getMaxValueTopDown(index, remainingCapacity - weights[index], weights, values, dp) + values[index], maxValue);
        }
        return dp[index][remainingCapacity] = maxValue;
    }

    /* Get maximum value */
    // Bottum-up approach - include/exclude pattern
    // state: index, capacity
    // Time: O(N × W) | Space: O(N × W) dp table
    public static int getMaxValueBottomUp(int[] weights, int[] values, int capacity){
        // memory
        int[][] dp = new int[capacity+1][weights.length+1];

        // base case - already set

        // transition
        for(int cap = 1; cap <= capacity; cap++){
            for(int index = weights.length-1; index >=0; index--){
                dp[cap][index] = (cap-weights[index] >= 0) ? Math.max(dp[cap][index+1], values[index] + dp[cap-weights[index]][index]) : dp[cap][index+1];
            }
        }

        /* Get maximum value- item indices */
        int index = 0, cap = capacity;
        List<Integer> takenIndices = new ArrayList<>();
        while (index < weights.length && cap > 0){
            if(dp[cap][index] != dp[cap][index+1]){
                takenIndices.add(index);
                cap -= weights[index];
                // index++;
            }
            else{
                index++;
            }

        }

        System.out.println("Selected items: " + takenIndices);
        return dp[capacity][0];
    }


    /* Get maximum value - Space optimized*/
    // Bottum-up approach - include/exclude pattern
    // state: capacity
    // Time: O(N × W) | Space: O(W)
    public static int getMaxValueBottomUpSpaceOptimized(int[] weights, int[] values, int capacity){
        // memory
        int[] dp = new int[capacity+1];

        // base case - already set

        // transition
        for(int index = weights.length-1; index >= 0; index--){
            for(int cap = 0; cap <= capacity; cap++){
                if(cap - weights[index] >= 0 && dp[cap - weights[index]] + values[index] > dp[cap]){
                    dp[cap] = dp[cap - weights[index]] + values[index];
                }
            }
        }

        return dp[capacity];
    }

    /* Get maximum value */
    // Top-down approach
    // try-all-possibilities pattern
    // state: remaining capacity
    // Time: O(N × W) | Space: O(W) dp array + O(W) stack
    public static int getMaxValueTopDownTryAll(int remainingCapacity, int[] weights, int[] values, int[] dp){
        // memory
        if(dp[remainingCapacity] != -1){
            return dp[remainingCapacity];
        }

        // base case
        if(remainingCapacity == 0){
            return dp[0] = 0;
        }

        // transition
        int maxValue  = 0;
        for(int i = 0; i < weights.length; i++){
            if(remainingCapacity >= weights[i]){
                maxValue = Math.max(getMaxValueTopDownTryAll(remainingCapacity - weights[i], weights, values, dp) + values[i], maxValue);
            }
        }

        return dp[remainingCapacity] = maxValue;
    }

}