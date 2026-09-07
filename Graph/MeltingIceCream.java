package Graph;

import java.util.*;
import java.io.*;

/*
Melting Ice Cream
https://www.algouniversity.com/problem/173/?asid=2036

You and your brother bought ice cream on a summer evening, and it is melting --
you must reach home within k minutes to save it.

You are given a grid representing your locality. Roads are '*' and buildings
are '#'. The ice cream shop is marked 'i' and your home is marked 'h'.

In 1 minute you may move UP, DOWN, LEFT or RIGHT into an adjacent cell,
provided that cell lies inside the grid and is not a building.

Constraints:
    1 <= N, M <= 1e3
    1 <= K <= 1e9

INPUT
First line contains three integers n, m and k: the grid dimensions and the time
before the ice cream melts.
The next n lines each contain a string of length m representing one row.

OUTPUT
Print YES if you can get home in time, otherwise NO.

EXAMPLE
    Sample 1 INPUT:
        5 5 16
        i#***
        *#*#*
        *#*#*
        *#*#*
        ***#h
    Sample 1 OUTPUT:
        YES

    The only route is a long S-shape: down the left column, right along the
    bottom, up the middle column, right along the top, then down the right
    column. It takes exactly 16 moves, and k is 16.

    Sample 2 INPUT:
        5 5 15
        i****
        ####*
        *****
        *####
        ****h
    Sample 2 OUTPUT:
        NO

    The zig-zag route here ALSO takes exactly 16 moves, but k is only 15.

    Sample 3 INPUT:
        3 3 100
        i#*
        ###
        *#h
    Sample 3 OUTPUT:
        NO

    Home is walled off completely, so no value of k helps.

NOTE on "before k minutes": the two given samples pin this down precisely.
Both have a shortest route of exactly 16 moves; sample 1 has k = 16 and
answers YES, sample 2 has k = 15 and answers NO. So the test is

    reachable AND shortest distance <= k

-- arriving exactly on minute k counts as saving the ice cream.

HINT (BFS on an implicit graph): this is the same BFS you have been writing,
but there is no edge list to build. The graph is the grid itself: the nodes are
cells, and the neighbours of (r, c) are the four cells (r-1,c), (r+1,c),
(r,c-1), (r,c+1). Do not construct an adjacency list -- generate neighbours on
the fly with direction arrays:

    int[] dr = {-1, 1, 0, 0};
    int[] dc = {0, 0, -1, 1};

For each of the four, compute (nr, nc), and accept it only if
    0 <= nr < n  and  0 <= nc < m  and  grid[nr][nc] != '#'  and  not visited.
Check the bounds BEFORE indexing the grid, or you will get an
ArrayIndexOutOfBoundsException at the border.

Scan the grid once first to locate 'i' and 'h'. Both are walkable cells -- only
'#' blocks you. Then BFS from 'i' with a dist[][] (or a visited[][] plus a
level counter) and compare dist at 'h' against k.

Things to get right:
  - k can be as large as 1e9, but the grid has at most 1e3 * 1e3 = 1e6 cells,
    so the longest possible route is under 1e6. k's size is a red herring; do
    NOT size any array by k
  - unreachable home is NO regardless of k (Sample 3)
  - 1e6 cells is enough that boxing matters: prefer encoding a cell as the
    single int r * m + c in an ArrayDeque<Integer>, or two int[] arrays used as
    a manual queue, over queueing a new int[]{r,c} per cell
  - read the grid rows with br.readLine() directly; they are strings, not
    whitespace-separated tokens, so do not run them through StringTokenizer
  - the first line has THREE integers, not two
*/
public class MeltingIceCream {
    public static void main(String[] args) throws IOException{
        int n, m, k;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        
        List<List<Integer>> adjList = new ArrayList<>();
        for(int i = 0; i < n*m; i++){
            adjList.add(new ArrayList<>());
        }

        int src = 0, dest = 0;
        for(int r = 0; r < n; r++){
            char[] row = br.readLine().toCharArray();
            for(int c = 0; c < m; c++){
                if(row[c] != '#'){
                    if(r > 0){
                        adjList.get((r-1)*m + c).add(r*m + c);
                    }
                    if(c > 0){
                        adjList.get(r*m + c-1).add(r*m + c);
                    }
                    if(c != m-1){
                        adjList.get(r*m + c+1).add(r*m + c);
                    }
                    if(r != n-1){
                        adjList.get((r+1)*m + c).add(r*m + c);
                    }
                }
                if(row[c] == 'i'){
                    src = r*m + c;
                }
                if(row[c] == 'h'){
                    dest = r*m + c;
                }
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        int[] levelMap = new int[n*m];
        Arrays.fill(levelMap, -1);

        queue.offer(src);
        levelMap[src] = 0;

        while(!queue.isEmpty()){
            int current = queue.poll();
            List<Integer> currList = adjList.get(current);

            for(Integer num : currList){
                if(levelMap[num] == -1){
                    levelMap[num] = levelMap[current] + 1;
                    queue.offer(num);
                }
                if(num == dest){
                    queue.clear();
                    break;
                }
            }
        }

        if(levelMap[dest] == -1 || levelMap[dest] > k){
            System.out.println("NO");
        }
        else{
            System.out.println("YES");
        }

    }
    
}
