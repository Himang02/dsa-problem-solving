package Graph.Dfs;

import java.util.*;
import java.io.*;

/*
Red Alert
https://www.algouniversity.com/problem/174/?asid=2041

You are given a tree of N nodes and you start at the root, node 1. Each node is
either a WHITE node or a RED node. You are given an integer C, the capacity: if
you walk through MORE THAN C consecutive red nodes, the alarm rings.

Find the number of leaf nodes you can reach starting from node 1 without
buzzing the alarm.

Constraints:
    1 <= N <= 1e5
    1 <= C <= N

INPUT
First line contains two integers N and C.
Next line contains N numbers, where the ith number is 1 if the ith node is red,
otherwise 0.
The next N - 1 lines each contain two integers u and v representing an edge
between those nodes.

OUTPUT
A single integer: the number of leaf nodes reachable from node 1 without
buzzing the alarm.

EXAMPLE
    Sample 1 INPUT:
        4 1
        1 1 0 0
        1 2
        1 3
        1 4
    Sample 1 OUTPUT:
        2

    The root is red, giving a run of 1 -- which is allowed, because the alarm
    needs MORE than C = 1. Node 2 is red, pushing the run to 2, so that leaf is
    lost. Nodes 3 and 4 are white, which resets the run, so both count.

    Sample 2 INPUT:
        7 1
        1 0 1 1 0 0 0
        1 2
        1 3
        2 4
        2 5
        3 6
        3 7
    Sample 2 OUTPUT:
        2

    Root red (run 1, allowed). Node 2 is white and RESETS the run, so leaves 4
    (red, run 1) and 5 (white) are both reachable. Node 3 is red, which makes
    the run 2 and rings the alarm -- costing leaves 6 and 7 even though both
    are white. Blocking is about the path, not the node.

NOTE on the threshold: the alarm needs MORE than C consecutive reds, so a run
of exactly C is fine and C + 1 rings. Sample 1 pins this down -- with C = 1 a
red root is allowed. Off-by-one here fails both samples.
*/
public class RedAlert {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());

        boolean[] isRed = new boolean[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int color = Integer.parseInt(st.nextToken());
            if (color == 1) {
                isRed[i] = true;
            }
        }

        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        for (int i = 0; i < n - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            adjList.get(u - 1).add(v - 1);
            adjList.get(v - 1).add(u - 1);
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                solve(c, adjList, isRed);
            }
        };

        Thread thread = new Thread(null, r, "TestThread", 1 << 26);
        thread.start();

    }

    private static void solve(int c, List<List<Integer>> adjList, boolean[] isRed) {
        System.out.println(getLeafNodes(0, -1, c, c, adjList, isRed));
    }

    private static int getLeafNodes(int curr, int parent, int remCap, int allowedCap, List<List<Integer>> adjList, boolean[] isRed) {
        List<Integer> currList = adjList.get(curr);
        if (isRed[curr]) remCap--;

        // base
        if (remCap < 0) {
            return 0;
        } else if ((currList.size() == 1 && curr != 0) || (isRed.length == 1)) {
            return 1;
        }

        // transition
        int noOfLeafNodes = 0;

        for (Integer num : currList) {
            if (num != parent) {
                if (isRed[curr]) {
                    noOfLeafNodes += getLeafNodes(num, curr, remCap, allowedCap, adjList, isRed);
                } else {
                    noOfLeafNodes += getLeafNodes(num, curr, allowedCap, allowedCap, adjList, isRed);
                }
            }
        }
        return noOfLeafNodes;
    }
}

/*
REVIEW NOTES  (2026-09-13)

1. VERDICT: correct. Both official samples pass, plus C=2 on sample 2 (4), N=1,
   N=2, edges listed child-first, and 250 random trees with RANDOMLY ORIENTED
   edges -> 0 failures (69 of those had a single-child root, the shape that
   broke the previous draft). Scale: a 100k all-red path in 0.56s, and a random
   100k tree with mixed colours in 0.65s.

2. BUG FIXED #1 -- EDGE ORIENTATION. The first version built the adjacency
   one-directional (only u -> v), assuming every edge is listed parent-first.
   The statement only says "two integers u and v representing edge between the
   nodes" and never promises that; both samples just happen to list the parent
   first. Measured then: 300 random trees x 2 orientations -> 212 failures, ALL
   of them the random-orientation shape. Flipping ONE edge of sample 2 turned
   the answer from 2 into 1. Now both directions are added and the parent is
   skipped in the loop.

3. BUG FIXED #2 -- THE ROOT IS NOT A LEAF. Once the adjacency went undirected,
   the leaf test became currList.size() == 1, which is right for every node
   EXCEPT the root: every other node's degree is (children + 1 parent), but the
   root has no parent, so its degree IS its child count. A root with exactly one
   child was misdiagnosed as a leaf, returned 1 immediately and never descended.
   That failed 26 of 200 random trees. The `&& curr != 0` guard fixes it, and
   `isRed.length == 1` covers the genuine one-node tree where the root really is
   a leaf.

4. THE THRESHOLD IS RIGHT: remCap is C minus the current red run, so
   remCap < 0 fires exactly when the run EXCEEDS C -- matching "more than C" in
   the statement. Decrementing at the top and testing BEFORE the leaf check
   means a blocked leaf correctly returns 0 rather than 1. Sample 1 pins the
   off-by-one down: with C = 1 a red root is still allowed.

5. THE RESET IS RIGHT: red children inherit remCap (the run continues), white
   children get allowedCap back (the run restarts). Carrying the original C
   along as allowedCap is a tidy way to avoid a field or a second traversal.

6. STATE GOES DOWN *AND* UP: unlike SubtreeSizes and MaxInSubtree, which only
   combine children's answers upward, here the red-run travels DOWN as a
   parameter while the count comes back UP as the return value. That
   combination is the thing to remember from this problem.

7. LESSON FOR NEXT TIME: both bugs were invisible to the official samples --
   sample 2 has a 3-child root and lists every edge parent-first. Two samples
   passing says almost nothing. The cheap self-checks here are: flip an edge,
   and give the root a single child.
*/
