import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * There are n rectangular boxes of the same size: w in width and h in length. 
 * You are required to find a square of the smallest size into which these boxes can be packed. Boxes cannot be rotated.
 * INPUT: The first line contains the number of testcases t(1≤t≤104). The input for each test case contains three integers w,h,n(1≤w,h,n≤109).
 * OUTPUT: Output t lines. For each test case, output the minimum length of a side of a square, into which the given boxes can be packed.
 */


public class BoxPacking {
  public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
    int t = Integer.parseInt(st.nextToken());
    
    for(int i = 0; i< t; i++){
      st = new StringTokenizer(br.readLine());
      int w = Integer.parseInt(st.nextToken());
      int h = Integer.parseInt(st.nextToken());
      int n = Integer.parseInt(st.nextToken());
      System.out.println(getMinSquareSize(w, h, n));
    }

  }
  
  private static long getMinSquareSize(int w, int h, int n){
    long L = 0, R = Math.max((long)w*(long)n, (long)h*(long)n);
    
    while(R-L>1){
      long M = L + (R-L)/2;
      if(pred(w, h, n, M)){
        R = M;
      }
      else{
        L = M;
      }
    }
    return R;
  }
  
  private static boolean pred(int w, int h, int n, long M){
    long hor = M/w;
    long ver = M/h;
    if(ver == 0) return false;
    return hor >= (n + ver - 1) / ver;
  }
}