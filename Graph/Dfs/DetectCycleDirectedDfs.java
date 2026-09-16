package Graph.Dfs;

import java.util.*;
import java.io.*;

/*
Cycle Detection In A DIRECTED Graph -- DFS

You are given a directed graph with N nodes numbered 1 to N and M edges. Each
edge u v is ONE-WAY: it lets you go from u to v, never back. Determine whether
the graph contains a directed cycle -- a route that leaves some node and
returns to it following edges forwards.

The graph need not be connected.

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5

INPUT
First line contains two integers N and M.
The next M lines contain two integers each, u and v, denoting a directed edge
from u to v.

OUTPUT
Print "YES" if the graph contains a directed cycle, otherwise "NO".

EXAMPLE
    Sample 1 INPUT:
        3 3
        1 2
        2 3
        3 1
    Sample 1 OUTPUT:
        YES

    1 -> 2 -> 3 -> 1 returns to where it started.

    Sample 2 INPUT:
        4 4
        1 2
        1 3
        2 4
        3 4
    Sample 2 OUTPUT:
        NO

    THIS IS THE TRAP. Node 4 is reached twice -- once via 2 and once via 3 --
    so a search that reports a cycle whenever it meets an already-visited node
    answers YES here. But every edge points forwards; there is no way back to
    1. This is a DAG, and the answer is NO.

    Sample 3 INPUT:
        5 4
        1 2
        3 4
        4 5
        5 3
    Sample 3 OUTPUT:
        YES

    The cycle 3 -> 4 -> 5 -> 3 sits in the second component. A search that
    starts only at node 1 answers NO and is wrong.

    Sample 4 INPUT:
        2 2
        1 2
        2 2
    Sample 4 OUTPUT:
        YES

    Edge 2 -> 2 is a self loop, a directed cycle of length 1.

HINT (three colours -- the parent trick does NOT transfer): in the undirected
version you skipped the node you came from, because every edge appeared in both
adjacency lists. Here edges appear once, in one direction only, so there is
nothing to skip -- and the undirected rule would be wrong anyway.

What matters instead is WHETHER THE NODE YOU REACHED IS STILL ON THE CURRENT
RECURSION PATH. Keep three states per node:

    0 = WHITE, not visited yet
    1 = GRAY,  visited and still on the stack -- you are inside its dfs call
    2 = BLACK, visited and finished -- its dfs call has returned

    dfs(u):
        colour[u] = GRAY
        for each v in adj[u]:
            if colour[v] == GRAY  -> CYCLE, return true    // back edge
            if colour[v] == WHITE and dfs(v) -> return true
        colour[u] = BLACK        // only after the loop
        return false

Meeting a GRAY node means you have looped back onto the path you are currently
walking, which is exactly a directed cycle. Meeting a BLACK node means you have
merely rejoined something already explored and finished -- harmless, and Sample
2 is built from precisely that. A single boolean visited[] cannot tell the two
apart, which is why two bits are needed rather than one.

Then, as always, loop over all nodes 1..N and start a fresh DFS from each white
one -- Sample 3 catches a solution that forgets.

Things to get right:
  - colour[u] must be set BLACK only AFTER the child loop, not before; setting
    it early loses the very information the check depends on
  - a self loop u -> u is a cycle (u is GRAY when you look at it), and needs no
    special case
  - two opposite edges u -> v and v -> u ARE a cycle here, unlike the undirected
    case where that same pair would be one edge
  - add the edge in ONE direction only when reading input
  - do not reset colours between components, and do not stop after the first
  - M = 0 is legal and the answer is NO
  - N up to 1e5 means a chain recurses N deep, so use the 64 MB thread -- see
    the TreeHeight notes

WORTH KNOWING: "has no directed cycle" is exactly "is a DAG", which is the
condition for a topological sort to exist. Kahn's algorithm (repeatedly remove
nodes of in-degree 0 and check whether all N come out) answers this same
question with BFS and no recursion at all -- a good cross-check against your
DFS.
*/
public class DetectCycleDirectedDfs {
    public static void main(String[] args) throws IOException{
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
        boolean[] tested = new boolean[n];
        for(int i = 0; i < n; i++){
            // System.out.println("Tested: " + Arrays.toString(tested));
            if(!tested[i]){
                visited[i] = true;
                if(hasCycle(i, adjList, visited, tested)){
                    // System.out.println("Received true");
                    return true;
                }
                visited[i] = false;
            }
        }
        return false;
    }

