import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

public class RangeSearchQueries {
  public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int q = Integer.parseInt(st.nextToken());
    
    int[] arr = new int[n];
    st = new StringTokenizer(br.readLine());
    for(int i = 0; i< n ; i++){
      arr[i] = Integer.parseInt(st.nextToken());
    }
    
    Arrays.sort(arr);
    
    
    for(int i =0; i< q; i++){
      st = new StringTokenizer(br.readLine());
      int l = Integer.parseInt(st.nextToken());
      int r = Integer.parseInt(st.nextToken());
      System.out.print(getEndIndex(arr, r) - getStartIndex(arr, l)+" ");
    }

  }
  
  private static int getStartIndex(int[] arr, int start){
    int l = -1, r = arr.length;
    while(r-l>1){
      int m = l + (r-l)/2;
      if(arr[m]>=start){
        r = m;
      }
      else{
        l = m;
      }
    }
    return r;
  }
  
  private static int getEndIndex(int[] arr, int end){
    int l = -1, r = arr.length;
    while(r-l>1){
      int m = l + (r-l)/2;
      if(arr[m]>end){
        r = m;
      }
      else{
        l = m;
      }
    }
    return r;
  }
  
}