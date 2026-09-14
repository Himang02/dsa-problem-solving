package Graph.Dfs;

import java.util.*;
import java.io.*;

/*
Most Profitable Team
https://www.algouniversity.com/problem/225/?asid=2041

You are given the employee hierarchy of a High Frequency Trading company. Every
employee except the CEO has exactly one direct boss. Employees are numbered 1
to N, and employee 1 is the CEO.

You are also given the profit/loss each employee DIRECTLY makes for the company.

A team headed by employee X consists of X plus everyone who reports to X
directly or indirectly -- that is, X's subtree. There are N teams, one per
employee; an employee who manages nobody still heads a team of just themselves.

Report the maximum profit made by any team.

Constraints:
    1 <= N <= 2e5
    -200 <= profit/loss of any employee <= 200

INPUT
First line has an integer N, the number of employees.
Next line contains N - 1 integers: for each employee 2, 3, ..., N, their direct
boss.
Next line contains N integers: the profit/loss each employee directly generates.

OUTPUT
A single integer: the profit/loss of the most profitable team.

EXAMPLE
    Sample 1 INPUT:
        5
        1 2 3 4
        10 10 -100 15 15
    Sample 1 OUTPUT:
        30

    The hierarchy is a straight chain 1 -> 2 -> 3 -> 4 -> 5. Team totals from
    the bottom up: team 5 = 15, team 4 = 15 + 15 = 30, team 3 = -100 + 30 =
    -70, team 2 = 10 - 70 = -60, team 1 = 10 - 60 = -50. The best is 30.

    Notice the CEO's team is NOT the answer, and team 4 beats team 5 even
    though both contain good employees -- one loss high up poisons everything
    above it.

HINT (subtree sums, then a max): this is SubtreeSizes with values instead of
1s. Post-order:

    total(u) = value[u] + sum over children c of total(c)

Compute total(u) for every u, and answer with the largest of the N totals.

The input shape is different from the tree problems so far, and EASIER: you are
handed the parent of every employee directly, so the tree already has a
direction. There are no undirected edges, no parent to skip, and none of the
edge-orientation ambiguity that RedAlert had. Just build children lists:

    for (int i = 2; i <= n; i++) children[boss[i]].add(i);

Things to get right:
  - PROFITS CAN BE NEGATIVE, so the answer can be negative. Seed your running
    maximum with a real total (or Integer.MIN_VALUE), never with 0 -- a company
    losing money everywhere would otherwise report 0, which is no team at all.
    Same trap as MaxInSubtree.
  - int is ENOUGH here: the largest possible magnitude is 2e5 * 200 = 4e7, well
    inside int's 2.1e9. Worth checking rather than assuming -- a sum over a tree
    usually does need long, and it is only the tight -200..200 bound that saves
    you.
  - N = 1 means the second line is EMPTY (N - 1 = 0 bosses). The answer is just
    the CEO's own value, which may be negative.
  - every employee heads a team, including leaves -- so all N totals are
    candidates, not just the internal nodes
  - N up to 2e5 and the sample is already a CHAIN, so a recursive DFS will
    overflow the default stack. Use the 64 MB thread, or go iterative.

WORTH KNOWING (the no-traversal trick): if the input guarantees that every
employee's boss has a SMALLER number than they do -- true in the sample, where
boss[i] = i - 1 -- then you do not need a traversal at all:

    for (int i = n; i >= 2; i--) total[boss[i]] += total[i];

Walking i downward guarantees a child is finished before its boss is reached,
which is post-order for free, in O(N) with no recursion and no stack worries.
The statement does NOT actually promise boss[i] < i though, so only reach for
this if you have checked -- otherwise build the children lists and traverse.
*/
public class MostProfitableTeam {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());

        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        st = new StringTokenizer(br.readLine());
        if (n == 1) {
            st = new StringTokenizer(br.readLine());
            System.out.println(Integer.parseInt(st.nextToken()));
            return;
        }
        for (int i = 1; i < n; i++) {
            adjList.get(Integer.parseInt(st.nextToken()) - 1).add(i);
        }

        int[] profits = new int[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            profits[i] = Integer.parseInt(st.nextToken());
        }

        int[] teamProfit = new int[n];
        getProfit(0, adjList, profits, teamProfit);
        int maxProfit = teamProfit[0];
        for (int i = 1; i < n; i++) {
            maxProfit = Math.max(maxProfit, teamProfit[i]);
        }
        System.out.println(maxProfit);
    }

    private static int getProfit(int node, List<List<Integer>> adjList, int[] profits, int[] teamProfit) {

        // transition
        int profit = profits[node];
        List<Integer> currList = adjList.get(node);

        for (int num : currList) {
            int subordinateProfit = getProfit(num, adjList, profits, teamProfit);
            profit += subordinateProfit;
        }

        return teamProfit[node] = profit;
    }
}

