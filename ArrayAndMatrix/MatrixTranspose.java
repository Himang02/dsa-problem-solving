import java.util.*;

public class MatrixTranspose {
    public static void main(String[] args) {
        // For N X N matrix
        int[][] mat1 = {{1}};
        int[][] mat2 = {{1, 2}, {3, 4}};
        int[][] mat3 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] mat4 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};

        transpose(mat1);
        printMatrix(mat1);

        transpose(mat2);
        printMatrix(mat2);

        transpose(mat3);
        printMatrix(mat3);

        transpose(mat4);
        printMatrix(mat4);

    }

    // Function to transpose a matrix
    public static void transpose(int[][] matrix) {
        for(int row = 0; row < matrix.length; row++){
            for(int col = 0; col < row; col++){
                int temp = matrix[row][col];
                matrix[row][col] = matrix[col][row];
                matrix[col][row] = temp;
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