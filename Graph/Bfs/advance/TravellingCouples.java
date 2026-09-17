package Graph.Bfs.advance;

import java.util.*;
import java.io.*;

/*
Travelling Couples
https://www.algouniversity.com/problem/25/?asid=2044

AlgoLand has N cities and M pairs of cities joined by bidirectional trains. To
ride between any connected pair you buy one ticket, and the price does not
depend on which pair it is:

    Type I   costs C1   -- only a male may travel on it
    Type II  costs C2   -- only a female may travel on it
    Type III costs C3   -- only a couple travelling TOGETHER from the same city

A man is in city 1 and his wife is in city 2. Both must reach home in city N.
They may arrive separately or together. Find the minimum total cost of tickets.

Constraints:
    1 <= C1, C2, C3 <= 4e4
    3 <= N <= 4e4
    every city is reachable from every other city

INPUT
First line contains five integers: C1 C2 C3 N M.
The next M lines each contain a pair of cities joined by a train route.

OUTPUT
A single integer: the minimum collective ticket cost.

EXAMPLE
    Sample 1 INPUT:
        4 4 5 8 8
        1 4
        2 3
        3 4
        4 7
        2 5
        5 6
        6 8
        7 8
    Sample 1 OUTPUT:
        22

    The husband rides 1 -> 4 alone (1 hop, cost 4). The wife rides 2 -> 3 -> 4
    alone (2 hops, cost 8). Now both are in city 4, so they continue together
    4 -> 7 -> 8 on couple tickets (2 hops, cost 10). Total 4 + 8 + 10 = 22.

    Sample 2 INPUT:
        4 4 100 8 8
        1 4
        2 3
        3 4
        4 7
        2 5
        5 6
        6 8
        7 8
    Sample 2 OUTPUT:
        24

    The SAME map, but a couple ticket now costs 100 instead of 5 -- far more
    than two singles at 4 + 4. So meeting up buys them nothing: he rides
    1 -> 4 -> 7 -> 8 (3 hops, 12) and she rides 2 -> 5 -> 6 -> 8 (3 hops, 12),
    never travelling together at all. Total 24.

HINT (pick the meeting city, then three BFS runs): whatever they do, the
journey has exactly one shape -- each travels alone to some MEETING CITY K, and
from K they finish the trip together. Fix K and the cost is forced:

    cost(K) = C1 * dist(1, K)
            + C2 * dist(2, K)
            + togetherCost * dist(K, N)

So the answer is the smallest cost(K) over all N choices of K. Getting every
dist you need is three BFS runs -- from city 1, from city 2, and from city N --
after which one sweep over K finishes it. Edges are unweighted, so BFS gives
the hop counts directly; no Dijkstra needed.

Note the third BFS runs from N, not to N. The graph is undirected, so distance
from N to K equals distance from K to N, and one search from N gives you the
last leg for every possible K at once. That is the same "fix the sources, let
the destinations fall out" move as CarFactoryLocation -- and, as there, these
are three SEPARATE runs into three separate arrays, NOT a multi-source BFS: you
need each distance individually because each is multiplied by a different
price.

    togetherCost = min(C3, C1 + C2)

because a couple moving together may always ignore the couple ticket and buy
one male and one female ticket instead. Sample 2 is exactly this case.

Things to get right:
  - USE long. The worst case is about 3 * 4e4 * 4e4 = 4.8e9, which overflows a
    32-bit int (max 2.1e9). Unlike MostProfitableTeam, where the tight value
    bound rescued int, here it genuinely does not -- check the arithmetic, do
    not assume either way
  - togetherCost is min(C3, C1 + C2), never C3 blindly
  - K = N is a legal choice and means they never meet before home (Sample 2);
    the formula covers it because dist(N, N) = 0, so no special case is needed
  - K = 1 and K = 2 are legal too -- one of them simply does not move first
  - the first line holds FIVE integers, and N and M come LAST, after the three
    prices
  - the graph is guaranteed connected, so no distance is ever unreachable
  - BFS is iterative, so no 64 MB thread needed here
*/
public class TravellingCouples {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int c1 = Integer.parseInt(st.nextToken());
        int c2 = Integer.parseInt(st.nextToken());
        int c3 = Integer.parseInt(st.nextToken());
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

