package StacksAndQueues;

import java.util.ArrayDeque;
import java.util.Deque;

/*
Minimum Remove to Make Valid Parentheses  (LeetCode 1249)

Given a string s of '(', ')' and lowercase English letters, remove the MINIMUM
number of parentheses so that the resulting string is valid, and return any such
valid string. Letters are never removed; only parentheses may be removed.

    Input: "a)b(c)d"   -> "ab(c)d"
    Input: "))(("       -> ""

Because ANY minimal valid answer is accepted, this harness does not compare
against a fixed expected string. Instead it VALIDATES your output:
    (1) it is a subsequence of the input (only deletions, order preserved),
    (2) every letter of the input is still present (only parens removed),
    (3) it is balanced, and
    (4) exactly the minimum number of parentheses were removed.

Hint (stack of indices): push indices of '('; on ')' pop a matching '(' or, if
none, mark this ')' for removal; any '(' left on the stack at the end is also
marked for removal. Build the result skipping the marked indices.
*/
public class MinRemoveValidParentheses {

    public static String minRemoveToValid(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        
        int i = 0;
        while(i < s.length()){
            if(s.charAt(i) == '('){
                stack.push(i);
                i++;
            }
            else if(s.charAt(i) == ')'){
                if(stack.isEmpty()){
                    s = s.substring(0, i) + s.substring(i+1);
                }
                else{
                    stack.pop();
                    i++;
                }
            }
            else{
                i++;
            }
        }

        while(!stack.isEmpty()){
            int index = stack.pop();
             s = s.substring(0, index) + s.substring(index+1);
        }


        return s;
    }

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        String[] inputs = {
            "a)b(c)d",          // one stray ')'
            "))((",             // nothing salvageable -> ""
            "lee(t(c)o)de)",    // one stray ')' at the end
            "(a(b(c)d)",        // one stray '(' somewhere
            "",                 // empty
            "abc",              // no parens at all -> unchanged
            "(((",              // all opens removed
            ")))",              // all closes removed
            "()",               // already valid
            "(())",             // already valid, nested
            "a)(b",             // one stray ')' and one stray '(' -> "ab"
            ")(",               // both stray -> ""
            "(a)()",            // already valid with letters
            "(u(mr))ok)(",      // mixed strays on both sides
            "))a((b))c((",      // strays surrounding valid middles
        };

        for (String in : inputs) {
            total++;
            passed += check(in) ? 1 : 0;
        }

        System.out.println("--------------------------------------");
        System.out.println("Passed " + passed + " / " + total + " tests");
    }

    // Validates the produced output instead of comparing to a fixed answer.
    private static boolean check(String s) {
        String out = minRemoveToValid(s);
        int minR = minRemoval(s);

        boolean sub = isSubsequence(out, s);
        boolean lettersOk = lettersOnly(out).equals(lettersOnly(s));
        boolean balanced = isBalanced(out);
        boolean minimal = out.length() == s.length() - minR;
        boolean ok = sub && lettersOk && balanced && minimal;

        StringBuilder why = new StringBuilder();
        if (!sub) why.append("not-subsequence ");
        if (!lettersOk) why.append("letters-changed ");
        if (!balanced) why.append("not-balanced ");
        if (!minimal) why.append("not-minimal(removed=" + (s.length() - out.length())
                + " need=" + minR + ") ");

        System.out.println(
            (ok ? "PASS" : "FAIL") +
            " | s=\"" + s + "\" -> \"" + out + "\"" +
            (ok ? "" : "  [" + why.toString().trim() + "]"));
        return ok;
    }

    // Minimum number of parentheses that must be removed to make s valid.
    private static int minRemoval(String s) {
        int open = 0, removals = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                open++;
            } else if (c == ')') {
                if (open > 0) open--;
                else removals++;
            }
        }
        return removals + open;
    }

    // Is `out` obtainable from `s` by deletions only (order preserved)?
    private static boolean isSubsequence(String out, String s) {
        int j = 0;
        for (int i = 0; i < s.length() && j < out.length(); i++) {
            if (s.charAt(i) == out.charAt(j)) j++;
        }
        return j == out.length();
    }

    // Are the parentheses in `t` balanced (letters ignored)?
    private static boolean isBalanced(String t) {
        int bal = 0;
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c == '(') bal++;
            else if (c == ')') {
                bal--;
                if (bal < 0) return false;
            }
        }
        return bal == 0;
    }

    // The non-parenthesis characters of `t`, in order.
    private static String lettersOnly(String t) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c != '(' && c != ')') sb.append(c);
        }
        return sb.toString();
    }
}
