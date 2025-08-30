
public class MatrixBoundaryTraversal {
    public static void main(String[] args) {
        int[][] m1 = {{1}};
        unkown(m1);
        System.out.println();

        int[][] m2 = {{1, 2}};
        unkown(m2);
        System.out.println();

        int[][] m3 = {{1}, {2}};
        unkown(m3);
        System.out.println();

        int[][] m4 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        unkown(m4);
        System.out.println();
        
        int[][] m5 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12}
        };
        unkown(m5);
        System.out.println();
        
        int[][] m6 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        unkown(m6);
        System.out.println();


    }


    /* Pattern - 1: Print the boundary elements of a matrix in clockwise manner
     * Edge cases: single row, single column
     */

    private static void printBoundary(int[][] matrix, int startingRow, int startingCol) {
        int totalRows = matrix.length - 2 * startingRow;
        int totalCols = matrix[0].length - 2 * startingCol;
        int column = startingCol, row = startingRow;
        
        // left to right top traversal
        while(column < startingCol + totalCols) {
            System.out.print(matrix[row][column++] + " ");
        }
        column--;
        row++;

        // top to bottom right traversal
        while(row < startingRow + totalRows){
            System.out.print(matrix[row++][column] + " ");
        }
        row--;
        column--;

        // right to left bottom traversal
        if(totalRows > 1) {
            while(column >= startingCol) {
                System.out.print(matrix[row][column--] + " ");
            }
            column++;
            row--;
        }

        // bottom to top left traversal
        if(totalCols > 1) {
            while(row > startingRow) {
                System.out.print(matrix[row--][column] + " ");
            }
        }
    }

    /* Pattern - 2: Spiral traversal of a matrix
     */

    private static void spiralTraversal(int[][] matrix){
        int totalRows = matrix.length;
        int totalCols = matrix[0].length;
        int startingRow = 0, startingCol = 0;

        while(startingRow <= totalRows / 2 && startingCol <= totalCols / 2) {
            printBoundary(matrix, startingRow, startingCol);
            startingRow++;
            startingCol++;
        }
    }
}
