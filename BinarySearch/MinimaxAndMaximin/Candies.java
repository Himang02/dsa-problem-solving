import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * There are N boxes of candies and M small children. The number of candies in every box is given to you as an input array.
 * You have to divide the array of size N into M non-empty contiguous blocks such that every child gets one block of boxes.
 * Every child must have at least 1 box. You don't want the children to eat a lot of candies. 
 * So your task is to divide the array in such a way that the maximum number of total candies given to any child is minimized. 
 * Output this minimized number. In case the distribution isn't possible output -1.
 * INPUT: The first line contains two integers N and M. The second line contains N integers indicating the number of candies in the N boxes i.e. the array A.
 * OUTPUT: Output a single integer.
 */

public class Candies {
  public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());
    
    int[] arr = new int[N];
    st = new StringTokenizer(br.readLine());
    int max = 0;
    long sum = 0;
    for(int i=0; i< N; i++){
      arr[i] = Integer.parseInt(st.nextToken());
      sum+=arr[i];
      if(arr[i]>max){
        max = arr[i];
      }
    }
    
    if(N>=M){
      long l = max -1, r = sum;
      while(r-l>1){
        long m = l+(r-l)/2;
        if(pred(arr, m, M)){
          r= m;
        }
        else{
          l = m;
        }
      }
      System.out.println(r);
    }
    else{
      System.out.println(-1);
    }
    
  }
  
  private static boolean pred(int[] arr, long m, int M){
    long chocs=0;
    int children = 0, index = 0;
    while(index<arr.length){
      if(chocs+arr[index] <= m){
        chocs= chocs+arr[index];
        index++;
      }
      else{
        children++;
        chocs=0;
      }
      
      if(children>=M){
        // System.out.println("max: " + m + ", children: "+ children );
        return false;
      }
      
    }
  
    return true;
  }
}