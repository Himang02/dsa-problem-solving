package StacksAndQueues;

import java.util.ArrayDeque;
import java.util.Deque;

/*
Longest Valid Bracket Substring — THREE bracket types: ()  []  {}

Given a string containing only the characters ( ) [ ] { }, return the length of
the LONGEST contiguous substring that is a valid (properly matched and nested)
bracket sequence.

This is LeetCode 32 (Longest Valid Parentheses) generalized to three types.
The extra wrinkle vs. the single-type version: a TYPE MISMATCH (e.g. ')' meeting
a '[' on top) is a hard invalid boundary — no valid substring can span across it.

Hint (stack of indices, like LC 32):
    keep a stack of indices; track lastInvalid = index of the last position that
    cannot belong to any valid substring (start it at -1).
    for each i, c:
        c is an opener            -> push i
        c closes & top matches    -> pop; base = stack.isEmpty() ? lastInvalid
                                          : stack.peek(); max = max(max, i - base)
        otherwise (unmatched OR wrong type) -> lastInvalid = i; stack.clear()
*/
public class LongestValidBrackets {

    public static int longestValid(String s) {

        Deque<int[]> stack = new ArrayDeque<>();
        int maxLen = 0;
        int startingIndex = 0;
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '(' || s.charAt(i) == '{' || s.charAt(i) == '['){
                int type = (s.charAt(i) == '(') ? 1 : (s.charAt(i) == '{') ? 2 : 3;
                stack.push(new int[]{type, i});
            }
            else{
                if(!stack.isEmpty() &&  ((s.charAt(i) == ')'  && stack.peek()[0] == 1) || (s.charAt(i) == '}'  && stack.peek()[0] == 2) || (s.charAt(i) == ']'  && stack.peek()[0] == 3))){
                    stack.pop();
                    if(stack.isEmpty()){
                        maxLen = Math.max(maxLen, i - startingIndex + 1);
                    }
                    else{
                        maxLen = Math.max(maxLen, i - stack.peek()[1]);
                    }                    
                }
                else{
                    startingIndex = i + 1;
                    stack.clear();
                }
            }
        }

        return maxLen;
    }

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        // empty string
        total++; passed += check("", 0) ? 1 : 0;

        // simplest valid pairs
        total++; passed += check("()", 2) ? 1 : 0;
        total++; passed += check("()[]{}", 6) ? 1 : 0;

        // fully nested across all three types
        total++; passed += check("([{}])", 6) ? 1 : 0;
        total++; passed += check("([]{})", 6) ? 1 : 0;

        // interleaved / crossed brackets -> NOT valid anywhere
        total++; passed += check("([)]", 0) ? 1 : 0;
        total++; passed += check("[(])", 0) ? 1 : 0;

        // valid prefix only: trailing wrong-type close breaks the tail
        total++; passed += check("(()]", 2) ? 1 : 0;      // leading "()"
        total++; passed += check("(()", 2) ? 1 : 0;       // inner "()"

        // valid block, then an invalid crossed block afterwards
        total++; passed += check("{[]}[(])", 4) ? 1 : 0;  // "{[]}"

        // RECOVERY: mismatch in the middle, then a fresh valid pair at the end
        total++; passed += check("([)]()", 2) ? 1 : 0;    // trailing "()"

        // lone / reversed brackets
        total++; passed += check("(]", 0) ? 1 : 0;
        total++; passed += check(")(", 0) ? 1 : 0;
        total++; passed += check("{[]}()", 6) ? 1 : 0;

        System.out.println("--------------------------------------");
        System.out.println("Passed " + passed + " / " + total + " tests");
    }

    private static boolean check(String s, int expected) {
        int actual = longestValid(s);
        boolean ok = actual == expected;
        System.out.println(
            (ok ? "PASS" : "FAIL") +
            " | s=\"" + s + "\"" +
            " | expected=" + expected + " actual=" + actual);
        return ok;
    }
}