/*
REVIEW NOTES  (2026-09-14)

1. VERDICT: the ALGORITHM is correct -- 200 random hierarchies vs a reference
   gave 0 mismatches, and the official sample gives 30. But as written it will
   FAIL the judge: see point 2.

2. OPEN BUG, FATAL -- NO STACK PROTECTION. getProfit recurses without the 64 MB
   thread, and N can be 2e5. Measured: chain N=5000 fine, chain N=9000
   StackOverflowError, chain N=200000 StackOverflowError. The OFFICIAL SAMPLE'S
   hierarchy is already a chain (1->2->3->4->5), so a deep chain is exactly what
   the judge will test. Wrap the solve step in the same thread used by
   TreeHeight / SubtreeSizes / MaxInSubtree / RedAlert -- verified to fix it
   (N=9000 -> 9000, N=200000 -> 200000):
       Runnable r = () -> { getProfit(...); ...; System.out.println(maxProfit); };
       new Thread(null, r, "solver", 1 << 26).start();
   NOTE the failure mode changes once you do: an overflow ON that thread exits
   with code 0 and no output, which looks like a wrong answer rather than a
   crash.

3. OPEN RISK -- N = 1 DEPENDS ON A BLANK LINE. With N = 1 there are zero bosses,
   and the code assumes the judge still emits an empty second line:
       "1

-7
"  -> -7      (blank line present)
       "1
-7
"    -> NullPointerException
   The statement cannot tell you which. Reading TOKENS on demand (StreamTokenizer,
   or a helper that refills from the next line when the tokenizer runs dry)
   removes the question entirely -- and the whole n == 1 special case with it.

4. DONE RIGHT -- NEGATIVES: maxProfit is seeded with teamProfit[0], not 0, so an
   all-loss company reports its least-bad team (-1 on a test of -5/-1/-3) rather
   than a meaningless 0. Same trap as MaxInSubtree, avoided again.

5. DONE RIGHT -- int IS ENOUGH, and it is worth having checked rather than
   assumed: the largest possible magnitude is 2e5 * 200 = 4e7, well inside int's
   2.1e9. Verified with an all-+200 tree at N=2e5 -> exactly 40000000. A subtree
   SUM usually does need long; only the tight -200..200 bound saves it here.

6. DONE RIGHT -- POST-ORDER PLACEMENT: `return teamProfit[node] = profit;`
   stores after the child loop, so nothing caches a half-finished total. Same
   shape as SubtreeSizes.

7. THE SHORTCUT THIS PROBLEM ALLOWS: because the parent of every employee is
   given directly, IF bosses always have smaller numbers than their reports
   (true in the sample, where boss[i] = i-1) the whole thing is four lines with
   no recursion, no thread and no depth question:
       for (int i = n; i >= 2; i--) total[boss[i]] += total[i];
   Walking i downward finishes every child before its boss. The statement does
   not promise boss[i] < i, so check before relying on it -- but this is the one
   input shape where the stack problem can be DODGED rather than worked around.
*/
