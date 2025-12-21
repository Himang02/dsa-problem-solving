import java.util.*; 
public class GenerateParentheses { 
    static int noOfRecursiveCalls = 0;
    public static void main(String[] args) { 
        Set<String> set = new HashSet<>();
        Set<String> alreadyRecursed = new HashSet<>(); 
        getBracketPermutations("", 8, set, alreadyRecursed); 
        for(String s : set){ 
            System.out.println(s); 
        } 
        System.out.println("Size of set: " + set.size()); 
        System.out.println("No. of recursive calls: " + noOfRecursiveCalls); 

        noOfRecursiveCalls = 0;
        List<String> combs = new ArrayList<>();
        getBracketPermutations2("", combs, 0, 0, 8);

        System.out.println("Size of list: " + combs.size());
        System.out.println("No. of recursive calls: " + noOfRecursiveCalls);
        // for(String s : combs){
        //     System.out.println(s);
        // }
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
}