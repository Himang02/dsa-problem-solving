class NQueens {
    // T(N) = N * T(N-1) = O(N!)
    // S(N) = O(N^2)
    public static void main(String[] args) {
        System.out.println("Total arrangements for 1 queens: " + getAllPatterns(1));
        System.out.println("Total arrangements for 2 queens: " + getAllPatterns(2));
        System.out.println("Total arrangements for 3 queens: " + getAllPatterns(3));
        System.out.println("Total arrangements for 4 queens: " + getAllPatterns(4));
        System.out.println("Total arrangements for 5 queens: " + getAllPatterns(5));
        System.out.println("Total arrangements for 6 queens: " + getAllPatterns(6));
        System.out.println("Total arrangements for 7 queens: " + getAllPatterns(7));
        // System.out.println("Total arrangements for 8 queens: " + getAllPatterns(8));
        // System.out.println("Total arrangements for 9 queens: " + getAllPatterns(9));
    }

    private static int getAllPatterns(int N) {

        boolean[][] board = new boolean[N][N];
        boolean[] rows = new boolean[N];
        boolean[] diag1 = new boolean[2*N - 1];
        boolean[] diag2 = new boolean[2*N - 1];

        return forCol(0, board, rows, diag1, diag2);
    }

    private static int forCol(int col, boolean[][] board, boolean[] rows, boolean[] diag1, boolean[] diag2) {
        
        // base condition
        if (col == board.length) {
            printBoard(board);
            return 1; // Found a valid arrangement
        }

        // transition
        int count = 0;
        for (int row = 0; row < board.length; row++) {
            if (!rows[row] && !diag1[row + col] && !diag2[board.length - 1 + col - row]) {
                
                // action
                board[row][col] = true;
                rows[row] = true;
                diag1[row + col] = true;
                diag2[board.length - 1 + col - row] = true;

                // recur
                count += forCol(col + 1, board, rows, diag1, diag2);

                // cancellation
                board[row][col] = false;
                rows[row] = false;
                diag1[row + col] = false;
                diag2[board.length - 1 + col - row] = false;
            }
        }
        return count;
    }

    private static void printBoard(boolean[][] board) {
        for (boolean[] row : board) {
            for (boolean cell : row) {
                System.out.print(cell ? "Q " : "X ");
            }
            System.out.println();
        }
        System.out.println("-----------------");
    }


}