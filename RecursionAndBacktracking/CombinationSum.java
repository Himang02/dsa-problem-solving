import java.util.*;

public class CombinationSum {

    public static void main(String[] args) {
        int[] candidates = {2, 3, 5};
        int target = 8;
        List<List<Integer>> result = combinationSum(candidates, target);
        System.out.println(result);
    }

    private static List<List<Integer>> combinationSum(int[] candidates, int target){
        List<Integer> current = new ArrayList<>();
        List<List<Integer>> combs = new ArrayList<>();
        getCombinations(current, target, 0, candidates, combs);
        return combs;
    }

    private static void getCombinations(List<Integer> current, int remainingSum, int index, int[] candidates, List<List<Integer>> combs){
        // base case
        if(remainingSum == 0){
            List<Integer> newList = new ArrayList<>(current);
            combs.add(newList);
        }

        // transisiton
        for(; index < candidates.length; index++){
            if(remainingSum - candidates[index] >= 0){
                // action
                current.add(candidates[index]);
                remainingSum -= candidates[index];

                // recur
                getCombinations(current, remainingSum, index, candidates, combs);

                // cancellation
                current.remove(current.size()-1);
                remainingSum += candidates[index];
            }
        }
    }
}