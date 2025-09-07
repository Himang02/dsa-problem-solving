
/* Upper Bound: Index of first element that is greater than the target. If all elements are less than or equal to the target, the upper bound is the length of the array
 * Lower Bound: Index of first element that is greater than or equal to the target. If all elements are less than the target, the lower bound is the length of the array
 * If target is present in the array, then lower bound is the index of first occurrence of target and upper bound is index of last occurrence + 1.
 * If target is not present in the array, then lower bound and upper bound are same.
 * Number of occurrences of target = upper bound - lower bound
 */

public class Bounds{
    public static void main(String[] args) {
        int[] arr1 = {1};
        System.out.println("Upper Bound of 0: " + getUpperBound(arr1, 0));
        System.out.println("Lower Bound of 0: " + getLowerBound(arr1, 0));
        System.out.println("Count of 0: " + getCount(arr1, 0));
        System.out.println("Upper Bound of 1: " + getUpperBound(arr1, 1));
        System.out.println("Lower Bound of 1: " + getLowerBound(arr1, 1));
        System.out.println("Count of 1: " + getCount(arr1, 1));
        System.out.println("Upper Bound of 2: " + getUpperBound(arr1, 2));
        System.out.println("Lower Bound of 2: " + getLowerBound(arr1, 2));
        System.out.println("Count of 2: " + getCount(arr1, 2));


        int[] arr2 = {1, 1};
        System.out.println("Upper Bound of 0: " + getUpperBound(arr2, 0));
        System.out.println("Lower Bound of 0: " + getLowerBound(arr2, 0));
        System.out.println("Count of 0: " + getCount(arr2, 0));
        System.out.println("Upper Bound of 1: " + getUpperBound(arr2, 1));
        System.out.println("Lower Bound of 1: " + getLowerBound(arr2,   1));
        System.out.println("Count of 1: " + getCount(arr2, 1));
        System.out.println("Upper Bound of 2: " + getUpperBound(arr2, 2));
        System.out.println("Lower Bound of 2: " + getLowerBound(arr2, 2));
        System.out.println("Count of 2: " + getCount(arr2, 2));

        int[] arr3 = {1, 2, 2, 2, 3, 4, 5};
        System.out.println("Upper Bound of 0: " + getUpperBound(arr3, 0));
        System.out.println("Lower Bound of 0: " + getLowerBound(arr3, 0));
        System.out.println("Count of 0: " + getCount(arr3, 0));
        System.out.println("Upper Bound of 1: " + getUpperBound(arr3, 1));
        System.out.println("Lower Bound of 1: " + getLowerBound(arr3, 1));
        System.out.println("Count of 1: " + getCount(arr3, 1));
        System.out.println("Upper Bound of 2: " + getUpperBound(arr3, 2));
        System.out.println("Lower Bound of 2: " + getLowerBound(arr3, 2));
        System.out.println("Count of 2: " + getCount(arr3, 2));
        System.out.println("Upper Bound of 3: " + getUpperBound(arr3,   3));
        System.out.println("Lower Bound of 3: " + getLowerBound(arr3,   3));
        System.out.println("Count of 3: " + getCount(arr3, 3));
        System.out.println("Upper Bound of 4: " + getUpperBound(arr3, 4));
        System.out.println("Lower Bound of 4: " + getLowerBound(arr3, 4));
        System.out.println("Count of 4: " + getCount(arr3, 4));
        System.out.println("Upper Bound of 5: " + getUpperBound(arr3, 5));
        System.out.println("Lower Bound of 5: " + getLowerBound(arr3, 5));
        System.out.println("Count of 5: " + getCount(arr3, 5));
        System.out.println("Upper Bound of 6: " + getUpperBound(arr3, 6));
        System.out.println("Lower Bound of 6: " + getLowerBound(arr3, 6));
        System.out.println("Count of 6: " + getCount(arr3, 6));
    }


    private static int getUpperBound(int[] arr, int target){
        int start = -1, end = arr.length;

        while(end - start > 1){
            int mid = start + (end - start) / 2;
            if(upperBoundPredicate(arr[mid], target)){
                end = mid;
            }
            else{
                start = mid;
            }
        }
        return end;
    }
    
    
    private static int getLowerBound(int[] arr, int target){
        int start = -1, end = arr.length;

        while(end - start > 1){
            int mid = start + (end - start) / 2;
            if(lowerBoundPredicate(arr[mid], target)){
                end = mid;
            }
            else{
                start = mid;
            }
        }
        return end;
    }

    private static boolean upperBoundPredicate(int number, int target){
        return number > target;
    }
    private static boolean lowerBoundPredicate(int number, int target){
        return number >= target;
    }

    private static int getCount(int [] arr, int target){
        int upperBound = getUpperBound(arr, target);
        int lowerBound = getLowerBound(arr, target);
        // System.out.println("Count of " + target + " is: " + (upperBound - lowerBound));
        return upperBound - lowerBound;
    }
}