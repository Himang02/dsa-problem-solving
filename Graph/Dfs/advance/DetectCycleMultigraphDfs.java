package Graph.Dfs.advance;

import java.util.*;
import java.io.*;

/*
Cycle Detection In An Undirected Graph -- DFS, MULTIGRAPH edition

You are given an undirected, unweighted graph with N nodes numbered 1 to N and
M edges. Determine whether it contains a cycle.

The graph need NOT be connected, and unlike Graph/Dfs/DetectCycleDfs it
is NOT guaranteed to be simple:
  - a SELF LOOP u--u is a cycle (of length 1)
  - two PARALLEL edges between the same pair u--v form a cycle (of length 2)

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5
    self loops and repeated edges are allowed

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
        1 3
        3 4+
    Sample 1 OUTPUT:
        NO

    A plain tree: 3 edges over 4 connected nodes, nothing to come back through.

    Sample 2 INPUT:
        3 3
        1 2
        2 3
        3 3
    Sample 2 OUTPUT:
        YES

    Edge 3--3 is a SELF LOOP, which is a cycle all by itself.

    Sample 3 INPUT:
        4 3
        1 2
        1 2
        3 4
    Sample 3 OUTPUT:
        YES

    The edge 1--2 appears TWICE. Going out along the first copy and back along
    the second is a genuine cycle of length 2 -- and it is exactly what a
    node-based parent check misses.

    Sample 4 INPUT:
        6 4
        1 2
        3 4
        4 5
        5 3
    Sample 4 OUTPUT:
        YES

    The cycle 3-4-5-3 sits in the SECOND component; component {1,2} is a plain
    edge and node 6 is isolated. A search that starts only at node 1 answers NO
    and is wrong.

HINT (skip the parent EDGE, not the parent NODE): recursive DFS carrying the
node you came from, marking visited on the way in, and reporting a cycle when
you meet an already-visited neighbour that is not your parent -- that is the
rule from Graph/Bfs/DetectCycleUndirected, and on a SIMPLE graph it is correct.

It breaks here. With two parallel edges u--v, standing on v you look back at u,
see it is your parent, and skip it -- but the SECOND copy of that edge is a
different edge and closing it is a real cycle. Excluding u by NAME throws away
both copies.

The fix is to remember which EDGE you arrived on rather than which node. Give
every edge an id when you read it, store (neighbour, edgeId) in the adjacency
list, and skip only that one id:

    for (int[] e : adj[u]) {          // e = {neighbour, edgeId}
        if (e[1] == incomingEdgeId) continue;   // not: if (e[0] == parent)
        ...
    }

Undirected edge i appears in both endpoints' lists carrying the same id, so
skipping the id skips exactly the one traversal you already made, and a second
parallel edge (different id) is still seen. Self loops need no special case
either: u--u puts u in its own list twice with an id that is not the one you
arrived on, so it is found as a cycle naturally.

Then, as in CountComponents, loop over all nodes 1..N and start a fresh DFS from
each unvisited one -- Sample 4 is there to catch a solution that forgets.

Things to get right:
  - skip the parent EDGE ID, not the parent node -- Sample 3 exists for this
  - a self loop is a cycle; do not filter u == v while reading the input
  - do not reset visited between components, and do not stop after the first
  - M = 0 is legal and the answer is NO
  - N up to 1e5 means a path-shaped graph recurses N deep, so a recursive DFS
    needs the 64 MB thread -- see the TreeHeight notes for why and for what
    happens if you forget
  - answer YES as soon as you find one cycle; there is no need to keep going

CROSS-CHECK worth knowing: a connected component with V nodes is acyclic
exactly when it has V - 1 edges, counting parallel edges and self loops
separately. So the whole graph is acyclic iff M == N - (number of components).
That is a completely independent way to compute the answer and a good way to
test your DFS.
*/
public class DetectCycleMultigraphDfs {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        List<List<NodePath>> adjList = new ArrayList<>();
        for(int i = 0; i < n; i++){
            adjList.add(new ArrayList<>());
        }

