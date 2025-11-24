

class MaxProductOfKSubarray {
    public static void main(String[] args){
        // test cases (with -ve, +ve and zero values)
        int[] arr1 = {1, -2, -3, 4, 5};
        int k1 = 2;
        System.out.println(maxProductOfKSubarray(arr1, arr1.length, k1)); // Output: 15 (4*5)
        int[] arr2 = {-1, -2, -3, -4};
        int k2 = 3;
        System.out.println(maxProductOfKSubarray(arr2, arr2.length, k2));
        int[] arr3 = {0, -1, 2, -3, 4};
        int k3 = 2;
        System.out.println(maxProductOfKSubarray(arr3, arr3.length, k3));
        int[] arr4 = {0, 0, 0};
        int k4 = 2;
        System.out.println(maxProductOfKSubarray(arr4, arr4.length, k4)); // Output: 0
        int[] arr5 = {1, 2, 3, 0, 4, 5};
        int k5 = 3;
        System.out.println(maxProductOfKSubarray(arr5, arr5.length, k5));
    }

    private static long maxProductOfKSubarray(int[] arr, int n, int k) {
        long maxProduct = Long.MIN_VALUE, currentProduct = 1, zeroCount = 0;
        for (int i = 0; i < k; i++) {
            if (arr[i] == 0) {
                zeroCount++;
            } else {
                currentProduct *= arr[i];
            }
        }
        if (zeroCount == 0) {
            maxProduct = Math.max(maxProduct, currentProduct);
        } else {
            maxProduct = Math.max(maxProduct, 0);
        }

        for (int i = k; i < n; i++) {
            // removing the first element of the previous window before adding the new element to prevent overflow
            if (arr[i - k] == 0) {
                zeroCount--;
            } else {
                currentProduct /= arr[i - k];
            }
            // adding the new element
            if (arr[i] == 0) {
                zeroCount++;
            } else {
                currentProduct *= arr[i];
            }
            // updating maxProduct
            if (zeroCount == 0) {
                maxProduct = Math.max(maxProduct, currentProduct);
            } else {
                maxProduct = Math.max(maxProduct, 0);
            }
        }
        return maxProduct;
    }
}