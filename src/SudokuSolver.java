public class SudokuSolver {
    private static final int SIZE = 9;

    public interface UpdateCallBack{
        void onUpdate(int[][] board);
    }
    public static boolean solveBoard(int[][] board, UpdateCallBack callback){
        for(int row = 0; row<SIZE; row++){
            for(int col= 0; col<SIZE; col++){
                if(board[row][col] == 0){
                    for(int i = 1; i<= SIZE; i++){
                        if(isValid(board, row, col, i)){
                            board[row][col] = i;
                            callback.onUpdate(copyBoard(board)); // Send current state
                            try { Thread.sleep(3); } catch (InterruptedException ignored) {}

                            if(solveBoard(board, callback)){
                                return true;
                            }else{
                                board[row][col] = 0;
                                callback.onUpdate(copyBoard(board));
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static int[][] copyBoard(int[][] board) {
        int[][] copy = new int[SIZE][SIZE];
        for(int i = 0; i<SIZE; i++){
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
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
