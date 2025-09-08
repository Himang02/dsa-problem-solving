import java.util.*;

/* Ceil and Floor in a Sorted Array
 * Given a sorted array A of size N and an integer K, find the floor and ceil of K in the array A.
 * Floor of K is the largest element in the array which is smaller than or equal to K.
 * Ceil of K is the smallest element in the array which is greater than or equal to K.
 * If floor or ceil does not exist, output -1 for them.
 * 1≤N≤10^5 && 1≤A[i],K≤10^9
 * Input: The first line contains two space separated integers N and K. The second line contains N space separated integers denoting the elements of array A.
 * Output: Output two space separated integers denoting the floor and ceil of K respectively.
 */

public class CeilAndFloor{
    public static void main(String[] args) {
        int[] arr1 = {1, 1, 2, 2, 3, 4, 5};
        int k = 0;
        
        System.out.println("Ceil: " + getCeil(arr1, 0));
        System.out.println("Floor: " + getFloor(arr1, 0));
        System.out.println("Ceil: " + getCeil(arr1, 1));
        System.out.println("Floor: " + getFloor(arr1, 1));
        System.out.println("Ceil: " + getCeil(arr1, 2));
        System.out.println("Floor: " + getFloor(arr1, 2));
        System.out.println("Ceil: " + getCeil(arr1, 5));
        System.out.println("Floor: " + getFloor(arr1, 5));
        System.out.println("Ceil: " + getCeil(arr1, 6));
        System.out.println("Floor: " + getFloor(arr1, 6));
    }


    private static int getCeil(int[] arr, int target){
        int start=-1, end=arr.length;

        while(end-start>1){
            int mid = start +(end-start)/2;
            if(arr[mid]>=target){
                end =  mid;
            }
            else{
                start = mid;
            }
        }
        if(end==arr.length){
            return -1;
        }
        return arr[end];

    }

    private static int getFloor(int[] arr, int target){
        int start = -1, end = arr.length;
        
        while(end-start>1){
            int mid = start + (end-start)/2;
            if(arr[mid]<=target){
                start = mid;
            }
            else{
                end = mid;
            }
        }

        if(start == -1){
            return -1;
        }
        return arr[start];
    }
}