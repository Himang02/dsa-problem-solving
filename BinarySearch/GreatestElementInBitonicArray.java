import java.util.*;

public class GreatestElementInBitonicArray {
    public static void main(String[] args) {

        // Test case 1
        int[] bitonicArray1 = {1, 3, 8, 12, 4, 2};
        System.out.println("Greatest element in bitonicArray1: " + findGreatestInBitonicArray(bitonicArray1));
        
        // Test case 2
        int[] bitonicArray2 = {3, 8, 3, 1};
        System.out.println("Greatest element in bitonicArray2: " + findGreatestInBitonicArray(bitonicArray2));

        // Test case 3
        int[] bitonicArray3 = {1, 3, 8, 12};
        System.out.println("Greatest element in bitonicArray3: " + findGreatestInBitonicArray(bitonicArray3));

        // Test case 4
        int[] bitonicArray4 = {10, 9, 8};
        System.out.println("Greatest element in bitonicArray4: " + findGreatestInBitonicArray(bitonicArray4));

        // Test case 5
        int[] bitonicArray5 = {1};
        System.out.println("Greatest element in bitonicArray4: " + findGreatestInBitonicArray(bitonicArray5));
        
    }

    public static int findGreatestInBitonicArray(int[] arr) {
        int start = -1, end = arr.length;

        while(end - start > 1){
            int mid = start + (end - start)/2;

            if(mid == arr.length - 1 || arr[mid] > arr[mid + 1]){ // predicate is 1
                end = mid;
            }
            else{
                start = mid;
            }
        }

        return arr[end];
    }
}