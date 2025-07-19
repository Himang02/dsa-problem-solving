import java.util.*;

public class PossiblePermutations{
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        System.out.println("Total arrangements for 0 elements: " + getAllPermutations(list)+"\n");
        list.add(1);
        System.out.println("Total arrangements for 1 elements: " + getAllPermutations(list)+"\n");
        list.add(2);
        System.out.println("Total arrangements for 2 elements: " + getAllPermutations(list)+"\n");
        list.add(3);
        System.out.println("Total arrangements for 3 elements: " + getAllPermutations(list)+"\n");
        list.add(4);
        System.out.println("Total arrangements for 4 elements: " + getAllPermutations(list)+"\n");
    }

    private static int getAllPermutations(List<Integer> list) {
        // No. of recursive calls = N*N! (factorial of N)
        // TC = No. of recursive calls * time complexity of each call
        // TC = (N * N!) * N  = O(N^2 * N!)

        // SC = O(N) for the recursion stack + O(N) for the current permutation
        // SC = O(N + N) = O(N)        
        List<Integer> current = new ArrayList<>();
        boolean[] used = new boolean[list.size()];
        return permute(current, used, list);
    }

    private static int permute(List<Integer> current, boolean[] used, List<Integer> list){
        System.out.println("Current permutation: " + current);
        
        // base condition
        if(current.size() == list.size()){
            return 1; // Found a valid arrangement
        }

        // transition
        int count = 0;
        for(int index = 0; index < list.size(); index++){
            if(!used[index]){
                // action
                current.add(list.get(index));
                used[index] = true;

                // recur
                count += permute(current, used, list);

                // cancellation
                current.remove(current.size() - 1);
                used[index] = false;
            }
        }


        return count+1;
    }
}