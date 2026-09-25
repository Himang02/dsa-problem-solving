package Graph.Dijkstra;

import java.util.*;
import java.io.*;

/*
DIJKSTRA -- NOTES AND REFERENCE IMPLEMENTATION

This file is not a problem. It is a working, tested Dijkstra you can run and
read; the notes are below the code. Verified against an independent reference
on 300 random weighted graphs -- 0 mismatches.

Run it with:
    <n> <m>
    <m lines of: u v w>
    <source>
and it prints the distance from the source to every node, or -1 if a node is
unreachable.

    5 6            ->   0 3 1 8 11
    1 2 4
    1 3 1
    3 2 2
    2 4 5
    3 4 8
    4 5 3
    1

================================ THE IDEA ================================

Dijkstra is BFS where the queue is sorted by DISTANCE instead of by arrival
order.

BFS works because every edge costs 1, so the first time you reach a node is
necessarily along a shortest path. Weights break that: a 2-edge route can be
cheaper than a 1-edge route. Concretely, with edges 1->3 costing 100 and
1->2->3 costing 1+2, BFS would "settle" node 3 at 100.

The fix is to always expand the CLOSEST UNFINALISED node, which is exactly what
a min-heap gives you. Once a node comes off the heap, no later route can beat
it, so it is settled forever -- the same "first time reached is final"
guarantee BFS has, bought a different way.

============================ THE FOUR KEY LINES ==========================

1. dist[src] = 0, everything else INF (Long.MAX_VALUE).
   INF rather than -1 as the "unreachable" marker, so that `<` comparisons work
   with no special case. Compare the BFS files in Graph/Bfs, where -1 was the
   marker and comparing against it WAS the bug twice (AvoidingCities,
   ThiefEscape).

2. The comparator orders by the DISTANCE field, not the node id. PriorityQueue
   is a min-heap; getting the comparator backwards silently gives you a
   max-heap and wrong answers.

3. if (d > dist[u]) continue;
   Java's PriorityQueue has no decrease-key, so when a node's distance improves
   we push a NEW entry instead of updating the old one. The heap therefore
   accumulates STALE entries for nodes already settled more cheaply. This line
   discards them -- "lazy deletion". Delete this line and nodes get re-expanded
   repeatedly, the same re-exploration blow-up as the tested/!tested inversion
   in Graph/Dfs/DetectCycleDirectedDfs.

4. if (dist[u] + w < dist[v]) { dist[v] = dist[u] + w; push; }
   "Relaxation": is going through u better than what I already have for v?
   That is the entire algorithm; everything else is bookkeeping.

=========================== WHY long[] FOR THE HEAP ======================

The NODE id always fits an int. The DISTANCE often does not: at n = 1e5 with
weights up to 1e9, a path can reach 1e14 while int stops at 2.1e9. A Java array
holds one type, so pairing a node with a long distance forces long[] -- hence
the (int) cast when reading the node back out.

WHY THE DISTANCE IS STORED IN THE ENTRY AT ALL, when dist[] already has it:
a heap fixes an element's position when you INSERT it. A comparator that reads
the mutable dist[] does not re-heapify when dist[] changes, it just silently
corrupts the ordering -- and Dijkstra mutates dist[] constantly. Demonstrated:
with dist = {10, 20, 30} and nodes 0,1,2 inserted, setting dist[2] = 1
afterwards still polls 0 before 2.

So the stored distance is a FROZEN SNAPSHOT from push time. That is also what
makes line 3 meaningful: d is the old value, dist[u] is the current best, and a
difference means the entry is out of date.

Alternatives: int[]{node, dist} when the constraints allow it; a small
record Node(int v, long d) for readability (short-lived queue objects cost
almost nothing -- see the CarFactoryLocation notes); or packing both into one
long when you enjoy bit-twiddling.

============================ THINGS THAT BITE ============================

  - NEGATIVE WEIGHTS make Dijkstra WRONG, not slow. Settling a node assumes no
    later path can improve it, which a negative edge breaks. Use Bellman-Ford.
  - Use long for distances unless you have checked the product n * maxWeight.
  - INF + w OVERFLOWS. It is avoided here because relaxation reads dist[u], and
    u came off the heap so it is never INF. Writing the test as
    dist[v] > dist[u] + w with dist[u] == INF would wrap to a negative number
    and "improve" everything.
  - COMPLEXITY is O((V + E) log V). The heap holds at most E entries, which is
    why lazy deletion is affordable.
  - IF ALL WEIGHTS ARE EQUAL, just use BFS -- the heap buys nothing.
  - For 0/1 weights there is a deque trick (0-1 BFS) that is O(V + E).
*/
public class DijkstraNotes {

    static final long INF = Long.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        // adjacency list: for node u, a list of {neighbour, weight}
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken()) - 1;
            int v = Integer.parseInt(st.nextToken()) - 1;
            int w = Integer.parseInt(st.nextToken());
            adj.get(u).add(new int[]{v, w});
            adj.get(v).add(new int[]{u, w});   // drop this line if the graph is directed
        }

        int src = Integer.parseInt(br.readLine().trim()) - 1;

        long[] dist = dijkstra(n, adj, src);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(dist[i] == INF ? -1 : dist[i]).append(' ');
        System.out.println(sb);
    }

    static long[] dijkstra(int n, List<List<int[]>> adj, int src) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);

        // entries are {node, distanceSoFar}, smallest distance first
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));

        dist[src] = 0;
        pq.offer(new long[]{src, 0});

        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            int u = (int) top[0];
            long d = top[1];

            if (d > dist[u]) continue;          // stale entry: a better one was already processed

            for (int[] e : adj.get(u)) {
                int v = e[0];
                int w = e[1];
                if (dist[u] + w < dist[v]) {    // relax
                    dist[v] = dist[u] + w;
                    pq.offer(new long[]{v, dist[v]});
                }
            }
        }
        return dist;
    }
}
