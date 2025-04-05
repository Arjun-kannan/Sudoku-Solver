public class SudokuSolver {
    private static final int SIZE = 9;

    public static boolean solveBoard(int[][] board){
        for(int row = 0; row<SIZE; row++){
            for(int col= 0; col<SIZE; col++){
                if(board[row][col] == 0){
                    for(int i = 1; i<= SIZE; i++){
                        if(isValid(board, row, col, i)){
                            board[row][col] = i;
                            if(solveBoard(board)){
                                return true;
                            }else{
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(int[][] board, int row, int col, int n){
        for(int i = 0; i<SIZE; i++){
            if(board[i][col] == n || board[row][i] == n)
                return false;
        }

        int rowStart = row - row%3;
        int colStart = col - col%3;

        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                if(board[rowStart+i][colStart+j] == n){
                    return false;
                }
            }
        }
        return true;
    }
}
