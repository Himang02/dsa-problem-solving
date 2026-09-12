package Graph.Dfs;

import java.util.*;
import java.io.*;

/*
Size Of Every Subtree  (practice variant -- not from AlgoUniversity)

You are given a TREE with N nodes numbered 1 to N, rooted at node 1. A tree
with N nodes has exactly N - 1 edges and is connected.

The SUBTREE of a node u is u together with every node that lies below it -- all
nodes whose path to the root passes through u. Its SIZE is how many nodes that
is, counting u itself.

Print the subtree size of every node, from node 1 to node N.

Constraints:
    1 <= N <= 1e5

INPUT
First line contains a single integer N: the number of nodes.
The next N - 1 lines contain two integers each, u and v, denoting an undirected
edge between node u and node v.

OUTPUT
N integers separated by spaces: the subtree size of node 1, node 2, ..., node N.

EXAMPLE
    Sample 1 INPUT:
        7
        1 2
        1 3
        2 4
        2 5
        3 6
        6 7
    Sample 1 OUTPUT:
        7 3 3 1 1 2 1

    The same tree as TreeHeight. Leaves 4, 5 and 7 have size 1. Node 6 holds
    itself plus 7, so 2. Node 2 holds itself plus 4 and 5, so 3. Node 3 holds
    itself plus 6 and 7, so 3. The root holds everything, so 7 -- the root's
    answer is ALWAYS N.

    Sample 2 INPUT:
        1
    Sample 2 OUTPUT:
        1

    A lone root. Note that size counts NODES, so this is 1, not 0 -- unlike
    height, which counts edges and gives 0 here.

    Sample 3 INPUT:
        5
        1 2
        2 3
        3 4
        4 5
    Sample 3 OUTPUT:
        5 4 3 2 1

    On a path each node holds exactly the tail below it.

HINT (post-order DFS): the recurrence is

    size(u) = 1 + sum over children c of size(c)

which looks like the height recurrence but differs in one important way: you
must have the answer for EVERY child before you can finish u, and you ADD them
all rather than taking a max. So do the work on the way back UP:

    int size = 1;                       // count u itself
    for (int v : adj[u]) {
        if (v == parent) continue;
        size += subtree(v, u);          // recurse first
    }
    subtreeSize[u] = size;              // record only after the loop
    return size;

That "record after the loop" placement is what makes it a POST-order traversal.
Storing into subtreeSize[u] before recursing would save a value that is not
finished yet.

Reuse the parent-skip from TreeHeight, and the same 64 MB thread wrapper -- a
path-shaped tree recurses N deep here too, and the default stack overflows
around 9000 frames.

WORTH KNOWING: this one can also be done without recursion, but not by a plain
BFS reading answers off as it goes. Run a BFS from the root recording the order
nodes come out of the queue, then walk that order BACKWARDS adding each node's
size into its parent. Reverse BFS order guarantees every child is finished
before its parent is reached -- it is post-order by another name.

Things to get right:
  - size counts the node itself, so every leaf is 1 and nothing is ever 0
  - subtreeSize[1] must come out as exactly N; it is a free self-check
  - the sizes sum to more than N overall (each node is counted once per
    ancestor), so do not expect the output to add up to N
  - N = 1 means no edge lines follow at all
  - print with one StringBuilder, not N separate prints
*/
public class SubtreeSizes {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());

        List<List<Integer>> adjList = new ArrayList<>();
        for(int i = 0; i < n; i++){
            adjList.add(new ArrayList<>());
        }

        for(int i = 0; i < n - 1; i++){
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            adjList.get(u-1).add(v);
            adjList.get(v-1).add(u);
        }

        Runnable solverRunnable = new Runnable() {
            @Override 
            public void run(){
                solve(adjList);
            }
        };


        Thread solverThread = new Thread(null, solverRunnable, "SolverThread", 1<<26);
        solverThread.start();
    }

    private static void solve(List<List<Integer>> adjList){
        int[] dp = new int[adjList.size()];
        Arrays.fill(dp, -1);
        
        getSubtreeSize(1, -1, adjList, dp);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < dp.length; i++){
            sb.append(dp[i]).append(' ');
        }
        System.out.println(sb);
    }

    private static int getSubtreeSize(int currNode, int parent, List<List<Integer>> adjList, int[] dp){
        // memory
        if(dp[currNode-1] != -1){
            return dp[currNode-1];
        }
        
        // transition
        int subtreeSize = 1;
        List<Integer> currentList = adjList.get(currNode -1);
        for(int i = 0; i < currentList.size(); i++){
            if(currentList.get(i) != parent){
                subtreeSize += getSubtreeSize(currentList.get(i), currNode, adjList, dp);
            }
        }
        return dp[currNode-1] = subtreeSize;
    }
    
}

/*
REVIEW NOTES  (2026-09-12)

1. VERDICT: correct, no bugs. Samples, star, and child-first edge ordering all
   pass; a 100,000-node path prints exactly 100000 99999 ... 2 1 in 2.36s; and
   300 random trees with shuffled edges and random orientation gave zero
   mismatches.

2. THE POST-ORDER PLACEMENT IS RIGHT: dp[currNode-1] is written only AFTER the
   child loop, via `return dp[currNode-1] = subtreeSize;`. That assignment-as-
   expression stores and returns in one step, and its position after the loop is
   what makes this post-order. Writing before recursing would cache a value that
   is not finished yet -- the classic way this problem breaks.

3. SIZE vs HEIGHT: same shape, one word different.
       height(u) = 1 + MAX over children
       size(u)   = 1 + SUM over children
   With height you can take each child's answer as it arrives; with size you
   need EVERY child finished before u is done.

4. THREADING IMPROVED ON THE TEMPLATE: input parsing stays on main and only the
   recursion moves to the 64 MB thread. Reading is iterative and has no business
   on a deep-stack thread, and main keeps its legitimate `throws IOException`
   instead of needing a try/catch. Do it this way from now on.

5. THE MEMO NEVER FIRES: `if(dp[currNode-1] != -1) return dp[currNode-1];` is
   dead on a tree -- with the parent skip every node is reached exactly once, so
   dp is purely the output array. Harmless, and the -1 fill is a useful
   unvisited marker, but memoization only earns its place when a node can be
   reached by several routes (a DAG, or DP over states).

6. FREE SELF-CHECK: dp[0] must come out as exactly N. And note the sizes sum to
   MORE than N overall -- each node is counted once per ancestor -- so do not
   expect the output to add up to N.
*/
