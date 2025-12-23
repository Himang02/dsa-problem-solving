import java.util.Arrays;

class Sudoku {

    public static void main(String[] args) {
        char[][] board = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };

        solveSudoku(board);

        // Print the solved board
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    public static void solveSudoku(char[][] board) {
        int filled = 0;
        boolean[][][] hash = new boolean[9][3][9];
        int[] freq = new int[9];
        for(int row = 0; row <9; row++){
            for(int col = 0; col < 9; col++){
                if(board[row][col] <='9' && board[row][col]>='1'){
                    filled++;
                    freq[board[row][col]-'1']++;
                    hash[board[row][col]-'1'][0][row] = true;
                    hash[board[row][col]-'1'][1][col] = true;
                    hash[board[row][col]-'1'][2][(row/3)*3 + (col/3)] = true;
                }
            }
        }
        System.out.println("freq:"+Arrays.toString(freq));
        int maxFreq = 0, nextNum = 0;
                            for(int i =0; i< 9; i++){
                                maxFreq = Math.max(maxFreq, freq[i]%9);
                                if(freq[nextNum]<maxFreq){
                                    nextNum = i;
                                }
                            }
        solve(board, hash, filled, freq, nextNum);
    }

    private static boolean solve(char[][] board, boolean[][][] hash, int filled, int[] freq, int num){
        // base case
        if(filled == 81){
            return true;
        }

        // transition
        // for(int i = 0; i<9; i++){
        //     int num = 
            for(int row = 0; row <9; row++){
                if(!hash[num][0][row]){
                    for(int col = 0; col<9; col++){
                        if(!hash[num][1][col] && !hash[num][2][(row/3)*3 + (col/3)] && !(board[row][col]<='9' && board[row][col]>='1' )){
                            // action
                            hash[num][0][row] = true;
                            hash[num][1][col] = true;
                            hash[num][2][(row/3)*3 + (col/3)] = true;
                            board[row][col] = (char)('1'+(char)num);
                            filled++;
                            freq[num]++;
                            // System.out.println((num+1)+" at "+row+", "+col);
                            // System.out.println("freq:"+Arrays.toString(freq));
                            //recursion
                            int maxFreq = 0, nextNum = 0;
                            for(int i =0; i< 9; i++){
                                maxFreq = Math.max(maxFreq, freq[i]%9);
                                if((freq[nextNum]%9)<maxFreq || freq[nextNum]==9){
                                    nextNum = i;
                                }
                            }
                            if(solve(board, hash, filled, freq, nextNum)){
                                return true;
                            }

                            // cancellation
                            hash[num][0][row] = false;
                            hash[num][1][col] = false;
                            hash[num][2][(row/3)*3 + (col/3)] = false;
                            board[row][col] = 0;
                            filled--;
                            freq[num]--;
                        }
                    }
                    if(!hash[num][0][row]){
                        return false;
                    }
                }
                
            }
        // }
        return false;
    }
}