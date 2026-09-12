package Graph.Dfs;

import java.util.*;
import java.io.*;

/*
Maximum Value In Every Subtree  (practice variant -- not from AlgoUniversity)

You are given a TREE with N nodes numbered 1 to N, rooted at node 1. Each node
carries an integer value, and values may be NEGATIVE.

The SUBTREE of a node u is u together with every node below it. For every node,
print the largest value appearing anywhere in its subtree (including u's own
value).

Constraints:
    1 <= N <= 1e5
    -1e9 <= value of a node <= 1e9

INPUT
First line contains a single integer N: the number of nodes.
Second line contains N integers: the values of node 1, node 2, ..., node N.
The next N - 1 lines contain two integers each, u and v, denoting an undirected
edge between node u and node v.

OUTPUT
N integers separated by spaces: the maximum value in the subtree of node 1,
node 2, ..., node N.

EXAMPLE
    Sample 1 INPUT:
        7
        5 3 8 9 1 2 7
        1 2
        1 3
        2 4
        2 5
        3 6
        6 7
    Sample 1 OUTPUT:
        9 9 8 9 1 7 7

    The same tree shape as SubtreeSizes. Node 4 holds the global maximum 9, so
    both node 2 and the root report 9. Node 3 only sees itself (8), node 6 (2)
    and node 7 (7), so its answer is 8 -- NOT the global maximum. That is the
    whole point: each node reports what is below IT.

    Sample 2 INPUT:
        1
        42
    Sample 2 OUTPUT:
        42

    A lone root reports its own value. There are no edge lines.

    Sample 3 INPUT:
        3
        -5 -9 -7
        1 2
        2 3
    Sample 3 OUTPUT:
        -5 -7 -7

    Every value is negative. Node 2's subtree is {-9, -7}, so its answer is -7.
    If you start your running maximum at 0, every answer here comes out 0 and
    the whole thing is wrong.

HINT (post-order again): identical shape to SubtreeSizes, with two changes --
you combine with max instead of sum, and you SEED with the node's own value
instead of with 1:

    int best = value[u];                // NOT 0, and no need for MIN_VALUE
    for (int v : adj[u]) {
        if (v == parent) continue;
        best = Math.max(best, maxInSubtree(v, u));
    }
    ans[u] = best;
    return best;

Seeding with value[u] is both simpler and safer than seeding with
Integer.MIN_VALUE: it is automatically correct for a leaf, and it removes any
chance of an all-negative subtree being reported as 0.

Same 64 MB thread wrapper as before -- a path-shaped tree still recurses N deep.

Things to get right:
  - values can be negative; seeding the accumulator at 0 is the classic way to
    fail Sample 3 while passing the other two
  - the root's answer is the global maximum, which is a free self-check, but no
    other node's answer has to equal it
  - a leaf's answer is just its own value
  - |value| <= 1e9 fits in an int, and max never accumulates, so there is no
    overflow risk here -- unlike a SUM version of this problem, which would
    need long
  - the values line holds N integers on ONE line, separate from the edge lines
  - N = 1 means no edge lines follow at all
*/
public class MaxInSubtree {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        Map<Integer, Integer> indexMap = new HashMap<>();
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < n; i++){
            indexMap.put(i, Integer.parseInt(st.nextToken()));
        }

        List<List<Integer>> adjList = new ArrayList<>();
        for(int i = 0; i < n; i++){
            adjList.add(new ArrayList<>());
        }

        for(int i = 0; i < n-1; i++){
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            adjList.get(u-1).add(v-1);
            adjList.get(v-1).add(u-1);
        }

        Runnable r = new Runnable(){
            @Override 
            public void run(){
                solve(adjList, indexMap);
            }
        };

        Thread thread = new Thread(null, r, "MaxFinder", 1<<26);
        thread.start();
    }

    private static void solve(List<List<Integer>> adjList, Map<Integer, Integer> indexMap){
        int[] maxValues = new int[adjList.size()];

        getMax(0, -1, adjList, indexMap, maxValues);

        StringBuilder st = new StringBuilder();
        for(int i = 0; i < maxValues.length; i++){
            st.append(maxValues[i]).append(' ');
        }
        System.out.println(st);
    }

    private static int getMax(int currNode, int parent, List<List<Integer>> adjList, Map<Integer, Integer> indexMap, int[] maxValues){

        // transition
        int maxValue = indexMap.get(currNode);
        List<Integer> currList = adjList.get(currNode);
        for(Integer num : currList){
            if(num != parent){
                maxValue = Math.max(maxValue, getMax(num, currNode, adjList, indexMap, maxValues));
            }
        }

        return maxValues[currNode] = maxValue;
    }
}

/*
REVIEW NOTES  (2026-09-12)

1. VERDICT: no bugs. Passes all 3 samples, a star with the max at a leaf,
   child-first edge ordering, +-1e9 extremes, a 100k-node path with random
   values (0.49s, exact match), and 300 random trees run with all-negative,
   all-positive and mixed value sets -- 0 mismatches.

2. THE TRAP, AVOIDED: seeding with the node's own value
   (int maxValue = indexMap.get(currNode)) is what makes Sample 3 work. Seeding
   the accumulator at 0 would still pass Samples 1 and 2 and silently print 0
   for every all-negative subtree. Always seed a max with a real element, never
   with 0.

3. DONE WELL: nodes are converted to 0-indexed ONCE at read time
   (adjList.get(u-1).add(v-1)), so the recursion never repeats currNode-1
   arithmetic. Cleaner than TreeHeight/SubtreeSizes -- do it this way from now on.

4. DONE WELL: reading stays on main and only the recursion moves to the 64 MB
   thread. Input parsing is iterative and never needs a deep stack, and main
   keeps its legitimate `throws IOException` instead of needing a try/catch.

5. IMPROVE: Map<Integer,Integer> indexMap should just be int[] values. The keys
   are the dense range 0..n-1, which is what an array is for; the HashMap costs
   ~3 objects per entry plus hashing on every lookup, for zero benefit. Reserve
   HashMap for sparse or non-integer keys (like the city names in
   AlgoLandJourney). The name also misleads: it maps index -> value, not to an
   index.

6. SAFE, DO NOT "FIX": `num != parent` compares Integer against int, so it
   unboxes and compares by value. Making both sides Integer would turn it into
   a reference comparison and break for node numbers above 127.
*/
