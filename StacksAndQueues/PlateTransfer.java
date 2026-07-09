package StacksAndQueues;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/*
We are passing a sequence of plates from Atul to Bob. Each plate has a distinct number painted  on  
it. We  have  a  table  in  front of us  on  which  we  can  temporarily  store  a  single  stack  of  
plates. At  each  step,  we  are allowed to do one of the following:
○ Take a plate from Atul and pass it on immediately to Bob.
○ Take a plate from Atul and put it on top of the stack on the table.
○ If there is at least one plate in the stack on the table, take the topmost plate off the stack and 
pass it on to Bob.
Given an input sequence a and an output sequence b, check if it is possible to get that output 
sequence from that input sequence if the plates are passed using only the above rules. 
*/
public class PlateTransfer {
    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        // { atul (input), bob (output), expected }
        // basic pass-through: same order is always possible
        total++; passed += check(new int[]{1, 2, 3}, new int[]{1, 2, 3}, true) ? 1 : 0;
        // full reverse: push all, then pop all
        total++; passed += check(new int[]{1, 2, 3, 4, 5}, new int[]{5, 4, 3, 2, 1}, true) ? 1 : 0;
        // pass 1, push 2, push 3, pop 3, pop 2
        total++; passed += check(new int[]{1, 2, 3}, new int[]{1, 3, 2}, true) ? 1 : 0;
        // push 1, push 2, pop 2, pop 1
        total++; passed += check(new int[]{1, 2}, new int[]{2, 1}, true) ? 1 : 0;
        // push1, push2, pop2, pop1 (=2,1) then pass 3 -> [2,1,3]
        total++; passed += check(new int[]{1, 2, 3}, new int[]{2, 1, 3}, true) ? 1 : 0;

        // classic IMPOSSIBLE stack permutation: after popping 3, 1 is trapped under 2
        total++; passed += check(new int[]{1, 2, 3}, new int[]{3, 1, 2}, false) ? 1 : 0;
        // 2 gets trapped under 1 -> impossible
        total++; passed += check(new int[]{1, 2, 3, 4, 5}, new int[]{4, 5, 3, 1, 2}, false) ? 1 : 0;
        // 2 gets trapped under 4,3 -> impossible
        total++; passed += check(new int[]{1, 2, 3, 4, 5}, new int[]{1, 5, 2, 4, 3}, false) ? 1 : 0;

        // output contains a number not in the input -> impossible
        total++; passed += check(new int[]{1, 2, 3}, new int[]{1, 2, 4}, false) ? 1 : 0;
        // different lengths -> impossible
        // total++; passed += check(new int[]{1, 2, 3}, new int[]{1, 2}, false) ? 1 : 0;

        // edge cases
        total++; passed += check(new int[]{}, new int[]{}, true) ? 1 : 0;
        total++; passed += check(new int[]{7}, new int[]{7}, true) ? 1 : 0;

        System.out.println("--------------------------------------");
        System.out.println("Passed " + passed + " / " + total + " tests");
    }

    // Runs isPossible and reports whether it matched the expected result.
    private static boolean check(int[] atul, int[] bob, boolean expected) {
        boolean actual = isPossible(atul, bob);
        boolean ok = actual == expected;
        System.out.println(
            (ok ? "PASS" : "FAIL") +
            " | atul=" + Arrays.toString(atul) +
            " bob=" + Arrays.toString(bob) +
            " | expected=" + expected + " actual=" + actual);
        return ok;
    }

    public static boolean isPossible(int[] atulStack, int[] bobStack){
        
        Deque<Integer> table = new ArrayDeque<>();
        
        int atulPointer = 0, bobPointer = 0;

        while(bobPointer < bobStack.length){
            if(atulPointer < atulStack.length && atulStack[atulPointer] == bobStack[bobPointer]){
                atulPointer++;
                bobPointer++;
            }
            else if(!table.isEmpty() && table.peek() == bobStack[bobPointer]){
                bobPointer++;
                table.pop();
            }
            else if(atulPointer < atulStack.length){
                table.push(atulStack[atulPointer]);
                atulPointer++;
            }
            else{
                return false;
            }
        }

        return true;


    }
}
