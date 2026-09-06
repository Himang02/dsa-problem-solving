package Graph;

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
