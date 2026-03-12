
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

class BoundedKnapsack {
    public static void main(String[] args) {
        new BoundedKnapsackTest().testAll();
        
    }

   public static int getMaxValueTopDownHelper(int[] weights, int[] values, int[] counts, int capacity) {
// 
        // System.out.println("\n########################### new test case ###########################");
        int maxCount = 0;
        for (int count : counts) {
            maxCount = Math.max(maxCount, count);
        }
        int[][][] dp = new int[weights.length + 1][capacity + 1][maxCount + 1];
        for (int[][] arr2D : dp) {
            for (int[] arr : arr2D) {
                Arrays.fill(arr, -1);
            }
        }

        int maxValue = getMaxValueTopDown(0, capacity, counts[0], weights, values, counts, dp);

        // // print dp table
        // for(int i = 0; i < dp.length; i++){
        //     System.out.println("Index " + i + ": " + Arrays.toString(dp[i]));
        // }
        return maxValue;
    }
   
    public static int getMaxValueTopDownTryIncludeCountsHelper(int[] weights, int[] values, int[] counts, int capacity) {

        // int maxCount = 0;
        // for (int count : counts) {
        //     maxCount = Math.max(maxCount, count);
        // }
        // int[][][] dp = new int[weights.length + 1][capacity + 1][maxCount + 1];
        // for (int[][] arr2D : dp) {
        //     for (int[] arr : arr2D) {
        //         Arrays.fill(arr, -1);
        //     }
        // }

        int[][] dp = new int[weights.length+1][capacity+1];
        for(int[] arr: dp){
            Arrays.fill(arr, -1);
        }

        int maxValue = getMaxValueTopDownTryIncludeCounts(0, capacity, weights, values, counts, dp);

        // // print dp table
        // for(int i = 0; i < dp.length; i++){
        //     System.out.println("Index " + i + ": " + Arrays.toString(dp[i]));
        // }

        // print path
        int currentIndex = 0, cap = capacity;
        // System.out.println("\n########################### try-all-include-counts ###########################");
        while(currentIndex < weights.length && cap > 0){
            if(dp[currentIndex+1][cap] == dp[currentIndex][cap]){
                currentIndex++;
                continue;
            }
            System.out.print(weights[currentIndex]+", ");
            for(int i = 1; i <= counts[currentIndex]; i++){
                // System.out.println(dp[currentIndex][cap] + ",  " +  i*values[currentIndex] + ", " + dp[currentIndex+1][cap - i*weights[currentIndex]] );
                if((dp[currentIndex][cap]- i*values[currentIndex]) != dp[currentIndex+1][cap - i*weights[currentIndex]]){

                    System.out.print(weights[currentIndex]+", ");
                }
                else{
                    cap -= i*weights[currentIndex];
                    currentIndex++;
                    break;
                }
            }
            
        }
        System.out.println();
        
        return maxValue;
    }

    /* Get maximum value */
    // Top-down approach
    // state: index, capacity, remaining count of current item
    // Time: O(N × W × maxCount) | Space: O(N × W × maxCount) dp table + O(N*maxCount) stack
    // pattern: include-exclude with count limit
    private static int getMaxValueTopDown(int index, int capacity, int remainingCount, int[] weights, int[] values, int[] counts, int[][][] dp) {
        // System.out.println("Function called for: index: " + index + ", cap: " + capacity + ", counts: " + remainingCount);

        // memory
        if(dp[index][capacity][remainingCount] != -1){
            int returnValue = dp[index][capacity][remainingCount];
            // System.out.println("Returning memoized value: " + returnValue + " for index: " + index + ", cap: " + capacity + ", counts: " + remainingCount);
            return returnValue;
        }

        // base case
        if(index == weights.length || capacity == 0){
            // System.out.println("Base case reached for: index: " + index + ", cap: " + capacity + ", counts: " + remainingCount);
            return dp[index][capacity][remainingCount] = 0;
        }

        // transition
        int remainingCapacity = capacity;
        int maxValue = 0;
        if(index < counts.length-1){
            maxValue += getMaxValueTopDown(index + 1, capacity, counts[index+1], weights, values, counts, dp);
            // System.out.println("Transition for exclusion maxValue: " + maxValue + " for index: " + index + ", cap: " + capacity + ", counts: " + remainingCount);
        }

        if(capacity >= weights[index] && remainingCount > 0){
            maxValue = Math.max(maxValue, values[index] + getMaxValueTopDown(index, capacity - weights[index], remainingCount-1, weights, values, counts, dp));
            // System.out.println("Transition for inclusion maxValue: " + maxValue + " for index: " + index + ", cap: " + capacity + ", counts: " + remainingCount);
        }
        

        return dp[index][capacity][remainingCount] = maxValue;
        
    }

