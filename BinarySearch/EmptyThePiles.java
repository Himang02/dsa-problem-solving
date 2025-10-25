import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.IOException;

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
