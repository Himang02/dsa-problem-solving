
public class MatrixBoundaryTraversal {

    /*
        * No. of diaginals of one '/' or '\' type = totalRows + totalCols - 1
        * diagonal index '/' type = totalCols - 1 - col + row
        * diagonal index '\' type = totalRows - 1 - row + col 
    */
    public static void main(String[] args) {
        int[][] m1 = {{1}};
        unkown(m1, 0);
        System.out.println();

        int[][] m2 = {{1, 2}};
        unkown(m2, 0);
        System.out.println();

        int[][] m3 = {{1}, {2}};
        unkown(m3, 0);
        System.out.println();

        int[][] m4 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        unkown(m4, 0);
        System.out.println();
        
        int[][] m5 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12}
        };
        unkown(m5, 0);
        System.out.println();
        
        int[][] m6 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        unkown(m6, 0);
        System.out.println();

        int[][] m7 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        };
        unkown(m7, 0);
        System.out.println();


    }


    /* Variation - 1: Print the boundary elements of a matrix in clockwise manner
        * Edge cases: single row, single column
     */

    private static void printBoundary(int[][] matrix, int start) {
        int totalRows = matrix.length - 2 * start;
        int totalCols = matrix[0].length - 2 * start;
        int column = start, row = start;
        
        // left to right top traversal
        while(column < start + totalCols) {
            System.out.print(matrix[row][column++] + " ");
        }
        column--;
        row++;

        // top to bottom right traversal
        while(row < start + totalRows){
            System.out.print(matrix[row++][column] + " ");
        }
        row--;
        column--;

        // right to left bottom traversal
        if(totalRows > 1) {
            while(column >= start) {
                System.out.print(matrix[row][column--] + " ");
            }
            column++;
            row--;
        }

        // bottom to top left traversal
        if(totalCols > 1) {
            while(row > start) {
                System.out.print(matrix[row--][column] + " ");
            }
        }
    }

    /* Variation - 2: Spiral traversal of a matrix
     */

    private static void spiralTraversal(int[][] matrix){
        int totalRows = matrix.length;
        int totalCols = matrix[0].length;
        int start = 0;

        while(start < totalRows / 2.0 && start < totalCols / 2.0) {         // VIMP: this was wrong in previous commit
            printBoundary(matrix, start);
            start++;
        }
    }

    /* Variation - 3: Recursive Spiral traversal of a matrix
    */
    private static void recursiveSpiralTraversal(int[][] matrix, int start) {
        // base condition
        if(start >= Math.min(matrix.length, matrix[0].length) / 2.0) {
            return;
        }

        // transition
        printBoundary(matrix, start);
        unkown(matrix, start + 1);
    }

    /* Variation - 4: Reverse Spiral traversal of a matrix
        * Edge cases: single row, single column
    */
    private static void reverseBoundaryTraversal(int[][] matrix, int rowIndexStart, int colIndexStart, int rowIndexEnd, int colIndexEnd) {
        int col, row;
        if(colIndexStart < colIndexEnd && rowIndexStart < rowIndexEnd) {
            col = colIndexStart;
            row = rowIndexStart+1;
            while(row <= rowIndexEnd) {
                System.out.print(matrix[row++][col] + " ");
            }
            row--;
            col++;
            while(col <= colIndexEnd) {
                System.out.print(matrix[row][col++] + " ");
            }
            col = colIndexEnd;
            row = rowIndexEnd-1;
        }
        else {
            col = colIndexEnd;
            row = rowIndexEnd;
        }

        while(row >= rowIndexStart) {
            System.out.print(matrix[row--][col] + " ");
        }
        row++;
        col--;
        while(col >= colIndexStart) {
            System.out.print(matrix[row][col--] + " ");
        }
    }

    private static void reverseSpiralTraversal(int[][] matrix) {
        int midRow = matrix.length / 2;
        int midCol = matrix[0].length / 2;
        if(midRow * 2 == matrix.length){
            midRow--;   
        }
        if(midCol * 2 == matrix[0].length) {
            midCol--;
        }
        int start = Math.min(midCol, midRow);

        for(; start >=0; start--){
            reverseBoundaryTraversal(matrix, start, start, matrix.length - 1 - start, matrix[0].length - 1 - start);
        }
    }

    /* Variaton - 5: Zig-Zag diagonal traversal of a matrix
    */

   private static void zigZagDiagonalTraversal(int[][] matrix) {
        int startRow = 0, startCol = 0;

        while(startRow < matrix.length){
            int row = startRow, col = startCol;

            while(row >= 0 && col < matrix[0].length){
                System.out.print(matrix[row][col] + " ");
                row--;
                col++;
            }
            startRow++;
        }
        startRow--;
        startCol++;
        while(startCol < matrix[0].length){
            int row = startRow, col = startCol;

            while(row >= 0 && col < matrix[0].length){
                System.out.print(matrix[row][col] + " ");
                row--;
                col++;
            }
            startCol++;
        }
    }


    /* TODO:
        * Zig-Zag Row-wise Traversal → Similar to snake, but in triangular sub-sections.
        * Boundary Sum / Spiral Sum → Instead of traversal, compute sum or min/max.
        * Rotate a matrix by 90/180/270 degree
        * Diagonal traversal of a matrix
        * Transpose of a matrix
        * Search an element in a sorted 2D array
        * Find median in a row-wise sorted matrix
        * Find k-th smallest element in a row-wise sorted matrix
        * Find common elements in all rows of a given matrix
        * Maximum size rectangle binary sub-matrix with all 1s
        * Print elements in sorted order using row-column wise sorted matrix
        * Spiral traversal with obstacles
    */ 
    
}
