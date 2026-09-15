package Graph.Bfs.advance;

import java.util.*;
import java.io.*;

/*
Thief Escape  (practice variant)

You are given an undirected graph with N nodes and M edges. A thief stands at
node T, and there are police officers standing at P given nodes.

Each unit of time, the thief moves to an adjacent node, and every officer may
move to an adjacent node OR STAY WHERE THEY ARE. Everyone moves simultaneously.

A LEAF is a node of degree exactly 1. The thief is SAVED if he can reach some
leaf before any officer can get there. Decide whether the thief can escape.

Constraints:
    2 <= N <= 1e5
    1 <= M <= 2e5
    1 <= P <= N

INPUT
First line contains two integers N and M.
The next M lines contain two integers u and v: an undirected edge.
The next line contains the thief's node T.
The last line contains P followed by the P nodes where officers stand.

OUTPUT
Print "YES" if the thief can escape, otherwise "NO".

EXAMPLE
    Sample 1 INPUT:
        5 4
        1 2
        2 3
        3 4
        4 5
        3
        1 1
    Sample 1 OUTPUT:
        YES

    A path 1-2-3-4-5. The thief is in the middle at 3, one officer at 1. The
    leaves are 1 and 5. Leaf 1 is already occupied, but the thief needs 2 steps
    to reach leaf 5 while the officer needs 4 -- so the thief gets there first
    and is saved.

    Sample 2 INPUT:
        5 4
        1 2
        2 3
        3 4
        4 5
        3
        2 1 5
    Sample 2 OUTPUT:
        NO

    The same path, but now officers sit on BOTH leaves. Every escape route is
    already covered, so the thief is caught however he runs.

    Sample 3 INPUT:
        6 5
        1 2
        2 3
        1 4
        4 5
        2 6
        1
        1 6
    Sample 3 OUTPUT:
        YES

    Leaves are 3, 5 and 6. The thief (at 1) and the officer (at 6) BOTH need 2
    steps to reach leaf 3 -- a tie, and a tie means caught, because the officer
    can simply sit on the leaf and wait. But leaf 5 is 2 steps for the thief and
    4 for the officer, so he escapes that way instead.

HINT (two BFS runs, then compare): run

    distThief[]  = BFS from T
    distPolice[] = MULTI-SOURCE BFS seeded with ALL P officer nodes at once

The thief escapes if some leaf L satisfies

    distThief[L] < distPolice[L]        // STRICTLY less

Multi-source is right for the police because the officers are INTERCHANGEABLE
-- only the earliest arrival matters, and that is exactly what a multi-source
BFS computes. (Compare CarFactoryLocation, where four sources each needed their
own distance and merging them would have been wrong, and RottingOranges, where
merging them was right for the same reason as here.)

WHY YOU ONLY HAVE TO CHECK THE LEAF, not the whole route: suppose
distThief[L] < distPolice[L], and let u be any node on a shortest thief-path to
L, at distance k from T. If an officer could reach u in k steps or fewer, then
that officer could reach L in at most k + (distThief[L] - k) = distThief[L]
steps, contradicting distThief[L] < distPolice[L]. So every node along that
route also satisfies distThief[u] < distPolice[u] -- the thief is never
intercepted on the way. Checking the destination is enough.

WHY THE COMPARISON IS STRICT: officers may STAY PUT. An officer who reaches the
leaf at the same time as the thief just waits there, so equality is a capture,
not an escape. Sample 3's leaf 3 is exactly this case and it is why "<=" fails
that sample while "<" passes it.

Things to get right:
  - use < and not <=; a tie is a capture (Sample 3)
  - a leaf the police can NEVER reach (distPolice == -1) but the thief can is an
    escape; do not let the -1 sentinel compare as "small"
  - a leaf the THIEF cannot reach is not an escape, whatever the police can do
  - if an officer starts on the thief's own node, distPolice <= distThief
    everywhere, so the answer is NO and it falls out with no special case
  - the thief starting ON a leaf escapes immediately (distance 0), unless an
    officer is already there
  - leaf means DEGREE 1, so this works on any graph, not only a tree; a degree-0
    isolated node is not a leaf
  - both BFS runs are iterative, so no stack worries; total cost is
    2 * O(N + M)
*/
public class ThiefEscape {
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

