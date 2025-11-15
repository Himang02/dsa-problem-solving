import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.IOException;

/*
 * Empty The Piles :
 * There are n piles of stones arranged in row where the i−th pile has ai stones. You can perform certain operations on these piles. 
 * In one operation, you can select any non-empty pile and remove at most k stones from it. 
 * Your goal to remove all the stones (i.e. empty all the n piles) in at most h operations. 
 * Find the minimum value of k such that you can empty all the piles within h operations.
 * INPUT: The first line contains 2 integers - n and h, 1≤n≤105 and n≤h≤109. The next line contains n spaced integers - ai, the number of stones in the i−th pile. 1≤ai≤109 for 0≤i≤n−1.
 * OUTPUT: Output a single integer - the minimum value of k.
 */

public class EmptyThePiles {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String line = br.readLine();
    StringTokenizer st = new StringTokenizer(line);
    int n = Integer.parseInt(st.nextToken());
    int h = Integer.parseInt(st.nextToken());
    int[] arr = new int[n];
    line = br.readLine();
    st = new StringTokenizer(line);
    int max = 0;
    for(int i = 0; i < n; i++){
      arr[i] = Integer.parseInt(st.nextToken());
      max = Math.max(max, arr[i]);
    }
    
    int L = 0, R = max;
    
    while(R - L > 1){
      int M = (L + (R - L)/2);
      if(predicate(M, h, arr)){
        R = M;
      }
      else{
        L = M;
      }
    }
    System.out.println(R);
    

  }
  
  private static boolean predicate(int M, int h, int[] arr){
    long ops = 0;
    for(int i = 0; i < arr.length; i++){
      ops += (arr[i] + M - 1) / M;
      if(ops>h){
        return false;
      }
    }
    return true;
  }
}
