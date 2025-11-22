import java.io.*;
import java.util.*;

/*
 * Kita and Soodle are besties. Soodle needs to construct a string A. Everyone knows a string can be easily constructed by appending characters. 
 * For Example, to construct a string “MUSEOUM”, you can take ‘M’ and append ‘U’, ‘S’, and so on.
 * But Kita won’t let soodle create the string in this obvious way. She has another string B. Kita wants Soodle to prove his bestiness. She wants Soodle to create his string only by concatenating B. To make things slightly easy, she allows Soodle to concatenate B any number of times he wants. He can also drop any letters he wants from B from each concatenation. For example, If B is “MOUSE”, Soodle can concatenate “MUSE”, “OU”, “M” to create “MUSEOUM”.
 * Your task is to find the minimum number of copies of B required to create A. If it is not possible to create A from B, output -1.
 * Input: First line contains the number of test cases T. Each test case contains two: lines First line contains the string A. Second line contains string B Both the strings only contain uppercase letters (‘A’,‘B’,…‘Z’)
 * Output: Output a single integer telling the minimum number of copies required for each test case. 
 * Constraints: T <=100, Length of A <= 10^5, Length of B <= 10^5
*/

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int T = Integer.parseInt(br.readLine());

    for (int i = 0; i < T; i++) {
      String a = br.readLine();
      String b = br.readLine();
      System.out.println(getCopies(a, b));
    }
  }

  private static int getCopies(String a, String b) {
    List<List<Integer>> map = new ArrayList<>();
    for(int i = 0; i< 26; i++){
      map.add(new ArrayList<>());
    }
    for(int i = 0; i< b.length(); i++){
      map.get(b.charAt(i)-'A').add(i);
    }
    int aIndex = 0, bIndex = -1, count =1;
    while(aIndex<a.length()){
      if(map.get(a.charAt(aIndex)-'A').size()==0){
        return -1;
      }

      int mapIndex = a.charAt(aIndex)-'A';
      int l = -1, r = map.get(mapIndex).size();
      while(r-l>1){
        int m = l+(r-l)/2;
        if(map.get(mapIndex).get(m)>bIndex){
          r = m;
        }
        else{
          l = m;
        }
      }
      if(r==map.get(mapIndex).size()){
        bIndex=-1;
        count++;
      }
      else{
        bIndex = map.get(mapIndex).get(r);
        aIndex++;
      }
    }
    return count;
  }
}
