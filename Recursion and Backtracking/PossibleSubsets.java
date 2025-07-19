import java.util.*;

public class PossibleSubsets {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        System.out.println("Total arrangements for 0 elements: " + getAllSubsets(list) + "\n");
        list.add(1);
        System.out.println("Total arrangements for 1 elements: " + getAllSubsets(list) + "\n");
        list.add(2);
        System.out.println("Total arrangements for 2 elements: " + getAllSubsets(list) + "\n");
        list.add(3);
        System.out.println("Total arrangements for 3 elements: " + getAllSubsets(list) + "\n");
    }

    private static int getAllSubsets(List<Integer> list) {
        // No. of subsets = 2^N
        // TC = O(N * 2^N) for generating all subsets
        // SC = O(N) for the recursion stack + O(N) for storing subsets for each call
        List<Integer> current = new ArrayList<>();
        return generateSubsets(current, list, 0);
    }

    private static int generateSubsets(List<Integer> current, List<Integer> list, int index) {
        System.out.println("Current subset: " + current);
        // base case


        // transition
        int count = 0;
        for(; index < list.size(); index++){
            // action
            current.add(list.get(index));
            index++;

            // recur
            count += generateSubsets(current, list, index);

            // cancellation
            current.remove(current.size() - 1);
            index--;
        }

        return count + 1; // +1 for current subset

    }
}