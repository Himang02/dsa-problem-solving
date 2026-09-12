package Graph.Bfs;

import java.util.*;
import java.io.*;

/*
Minimum Jumps
https://www.algouniversity.com/problem/171/?asid=2036

You are given N buildings numbered 1 to N, and M pairs of buildings denoting
which pairs are close enough to jump between. Given the building you are
currently at and the final building you want to reach, find the minimum number
of jumps required. If the final building is not reachable, print 0.

Constraints:
    1 <= N <= 3500
    1 <= M <= 1e6

INPUT
First line contains two integers N and M: the number of buildings and the
number of pairs of buildings that are close enough to jump between.
The next M lines contain two integers each, denoting a pair of buildings that
are close enough.
The last line contains two integers: the building you start at and the
building you want to reach.

OUTPUT
Print the minimum number of jumps required to reach the final building.
If it is not possible, print 0.

EXAMPLE
    Sample 1 INPUT:
        5 5
        1 3
        2 3
        1 2
        3 5
        4 5
        1 4
    Sample 1 OUTPUT:
        3

    1 -> 3 -> 5 -> 4 is the shortest route, so 3 jumps.

    Sample 2 INPUT:
        5 3
        1 3
        1 2
        4 5
        1 4
    Sample 2 OUTPUT:
        0

    Building 4 sits in the component {4, 5}, disconnected from the start.

NOTE: edges are unweighted, so plain BFS from the start gives the shortest
path; the answer is the BFS level of the destination. M can reach 1e6 while
N is only 3500, so the input is edge-heavy -- read with StreamTokenizer (or
a manual byte reader) rather than split(), and store the graph as adjacency
lists, not an N x M structure. Note that 0 doubles as both "unreachable" and
the answer when start == end.
*/
public class MinimumJumps {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        List<Integer>[] adjList = new List[n];
        for (int i = 0; i < n; i++) {
            adjList[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            int a, b;
            st = new StringTokenizer(br.readLine());
            a = Integer.parseInt(st.nextToken());
            b = Integer.parseInt(st.nextToken());

            adjList[a - 1].add(b);
            adjList[b - 1].add(a);
        }
        st = new StringTokenizer(br.readLine());

        int src = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());

        usingTwoQueues(adjList, src, dest);
        usingSingleQueueAndMap(adjList, src, dest);

    }

    public static void usingTwoQueues(List<Integer>[] adjList, int src, int dest) {
        if (src == dest) { System.out.println(0); return; }



        Queue<Integer> odd = new ArrayDeque<>();
        Queue<Integer> even = new ArrayDeque<>();

        Set<Integer> visited = new HashSet<>();
        odd.offer(src);
        visited.add(src);
        int jumps = 1;

        boolean found = false;
        while (!(odd.isEmpty() && even.isEmpty()) && !found) {

            while (jumps % 2 != 0 && !odd.isEmpty() && !found) {
                int current = odd.peek();
                for (int i = 0; i < adjList[current - 1].size(); i++) {
                    if (!visited.contains(adjList[current - 1].get(i))) {
                        even.offer(adjList[current - 1].get(i));
                        visited.add(adjList[current - 1].get(i));
                    }

                    if (adjList[current - 1].get(i) == dest) {
                        System.out.println(jumps);
                        found = true;
                        break;
                    }
                }
                odd.poll();
            }
            jumps++;

            while (jumps % 2 == 0 && !even.isEmpty() && !found) {
                int current = even.peek();
                for (int i = 0; i < adjList[current - 1].size(); i++) {
                    if (!visited.contains(adjList[current - 1].get(i))) {
                        odd.offer(adjList[current - 1].get(i));
                        visited.add(adjList[current - 1].get(i));
                    }

                    if (adjList[current - 1].get(i) == dest) {
                        System.out.println(jumps);
                        found = true;
                        break;
                    }
                }
                even.poll();
            }
            jumps++;

        }

        if (!found) {
            System.out.println(0);
        }
    }

    public static void usingSingleQueueAndMap(List<Integer>[] adjList, int src, int dest) {

        if (src == dest) { System.out.println(0); return; }



        Queue<Integer> queue = new ArrayDeque<>();
        Map<Integer, Integer> levelMap = new HashMap<>();

        Set<Integer> visited = new HashSet<>();
        queue.offer(src);
        visited.add(src);
        levelMap.put(src, 0);

        boolean found = false;
        while (!queue.isEmpty() && !found) {

            int current = queue.peek();
            for (int i = 0; i < adjList[current - 1].size(); i++) {
                if (!visited.contains(adjList[current - 1].get(i))) {
                    queue.offer(adjList[current - 1].get(i));
                    visited.add(adjList[current - 1].get(i));
                    levelMap.put(adjList[current - 1].get(i), levelMap.get(current) + 1);
                }

                if (adjList[current - 1].get(i) == dest) {
                    System.out.println(levelMap.get(current) + 1);
                    found = true;
                    break;
                }
            }
            queue.poll();

        }

        if (!found) {
            System.out.println(0);
        }
    }
}

/*
REVIEW NOTES  (2026-09-12)

1. VERDICT: OPEN BUG -- src == dest returns 2 instead of 0, in BOTH methods.
   Differential-tested against a reference BFS on 300 random graphs: 66
   mismatches, every single one this case. Example: n=2, edge 2-1, start 1,
   target 1 -> prints 2.

2. THE CAUSE: src is added to `visited` before any == dest comparison happens,
   so the start node is never tested against the target. BFS then walks out to a
   neighbour and back, sees src in that neighbour's adjacency list, and reports
   the length of the round trip. FIX, at the top of each method:
       if (src == dest) { System.out.println(0); return; }
   Worth doing even though 0 is also the "unreachable" output -- right now it
   prints 2, which is neither.

3. BEFORE SUBMITTING: main calls BOTH methods, so it prints two lines. Fine for
   comparing approaches locally, but comment one out or the judge sees doubled
   output.

4. THE HEADER NOTE IS OVERCAUTIOUS: BufferedReader + StringTokenizer was
   measured reading the full worst case (n=3500, m=1e6, a 10MB file) in 0.68s,
   still running under a 64MB heap. No StreamTokenizer rewrite needed. Follow-up
   benchmark: Scanner ~755ms, split ~232ms, StringTokenizer ~186ms,
   StreamTokenizer ~185ms -- the last two are a tie. Only Scanner is genuinely
   bad.

5. THE TWO-QUEUE VERSION IS CORRECT BUT NOT WORTH KEEPING: `jumps` increments
   twice per outer iteration and its parity gates which queue drains, so
   jumps == the level being expanded. It works, but the single-queue + level map
   is a third of the code with no parity invariant to keep straight. Keep that
   one.

6. IMPROVE: `new List[n]` raw type -- same unchecked warning as
   AdjMatrixToAdjList.
*/
