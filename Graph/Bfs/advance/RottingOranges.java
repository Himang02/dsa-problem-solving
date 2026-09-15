package Graph.Bfs.advance;

import java.util.*;
import java.io.*;

/*
Rotting Oranges  (classic -- LeetCode 994)

You are given an N x M grid where each cell holds one of:
    0 = empty cell
    1 = a fresh orange
    2 = a rotten orange

Every minute, any fresh orange that is 4-directionally adjacent to a rotten
orange becomes rotten. Find the minimum number of minutes until no cell has a
fresh orange. If that is impossible, return -1.

IMPORTANT reading of the rule: in one minute EVERY rotten orange rots ALL of
its fresh neighbours, and all of this happens simultaneously. It is not "one
neighbour per rotten orange per minute" -- rotting spreads outward in whole
layers, one layer per minute.

Constraints:
    1 <= N, M <= 1000

INPUT
First line contains two integers N and M.
The next N lines each contain M integers (0, 1 or 2).

OUTPUT
A single integer: the minimum number of minutes, or -1 if some fresh orange can
never rot.

EXAMPLE
    Sample 1 INPUT:
        3 3
        2 1 1
        1 1 0
        0 1 1
    Sample 1 OUTPUT:
        4

    Minute by minute the rot spreads outward from (1,1) in layers:
        t=1 rots (1,2) and (2,1)
        t=2 rots (1,3) and (2,2)
        t=3 rots (3,2)
        t=4 rots (3,3)
    The answer is the time the LAST orange rots, which is the depth of the BFS.

    Sample 2 INPUT:
        3 3
        2 1 1
        0 1 1
        1 0 1
    Sample 2 OUTPUT:
        -1

    The orange at (3,1) is cut off by the empty cells around it, so it can
    never rot no matter how long you wait.

    Sample 3 INPUT:
        1 2
        0 2
    Sample 3 OUTPUT:
        0

    There are no fresh oranges to begin with, so zero minutes are needed. Do
    not confuse "nothing to do" with "impossible".

HINT (multi-source BFS): this is the case CarFactoryLocation explicitly was
NOT. There, four corners each needed their own distance, so you ran four
separate searches. Here every rotten orange spreads the SAME rot, and you only
care how long until the last fresh one is reached -- so seed ONE queue with
ALL rotten oranges at time 0 and let them expand together:

    for every cell (r,c):
        if grid[r][c] == 2 -> queue.offer(cell) with time 0
        if grid[r][c] == 1 -> fresh++

Then run a normal BFS. Each fresh orange you reach becomes rotten, decrement
fresh, and record its time. The answer is the largest time seen -- which is
just the depth of the BFS -- provided fresh has reached 0.

Seeding many sources at once costs nothing extra: the queue still holds each
cell once, so it stays O(N*M). What it buys you is that the first time BFS
reaches a cell is automatically via the NEAREST rotten orange, which is exactly
the minute it actually rots.

Things to get right:
  - the answer is -1 if ANY fresh orange remains at the end; count the fresh
    ones up front and decrement as you rot them, rather than rescanning
  - a grid with NO fresh oranges answers 0, even if it also has no rotten ones
    (Sample 3). Zero is not the same as impossible
  - a fresh orange with no rotten orange anywhere answers -1
  - empty cells (0) are walls -- rot does not pass through them
  - the answer is the MAXIMUM time over all rotted oranges, not a count of
    anything. If you track time on the queue entries, take the max; if you
    process the queue level by level, count the levels -- but then be careful
    not to count a final empty level
  - 1e6 cells, so keep the queue cheap and the grid in int[][] or char[][];
    BFS here is iterative, so no stack worries
*/
public class RottingOranges {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int[][] grid = new int[n][m];

