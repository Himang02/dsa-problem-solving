import java.util.*;

public class SortedSquareArrayFromSortedArray {
    public static void main(String[] args) {
        int[] arr = {-6, -3, -1, 2, 4, 5};
        System.out.println(Arrays.toString(sortedSquareArrayApp1(arr)));
        System.out.println(Arrays.toString(sortedSquareArrayApp2(arr)));
    }

    // Approach - 1 square and then sort (o(nlogn))
    private static int[] sortedSquareArrayApp1(int[] arr) {
        int[] result = new int[arr.length];
        Arrays.setAll(result, i -> arr[i] * arr[i]);
        Arrays.sort(result);
        return result;
    }
    
    // Approach - 2 two pointer (TC: o(n), SC: o(n))
    private static int[] sortedSquareArrayApp2(int[] arr) {
        int[] result = new int[arr.length];
        int index = arr.length - 1;
        int left = 0, right = arr.length - 1;
        while(left <= right) {
            if(Math.abs(arr[left]) > Math.abs(arr[right])) {
                result[index--] = arr[left] * arr[left];
                left++;
            } else {
                result[index--] = arr[right] * arr[right];
                right--;
            }
        }
        return result;
    }

    // Approach - 3 (without extra space, if range of elements is known and small)
    // count sort with abs value and then fill the result array with squares in sorted order 
}