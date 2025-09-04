import java.util.*;

/* P33: Matrix Rotation
 * Given a square matrix A of size N×N, rotate it by 90 degrees in the anti-clockwise direction and output the new rotate matrix.
 * 1≤N≤1000 && 0≤Aij≤109
 * Input: The first line contains a single integer N. The next N lines contain N integers each indicating the rows of the matrix.
 * Output: Output N rows with N space separated integers representing the rotated matrix.
 */

public class MatrixRotation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        
        int[][] matrix = new int[n][n];
        for(int row = 0; row< n; row++){
            for(int col = 0; col<n; col++){
                matrix[row][col] = sc.nextInt();
            }
        }

        System.out.println("Approach 1: Copy to another matrix");
        printMatrix(appraoch1(matrix));
        System.out.println("Approach 2: Layer by layer rotation");
        approach2(matrix);
        printMatrix(matrix);
        System.out.println("Approach 3: Transpose + Reverse each column");
        approach3(matrix);
        printMatrix(matrix);
    
    }

    // Approach - 1: Copy to another matrix, TC: O(N^2), SC: O(N^2)
    private static int[][] appraoch1(int[][] matrix) {
        int n = matrix.length;
        int[][] rotatedMatrix = new int[n][n];
        
        for(int row = 0; row < n; row++){
            for(int col = 0; col < n; col++){
                rotatedMatrix[n - col - 1][row] = matrix[row][col];
            }
        }
        return rotatedMatrix;
    }

    // Approach - 2: Layer by layer rotation, TC: O(N^2), SC: O(1)
    private static void approach2(int[][] matrix) {
        int n = matrix.length;
        for(int layer = 0; layer < n/2; layer++){
            rotateLayerAnticlockwise(matrix, layer);
        }
    
    }

    private static void rotateLayerAnticlockwise(int[][] matrix, int layer) {
        int start = layer;
        int end = matrix.length - layer - 1;
        for(int i = start; i<end; i++){
            int temp = matrix[start][i];
            matrix[start][i] = matrix[i][end];
            matrix[i][end] = matrix[end][end + start - i];
            matrix[end][end + start - i] = matrix[end + start - i][start];
            matrix[end + start - i][start] = temp;
        }
    }

    // Approach - 3: Transpose + Reverse each column, TC: O(N^2), SC: O(1)
    private static void approach3(int[][] matrix) {
        transpose(matrix);
        reverseEachColumn(matrix);
    }

    private static void transpose(int[][] matrix) {
        for(int row = 0; row < matrix.length; row++){
            for(int col = 0; col < row; col++){
                int temp = matrix[row][col];
                matrix[row][col] = matrix[col][row];
                matrix[col][row] = temp;
            }
        }
    }

    private static void reverseEachColumn(int[][] matrix) {
        int n = matrix.length;
        for(int col = 0; col < n; col++){
            int top = 0;
            int bottom = n - 1;
            while(top < bottom){
                int temp = matrix[top][col];
                matrix[top][col] = matrix[bottom][col];
                matrix[bottom][col] = temp;
                top++;
                bottom--;
            }
        }
    }

    private static void printMatrix(int[][] matrix) {
        for(int row = 0; row < matrix.length; row++){
            for(int col = 0; col < matrix[0].length; col++){
                System.out.print(matrix[row][col] + " ");
            }
            System.out.println();
        }
    }
}

