package Graph;

import java.util.*;
import java.io.*;

/*
Detect Cycle In An Undirected Graph  (practice variant)

You are given a simple undirected, unweighted graph with N nodes numbered 1 to
N and M edges. Determine whether the graph contains a cycle.

The graph need NOT be connected. A cycle anywhere in it counts.

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5
    the graph is simple: no self loops, no repeated edges

INPUT
First line contains two integers N and M: the number of nodes and edges.
The next M lines contain two integers each, u and v, denoting an undirected
edge between node u and node v.

OUTPUT
Print "YES" if the graph contains at least one cycle, otherwise print "NO".

EXAMPLE
    Sample 1 INPUT:
        4 3
        1 2
        2 3
        3 1
    Sample 1 OUTPUT:
        YES

    Nodes 1, 2, 3 form a triangle. Node 4 is isolated and irrelevant.

    Sample 2 INPUT:
        4 3
        1 2
        1 3
        3 4
    Sample 2 OUTPUT:
        NO

    This is a tree: 3 edges over 4 connected nodes, no way back to a node you
    have already left.

    Sample 3 INPUT:
        6 4
        1 2
        3 4
        4 5
        5 3
    Sample 3 OUTPUT:
        YES

    The cycle is 3 -> 4 -> 5 -> 3, sitting in the SECOND component. Component
    {1, 2} is a plain edge and node 6 is isolated. If your search only starts
    from node 1 you will answer NO and be wrong.

HINT (BFS with a parent): traverse as usual, but remember which node you came
FROM. When you are expanding node u and look at a neighbour v:

    - if v is unvisited, visit it and record parent[v] = u
    - if v IS visited and v != parent[u], you have reached an already-seen node
      by a second, different route -- that is a cycle, answer YES

The parent check is the whole trick, and it is there because every undirected
edge appears in BOTH adjacency lists. Standing on u you will always see the
node you just came from sitting there marked visited; that is the same edge
looked at backwards, not a cycle. Excluding exactly the parent removes it.

And as in CountComponents, wrap this in an outer loop over all nodes 1..N so
that every component gets searched -- Sample 3 exists precisely to catch a
solution that forgets.

ALTERNATIVE (counting): a connected component with V nodes is acyclic exactly
when it has V - 1 edges; one more edge than that forces a cycle. So the whole
graph is acyclic if and only if M == N - (number of components), which you can
already compute. Worth implementing as a cross-check against your BFS answer.

Things to get right:
  - do not reset visited between components, and do not stop after the first
  - parent must be excluded ONCE, not forever: a node with two separate edges
    to the same neighbour would be a cycle, which is why this problem promises
    a simple graph. If repeated edges were allowed, tracking the parent NODE
    would wrongly ignore the second copy and you would have to track the parent
    EDGE instead
  - a self loop is a cycle of length 1, also excluded here by "simple"
  - M = 0 is legal and the answer is NO
  - answer YES the moment you find one cycle; there is no need to keep going
*/
public class DetectCycleUndirected {
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

        
        int[] parents = new int[n];
        Arrays.fill(parents, -1);
        int index = 0;
        boolean isCyclic = false;
        while(index < n){
            if(parents[index] == -1 && isCyclic(index, adjList, parents)){
                isCyclic = true;
                break;
            }
            index++;
        }

        System.out.println((isCyclic) ? "YES" : "NO");

    }

    public static boolean isCyclic(int index, List<List<Integer>> adjList, int[] parents){
        
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(index + 1);
        parents[index] = 0; // src

        boolean isCyclic = false;

        while(!queue.isEmpty() && !isCyclic){
            int current  = queue.poll();
            List<Integer> currList = adjList.get(current-1);
            for(Integer num : currList){
                if(parents[num - 1] != -1 && num != parents[current - 1]){
                    isCyclic = true;
                    break;
                }
                if(parents[num - 1] == -1){
                    parents[num - 1] = current;
                    queue.offer(num);
                }
            }
        }

        return isCyclic;
    }

}
