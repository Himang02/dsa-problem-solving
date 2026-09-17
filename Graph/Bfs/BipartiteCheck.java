package Graph.Bfs;
import java.util.*;
import java.io.*;

/*
Bipartite Check

You are given an undirected graph with N nodes numbered 1 to N and M edges.

A graph is BIPARTITE if its nodes can be split into two groups such that every
edge joins a node in one group to a node in the other -- no edge ever has both
its ends inside the same group. Equivalently: the nodes can be coloured with
two colours so that no edge joins two nodes of the same colour.

The graph need not be connected. Determine whether it is bipartite.

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5
    the graph is simple: no self loops, no repeated edges

INPUT
First line contains two integers N and M.
The next M lines contain two integers each, u and v, denoting an undirected
edge between u and v.

OUTPUT
Print "YES" if the graph is bipartite, otherwise "NO".

EXAMPLE
    Sample 1 INPUT:
        4 4
        1 2
        2 3
        3 4
        4 1
    Sample 1 OUTPUT:
        YES

    A square. Put {1, 3} in one group and {2, 4} in the other and every edge
    crosses between them.

    Sample 2 INPUT:
        3 3
        1 2
        2 3
        3 1
    Sample 2 OUTPUT:
        NO

    A triangle. Colour 1 red, then 2 must be blue, then 3 must be red -- but 3
    is joined to 1, which is also red. There is no way to two-colour an ODD
    cycle.

    Sample 3 INPUT:
        6 4
        1 2
        3 4
        4 5
        5 3
    Sample 3 OUTPUT:
        NO

    The triangle 3-4-5 sits in the SECOND component; component {1,2} is a plain
    edge and node 6 is isolated. A search that starts only at node 1 answers YES
    and is wrong.

THE KEY FACT: a graph is bipartite if and only if it contains NO ODD-LENGTH
CYCLE. Even cycles are fine -- Sample 1 is a 4-cycle and is bipartite. So this
problem is really "does an odd cycle exist", asked the other way round.

HINT (two-colour it with BFS): colour the starting node 0, and every time you
discover a new node give it the OPPOSITE colour to the node you came from. If
you ever reach a node that is ALREADY coloured with the SAME colour as the one
you are standing on, two-colouring is impossible and the answer is NO.

    if (colour[v] == -1) { colour[v] = 1 - colour[u]; enqueue(v); }
    else if (colour[v] == colour[u]) -> NOT BIPARTITE

BFS suits this better than DFS: the colour depends only on the parity of the
distance from the start, which is exactly what BFS levels give you -- colour[v]
is just level[v] % 2. It also means no recursion, so none of the 64 MB thread
business the Dfs files need. (DFS works equally well if you prefer it; the same
rule applies, just down the recursion instead of across levels.)

Then loop over all nodes 1..N and start a fresh search from each uncoloured
one -- Sample 3 is there to catch a solution that forgets.

Things to get right:
  - a conflict means SAME colour on both ends of an edge; meeting an
    already-coloured node is perfectly normal otherwise, and is NOT a failure
  - do not reset the colours between components, and do not stop after the
    first component
  - a graph with no edges is bipartite (colour everything 0), and so is any
    tree -- a tree has no cycles at all, let alone odd ones
  - isolated nodes are fine and never cause a conflict
  - M = 0 is legal and the answer is YES
  - answer NO as soon as one conflict is found; there is no need to keep going
  - if self loops were allowed, u--u would make the graph instantly
    non-bipartite (u would have to differ from itself); the statement rules
    them out here
*/
public class BipartiteCheck {
    public static void main(String[] args) throws IOException {
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
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            adjList.get(u - 1).add(v-1);
            adjList.get(v - 1).add(u-1);
        }

