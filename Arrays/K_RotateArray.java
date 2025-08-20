import java.util.*;

/*
    * https://www.geeksforgeeks.org/dsa/array-rotation/
    * Given an array of integers arr[] of size n, the task is to rotate the array elements to the left by d positions.
*/
public class K_RotateArray {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    
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
}