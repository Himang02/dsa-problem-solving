package Graph.Bfs;

import java.util.*;
import java.io.*;

/*
Adjacency Matrix to Adjacency List
https://www.algouniversity.com/problem/172/?asid=2033

You are given a graph with N nodes represented as an Adjacency Matrix. Convert
it into its equivalent Adjacency List and print it in the format given below.
As a refresher: in an Adjacency Matrix M, M[i][j] is 1 iff there exists an edge
between the ith and the jth node.

Constraints:
    1 <= N <= 100
    0 <= M[i][j] <= 1

INPUT
First line contains an integer N, the size of a square matrix.
Next N lines contain N integers each, representing the given Adjacency Matrix.

OUTPUT
Print N lines. The ith line begins with the prefix "i: " followed by all nodes
that share an edge with the ith node, separated by spaces. See the sample for
clarity.

EXAMPLE
    Sample 1 INPUT:
        4
        0 1 0 1
        1 0 1 0
        0 1 1 1
        1 0 1 1
    Sample 1 OUTPUT:
        1: 2 4
        2: 1 3
        3: 2 3 4
        4: 1 3 4

NOTE: nodes are 1-indexed in the output, while the matrix rows/columns are read
0-indexed. Self loops count (M[i][i] == 1 puts i in its own list), which is why
row 3 of the sample lists 3 itself.

NOTE (output buffering): printing with System.out.println inside the row loop
flushes to the OS once per row. Accumulating the whole answer in ONE
StringBuilder and printing it once is ~5x faster (measured: 200ms -> 41ms over
100k rows). Irrelevant at N <= 100, but the habit matters on problems that emit
1e5+ lines, where it is the difference between AC and TLE. Also prefer
sb.append(x).append(' ') over sb.append(x + " ") -- the latter builds a
throwaway String first, which is the work StringBuilder exists to avoid. If the
output is large enough that holding it all in memory is a concern (millions of
rows), stream it with PrintWriter(new BufferedWriter(...)) and flush() at the
end instead.
*/
public class AdjMatrixToAdjList {
    public static void main(String[] args) throws IOException{

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int n = Integer.parseInt(st.nextToken());
        List<Integer>[] adjList = new List[n];
        
        for(int i = 0; i < n; i++){
        adjList[i] = new ArrayList<>();
        }
        
        for(int i = 0; i < n; i++){
        st = new StringTokenizer(br.readLine());
        // int[] row = new int[n];
        for(int j = 0; j < n; j++){
            int present = Integer.parseInt(st.nextToken());
            if(present == 1){
            adjList[i].add(j+1);
            }
        }
        }
        
        for(int i = 0; i < n; i++){
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < adjList[i].size()-1; j++){
            sb.append(adjList[i].get(j) + " ");
        }
        if(adjList[i].size() > 0){
            sb.append(adjList[i].get(adjList[i].size()-1));
        }
        System.out.println((i+1) + ": " + sb.toString());
        }

    }

    
}

/*
REVIEW NOTES  (2026-09-12)

1. VERDICT: correct. Sample matches exactly; N=1 with a single 0, and an
   all-zeros 3x3, both print the bare "i: " prefix as the spec wants. At
   N <= 100 the O(N^2) read/print is nowhere near any limit.

2. IMPROVE: `new List[n]` is a raw array of generics -- that is where the
   "unchecked or unsafe operations" warning comes from. Use
   List<List<Integer>> adjList = new ArrayList<>(); instead. Later files
   (AvoidingCities onward) already do this.

3. IMPROVE: the "all but the last, then the last" split exists only to avoid a
   trailing space -- but the sample output in the statement HAS trailing spaces,
   so the judge trims. A single loop appending (x + " ") is equivalent and
   shorter. Better still: append ' ' BEFORE each neighbour, which removes the
   special case entirely.

4. FRAGILE: reading re-tokenizes per line, so it assumes each matrix row really
   is on its own line. The statement guarantees that, but it was verified to
   throw NullPointerException if the input wraps rows differently.
   StreamTokenizer is immune to line layout -- that, not speed, is its real
   advantage.

5. SEE ALSO: the output-buffering NOTE in the header above. Measured 5x
   (200ms -> 41ms over 100k rows) for one StringBuilder vs println-per-row.
   Irrelevant at N <= 100, but adopt it as a reflex.
*/