    private static int getMaxValueTopDownTryIncludeCounts(int index, int capacity, int[] weights, int[] values, int[] counts, int[][] dp){
        // memory
        if(dp[index][capacity] != -1){
            return dp[index][capacity];
        }


        // base case
        if(index == weights.length || capacity == 0){
            return dp[index][capacity] = 0;
        }


        // transition
        int maxValue = getMaxValueTopDownTryIncludeCounts(index + 1, capacity, weights, values, counts, dp);
        for(int i = 1; i <= counts[index]; i++){
            if(capacity >= i*weights[index]){
                maxValue = Math.max(maxValue, i*values[index] + getMaxValueTopDownTryIncludeCounts(index+1, capacity - i*weights[index], weights, values, counts, dp));
            }
            else{
                break;
            }
        }

        return dp[index][capacity] = maxValue;
    }

    public static int getMaxValueByConvertingTo01Helper(int capacity, int[] weights, int[] values, int[] counts){
        int len = 0;
        for(int i = 0; i< counts.length; i++){
            len += counts[i];
        }
        List<Integer> weightsList = new ArrayList<>();
        List<Integer> valuesList = new ArrayList<>();

        int index = 0;
        while(index < counts.length){
            for(int i = 0; i< counts[index]; i++){
                weightsList.add(weights[index]);
                valuesList.add(values[index]);
            }
            index++;
        }

        int[][] dp = new int[len+1][capacity+1];

        for(int[] arr: dp){
            Arrays.fill(arr, -1);
        }

        int ans = getMaxValueByConvertingTo01(0, capacity, weightsList, valuesList, dp);

        //     // print dp table
        // for(int i = 0; i < dp.length-1; i++){
        //     System.out.println("Index " + i + "-"+weightsList.get(i)+": " + Arrays.toString(dp[i]));
        // }

        // print path
        int currentIndex = 0, cap = capacity;
        // System.out.println("\n########################### 0-1 knapsack ###########################");
        while(currentIndex < weightsList.size() && cap > 0){
            if(dp[currentIndex][cap] != dp[currentIndex+1][cap]){
                System.out.print(weightsList.get(currentIndex)+", ");
                cap -= weightsList.get(currentIndex);
                
            }
            currentIndex++;
        }
        System.out.println();

        return ans;
    }

    private static int getMaxValueByConvertingTo01(int index, int capacity, List<Integer> weights, List<Integer> values, int[][] dp){
        // memory
        if(dp[index][capacity] != -1){
            return dp[index][capacity];
        }

        // base case
        if(index == weights.size() || capacity == 0){
            return dp[index][capacity] = 0;
        }

        // transition
        int maxValue = getMaxValueByConvertingTo01(index+1, capacity, weights, values, dp);         // exclude
        if(capacity >= weights.get(index)){             // include
            maxValue = Math.max(maxValue, values.get(index) + getMaxValueByConvertingTo01(index+1, capacity - weights.get(index), weights, values, dp));
        }

        return dp[index][capacity] = maxValue;
    }

    public static int getMaxValueBottomUp(int capacity, int[] weights, int[] values,  int[] counts){
        // memory
        int[] dp = new int[capacity+1];

        // base case - already set

        // transition
        for(int index = weights.length-1; index >=0; index--){
            for(int cap = capacity; cap >=0; cap--){
                for(int i = 1; i <= counts[index]; i++){
                    dp[cap] = cap-i*weights[index] >= 0 ? Math.max(dp[cap-i*weights[index]] + i*values[index], dp[cap]) : dp[cap];
                }
                
            }
        }

        return dp[capacity];
    }

}

