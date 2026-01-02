import java.util.Arrays;

// Find the minimum number of operations required to convert string s1 to s2
// You can perform the following operations on a string:
// 1. Insert a character
// 2. Remove a character
// 3. Replace a character

// PATTERN: perform all combinations

public class StringConversion {
    public static void main(String[] args) {
        String s1 = "sunday";
        String s2 = "saturday";
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\": " + minOperations(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Top-Down DP): " + minOperationsTopDown(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Bottom-Up DP): " + minOperationsBottomUp(s1, s2));

        // edge test cases
        s1 = "";
        s2 = "abc";
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\": " + minOperations(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Top-Down DP): " + minOperationsTopDown(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Bottom-Up DP): " + minOperationsBottomUp(s1, s2));

        s1 = "abc";
        s2 = "";
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\": " + minOperations(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Top-Down DP): " + minOperationsTopDown(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Bottom-Up DP): " + minOperationsBottomUp(s1, s2));

        s1 = "abc";
        s2 = "abc";
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\": " + minOperations(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Top-Down DP): " + minOperationsTopDown(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Bottom-Up DP): " + minOperationsBottomUp(s1, s2));

        s1 = "abcdefghi";
        s2 = "abdfghijk"; // expected 4
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\": " + minOperations(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Top-Down DP): " + minOperationsTopDown(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Bottom-Up DP): " + minOperationsBottomUp(s1, s2));

        s1 = "abcdefghi";
        s2 = "abcxyzghi"; // expected 3
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\": " + minOperations(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Top-Down DP): " + minOperationsTopDown(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Bottom-Up DP): " + minOperationsBottomUp(s1, s2));

        s1 = "abcdefghi";
        s2 = "xyzghijk";
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\": " + minOperations(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Top-Down DP): " + minOperationsTopDown(s1, s2, s1.length() - 1, s2.length() - 1));
        System.out.println("Minimum operations to convert \"" + s1 + "\" to \"" + s2 + "\" (Bottom-Up DP): " + minOperationsBottomUp(s1, s2));
    }


    // state: s1Index, s2Index: minimum operations to convert s1[0..s1Index] to s2[0..s2Index]
    private static int minOperations(String s1, String s2, int s1Index, int s2Index) {
        // base cases
        if(s1Index < 0){
            return s2Index + 1;
        }
        if(s2Index < 0){
            return s1Index + 1;
        }


        // transition
        if(s1.charAt(s1Index) == s2.charAt(s2Index)){
            return minOperations(s1, s2, s1Index - 1, s2Index - 1);
        }
        int replaceOp = 1 + minOperations(s1, s2, s1Index - 1, s2Index - 1);
        int deleteOp = 1 + minOperations(s1, s2, s1Index - 1, s2Index);
        int insertOp = 1 + minOperations(s1, s2, s1Index, s2Index - 1);
        return Math.min(replaceOp, Math.min(deleteOp, insertOp));
    }

    private static int minOperationsTopDown(String s1, String s2, int s1Index, int s2Index) {
        int[][] dp = new int[s1.length()][s2.length()];
        for(int[] row : dp) {
            Arrays.fill(row, -1);
        }

        int minOps = minOperationsTopDown(s1, s2, s1Index, s2Index, dp);
        // print dp
        // for(int[] row : dp) {
        //     System.out.println("--> "+Arrays.toString(row));
        // }
        return minOps;
    }

    private static int minOperationsTopDown(String s1, String s2, int s1Index, int s2Index, int[][] dp) {
        // base cases
        if(s1Index < 0){
            return s2Index + 1;
        }
        if(s2Index < 0){
            return s1Index + 1;
        }
        if(dp[s1Index][s2Index] != -1){
            return dp[s1Index][s2Index];
        } 

        

        // transition
        if(s1.charAt(s1Index) == s2.charAt(s2Index)){
            dp[s1Index][s2Index] = minOperationsTopDown(s1, s2, s1Index - 1, s2Index - 1, dp);
            return dp[s1Index][s2Index];
        }

        int replaceOp = 1 + minOperationsTopDown(s1, s2, s1Index - 1, s2Index - 1, dp);
        int deleteOp = 1 + minOperationsTopDown(s1, s2, s1Index - 1, s2Index, dp);
        int insertOp = 1 + minOperationsTopDown(s1, s2, s1Index, s2Index - 1, dp);
        dp[s1Index][s2Index] = Math.min(replaceOp, Math.min(deleteOp, insertOp));
        
        return dp[s1Index][s2Index];
    }

    private static int minOperationsBottomUp(String s1, String s2) {
        // memory
        int s1Len = s1.length();
        int s2Len = s2.length();
        int[][] dp = new int[s1Len+1][s2Len+1];

        // base case
        for(int i = 0; i <= s1Len; i++) {
            dp[i][0] = i;
        }
        for(int j = 0; j <= s2Len; j++) {
            dp[0][j] = j;
        }
        // transition
        for(int i = 1; i <= s1Len; i++) {
            for(int j = 1; j <= s2Len; j++) {
                if(s1.charAt(i-1) == s2.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1];
                    continue;
                }

                int minOp = Math.min(
                    1 + dp[i-1][j-1], // replace
                    Math.min(
                        1 + dp[i-1][j],   // delete
                        1 + dp[i][j-1]    // insert
                    )
                );
                dp[i][j] = minOp;



                // if(s1.charAt(i) == s2.charAt(j)){
                //     dp[i][j] = dp[i-1][j-1];
                // } else {
                //     int replaceOp = 1 + dp[i-1][j-1];
                //     int deleteOp = 1 + dp[i-1][j];
                //     int insertOp = 1 + dp[i][j-1];
                //     dp[i][j] = Math.min(replaceOp, Math.min(deleteOp, insertOp));
                // }
            }
        }
        // print dp
        for(int[] row : dp) {
            System.out.println(Arrays.toString(row));
        }
        return dp[s1Len][s2Len];
    }
}