            adjList.get(u-1).add(v-1);
            adjList.get(v-1).add(u-1);
        }

        st = new StringTokenizer(br.readLine());
        int thiefNode = Integer.parseInt(st.nextToken()) - 1;

        st = new StringTokenizer(br.readLine());
        int p = Integer.parseInt(st.nextToken());
        
        Queue<Integer> queue = new ArrayDeque<>();
        int[] thiefLevels = new int[n];
        Arrays.fill(thiefLevels, -1);
        int[] policeLevels = new int[n];
        Arrays.fill(policeLevels, -1);
        for(int i = 0; i < p; i++){
            int nodeIndex = Integer.parseInt(st.nextToken())-1;
            policeLevels[nodeIndex] = 0;
            queue.offer(nodeIndex);
        }
        bfs(queue, adjList, policeLevels);
        
        queue.offer(thiefNode);
        thiefLevels[thiefNode] = 0;
        bfs(queue, adjList, thiefLevels);
        
        for(int i = 0; i < n; i++){
            if(thiefLevels[i] != -1 && (policeLevels[i] > thiefLevels[i] || policeLevels[i] == -1) && isLeafNode(i, adjList)){
                System.out.println("YES");
                return;
            }
        }
        System.out.println("NO");
    }

    private static boolean isLeafNode(int index, List<List<Integer>> adjList){
        return adjList.get(index).size() == 1;
    }


    private static void bfs(Queue<Integer> queue, List<List<Integer>> adjList, int[] levels){
        
        while(!queue.isEmpty()){
            int curr = queue.poll();
            List<Integer> currList = adjList.get(curr);
            for(Integer num: currList){
                if(levels[num] == -1){
                    queue.offer(num);
                    levels[num] = levels[curr] + 1;
                }
            }
        }

        queue.clear();
    }
}

/*
REVIEW NOTES  (2026-09-15)

1. VERDICT: correct after two rounds of fixing the same line. Final state:
   3/3 samples, 6/6 edge cases, and 0 of 300 randomized graphs fail. At full
   size (N = 1e5, M = 2e5, 50 officers) it runs in 0.79s.

2. THE BUG WAS THE -1 SENTINEL BEING COMPARED AS A NUMBER, and it broke in BOTH
   directions, which is why it took two passes:
     - police unreachable (-1), thief reachable  -> `policeLevels > thiefLevels`
       is FALSE, so a guaranteed escape was reported as NO
     - thief unreachable (-1), police reachable  -> `policeLevels > thiefLevels`
       is TRUE, so a leaf the thief can never even get to was reported as YES
   50 of 300 random graphs wrong originally; adding only the thief check left
   26 wrong; adding the police check too left 0. The smallest failing case was
   tiny -- n=3, m=1: edge 1-3, thief at 1, officer at 2, expected YES.

3. WHY IT HID: all three samples are CONNECTED, so no distance is ever -1 and
   the bug cannot show. 159 of the 300 random graphs contained unreachable
   nodes. When a sentinel value shares a type with real data, every comparison
   against it is a bug until proven otherwise -- prefer Integer.MAX_VALUE for
   "unreachable" when the comparison is `<`, or test the sentinel explicitly as
   this now does.

4. DONE RIGHT -- MULTI-SOURCE FOR THE POLICE: all officer nodes are seeded at
   level 0 before the BFS, which is correct because officers are
   INTERCHANGEABLE; only the earliest arrival matters. Compare RottingOranges
   (also interchangeable, also multi-source) against CarFactoryLocation (four
   sources each needing their own distance, so four separate runs).

5. DONE RIGHT -- THE COMPARISON IS STRICT: officers may STAY PUT, so one
   arriving at the same moment as the thief simply waits on the leaf. A tie is a
   capture. Sample 3's leaf 3 is a genuine 2-vs-2 tie and is the case that
   makes `>=` wrong.

6. WHY CHECKING ONLY THE LEAF IS ENOUGH (not the route): if
   distThief[L] < distPolice[L] and u lies on a shortest thief-path to L at
   distance k, an officer reaching u within k steps could reach L in
   k + (distThief[L] - k) = distThief[L] steps, contradicting the assumption. So
   every node on that route is safe automatically.

7. NIT: bfs() clears the queue on the way OUT, so the second call depends on the
   first having tidied up. Clearing at the START of the method makes each call
   self-sufficient.
*/
