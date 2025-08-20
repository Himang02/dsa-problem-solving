import java.util.*;
import java.lang.*;

public class SubsetSum {
   // state : subA, index
  public static void main(String[] args) {
    int n;
    Scanner sc = new Scanner(System.in);
    n = sc.nextInt();
    int[] arr = new int[n];
    long totalSum = 0;
    for(int i = 0; i < n; i++){
      arr[i] = sc.nextInt();
      totalSum += arr[i];
    }
    System.out.println(getMinDiff(0, 0, arr, totalSum));
    
  }
  
  private static long getMinDiff(long subA, int index, int[] arr, long totalSum){
    // base condition
    if(index == arr.length){
      return Math.abs(totalSum - subA - subA);
    }
    
    // transition
    long minDiff = Long.MAX_VALUE;
    for(; index < arr.length; index++){
      // recurse
      minDiff = Math.min(getMinDiff(subA + arr[index], index + 1, arr, totalSum), minDiff);
      
    }
    
    return minDiff;
  }
}