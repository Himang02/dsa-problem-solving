package Graph;

import java.util.*;
import java.io.*;

/*
Shreya's Journey Through AlgoLand
https://www.algouniversity.com/problem/173/?asid=2036

Shreya has a secret map of AlgoLand, in which every city AND every road has a
name. She wants to travel from city S to the holy-capital-city T using the
minimum number of roads.

AlgoLand is planned algorithmically such that there is AT MOST 1 shortest path
between S and T. It has N cities and M uni-directional roads.

Print the roads in the order Shreya will take them, or print "Impossible" if
the journey cannot be made.

Constraints:
    1 <= N <= 1000
    1 <= M <= 1e6
    each city name and road name is at most 10 characters

INPUT
First line contains two integers N and M: the number of cities and roads.
The next M lines contain three strings U, V, L: there is a road named L
leading FROM city U TO city V.
The last line contains two strings S and T: the starting city and the
destination city.

OUTPUT
If the journey is impossible, print "Impossible".
Otherwise print the number of roads on the first line, then the name of each
road taken, one per line, in travel order.

EXAMPLE
    Sample 1 INPUT:
        4 4
        xor-city list-city kunal-road
        matrix-city xor-city agni-path
        list-city matrix-city pallav-maarg
        matrix-city graph-city manvendra-road
        xor-city graph-city
    Sample 1 OUTPUT:
        3
        kunal-road
        pallav-maarg
        manvendra-road

    xor-city -> list-city -> matrix-city -> graph-city. Note that agni-path
    (matrix-city -> xor-city) is never used; roads are ONE-WAY, so it does not
    give you a shortcut back.

    Sample 2 INPUT:
        7 5
        A B a
        B C b
        C D c
        E F d
        F G e
        A G
    Sample 2 OUTPUT:
        Impossible

    A reaches B, C, D. G is only reachable from E and F, which A never gets to.

HINT (naming, then the usual BFS): the graph here is given by NAME rather than
by number, so before you can run anything you must turn names into indices.
Keep a HashMap<String, Integer> and assign each new city name the next free id
as you meet it:

    Integer id = ids.get(name);
    if (id == null) { id = ids.size(); ids.put(name, id); }

Then it is the parent-array BFS you already wrote in ShortestPathTrace, with
ONE addition: alongside parent[v] = u, also store the NAME of the road you used
to get there, e.g. parentRoad[v] = L. Walking back from T then yields the road
names in reverse; reverse them and print.

The graph is DIRECTED this time -- add the edge u -> v only, never v -> u.

Things to get right:
  - the "at most 1 shortest path" promise means you never have to break ties,
    so whatever route your BFS finds first is THE answer
  - S == T is a journey of 0 roads: print 0 and then nothing else
  - a city that appears in no road at all never enters your map; if S or T is
    such a city the answer is Impossible (unless S == T), so look them up
    defensively rather than assuming they are present
  - M can be 1e6, and each line holds THREE tokens, so this is by far the most
    input-heavy problem here. That is ~3e6 tokens and ~2e6 HashMap lookups;
    keep the reading tight and do not build any structure of size N * M
  - N is only 1000 while M is up to 1e6, so parallel roads between the same
    pair of cities are expected. BFS handles them for free -- the first one
    discovered wins and the rest are skipped by the visited check
  - print with a single StringBuilder, not a println per road
*/
public class AlgoLandJourney {
  public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int n = Integer.parseInt(st.nextToken());
    int m = Integer.parseInt(st.nextToken());

    Map<String, Integer> cityIndices = new HashMap<>();
    String[][] roads = new String[n][n];

    int index = 0;
    for(int i = 0; i < m; i++){
      st = new StringTokenizer(br.readLine());
      String city1 = st.nextToken();
      String city2 = st.nextToken();
      String road = st.nextToken();
      if(!cityIndices.containsKey(city1)){
        cityIndices.put(city1, index++);
      }
      if(!cityIndices.containsKey(city2)){
        cityIndices.put(city2, index++);
      }
      roads[cityIndices.get(city1)][cityIndices.get(city2)] = road;
      
    }

    st = new StringTokenizer(br.readLine());
    Integer src = cityIndices.get(st.nextToken());
    Integer dest = cityIndices.get(st.nextToken());

    if(src == null || dest == null){
      System.out.println("Impossible"); return; 
    }

    Queue<Integer> queue = new ArrayDeque<>();
    queue.offer(src);
    int[] parent = new int[n];
    Arrays.fill(parent, -1);
    parent[src] = -2;

    while(!queue.isEmpty()){
      int current = queue.poll();

      for(int i = 0; i < n; i++){
        if(i != current && roads[current][i] != null && parent[i] == -1){
          parent[i] = current;
          queue.offer(i);
          
          if(i == dest){
            queue.clear();
            break;
          }
        
        }

        
      }
    }

    if(parent[dest] == -1){
      System.out.println("Impossible");
    }
    else{
      List<String> roadsTaken = new ArrayList<>();
      int to = dest;
      int from = dest;
      while(from != src){
        from = parent[to];
        roadsTaken.add(roads[from][to]);
        to = from;
      }

      StringBuilder sb = new StringBuilder();
      sb.append(roadsTaken.size()).append('\n');
      for(int i = roadsTaken.size()-1; i >= 0; i--){
        sb.append(roadsTaken.get(i)).append('\n');
      }

      System.out.println(sb);
    }
  }
    
}
