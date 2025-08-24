import java.util.Arrays;

public class ArraysMethods {
    public static void main(String[] args) {
        int arr[] = {5, 2, 8, 7, 1};
        System.out.println(arr.length);
        System.out.println(arr[0]);
        System.out.println(Arrays.toString(arr));
        
        // sorting array
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));

        // searching in array (binary search, array must be sorted)
        System.out.println(Arrays.binarySearch(arr, 2));   // if element not found then return -(insertion point) - 1
        System.out.println(Arrays.binarySearch(arr, 6));
        
        // fill array with specific value
        int arr2[] = new int[5];
        Arrays.fill(arr2, 10);
        System.out.println(Arrays.toString(arr2));

        // copy array
        int arr3[] = Arrays.copyOf(arr, arr.length);       // copy entire array
        System.out.println(Arrays.toString(arr3));
        int arr4[] = Arrays.copyOfRange(arr, 1, 4);       // copy from index 1 to index 4-1
        System.out.println(Arrays.toString(arr4));

        // compare two arrays
        int arr5[] = {1, 2, 3};
        int arr6[] = {1, 2, 3};
        int arr7[] = {1, 2, 4};
        System.out.println(Arrays.equals(arr5, arr6));
        System.out.println(Arrays.equals(arr5, arr7));




        // TODO -> comparator and comparables, binary search with comparator
    }
}