        int[] maleLevels = new int[n];
        int[] femaleLevels = new int[n];
        int[] homeLevels = new int[n];
        long minCost = Long.MAX_VALUE;
        Arrays.fill(maleLevels, -1);
        Arrays.fill(femaleLevels, -1);
        Arrays.fill(homeLevels, -1);

        levelOrderTraverse(0, adjList, maleLevels);
        levelOrderTraverse(1, adjList, femaleLevels);
        levelOrderTraverse(n-1, adjList, homeLevels);

        for(int i = 0; i < n; i++){
            if(maleLevels[i] != -1 && femaleLevels[i] != -1 && homeLevels[i] != -1){
                // long arithmetic: 3 * 4e4 * 4e4 = 4.8e9 overflows int
                long cost = (long) maleLevels[i]*c1 + (long) femaleLevels[i]*c2 + (long) homeLevels[i]*c3;
                minCost = Math.min(cost, minCost);
            }
        }

        System.out.println(minCost);
        
    }

    private static void levelOrderTraverse(int src, List<List<Integer>> adjList, int[] level){

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(src);
        level[src] = 0;

        while(!queue.isEmpty()){
            int curr = queue.poll();

            for(Integer neigh : adjList.get(curr)){
                if(level[neigh] == -1){
                    queue.offer(neigh);
                    level[neigh] = level[curr] + 1;
                }
            }
        }

    }
}

/*
REVIEW NOTES  (2026-09-17)

1. VERDICT: correct after one fix. 2/2 samples, 5/5 direct cases, 0 of 300
   randomized graphs fail, and the 40,000-city worst case now gives the right
   answer in 0.34s.

2. BUG FIXED -- INT OVERFLOW, SILENT. The cost line was all int, so a 40,000-city
   path with all three prices at 40,000 printed -2147447296 instead of
   1599960000. No exception, just a negative number. Now the products are cast
   to long and minCost seeds from Long.MAX_VALUE -- note BOTH had to change;
   widening only the arithmetic would still have compared against an int
   sentinel. The bound to check is 3 * 4e4 * 4e4 = 4.8e9 against int's 2.1e9.
   Contrast MostProfitableTeam, where the tight -200..200 value bound kept int
   safe: same question, opposite answer, so do the arithmetic every time rather
   than reaching for a default.

3. WHY NO SMALL TEST CATCHES IT: overflow needs N and the prices BOTH near
   their limits. 300 randomized graphs with n <= 9 and prices <= 40 saw nothing.
   When constraints give large bounds on two factors that get multiplied, build
   one test that maxes both.

4. THE MODEL IS RIGHT: fix a meeting city K and the journey's cost is forced --
   each travels alone to K, then both travel on to N. Three SEPARATE BFS runs
   (from 1, from 2, from N) into three arrays, then one sweep over K. The third
   runs FROM N, which works because the graph is undirected, and it supplies
   the last leg for every candidate K at once. Not a multi-source BFS: each
   distance is multiplied by a different price, so they cannot be merged.

5. CORRECTION TO THE HINT ABOVE -- min(C3, C1+C2) IS NOT ACTUALLY REQUIRED, and
   using raw c3 (as this code does) is correct. If C3 > C1 + C2 then by the
   triangle inequality d1[N] <= d1[K] + dN[K] and likewise for d2, so K = N --
   travelling separately the whole way -- is always at least as good, and the
   raw formula evaluates that case exactly at dN[N] = 0. Sample 2 passes for
   precisely this reason. The min is harmless, just redundant.

6. THE -1 GUARD BEFORE THE ARITHMETIC is right even though the statement
   promises a connected graph -- it costs nothing and stops an unreachable
   sentinel poisoning the sum if that promise is ever relaxed. Same discipline
   as the AvoidingCities and ThiefEscape notes, where -1 handling WAS the bug.
*/
