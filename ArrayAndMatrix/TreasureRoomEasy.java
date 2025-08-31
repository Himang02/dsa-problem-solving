/* P8: Treasure Room (Easy)
    * You have been exploring the tomb of some long dead person. There is a treasure room here full of vast riches. 
    * However, the door to it requires some trickery to unlock. There are N blocks of varied lengths available to you. 
    * The i-th block has length li. On both sides of the door, you must place one block each. Only when the sum of lengths of the blocks is equal to the width of the door will the door open. 
    * The width of the door is W. So, you should pick two blocks i and j such that li+lj=W. Which blocks should you pick?
    * Input: Two integers on the first line, N W. N integers on the second line where the i-th integer is li.
    * Constraints: 1≤N≤106 && 1≤W≤106 && 1≤li≤106
    * Output: Any two distinct integers i and j such that li+lj=W. If it is impossible, output −1.
*/

import java.util.*;

public class TreasureRoomEasy {
    public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    int W = sc.nextInt();
    
    int[] arr = new int[N];
    for(int index = 0; index < N; index++){
      arr[index] = sc.nextInt();
    }
    
    // approach1(arr, W);
    // approach2(arr, W);
    approach3(arr, W);
  }

  // Approach 1: Iterate through all pairs, TC: (O(N^2)), SC: O(1)
  private static void approach1(int[] arr, int W){
    int N = arr.length;
    for(int i = 0; i < N; i++){
      for(int j = i + 1; j < N; j++){
        if(arr[i] + arr[j] == W){
          System.out.print((i + 1) + " " + (j + 1));
          return;
        }
      }
    }
    System.out.print(-1);
  }

  // Approach 2: Two pointers, TC: O(N), SC: O(1) (Works only if array is sorted)
  private static void approach2(int[] arr, int W){
    int N = arr.length;
    Arrays.sort(arr);
    int left = 0;
    int right = N - 1;
    while(left < right){
      if(arr[left] + arr[right] == W){
        System.out.print((left + 1) + " " + (right + 1));
        return;
      }
      else if(arr[left] + arr[right] < W){
        left++;
      }
      else{
        right--;
      }
    }
    System.out.print(-1);
  }

  // Approach 3: Hashing, TC: O(N), SC: O(N)
  private static void approach3(int[] arr, int W){
    int N = arr.length;
    Map<Integer, Integer> map = new HashMap<>();
    for(int index = 0; index < N; index++){
      if(map.containsKey(W - arr[index])){
        System.out.print((map.get(W - arr[index]) + 1) + " " + (index + 1));
        return;
      }
      map.put(arr[index], index);
    }
    System.out.print(-1);
  }

}