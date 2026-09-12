package Graph.Bfs;

import java.util.*;
import java.io.*;

/*
Count Connected Components  (practice variant -- not from AlgoUniversity)

You are given an undirected graph with N nodes numbered 1 to N and M edges.
Two nodes belong to the same CONNECTED COMPONENT if there is some path between
them. Count how many connected components the graph has.

A node with no edges at all forms a component of its own, of size 1.

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5
    edges may repeat, and an edge may be a self loop (u == v)

INPUT
First line contains two integers N and M: the number of nodes and edges.
The next M lines contain two integers each, u and v, denoting an undirected
edge between node u and node v.

OUTPUT
A single integer: the number of connected components.

EXAMPLE
    Sample 1 INPUT:
        6 3
        1 2
        2 3
        5 6
    Sample 1 OUTPUT:
        3

    The components are {1, 2, 3}, {4} and {5, 6}. Node 4 appears in no edge,
    but it still counts.

    Sample 2 INPUT:
        4 0
    Sample 2 OUTPUT:
        4

    No edges at all, so every node is its own component.

    Sample 3 INPUT:
        3 4
        1 2
        1 2
        2 3
        3 3
    Sample 3 OUTPUT:
        1

    The repeated edge 1-2 and the self loop 3-3 add nothing; all three nodes
    are still joined into one component.

HINT (outer loop + BFS): the traversals you have written so far all started
from one given source. Here there is no source -- so loop over every node
1..N, and whenever you find one that is NOT yet visited, start a fresh BFS
from it and increment your counter. Each BFS floods exactly one component and
marks all of it visited, so the number of times you LAUNCH a BFS is the answer.

Total cost stays O(N + M): the outer loop touches each node once, and across
all the BFS runs together each edge is examined once.

Things to get right:
  - isolated nodes: the outer loop finds them, their BFS visits only
    themselves, and they still count as a component (Sample 1 and 2)
  - a self loop u--u puts u in its own adjacency list; harmless, since u is
    already visited by the time you look at it, but do not let it fool you
    into an infinite loop
  - duplicate edges are equally harmless for the same reason -- the visited
    check absorbs them, so there is no need to de-duplicate the input
  - M = 0 is legal, and the answer is then simply N
  - do not reset visited between components; that is the whole point

Compare this with BeautifulSnowFlakes, which asked you to reason about
components WITHOUT traversing them. This is the direct version: when you are
allowed to traverse, counting components is just counting BFS launches.
*/
public class CountComponents {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        
        List<List<Integer>> adjList = new ArrayList<>();
        for(int i = 0; i < n; i++){
            adjList.add(new ArrayList<>());
        }

        for(int i = 0; i < m; i++){
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            adjList.get(u-1).add(v);
            adjList.get(v-1).add(u);
        }

        int components = 0;
        int index = 0;
        boolean[] visited = new boolean[n];
        while(index < n){
            if(!visited[index]){
                levelOrderTraverse(index+1, visited, adjList);
                components++;
            }
            index++;
            
        }

        System.out.println(components);

    }

    public static void levelOrderTraverse(int src, boolean[] visited, List<List<Integer>> adjList){
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(src);
        visited[src-1] = true;

        while(!queue.isEmpty()){
            int current = queue.peek();
            List<Integer> currentList = adjList.get(current-1);
            for(int i = 0; i < currentList.size(); i++){
                if(!visited[currentList.get(i)-1]){
                    visited[currentList.get(i)-1] = true;
                    queue.offer(currentList.get(i));
                }
            }
            queue.poll();

        }
    }

    
}

/*
REVIEW NOTES  (2026-09-12)

1. VERDICT: correct, no bugs. All seven direct cases pass (sample, m=0, n=1,
   duplicate edge + self loop, star, forest of two trees, two triangles), 400
   randomized graphs vs a reference BFS gave zero mismatches, and at full size
   (N=1e5, M=2e5) it returns 2575 in 0.34s, matching the reference exactly.

2. THE KEY PROPERTY: `visited` is allocated ONCE in main and passed down, never
   reset between launches. That is what keeps the whole thing O(N + M) even
   though BFS appears inside a loop -- each edge is examined once ACROSS ALL
   runs, not once per run. Resetting it would make this O(N * (N + M)).

3. THE ANSWER IS THE LAUNCH COUNT: every BFS floods exactly one component and
   marks all of it, so counting how many times you START a BFS is the answer.
   Isolated nodes fall out for free -- the outer loop finds them, their BFS
   visits only themselves.

4. SELF LOOPS AND DUPLICATE EDGES NEED NO SPECIAL CASE: u sits in its own
   adjacency list, but is already visited by the time the loop reaches it, so
   the visited check absorbs both. No de-duplication of input required.

5. MARKING AT ENQUEUE (not dequeue) is right -- it prevents the same node
   entering the queue twice from two different neighbours.

6. NITS ONLY: `int current = queue.peek(); ... queue.poll();` should be
   `int current = queue.poll();`. currentList.get(i) appears three times in one
   condition -- use for-each. `while(index < n)` is a for loop in disguise. And
   levelOrderTraverse takes a 1-indexed src then immediately does src-1 while
   the caller passes index+1; the two conversions cancel out.
*/
