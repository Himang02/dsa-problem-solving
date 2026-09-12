package Graph.Dfs;

import java.util.*;

import javax.management.RuntimeErrorException;

import java.io.*;

/*
Height Of A Tree  (practice variant -- not from AlgoUniversity)

You are given a TREE with N nodes numbered 1 to N, rooted at node 1. A tree
with N nodes has exactly N - 1 edges and is connected, so there is exactly one
path between any two nodes.

The HEIGHT of the tree is the number of EDGES on the longest path from the root
down to any leaf. A tree consisting of the root alone has height 0.

Constraints:
    1 <= N <= 1e5

INPUT
First line contains a single integer N: the number of nodes.
The next N - 1 lines contain two integers each, u and v, denoting an undirected
edge between node u and node v.

OUTPUT
A single integer: the height of the tree.

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
        3

    Depths from the root: node 1 is at 0; nodes 2 and 3 at 1; nodes 4, 5 and 6
    at 2; node 7 at 3. The deepest is node 7, so the height is 3.

    Sample 2 INPUT:
        1
    Sample 2 OUTPUT:
        0

    Just the root, and no edge lines follow at all. Height counts EDGES, so a
    lone node is 0, not 1.

    Sample 3 INPUT:
        5
        1 2
        2 3
        3 4
        4 5
    Sample 3 OUTPUT:
        4

    A path is the worst case: every node has one child, so the height is N - 1.

HINT (DFS with a parent): this is the first problem here that wants DEPTH-first
rather than breadth-first. The recursive shape is small:

    height(u) = 0                                if u has no children
    height(u) = 1 + max over children c of height(c)

The edges are given undirected, so the adjacency list of u contains its parent
as well as its children. Pass the parent down and skip it, exactly like the
parent trick in DetectCycleUndirected:

    int h = 0;
    for (int v : adj[u]) {
        if (v == parent) continue;
        h = Math.max(h, 1 + height(v, u));
    }
    return h;

Because the graph is a tree there are no cycles, so skipping the parent is all
the protection you need -- no visited array is required.

WARNING (stack overflow): N can be 1e5, and Sample 3 shows the tree may be a
single path. A recursive DFS would then nest 1e5 calls deep, and Java's default
thread stack overflows somewhere around 10k-20k frames -- you get a
StackOverflowError, not a wrong answer. Three ways out:

  1. run the recursion inside a thread with a bigger stack:
         new Thread(null, () -> solve(), "main", 1 << 26).start();
  2. write the DFS iteratively with your own explicit Deque<Integer> stack
  3. note that for HEIGHT specifically, a BFS gives the same answer -- the
     height is just the largest level, so the level-order code you already have
     solves it without any recursion at all

Option 3 is the cheapest here, but do at least one of 1 or 2 as well: deep
recursion is the defining hazard of DFS in Java and it is better to meet it on
a problem this simple than on a hard one.

Things to get right:
  - height counts EDGES, not nodes: a single node is 0, a path of N nodes is
    N - 1
  - N = 1 means there are NO edge lines to read; do not try to read N - 1 = 0
    lines and then block waiting for input
  - the tree is rooted at 1 regardless of the order the edges are listed in;
    an edge line "6 7" does not tell you which of the two is the parent
*/
public class TreeHeight {
    public static void main(String[] args) throws IOException{
        new Thread(null, TreeHeight::solve, "deep", 1 << 26).start();   // 64 MB stack
    }

    static void solve(){

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());

            List<List<Integer>> adjList = new ArrayList<>();
            for(int i = 0; i < n; i++){
                adjList.add(new ArrayList<>());
            }

            for(int i = 0; i < n-1; i++){
                st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());

                adjList.get(u-1).add(v);
                adjList.get(v-1).add(u);
            }
            System.out.println(getHeight(1, -1, adjList));
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
        
    }

    public static int getHeight(int currNode, int parent, List<List<Integer>> adjList){

        // transition
        int maxHeight = 0;
        List<Integer> currList = adjList.get(currNode-1);
        for(int i = 0; i < currList.size(); i++){
            if(currList.get(i) != parent){
                maxHeight = Math.max(maxHeight, getHeight(currList.get(i), currNode, adjList) + 1);
            }
        }

        return maxHeight;
    }
}

/*
REVIEW NOTES  (2026-09-12)

1. VERDICT: logic was correct from the start; the RECURSION DEPTH was not.
   Measured on a path-shaped tree: N=5000 fine, N=8000 fine, N=9000
   StackOverflowError. That is roughly a twelfth of the allowed N -- it broke
   between 8,000 and 9,000 frames on the default stack.

2. PROOF IT WAS STACK, NOT LOGIC: the identical 100,000-node input printed
   99999 correctly under `java -Xss64m`. The algorithm was never wrong.

3. FIXED with the big-stack thread. Since a judge will not let you pass -Xss,
   the 4-arg Thread constructor is the ONLY way to request a stack size from
   inside the program:
       new Thread(null, TreeHeight::solve, "deep", 1 << 26).start();
   (1 << 26 = 67108864 bytes = 64 MB.) Now: N=100000 path in 0.69s, and 300
   random trees with shuffled edge order and random edge orientation gave zero
   mismatches.

4. THE COMPILE ERROR THIS CAUSES: solve() is used as a Runnable, and
   Runnable.run() declares no checked exceptions -- so solve() CANNOT say
   `throws IOException` the way main could. The reading code has to move inside
   a try/catch. Wrapping in RuntimeException satisfies the compiler without
   hiding the failure.

5. THE TRAP THAT COMES WITH THE THREAD: an uncaught exception on the worker
   thread kills only that thread and the JVM still exits 0. On a judge that
   looks like "wrong answer, no output" rather than a runtime error. Also:
   calling .run() instead of .start() would execute on the main thread with its
   original small stack, silently defeating the whole point.

6. BETTER STRUCTURE (see SubtreeSizes): keep the INPUT PARSING on main and move
   only the recursion to the big-stack thread. Reading is iterative and never
   needs a deep stack, and main then keeps its legitimate `throws IOException`.
   Leftover here: main still declares throws IOException but can no longer throw
   it -- harmless dead syntax.
*/
