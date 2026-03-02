import java.util.*; 
public class GenerateParentheses { 
    static int noOfRecursiveCalls = 0;
    public static void main(String[] args) { 
        // Set<String> set = new HashSet<>();
        // Set<String> alreadyRecursed = new HashSet<>(); 
        // getBracketPermutations("", 8, set, alreadyRecursed); 
        // for(String s : set){ 
        //     System.out.println(s); 
        // } 
        // System.out.println("Size of set: " + set.size()); 
        // System.out.println("No. of recursive calls: " + noOfRecursiveCalls); 

        // noOfRecursiveCalls = 0;
        List<String> combs = new ArrayList<>();
        getBracketPermutations2("", combs, 0, 0, 6);
        for(String s : combs){
            System.out.println(s);
        }

        System.out.println("Size of list: " + combs.size());
        combs = new ArrayList<>();
        getBracketPermutations3(6, 6, "", combs);

        // System.out.println("Size of list: " + combs.size());
        // System.out.println("No. of recursive calls: " + noOfRecursiveCalls);
        for(String s : combs){
            System.out.println(s);
        }
        System.out.println("Size of list: " + combs.size());

        int[][] dp = new int[7][7];
        for(int i = 0; i< dp.length; i++){
            Arrays.fill(dp[i], -1);
        }
        System.out.println(noOfBracketPermutations(6, 6, dp));

    } 
    private static void getBracketPermutations(String currentString, int n, Set<String> set, Set<String> alreadyRecursed){ 
        noOfRecursiveCalls++;
        // base condition 
        if(n == 0){ 
            set.add(currentString); 
            return; 
            
        } 
        // transition 
        for(int index = 0; index<=currentString.length(); index++){ 
            String newString = currentString.substring(0, index) + "()" + currentString.substring(index);
            if(!alreadyRecursed.contains(newString)) {
                alreadyRecursed.add(newString);
                getBracketPermutations(newString, n-1, set, alreadyRecursed); 
            } 
        } 
    }

    private static void getBracketPermutations2(String cur, List<String> combs, int open, int close, int n){
        noOfRecursiveCalls++;
        // base case
        if(cur.length() == 2*n){
            combs.add(cur);
            return;
        }

        // transisiton
        if(open < n){
            getBracketPermutations2(cur + "(", combs, open+1, close, n);
        }
        if(close< open){
            getBracketPermutations2(cur + ")", combs, open, close+1, n);
        }
    }

    // since we need all the paths, will use backtracking
    // since the order of brackets matter, we will use try-all-possibilities pattern
    // state: remaining open, remaining close
    // path: current string
    private static void getBracketPermutations3(int remainingOpen, int remainingClose, String cur, List<String> combs){
        noOfRecursiveCalls++;
        // base case
        if(remainingOpen == 0 && remainingClose == 0){
            combs.add(cur);
            return;
        }

        // transisiton
        if(remainingOpen > 0){
            getBracketPermutations3(remainingOpen-1, remainingClose, cur + "(", combs); // action (on string), recusion and cancellation all in one line
        }
        if(remainingClose > remainingOpen){
            getBracketPermutations3(remainingOpen, remainingClose-1, cur + ")", combs); // action (on string), recusion and cancellation all in one line
        }
    }


    // since the path - remaining open and close brackets is small, we can use memoization to avoid repeated calculations and reduce the number of recursive calls.
    // state: remaining open, remaining close
    private static int noOfBracketPermutations(int remainingOpen, int remainingClose, int[][] dp){
        // memory
        if(dp[remainingOpen][remainingClose] != -1){
            return dp[remainingOpen][remainingClose];
        }
        
        // base case
        if(remainingOpen == 0 && remainingClose == 0){
            return dp[remainingOpen][remainingClose] = 1;
        }

        // transisiton
        int count = 0;
        if(remainingOpen > 0){
            count = noOfBracketPermutations(remainingOpen-1, remainingClose, dp);
        }
        if(remainingClose > remainingOpen){
            count += noOfBracketPermutations(remainingOpen, remainingClose-1, dp);
        }
        return dp[remainingOpen][remainingClose] = count;
    }
}