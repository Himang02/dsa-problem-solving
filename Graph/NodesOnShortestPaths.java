package Graph;

import java.util.*;
import java.io.*;

/*
Nodes On Any Shortest Path  (practice variant)

You are given a simple undirected, unweighted graph with N nodes numbered 1 to
N and M edges, together with a source src and a destination dest.

A node v is INTERESTING if it lies on AT LEAST ONE shortest path from src to
dest. Find every interesting node.

Note that src and dest are themselves interesting whenever dest is reachable.

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5
    the graph is simple: no self loops, no repeated edges

INPUT
First line contains two integers N and M: the number of nodes and edges.
The next M lines contain two integers each, u and v, denoting an undirected
edge between node u and node v.
The last line contains two integers: src and dest.

OUTPUT
Print all interesting nodes in INCREASING order, separated by spaces.
If dest is not reachable from src, print -1 instead.

EXAMPLE
    Sample 1 INPUT:
        6 6
        1 2
        1 3
        2 4
        3 4
        2 5
        4 6
        1 4
    Sample 1 OUTPUT:
        1 2 3 4

    The shortest distance from 1 to 4 is 2, achieved by BOTH 1 -> 2 -> 4 and
    1 -> 3 -> 4. So 2 and 3 are each on a shortest path even though neither is
    on the same one. Node 5 hangs off node 2 and node 6 hangs off node 4, but
    detouring through either makes the walk longer, so they are not counted.

    Sample 2 INPUT:
        5 3
        1 2
        1 3
        4 5
        1 4
    Sample 2 OUTPUT:
        -1

    Node 4 is in a different component.

    Sample 3 INPUT:
        3 2
        1 2
        2 3
        2 2
    Sample 3 OUTPUT:
        2

    src == dest: the shortest path has length 0 and consists of src alone.

HINT (two BFS runs): do NOT try to enumerate the shortest paths and collect
their nodes. A graph can contain exponentially many shortest paths -- a chain
of k diamonds like Sample 1 has 2^k of them -- so any approach that walks them
one by one is hopeless. You want to TEST each node instead.

Run BFS twice on the same graph:
    distFromSrc[]  = BFS distances starting at src
    distFromDest[] = BFS distances starting at dest
(the graph is undirected, so the second run needs no reversed edges)

Let D = distFromSrc[dest], the shortest distance overall. Then node v lies on
some shortest path exactly when

    distFromSrc[v] + distFromDest[v] == D

Why: any walk through v costs at least distFromSrc[v] to reach v and at least
distFromDest[v] to finish from v, so the sum is a lower bound on the length of
the best src-to-dest walk THROUGH v. If that lower bound equals D, gluing the
two shortest halves together produces a genuine shortest path containing v; if
it exceeds D, every route through v is strictly longer. Two BFS runs is O(N+M)
regardless of how many shortest paths exist.

Things to get right:
  - a node unreachable from src, from dest, or from both must be excluded
    BEFORE you do the addition; if you store unreached as -1, then -1 + -1
    can accidentally satisfy the test, and if you store it as Integer.MAX_VALUE
    the addition overflows to a negative number, which is worse
  - dest unreachable from src prints only -1, and no node list
  - src == dest gives D = 0, and the answer is the single node src
  - output ascending, so just walk v from 1 to N and append the ones that pass
  - build that line in one StringBuilder, not with a print per node
*/
public class NodesOnShortestPaths {
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
        st = new StringTokenizer(br.readLine());
        int src = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());
        
        int[] levelMap = new int[n];
        Arrays.fill(levelMap, -1);
        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[n];

        boolean found = (src == dest);
        queue.offer(src);
        levelMap[src-1] = 0;
        visited[src-1] = true;

        while(!queue.isEmpty() && !found){
            int current = queue.poll();
            List<Integer> currList = adjList.get(current-1);

            for(int num: currList){
                if(!visited[num - 1]){
                    visited[num - 1] = true;
                    queue.offer(num);
                    levelMap[num-1] = levelMap[current - 1] + 1;
                }

                if(num == dest){
                    found = true;
                    break;
                }
            }
        }

        if(levelMap[dest-1] == -1){
            System.out.println(-1);
        }
        else{
            queue.clear();
            queue.offer(dest);
            List<Integer> ans = new ArrayList<>();
            ans.add(dest);
            visited = new boolean[n];
            visited[dest-1] = true;

            while(!queue.isEmpty()){
                int current = queue.poll();
                List<Integer> currList = adjList.get(current-1);

                for(int num: currList){
                    if(!visited[num-1] && levelMap[num-1] != -1 && levelMap[num-1] == levelMap[current-1] - 1){
                        ans.add(num);
                        queue.offer(num);
                        visited[num-1] = true;
                    }

                    if(num == src){
                        queue.clear();
                        break;
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            Collections.sort(ans);
            for(Integer num : ans){
                sb.append(num).append(' ');
            }
            System.out.println(sb);
        }
        

    }
}