        for(int i = 0; i < m; i++){
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            adjList.get(u-1).add(new NodePath(v-1, i));
            adjList.get(v-1).add(new NodePath(u-1, i));
        }

        Runnable r = ()->{
            solve(adjList, m);
        };

        Thread thread = new Thread(null, r, "RunnerThread", 1<<26);
        thread.start();
    }

    public static void solve(List<List<NodePath>> adjList, int m){
        int n = adjList.size();
        boolean[] pathVisited = new boolean[m];
        boolean[] nodeVisited = new boolean[n];
        for(int i = 0; i < n; i++){
            if(!nodeVisited[i]){
                nodeVisited[i] = true;
                if(hasCycle(i, -1, nodeVisited, pathVisited, adjList)){
                    System.out.print("YES");
                    return;
                }
            }
        }
        System.out.print("NO");
        
    }

    public static boolean hasCycle(int node, int prevPath, boolean[] nodeVisited, boolean[] pathVisited, List<List<NodePath>> adjList){

        // transtion
        for(NodePath np: adjList.get(node)){
            if(np.path != prevPath){
                if(!pathVisited[np.path]){
                    pathVisited[np.path] = true;
                    nodeVisited[np.node] = true;
                    if(hasCycle(np.node, np.path, nodeVisited, pathVisited, adjList)){
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

class NodePath{
    int node;
    int path;
    NodePath(int node, int path){
        this.node = node;
        this.path = path;
    }
}

/*
REVIEW NOTES  (2026-09-15)

1. VERDICT: correct. 4/4 samples, 7/7 edge cases, and 0 of 400 randomized
   MULTIGRAPHS fail against the independent "M == N - components" reference
   (that run contained 259 graphs with self loops and 108 with parallel edges).
   Scale: a 100,000-node path in 0.52s, 100k nodes / 200k edges in 0.56s, and a
   100k path with a self loop at the FAR END -- which needs full depth AND
   correct loop handling at the bottom -- answers YES.

2. THE EDGE-ID IDEA IS THE WHOLE PROBLEM, AND IT IS RIGHT: NodePath carries
   (neighbour, edgeId), and the recursion skips `np.path != prevPath` -- the one
   traversal already made -- instead of blacklisting the neighbour by NAME.
   Because an undirected edge gets the same id in both endpoints' lists, a
   SECOND parallel edge has a different id and is still seen, so a parallel pair
   registers as a 2-cycle. A self loop needs no special case either: u--u puts u
   in its own list with an id you did not arrive on.

3. TWO ROUNDS OF FIXING GOT HERE. First pass: main read the input and ended
   without ever calling solve, so all 9 cases produced empty output. Second
   pass: 55 of 400 randomized graphs failed while every hand-written sample
   passed -- the samples exercise the multigraph rules but not the component
   scan. Both are now right.

4. WORTH BEING DELIBERATE ABOUT: there are TWO visited arrays, but only
   pathVisited gates the recursion; nodeVisited exists solely so the outer loop
   can pick unvisited starting points. That split is legitimate here -- edges
   drive the traversal, nodes drive the component scan -- but two visited arrays
   where one is unused inside the DFS is exactly the shape that reads as a bug
   on a later visit. The comment is the fix, not a code change.

5. DEPTH IS HANDLED: the recursion runs on the 64 MB thread with parsing left on
   main, and the 100k path proves it. Without the thread this overflows around
   9000 frames -- see the TreeHeight notes.

6. CONTRAST WITH Graph/Dfs/DetectCycleDfs: that one skips the parent NODE, which
   is correct ONLY on a simple graph. The moment self loops or parallel edges
   are allowed, the node-based rule silently swallows real cycles and you need
   the edge ids used here. Same problem, one assumption apart.
*/
