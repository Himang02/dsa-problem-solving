class WordSearch {
    public static void main(String[] args) {
        char[][] board = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
        String word = "ABCCED";
        System.out.println(exist(board, word)); // true

        word = "SEE";
        System.out.println(exist(board, word)); // true

        word = "ABCB";
        System.out.println(exist(board, word)); // false
    }

    public static boolean exist(char[][] board, String word) {
        boolean[][] isVisited = new boolean[board.length][board[0].length];
        for(int i = 0; i< board.length; i++){
            for(int j = 0; j<board[0].length; j++){
                if(board[i][j] == word.charAt(0)){
                    isVisited[i][j] = true;
                    if(fun(isVisited, board, i, j, word, ""+word.charAt(0), 0)){
                        return true;
                    }
                    isVisited[i][j] = false;
                }
            }
        }
        return false;
    }

    private static boolean fun(boolean[][] isVisited, char[][]board, int row, int col, String word, String cur, int index){
        // base case
        if(cur.equals(word)){
            return true;
        }

        // transition
        if(col < board[0].length-1 && !isVisited[row][col+1] && word.charAt(index+1)== board[row][col+1]){
            isVisited[row][col+1] = true;
            String next = cur + board[row][col+1];
            // System.out.println(next + " : "+ row+", "+ (col+1));
            if(fun(isVisited, board, row, col+1, word, next, index+1)){
                return true;
            }
            isVisited[row][col+1] = false;
        }
        if(row < board.length-1 && !isVisited[row+1][col] && word.charAt(index+1)== board[row+1][col]){
            isVisited[row+1][col] = true;
            String next = cur + board[row+1][col];
            // System.out.println(next + " : "+ (row+1)+", "+ col);
            if(fun(isVisited, board, row+1, col, word, next, index+1)){
                return true;
            }
            isVisited[row+1][col] = false;
        }
        if(col > 0  && !isVisited[row][col-1] && word.charAt(index+1)== board[row][col-1]){
            isVisited[row][col-1] = true;
            String next = cur + board[row][col-1];
            // System.out.println(next + " : "+ row+", "+ (col-1));
            if(fun(isVisited, board, row, col-1, word, next, index+1)){
                return true;
            }
            isVisited[row][col-1] = false;
        }
        if(row > 0  && !isVisited[row-1][col] && word.charAt(index+1)== board[row-1][col]){
            isVisited[row-1][col] = true;
            String next = cur + board[row-1][col];
            // System.out.println(next + " : "+ (row-1)+", "+ (col));
            if(fun(isVisited, board, row-1, col, word, next, index+1)){
                return true;
            }
            isVisited[row-1][col] = false;
        }

        return false;
    }
}