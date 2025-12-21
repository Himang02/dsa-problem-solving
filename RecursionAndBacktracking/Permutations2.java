import java.util.ArrayList;
import java.util.List;


class Permutations2 {
    public static void main(String[] args) {
        int[] nums = {1,1,2};
        List<List<Integer>> perms = permuteUnique(nums);
        for(List<Integer> perm : perms){
            System.out.println(perm);
        }
    }

    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> perms = new ArrayList<>();
        int[] freq = new int[21];
        for(int i: nums){
            freq[i+10]++;
        }
        List<Integer> currentList = new ArrayList<>();
        getPermutations(perms, freq, currentList, nums);
        return perms;
    }

    private static void getPermutations(List<List<Integer>> perms, int[] freq, List<Integer> currentList, int[] nums){
        // base case
        if(currentList.size() == nums.length){
            perms.add(new ArrayList<>(currentList));
            return;
        }

        // transition
        for(int i = 0; i< freq.length; i++){
            if(freq[i] != 0){
                // action
                currentList.add(i-10);
                freq[i]--;
                
                // recursion
                getPermutations(perms, freq, currentList, nums);
                
                //cancellation
                currentList.remove(currentList.size()-1);
                freq[i]++;

            }
        }
    }
}