package Graph.Bfs;

import java.util.*;
import java.io.*;

/*
Beautiful Snowflakes
https://www.algouniversity.com/problem/1/?asid=2033

A connected component of a graph is called a SNOWFLAKE if it contains exactly
one node whose degree is not equal to 1. (Recall: the degree of a node is the
number of edges connected to it.)

A snowflake is BEAUTIFUL if no other snowflake in the graph is the same as it.
Two snowflakes are the same if they have the same number of nodes.

Given a graph, how many Beautiful snowflakes does it contain?

Constraints:
    2 <= n <= 100
    0 <= m <= 100

INPUT
First line contains two integers n and m: the number of nodes and edges.
The next m lines each contain two distinct integers in the range [1, n],
describing an edge.

OUTPUT
A single integer: the number of Beautiful snowflakes in the graph.

EXAMPLE
    Sample 1 INPUT:
        11 8
        1 2
        2 3
        4 5
        6 5
        7 5
        8 5
        9 5
        10 5
    Sample 1 OUTPUT:
        3

    The components are {1,2,3} (only node 2 has degree != 1), {4..10} (only
    node 5 has degree != 1) and the isolated node {11} (degree 0 != 1). All
    three are snowflakes, of sizes 3, 7 and 1 -- all distinct, so all three
    are Beautiful.

NOTE (from the problem): do NOT use BFS/DFS, think simple. Degrees alone are
enough. Watch the two easy-to-miss cases: an isolated node is a snowflake of
size 1, and a lone edge u--v is NOT a snowflake (it has zero nodes of degree
!= 1, not exactly one).
*/
public class BeautifulSnowFlakes {
    public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());
  
    int n, m;
    // io
    n = Integer.parseInt(st.nextToken());
    m = Integer.parseInt(st.nextToken());
    
    
    Set<Integer>[] graph = new HashSet[n];
    for(int i = 0; i < graph.length; i++){
      graph[i] = new HashSet<Integer>();
    }
    
    for(int i = 0; i < m; i++){
      // io
      st = new StringTokenizer(br.readLine());
      int s, d;
      s = Integer.parseInt(st.nextToken());
      d = Integer.parseInt(st.nextToken());
      
      graph[s-1].add(d);
      graph[d-1].add(s);
    }
    
    HashMap<Integer, Integer> map = new HashMap<>();

    for(int i = 0; i < n; i++){
      if(graph[i].size() != 1){
        boolean isValid = true;
        for(Integer neigh : graph[i]){
          if(graph[neigh-1].size()!=1){
            isValid = false;
            break;
          }
        }
        if(isValid){
          map.put(graph[i].size(), map.getOrDefault(graph[i].size(), 0) + 1);
        }
      }
    }
    
    int count = 0;
    for(Map.Entry<Integer, Integer> entry: map.entrySet()){
      if(entry.getValue() == 1){
        count++;
      }
    }
    
    System.out.println(count);
  }
}

/*
REVIEW NOTES  (2026-09-12)

1. VERDICT: correct. Passes the sample plus: no edges, a lone edge, a triangle,
   a 4-path, two equal stars, star+isolated, star(3)+star(4), and star+triangle.
   Obeys the "no BFS/DFS" hint -- degrees alone, O(N + M).

2. WHY IT WORKS: a snowflake is exactly a star. A node c with deg != 1 whose
   every neighbour has deg 1 means the component is precisely {c} + N(c). Two
   easy misses that were handled: an isolated node (deg 0 != 1) is a snowflake
   of size 1, and a lone edge u--v is NOT one (it has ZERO nodes of deg != 1,
   not exactly one).

3. SUBTLE BUT FINE: the map is keyed on degree, not on component size. It still
   gives the right count because size = degree + 1, so two snowflakes match on
   one iff they match on the other. It READS like an off-by-one -- add a comment
   or key on size() + 1, or you will re-litigate this with yourself later.

4. DELIBERATE CHOICE: Set adjacency silently collapses a repeated edge. The
   statement only promises the two integers on a line differ (no self loops); it
   never rules out a duplicate edge. On 3 3 / 1 2 / 1 2 / 1 3 this answers 1
   (simple-graph reading) where multiplicity would give 0. Almost certainly
   untested, and Set is the sane reading -- but know it is a choice.

5. IMPROVE: `new HashSet[n]` is the unchecked-warning source again. The tally
   loop also collapses to Collections.frequency(map.values(), 1).

6. SAFE, DO NOT "FIX": entry.getValue() == 1 compares Integer against an int
   literal, so it unboxes. The trap only bites when BOTH sides are Integer.
*/
