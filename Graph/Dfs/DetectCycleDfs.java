package Graph.Dfs;

import java.util.*;
import java.io.*;

/*
Cycle Detection In An Undirected Graph -- DFS

You are given a SIMPLE undirected, unweighted graph with N nodes numbered 1 to
N and M edges. Determine whether it contains a cycle.

The graph need NOT be connected. A cycle anywhere in it counts.

This is the same question as Graph/Bfs/DetectCycleUndirected, deliberately --
solve it with DFS this time and compare the two. The rule is identical; what
changes is that the traversal is recursive, which brings its own hazard (see
the last note below).

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5
    the graph is simple: no self loops, no repeated edges

INPUT
First line contains two integers N and M.
The next M lines contain two integers each, u and v, denoting an undirected
edge between u and v.

OUTPUT
Print "YES" if the graph contains at least one cycle, otherwise "NO".

EXAMPLE
    Sample 1 INPUT:
        4 3
        1 2
        2 3
        3 1
    Sample 1 OUTPUT:
        YES

    Nodes 1, 2, 3 form a triangle. Node 4 is isolated and irrelevant.

    Sample 2 INPUT:
        4 3
        1 2
        1 3
        3 4
    Sample 2 OUTPUT:
        NO

    A tree: 3 edges over 4 connected nodes, no way back to a node you have
    already left.

    Sample 3 INPUT:
        6 4
        1 2
        3 4
        4 5
        5 3
    Sample 3 OUTPUT:
        YES

    The cycle 3-4-5-3 sits in the SECOND component. Component {1,2} is a plain
    edge and node 6 is isolated. A search that starts only at node 1 answers NO
    and is wrong.

HINT (DFS carrying the parent): mark a node visited on the way in, and recurse
into each neighbour that is not the node you came from:

    boolean dfs(int u, int parent) {
        visited[u] = true;
        for (int v : adj[u]) {
            if (v == parent) continue;          // the edge you arrived on
            if (visited[v]) return true;        // a second route in -> cycle
            if (dfs(v, u)) return true;
        }
        return false;
    }

The parent check exists because every undirected edge sits in BOTH adjacency
lists: standing on u you will always see the node you just came from marked
visited, and that is the same edge looked at backwards, not a cycle. Anything
else already visited IS a genuine second route.

Wrap it in an outer loop over all nodes 1..N, starting a fresh DFS from each
unvisited one, exactly as in CountComponents. Sample 3 exists to catch a
solution that forgets.

Things to get right:
  - skip the parent, but only the parent; every other visited neighbour is a
    cycle
  - do not reset visited between components, and do not stop after the first
  - M = 0 is legal and the answer is NO
  - answer YES as soon as you find one cycle
  - RECURSION DEPTH: N can be 1e5 and the graph may be a path, so a recursive
    DFS overflows the default stack somewhere around 9000 frames. Use the 64 MB
    thread -- this is the one real difference from the BFS version, which has no
    such problem. See the TreeHeight notes for the measurements

CROSS-CHECK worth knowing: a connected component with V nodes is acyclic
exactly when it has V - 1 edges. So the whole graph is acyclic iff
M == N - (number of components), which you can already compute from
CountComponents. An independent way to verify your DFS.

NEXT: Graph/Dfs/advance/DetectCycleMultigraphDfs drops the "simple" guarantee.
Self loops and parallel edges then make the parent-NODE check above wrong, and
you have to skip the parent EDGE instead.
*/
public class DetectCycleDfs {

    public static void main(String[] args) throws IOException {

        // ---------- input ----------
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken()) - 1;   // stored 0-indexed
            int v = Integer.parseInt(st.nextToken()) - 1;
            adjList.get(u).add(v);
            adjList.get(v).add(u);
        }

        // ---------- run the search on a 64 MB stack ----------
        // Reading stays on main (it is iterative and needs no depth); only the
        // recursion moves across. See the TreeHeight notes for why.
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println(hasCycle(n, adjList) ? "YES" : "NO");
            }
        };
        new Thread(null, r, "solver", 1 << 26).start();
    }

    /*
     * ============================ YOUR PART ============================
     * Return true if the graph contains a cycle.
     *
     * Nodes are 0-indexed here: adjList.get(i) holds the neighbours of node
     * i+1 from the input. Remember the outer loop over all n nodes -- Sample 3
     * puts the only cycle in the second component.
     * ===================================================================
     */
    private static boolean hasCycle(int n, List<List<Integer>> adjList) {
        boolean[] visited = new boolean[n];
        for(int i = 0; i < n; i++){
            if(!visited[i]){
                visited[i] = true;
                if(hasCycle(i, -1, adjList, visited)){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasCycle(int node, int parent, List<List<Integer>> adjList, boolean[] visited){

        // transition
        for(Integer num : adjList.get(node)){
            if(num != parent){
                if(!visited[num]){
                    visited[num] = true;
                    if(hasCycle(num, node, adjList, visited)){
                        return true;
                    }
                }
                else{
                    return true;
                }
            }
        }

        return false;
    }
}

/*
REVIEW NOTES  (2026-09-15)

1. VERDICT: correct. 3/3 samples, 7/7 edge cases (no edges, n=1, single edge,
   star, 4-cycle, diamond, two-tree forest), and 0 of 400 randomized graphs
   fail against the independent "M == N - components" reference. Scale: a
   100,000-node path in 0.81s, a 100,000-node cycle in 0.34s, and 100k nodes /
   200k edges in 0.73s.

2. THE 100k PATH IS THE TEST THAT MATTERS: that is 1e5 nested frames, and it
   only survives because of the 64 MB thread. Without it this overflows around
   9000 -- see the TreeHeight notes. This is the ONE real difference from the
   BFS version of the same problem in Graph/Bfs/DetectCycleUndirected, which
   has no depth hazard at all.

3. FIXED DURING REVIEW -- the outer loop originally did not set
   visited[i] = true before recursing, so each component's START node stayed
   false through its own traversal. It still gave right answers, by
   coincidence: walking back into the start node can only happen via a
   non-parent edge, which already proves a cycle, and the re-entered call
   immediately finds another visited neighbour and returns true. Right answer,
   wrong reason, plus an extra node visit on every cyclic component. Now the
   invariant is plain: visited is set exactly when a node is entered.

4. NOTE THE ASYMMETRY THAT CAUSED IT: the recursive method marks its CHILDREN
   before descending rather than marking ITSELF on entry. That works, but it
   means the entry points have to remember to mark separately. Marking yourself
   on entry (visited[node] = true as the first line of the recursive method) is
   the form where the outer loop and the recursion agree and nothing can drift.

5. THE PARENT CHECK IS THE POINT: every undirected edge sits in BOTH adjacency
   lists, so standing on u you always see the node you just came from marked
   visited -- the same edge viewed backwards, not a cycle. Excluding exactly the
   parent removes the false positive; anything else already visited is a genuine
   second route in.

6. SAFE, DO NOT "FIX": `num != parent` compares Integer against int, so it
   unboxes and compares by value. Making both sides Integer would turn it into a
   reference comparison and break for node numbers above 127.

7. THIS RULE IS ONLY VALID ON A SIMPLE GRAPH. Allow self loops or parallel
   edges and skipping the parent NODE becomes wrong -- see
   Graph/Dfs/advance/DetectCycleMultigraphDfs, where you skip the parent EDGE ID
   instead.
*/
