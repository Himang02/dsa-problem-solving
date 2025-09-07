import java.util.*;

/* Searching Algo
 * 1. Define Search Space. Eg: For linear search - [0, N-1], [1, N-1]...
 * 2. Navigate the search space and reduce it.
 * 3. Rule of Invariant: L should always be in T0 and R in T1.
 * If P is a monotonous function (non-increasing or non-decreasing), the it can be converted to SBA using predicate function.
 */

public class SBATemplate {
    public static void main(String[] args) {
       int[] arr1 = {1, 1, 1, 1, 1, 1, 1};
       System.out.println("Last 0's index: " + binarySearch(arr1, -1, arr1.length));
       int[] arr2 = {0, 0, 0, 0, 0, 0, 0};
       System.out.println("Last 0's index: " + binarySearch(arr2, -1, arr2.length));
       int[] arr3 = {0, 0, 0, 0, 1, 1, 1};
       System.out.println("Last 0's index: " + binarySearch(arr3, -1, arr3.length));
    }

    public static int binarySearch(int[] arr, int start, int end){
        int L = start, R = end;

        while(R-L>1){
            int M = L + (R-L)/2; // To avoid overflow, (L+R)/2 may exceed int range
            if(arr[M]==0){
                L = M;
            }
            else{
                R = M;
            }
        }
        return L; // or R, depending on what is required
    }
}