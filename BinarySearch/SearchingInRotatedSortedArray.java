import java.util.*;

public class SearchingInRotatedSortedArray {
    public static void main(String[] args) {
        int[] arr = {4,5,6,7,0,1,2};
        int target = 0;
        int index = search(arr, target);
        System.out.println("Target " + target + " found at index: " + index);
    }

    public static int search(int[] nums, int target) {
        // finding pivot
        int start = -1, end = nums.length;
        while(end-start>1){
            int mid = start + (end-start)/2;
            if(pivotPredicate(nums[0], nums[mid])==0){
                start = mid;
            }
            else{
                end = mid;
            }
        }
    
        // checking element lies on which side of pivot
        if(target >= nums[0]){
            end = start+1;
            start = -1;
        }
        else{
            end = nums.length;
        }
        // System.out.println("start:"+ start+" "+ ", end: "+ end);
        while(end-start>1){
            int mid = start + (end-start)/2;
            if(lessThanEqualPredicate(target, nums[mid])==0){
                start = mid;
            }
            else{
                end = mid;
            }
        }
        if(nums[start]!= target){
            return -1;
        }
        return start;
    }

    private static int pivotPredicate(int firstElement, int currentElement){
        // System.out.println("first:"+ firstElement+" "+ ", current: "+ currentElement);
        if(firstElement <= currentElement){
        // System.out.println("returning 0");
            return 0;
        }
        return 1;
    }

    private static int lessThanEqualPredicate(int target, int currentElement){
        if(target >= currentElement){
            return 0;
        }
        return 1;
    }
}