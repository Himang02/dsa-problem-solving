package Graph;

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