    private static boolean hasCycle(int node, List<List<Integer>> adjList, boolean[] visited, boolean[] tested){
        tested[node] = true;

        
        // transition
        for(Integer num : adjList.get(node)){
            // System.out.println(num + ", " + Arrays.toString(visited));
            if(visited[num]){
                // System.out.println("Sending true");
                return true;
            }
            visited[num] = true;
            if(!tested[num] && hasCycle(num, adjList, visited, tested)){
                return true;
            }
            visited[num] = false;
        }

        return false;
    }
}

/*
REVIEW NOTES  (2026-09-16)

1. VERDICT: correct and linear, after three rounds. 4/4 samples, 6/6 edge cases
   (self loop, two-way pair, m=0, chain, cycle not containing the start), and
   0 of 400 randomized graphs fail. Scale: 100k chain 0.74s, 100k-node DAG with
   200k edges 0.50s, and the pathological diamond DAG now ~290ms at either
   numbering.

2. BUG 1 -- THE RETURN VALUE WAS DISCARDED. `hasCycle(num, ...);` was called
   bare, so a true found deep in the recursion was thrown away one frame up and
   the method fell through to `return false`. Symptom: the trace printed
   "Sending true" but the program printed NO. In a boolean-returning DFS a bare
   recursive call almost always wants to be `if (recurse(...)) return true;`.

3. BUG 2 -- THE PRUNING GATE WAS INVERTED. `if (tested[num] && recurse(...))`
   recursed BECAUSE the node was already tested. But tested[] is set on entry and
   never cleared, so it is a gate that opens permanently -- it permitted
   re-entry rather than preventing it. Nothing said "this node is finished, do
   not come back". Measured on a 49-node diamond DAG: 1,048,449 entries into
   hasCycle, with one node entered 262,141 times (= 2^18, one doubling per
   diamond). Flipping to `!tested[num]` gives 49 entries, worst node entered
   once.

4. THE MEASUREMENTS THAT CAUGHT IT WERE ALMOST MISSED: the first diamond
   generator numbered nodes in increasing order, which the broken version walked
   straight past -- it reported 265ms and looked fixed. The SAME graph shape
   renumbered so edges run high->low took 44 seconds at 85 nodes. If a timing
   suddenly looks great, check whether the input accidentally suits the bug.

5. THE TWO BOOLEANS ARE THE THREE COLOURS IN DISGUISE:
       !tested                  = WHITE, never entered
       visited                  = GRAY,  on the current recursion path
       tested && !visited       = BLACK, finished
   visited[num] catching a GRAY node is the CYCLE; tested[num] skipping a BLACK
   one is the PRUNING. Both are needed and they do different jobs -- a single
   boolean cannot tell "on my current path" from "finished long ago", which is
   exactly what Sample 2's diamond punishes.

6. visited IS CORRECTLY BACKTRACKED (set on the way down, cleared on the way
   back out) because it means "on the current path". tested is correctly NEVER
   cleared because it means "finished". Getting those two lifetimes the right
   way round is the whole problem.

7. CONTRAST WITH THE UNDIRECTED VERSIONS: Graph/Dfs/DetectCycleDfs skips the
   parent NODE, and Graph/Dfs/advance/DetectCycleMultigraphDfs skips the parent
   EDGE ID. Neither idea applies here -- directed edges appear once, in one
   direction, so there is nothing to skip. The question changes from "is this
   the edge I came in on?" to "is this node still on my current path?".
*/