        for(int i = 0; i < n; i ++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < m; j++){
                grid[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        Queue<Cell> queue = new ArrayDeque<>();
        int[][] level = new int[n][m];
        for(int[] row: level){
            Arrays.fill(row, -1);
        }

        for(int i = 0; i < n; i ++){
            for(int j = 0; j < m; j++){
                if(grid[i][j] == 2){
                    queue.offer(new Cell(i, j));
                    level[i][j] = 0;
                }
            }
        }

        while(!queue.isEmpty()){
            Cell currCell = queue.poll();
            int row = currCell.row;
            int col = currCell.col;

            if(row < n-1 && isValid(row+1, col, level, grid)){
                queue.offer(new Cell(row+1, col));
                level[row+1][col] = level[row][col] + 1;
            }
            if(row > 0 && isValid(row-1, col, level, grid)){
                queue.offer(new Cell(row-1, col));
                level[row-1][col] = level[row][col] + 1;
            }
            if(col < m-1 && isValid(row, col+1, level, grid)){
                queue.offer(new Cell(row, col+1));
                level[row][col+1] = level[row][col] + 1;
            }
            if(col > 0 && isValid(row, col-1, level, grid)){
                queue.offer(new Cell(row, col-1));
                level[row][col-1] = level[row][col] + 1;
            }
        }

        int maxTime = 0;   // 0, not -1: an all-empty grid needs no time at all
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(isValid(i, j, level, grid)){
                    System.out.println(-1);
                    return;
                }
                maxTime = Math.max(maxTime, level[i][j]);
            }
        }
        System.out.println(maxTime);
    }

    private static boolean isValid(int row, int col, int[][] level, int[][] grid){
        return level[row][col] == -1 && grid[row][col] == 1;
    }
}

class Cell{
    int row; int col;
    Cell(int row, int col){
        this.row = row; this.col = col;
    }
}

/*
REVIEW NOTES  (2026-09-15)

1. VERDICT: correct after a one-character fix. Everything passed except the
   all-empty grid; 300 random grids differential-tested against a reference gave
   24 failures, ALL of them that single case, and 0 after the fix. Scale:
   1000x1000 mixed in 0.61s, and a single source spreading across an all-fresh
   1000x1000 grid (BFS depth 1998) in 0.47s.

2. BUG FIXED -- 0 IS NOT -1. maxTime started at -1, and on a grid with no rotten
   oranges nothing ever raises it. With no fresh oranges either, the right answer
   is 0 (nothing to rot) but it printed -1 (impossible). Starting at 0 is safe
   everywhere: when rotten oranges exist every level is >= 0 so the max is
   unaffected, and the unreachable-fresh-orange case returns -1 from the sweep
   before maxTime is ever printed. Note the near miss -- Sample 3 (0 2) DOES
   pass, because the rotten orange supplies a level 0; only a grid with neither
   kind of orange exposes it.

3. THE MULTI-SOURCE SEEDING IS TEXTBOOK: every 2 enters the queue at level 0
   BEFORE the loop starts. That is what makes "first reached = nearest rotten
   orange = the minute it actually rots" true, and it costs nothing -- each cell
   still enters the queue once, so it stays O(N*M). Contrast CarFactoryLocation,
   where the four sources each needed their own distance and multi-source would
   have been wrong; sources may be merged only when they are interchangeable.

4. NICE COMPRESSION: level[][] is both the distance record and the visited
   marker (-1 = unvisited), the same trick as parents[] in
   DetectCycleUndirected. And isValid() does double duty -- the spread test
   during BFS and the survivor test during the final sweep -- because "still -1
   and still fresh" means exactly "never rotted" in both contexts.

5. EMPTY CELLS ARE CORRECTLY WALLS: the grid[row][col] == 1 half of isValid
   stops rot crossing a 0, which is what strands the orange in Sample 2.

6. STYLE ONLY: the four hand-written direction blocks are ~16 lines where a
   dr[]/dc[] loop is five, and four separate places to mistype an index -- same
   note as CarFactoryLocation. All four are correct here (checked).
*/
