public class NumberOccuringOnce {

    public static void main(String[] args) {
        // All elements occur thrice except one
        int[] arr = {2, 2, 2, 1, 3, 5, 4, 5, 3, 4, 4, 5, 3}; // Example input
        int result = findUniqueNumber(arr);
        System.out.println("The number occurring once is: " + result);
    }

    // TC: O(N) space: O(1)
    // explanation: https://www.geeksforgeeks.org/find-the-element-that-appears-once/
    private static int findUniqueNumberOptimal(int[] arr) {
        int ones = 0, twos = 0;
        for (int num : arr) {
            ones = (ones ^ num) & ~twos;
            twos = (twos ^ num) & ~ones;
        }
        return ones;
    }

    // TC: O(32 * N) where N is the size of the array
    private static int findUniqueNumber(int[] arr) {
        int[] bitOccurence = new int[32];
        for (int num : arr) {
            for (int i = 0; i < 32; i++) {
                if ((num & (1 << i)) != 0) {
                    bitOccurence[i]++;
                }
            }
        }
        int ans = 0;
        for(int i = 0; i < 32; i++) {
            if(bitOccurence[i] % 3 != 0) {
                ans |= (1 << i);
            }
        }
        return ans;
    }
}