
// https://leetcode.com/problems/maximum-product-subarray/?envType=problem-list-v2&envId=dynamic-programming


public class MaxProductSubarray {
   
    public static void main(String[] args) {
        MaxProductSubarray solution = new MaxProductSubarray();

        // [2, 3, -2, 4] -> 6 (subarray [2, 3])
        System.out.println(solution.maxProduct(new int[]{2, 3, -2, 4}));

        // [-2, 0, -1] -> 0 (subarray [0])
        System.out.println(solution.maxProduct(new int[]{-2, 0, -1}));

        // [-2, 3, -4] -> 24 (whole array, two negatives multiply to positive)
        System.out.println(solution.maxProduct(new int[]{-2, 3, -4}));

        // [2, -5, -2, -4, 3] -> 24 (subarray [-5, -2, -4] = -40? no -> [-2, -4, 3] = 24)
        System.out.println(solution.maxProduct(new int[]{2, -5, -2, -4, 3}));

        // single element [-3] -> -3
        System.out.println(solution.maxProduct(new int[]{-3}));
    }
    


    public int maxProduct(int[] nums) {
        
        int[] maxProdDp = new int[nums.length];
        int[] minProdDp = new int[nums.length];

        maxProdDp[0] = nums[0];
        minProdDp[0] = nums[0];

        int max = maxProdDp[0];

        for(int i = 1; i < nums.length; i++){
            maxProdDp[i] = Math.max(nums[i], Math.max(maxProdDp[i-1] * nums[i], minProdDp[i-1] * nums[i]));
            minProdDp[i] = Math.min(nums[i], Math.min(maxProdDp[i-1] * nums[i], minProdDp[i-1] * nums[i]));

            max = Math.max(max, maxProdDp[i]);
        }

        return max;

    }

    
}
