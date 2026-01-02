import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Arrays;

// You find yourself on the number-line and you start off at the origin (that is, with the x-coordinate 0). 
// In a single jump, you can jump ahead by one of the N jump lengths ai. 
// Formally, when you are at a co-ordinate x and you choose a jump-length of ai, in one jump, you land on the co-ordinate x+ai.


You pick a subset of jump lengths and perform these jumps in any order. You wonder if there is a way to reach the destination using such a subset of jumps. Note that choosing all of the jump lengths is a valid move. Output ''YES`` (without quotes) if it is possible, or ''NO`` (without quotes) otherwise.
// Note that a particular index can be included at most once in the subset, but however, two different indices might have the same jump-length value.

public class JumpPossibility {
  public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    int t = Integer.parseInt(br.readLine().trim());
    
    for(int testCase = 0; testCase < t; testCase++){
      StringTokenizer st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      int S = Integer.parseInt(st.nextToken());
      
      st = new StringTokenizer(br.readLine());
      int[] jumpLengths = new int[N];
      for(int i = 0; i < jumpLengths.length; i++){
        jumpLengths[i] = Integer.parseInt(st.nextToken());
      }
      
      boolean[][] dp = new boolean[S+1][jumpLengths.length+1];
      
      
      if(isPossible(S, 0, jumpLengths, dp)){
        System.out.println("YES");
      }
      else{
        System.out.println("NO");
      }
    }

  }
  
  // state: remainingDistance, index : is it possible to cover remainingDistance from jump options from index to jumpLengths.length
  private static boolean isPossible(int remainingDistance, int index, int[] jumpLengths, boolean[][] dp){
    // memory
    if(dp[remainingDistance][index]){
      return false;
    }
    
    // base case
    if(remainingDistance == 0){
      return true;
    }
    if(index == jumpLengths.length){
      dp[remainingDistance][index] = true;
      return false;
    }
    
    
    // transition
    if((remainingDistance - jumpLengths[index] >= 0 && index < jumpLengths.length && isPossible(remainingDistance - jumpLengths[index], index+1, jumpLengths, dp)) || 
    (index < jumpLengths.length && isPossible(remainingDistance, index+1, jumpLengths, dp))){
      return true;
    }
    dp[remainingDistance][index] = true;
    
    return false;
    
  }

  // state: remainingDistance, index : is it possible to cover remainingDistance from jump options from index to jumpLengths.length
  private static boolean isPossible(int remainingDistance, int[] jumpLengths){
    // memory
    boolean[][] dp = new boolean[remainingDistance+1][jumpLengths.length];
     
    // base case
    for(int i = 0; i < jumpLengths.length; i++){
      dp[0][i] = true;
    }
    
    for(int i = 0; i <= remainingDistance; i++){
      if(remainingDistance == jumpLengths[jumpLengths.length-1]){
        dp[i][jumpLengths.length-1] = true;
      }
    }
    
    // transition
    for(int dist = 1; dist <= remainingDistance; dist++){
      for(int index = jumpLengths.length-2; index >=0; index--){
        
        dp[dist][index] = (dist-jumpLengths[index] >= 0 && dp[dist-jumpLengths[index]][index+1]) || (dp[dist][index+1]);
      }
    }
    
    
    return dp[remainingDistance][0];
  }
  
}