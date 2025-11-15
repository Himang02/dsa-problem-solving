import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * You are at a candy shop, which has an unlimited type of candies and the number of candies of the i−th type is i.
 * Each candy costs 1 rupee and you have n rupees.
 * If you purchase a particular type of candy, you need to purchase all the candies of that type and the types less than that.
 * Find the maximum number of types of candies you can purchase with n rupees. For e.g. if you purchase 2 types of candies, you need to purchase 1 unit of the first type and 2 units of the second type.
 * INPUT: A single integer n - amount of money you have. 0≤n≤1018.
 * OUTPUT: Output a single integer - the maximum number of types of candies.
*/
public class MaximumCandies{
  public static void main(String[] args)  throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
    
    long n = Long.parseLong(st.nextToken());
    long L= 1, R = 2;
    while(pred(n, R)){
      R*=2;
    }
    L = R/2;
    
    while(R-L>1){
      long M = L + (R-L)/2;
      if(pred(n, M)){
        L = M;
      }
      else{
        R = M;
      }
    }
    System.out.println(L);
    
  }
  
  private static boolean pred(long n, long M){
    return ((M*(M+1))/2) <= n && ((M*(M+1))/2 > 0);
  }
}