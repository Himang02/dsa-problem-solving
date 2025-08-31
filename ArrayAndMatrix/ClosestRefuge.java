import java.util.*;

/*
    * You are given an integer N. You are also given an array A consisting of N integers.
    * Find the first natural number that is not present in A.
    * Input: One integer N on the first line. N integers on the second line. The i-th integer is vi.
    * Constraints: 1≤N≤106 && 1≤vi≤109
*/

public class ClosestRefuge {
    public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    
    int[] arr = new int[N];
    for(int index = 0; index < N; index++){
      arr[index] = sc.nextInt();
    }
    
    int index = 0;
    while(index < N){
      if(arr[index] <= N && arr[arr[index]-1]!=arr[index]){
        int temp = arr[index];
        arr[index] = arr[temp-1];
        arr[temp-1] = temp;
      }
      else{
        index++;  
      }
      
    }
    
    index = 0;
    while(index<N){
      if(index+1 != arr[index]){
        System.out.print((index+1)+" ");
        break;
      }
      index++;
    }
    if(index==N){
      System.out.print(N+1);
    }
  }

}