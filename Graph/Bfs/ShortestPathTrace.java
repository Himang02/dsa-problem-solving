package Graph.Bfs;

import java.util.*;
import java.io.*;

/*
Shortest Path Trace  (practice variant -- not from AlgoUniversity)

Same setup as Minimum Jumps, but this time printing the DISTANCE is only half
the job: you must also print the actual route taken.

You are given an undirected, unweighted graph with N nodes numbered 1 to N and
M edges. Given a source and a destination, find the shortest route from source
to destination: output how many edges it uses, and then the sequence of nodes
along it.

If several shortest routes exist, any one of them is acceptable.
If the destination is unreachable, output -1 and nothing else.

Constraints:
    1 <= N <= 1e5
    0 <= M <= 2e5

INPUT
First line contains two integers N and M: the number of nodes and edges.
The next M lines contain two integers each, u and v, denoting an undirected
edge between node u and node v.
The last line contains two integers: src and dest.

OUTPUT
If dest is reachable from src, print two lines:
    line 1: the number of EDGES on the shortest route
    line 2: the nodes on that route, from src to dest, separated by spaces
Otherwise print a single line containing -1.

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
        1 3 5 4

    The route 1 -> 3 -> 5 -> 4 uses 3 edges. (Here it happens to be the only
    shortest route, so the answer is unambiguous.)

    Sample 2 INPUT:
        5 3
        1 3
        1 2
        4 5
        1 4
    Sample 2 OUTPUT:
        -1

    Node 4 is in the component {4, 5}, disconnected from the start.

    Sample 3 INPUT:
        3 2
        1 2
        2 3
        2 2
    Sample 3 OUTPUT:
        0
        2

    src == dest: a route of zero edges, consisting of the single node itself.

HINT (parent array): BFS already discovers every node along a shortest route --
you just have to remember WHERE you came from. Keep an int[] parent alongside
your visited/level arrays, and at the moment you first discover a node v from a
node u, record parent[v] = u. Initialise parent[src] to a sentinel (0 or -1) so
you know where to stop.

Afterwards, walk BACKWARDS from dest: dest, parent[dest], parent[parent[dest]],
... until you hit the sentinel. That yields the route in reverse, so collect it
into a list and reverse it (or push onto a stack and pop). Do this iteratively,
not recursively -- with N up to 1e5 a recursive walk can blow the call stack on
a long path.

Things to get right:
  - the edge count is (number of nodes on the route) - 1, which is also just
    level[dest]; do not confuse the two
  - unreachable prints ONLY -1, with no second line
  - src == dest is 0 edges and a one-node route, not an empty one
  - build the route line in a single StringBuilder rather than printing node by
    node, for the reasons in the AdjMatrixToAdjList note
*/
public class ShortestPathTrace {
    
}