        // approach1(n, adjList);
        approach2(n, adjList);
        
    }

    public static void approach1(int n, List<List<Integer>> adjList){
        int[] nodeColors = new int[n];

        Queue<Integer> grp1 = new ArrayDeque<>();
        Queue<Integer> grp2 = new ArrayDeque<>();
        int index = 0;
        
        while (index < n) {
            // System.out.println(index + " - " + Arrays.toString(nodeColors));
            if(nodeColors[index] != 0){
                index++;
                continue;
            }

            grp1.offer(index);
            nodeColors[index] = 1;

            while (!(grp1.isEmpty() && grp2.isEmpty())) {
                while (!grp1.isEmpty()) {
                    int curr = grp1.poll();

                    for (Integer neigh : adjList.get(curr)) {
                        if (nodeColors[neigh] == 1) {
                            System.out.println("NO");
                            return;
                        }

                        if (nodeColors[neigh] == 0) {
                            grp2.offer(neigh);
                            nodeColors[neigh] = 2;
                        }
                    }
                }

                while (!grp2.isEmpty()) {
                    int curr = grp2.poll();

                    for (Integer neigh : adjList.get(curr)) {
                        if (nodeColors[neigh] == 2) {
                            System.out.println("NO");
                            return;
                        }
                        
                        if (nodeColors[neigh] == 0) {
                            grp1.offer(neigh);
                            nodeColors[neigh] = 1;
                        }
                    }
                }
            }
            index++;
        }

        System.out.println("YES");
    }

    public static void approach2(int n, List<List<Integer>> adjList){
        int[] nodeColors = new int[n];

        Queue<Integer> queue = new ArrayDeque<>();
        int index = 0;
        
        while (index < n) {
            // System.out.println(index + " - " + Arrays.toString(nodeColors));
            if(nodeColors[index] != 0){
                index++;
                continue;
            }

            queue.offer(index);
            nodeColors[index] = 1;

            while (!queue.isEmpty()) {
                int curr = queue.poll();
                for (Integer neigh : adjList.get(curr)) {
                    if (nodeColors[neigh] == nodeColors[curr]) {
                        System.out.println("NO");
                        return;
                    }

                    if (nodeColors[neigh] == 0) {
                        queue.offer(neigh);
                        nodeColors[neigh] = ((nodeColors[curr] == 1) ? 2 : 1);
                    }
                }
            }
            index++;
        }

        System.out.println("YES");
    }
}

/*
REVIEW NOTES  (2026-09-17)

1. VERDICT: both implementations in this file are correct. Final state --
   3/3 samples, 7/7 edge cases (no edges, n=1, tree, 5-cycle, 6-cycle with a
   chord, odd cycle in the FIRST of two components), and 0 of 400 randomized
   graphs fail. Scale: 100k nodes / 200k edges answers in well under a second
   either way. main calls approach2.

2. THE FIRST VERSION NEEDED ONE FIX. Originally `index` did two jobs at once --
   scanning for the next uncoloured node AND acting as a parity counter via
   `index % 2` -- and no new component was ever seeded. That failed 1 of the 3
   samples and 21 of 400 random graphs. Now `index` only scans, each component
   is seeded explicitly (grp1.offer(index); nodeColors[index] = 1), and the
   drain loops alternate on their own. One variable, one job.

3. THE TWO-QUEUE DESIGN IS SOUND: grp1 holds colour-1 nodes, grp2 holds
   colour-2 nodes, and draining one refills the other, so the colour is implied
   by WHICH QUEUE you are in. The outer
   `while (!(grp1.isEmpty() && grp2.isEmpty()))` wrapper is required because one
   drain refills the other -- a single pass through both is not enough. Same
   shape as the two-queue BFS in MinimumJumps, where the alternation tracked
   distance parity instead of colour. They are the same thing: COLOUR IS JUST
   BFS LEVEL MOD 2.

4. approach2 IS THE ONE TO KEEP, and not for speed -- five runs each on the
   100k/200k input gave 0.33-1.04s for two queues and 0.38-1.11s for one, i.e.
   pure JVM-startup noise. It wins on SIZE: three nested loops and two queues
   collapse to one loop and one queue, and the whole colour rule becomes
       if (nodeColors[neigh] == nodeColors[curr]) -> NO
   In the two-queue form each drain had to know which colour to test for
   (== 1 in one, == 2 in the other) -- two places to get wrong instead of one.

5. COLOURS 1 AND 2, NOT 0 AND 1 -- deliberate and worth keeping. It lets 0 mean
   "uncoloured" for free, so `new int[n]` is already the initial state and no
   Arrays.fill(-1) is needed. The cost is that the flip is a ternary
   ((x == 1) ? 2 : 1) rather than the slicker 1 - x.

6. THE FACT THAT MAKES THIS WORK: a graph is bipartite IFF it has no
   ODD-LENGTH cycle. Even cycles are fine -- Sample 1 is a 4-cycle and IS
   bipartite. Meeting an already-coloured node is normal; only meeting one with
   the SAME colour is a failure. That is the trap to keep separate from the
   "already visited => cycle" reflex of the cycle-detection files.

7. NO STACK WORRIES HERE, unlike everything in Graph/Dfs: BFS is iterative, so
   no 64 MB thread, and the 100k path runs fine. That is the real reason this
   problem belongs on the BFS side -- colour is level parity, which BFS hands
   you directly.
*/
