import java.util.*;


public class LexicographicStringPermutations {
  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    String str = sc.next();
    boolean used[] = new boolean[str.length()];
    getNextLexPerm("", getSortedString(str), used);
    
    

  }
  
  private static void getNextLexPerm(String current, String str, boolean[] used){
    // base condition
    if(current.length() == str.length()){
      System.out.println(current);
      return;
    }
    
    // transistion
    for(int index = 0; index < str.length(); index++){
      if(!used[index]){
        // action
        current += str.charAt(index);
        used[index] = true;
        
        //recurse
        getNextLexPerm(current, str, used);
        
        // cancellation
        current = current.substring(0, current.length() - 1);
        used[index] = false;
      }
    }
    
  }
  
  private static String getSortedString(String s){
    int[] hash = new int[26];
    for(int i = 0; i < s.length(); i++){
      hash[s.charAt(i)-'a']++;
    }
    String result = "";
    for(int i = 0; i < 26; i++){
      while(hash[i]>0){
        char add = 'a';
        add += i;
        result += add;
        hash[i]--;
      }
    }
    // System.out.println(result);
    return result;
  }
}