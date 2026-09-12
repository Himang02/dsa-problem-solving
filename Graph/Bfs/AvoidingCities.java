package Graph.Bfs;

import java.util.*;
import java.io.*;

/*
Avoiding Cities
https://www.algouniversity.com/problem/215/?asid=2036

There are N cities with M bi-directional roads between them. You want to travel
from a source city to a destination city, but some cities are CURSED and cannot
be visited.

Find the smallest number of roads you must take to complete the journey. If it
is not possible, output -1.

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5

INPUT
First line contains two integers N and M: the number of cities and roads.
Second line contains two integers src and dest: the source and destination.
Third line contains N integers, each 0 or 1, denoting whether each city is
cursed (1 means cursed).
The next M lines each contain two integers u and v, denoting a road between
city u and city v.

OUTPUT
The smallest number of roads needed to travel from src to dest.
If it is not possible, output -1.

EXAMPLE
    Sample 1 INPUT:
        5 5
        0 2
        0 1 0 0 0
        0 1
        1 2
        0 3
        3 4
        4 2
    Sample 1 OUTPUT:
        3

    City 1 is cursed, so the two-road route 0 -> 1 -> 2 is closed. The best
    remaining route is 0 -> 3 -> 4 -> 2, which uses 3 roads.

NOTE: cities are 0-INDEXED here (the previous problems were 1-indexed), and
unreachable prints -1, not 0. Unweighted edges again, so BFS from src gives the
answer as the level of dest. Handle cursed nodes by simply never enqueueing
them -- treat them as permanently visited. Two cases to decide up front: src
itself being cursed, and dest itself being cursed; both make the trip
impossible. With N up to 1e5 and M up to 2e5 use adjacency lists and an
iterative BFS, and read input with StreamTokenizer.
*/
public class AvoidingCities {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int src = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int[] isCursed = new int[n];
        for (int i = 0; i < n; i++) {
            isCursed[i] = Integer.parseInt(st.nextToken());
        }

        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            adjList.get(u).add(v);
            adjList.get(v).add(u);

        }

        Queue<Integer> queue = new ArrayDeque<>();
        int[] levelMap = new int[n];
        boolean[] isVisited = new boolean[n];

        queue.offer(src);
        isVisited[src] = true;
        Arrays.fill(levelMap, -1);
        levelMap[src] = 0;
        boolean isFound = (src == dest);

        while (!queue.isEmpty() && !isFound) {
            int current = queue.peek();

            for (int i = 0; i < adjList.get(current).size(); i++) {
                if (!isVisited[adjList.get(current).get(i)] && isCursed[adjList.get(current).get(i)] == 0) {
                    queue.offer(adjList.get(current).get(i));
                    isVisited[adjList.get(current).get(i)] = true;
                    levelMap[adjList.get(current).get(i)] = levelMap[current] + 1;
                }

                if (adjList.get(current).get(i) == dest) {
                    isFound = true;
                    break;
                }
            }

            queue.poll();

        }

        System.out.println(levelMap[dest]);

    }
}

/*
REVIEW NOTES  (2026-09-12)

1. VERDICT: clean except one gap. 400 randomized graphs vs a reference BFS
   produced zero mismatches outside the cursed-src case below. Direct cases all
   pass: sample, src==dest, disconnected, m=0, cursed destination, cursed cut
   vertex, direct edge. Performance is comfortable -- a 100k-node path (BFS
   depth 99999) in 0.33s, a 200k-edge graph in 0.40s.

2. OPEN GAP: a cursed SRC. The code enqueues src and sets levelMap[src] = 0
   without consulting isCursed[src], so BFS departs from a city it is not
   allowed to occupy and reports a real distance where -1 is expected. All 63
   differential mismatches were this and nothing else. One line:
       if (isCursed[src] == 1) { System.out.println(-1); return; }
   The statement never says whether src can be cursed, so it may be untested.

3. DONE WELL: pre-filling levelMap with -1 means the final println prints the
   right answer for both reachable and unreachable with NO branch at all. That
   is the neat version of this.

4. DONE WELL: isFound = (src == dest) is exactly the guard MinimumJumps is
   missing. Same author, same week -- the fix just never travelled back.

5. WORKS BY INITIALISATION, NOT BY CHECK: a cursed DESTINATION resolves
   correctly because the == dest test still fires and breaks, but the enqueue
   was skipped so levelMap[dest] stays -1. Right answer; understand that it is
   the -1 fill carrying it, not an explicit test.

6. IMPROVE: lines in the neighbour loop call adjList.get(current).get(i) four
   times in one if. Hoist `int next = ...` or use a for-each over
   adjList.get(current).
*/
