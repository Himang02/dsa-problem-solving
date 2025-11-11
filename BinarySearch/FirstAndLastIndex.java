import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.IOException;


/* You are given an array of n integers sorted in non-decreasing order.
 * You are also given q queries. In each query, you are given a single integer and you need to find the first and last occurence of that integer in the given array(considering 0-indexing).
 * If the element is not present in the array, output −1−1.
 * INPUT: The first line contains 2 integers n,q(1≤n,q≤105). The next line contains n separated integers a0,a1,...,an−1(−109≤ai≤109)- the given array. 
 * Each of the next q lines contain a single integer - x(−109≤x≤109).
 * OUTPUT: Output q lines, the answer to each query on a separate line.
 
 */

public class FirstAndLastIndex {
    public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
    
    int n = Integer.parseInt(st.nextToken());
    int q = Integer.parseInt(st.nextToken());
    int[] arr = new int[n];
    // int[] query = new int[q];
    st = new StringTokenizer(br.readLine());
    for(int i = 0; i< n; i++){
      arr[i] = Integer.parseInt(st.nextToken());
    }
    
    
    for(int i = 0; i< q; i++){
      st = new StringTokenizer(br.readLine());
      int num = Integer.parseInt(st.nextToken());
      int fO = getFirstOcc(arr, num);
      int lO = getLastOcc(arr, num);
      System.out.println(fO + " "+lO);
    }
    
    
    
  }
  
  private static int getFirstOcc(int[] arr, int num){
    int L = -1, R = arr.length-1;
    while(R-L>1){
      int M = L + (R-L)/2;
      if(arr[M]>=num){
        R = M;
      }
      else{
        L = M;
      }
    }
    if(arr[R]==num){
      return R;
    }
    return -1;
  }
  private static int getLastOcc(int[] arr, int num){
    int L = 0, R = arr.length;
    while(R-L>1){
      int M = L + (R-L)/2;
      if(arr[M]<=num){
        L = M;
      }
      else{
        R = M;
      }
    }
    if(arr[L]==num){
      return L;
    }
    return -1;
  }
}