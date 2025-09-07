
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
        System.out.println("Upper Bound of 1: " + getUpperBound(arr1, 1));
        System.out.println("Upper Bound of 2: " + getUpperBound(arr1, 2));
        System.out.println("Lower Bound of 0: " + getLowerBound(arr1, 0));
        System.out.println("Lower Bound of 1: " + getLowerBound(arr1, 1));
        System.out.println("Lower Bound of 2: " + getLowerBound(arr1, 2));
    }


    private static int getUpperBound(int[] arr, int target){
        int start = -1, end = arr.length;

        while(end - start > 1){
            int mid = start + (end - start) / 2;
            if(boundPredicate(arr[mid], target)){
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
            if(boundPredicate(arr[mid], target)){
                end = mid;
            }
            else{
                start = mid;
            }
        }
        return start;
    }

    private static boolean boundPredicate(int number, int target){
        return number > target;
    }
}