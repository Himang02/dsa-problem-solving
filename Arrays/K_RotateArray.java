import java.util.*;

/*
    * https://www.geeksforgeeks.org/dsa/array-rotation/
    * Given an array of integers arr[] of size n, the task is to rotate the array elements to the left by d positions.
*/

// Approach - 1
public class K_RotateArray {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(-12%7);
    
        rotateArrayByD(arr, 1);
        System.out.println(Arrays.toString(arr));
        rotateArrayByD(arr, 8);
        System.out.println(Arrays.toString(arr));
        rotateArrayByD(arr, 2);
        System.out.println(Arrays.toString(arr));
        rotateArrayByD(arr, 7);
        System.out.println(Arrays.toString(arr));
        rotateArrayByD(arr, 3);
        System.out.println(Arrays.toString(arr));
        rotateArrayByD(arr, 6);
        System.out.println(Arrays.toString(arr));
        rotateArrayByD(arr, 4);
        System.out.println(Arrays.toString(arr));
        rotateArrayByD(arr, 5);
        System.out.println(Arrays.toString(arr));
        
        
        System.out.println("----------");
        rotateArrayByReversal(arr, 1);
        System.out.println(Arrays.toString(arr));
        rotateArrayByReversal(arr, 8);
        System.out.println(Arrays.toString(arr));
        rotateArrayByReversal(arr, 2);
        System.out.println(Arrays.toString(arr));
        rotateArrayByReversal(arr, 7);
        System.out.println(Arrays.toString(arr));
        rotateArrayByReversal(arr, 3);
        System.out.println(Arrays.toString(arr));
        rotateArrayByReversal(arr, 6);
        System.out.println(Arrays.toString(arr));
        rotateArrayByReversal(arr, 4);
        System.out.println(Arrays.toString(arr));
        rotateArrayByReversal(arr, 5);
        System.out.println(Arrays.toString(arr));

    }

    private static void rotateArrayByD(int[] arr, int d) {
        int count = 0, index = 0, temp = arr[0];
        int n = arr.length;
        while(count < n) {
            if(((count*d) % n) == 0) {
                index = (index + 1) % n;
                temp = arr[index];
            }
            temp = dShiftElement(arr, index, d, temp);
            index = (index + d) % n;
            count++;
        }
    }

    private static int dShiftElement(int[] arr, int index, int d, int replaceWith){
        int nextIndex = (index + d) % arr.length;
        int replaced = arr[nextIndex];
        arr[nextIndex] = replaceWith;
        return replaced;
    }


    // Approach - 2

    private static void rotateArrayByReversal(int[] arr, int d){
        reverseArray(arr, 0, arr.length-d-1);
        reverseArray(arr, arr.length-d, arr.length-1);
        reverseArray(arr, 0, arr.length-1);
    }

    private static void reverseArray(int[] arr, int from, int to){
        int index = from;

        if(from < to){
            while(index < arr.length && index <= from + (to - from) / 2 ){
                int temp = arr[index];
                arr[index] = arr[to - index + from];
                arr[to - index + from] = temp;
                index++;
            }
        }
        
    }
}