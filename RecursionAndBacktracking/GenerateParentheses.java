import java.util.*; 
public class GenerateParentheses { 
    static int noOfRecursiveCalls = 0;
    public static void main(String[] args) { 
        Set<String> set = new HashSet<>();
        Set<String> alreadyRecursed = new HashSet<>(); 
        getBracketPermutations("", 4, set, alreadyRecursed); 
        for(String s : set){ 
            System.out.println(s); 
        } 
        System.out.println(set.size()); 
        System.out.println(noOfRecursiveCalls); 
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
}