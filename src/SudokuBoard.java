import java.util.ArrayList;

public class SudokuBoard {
    private static int[][] board;
    private String difficulty;

    public SudokuBoard(int[][] unsolvedBoard) {
        board = unsolvedBoard;
    }


    public static boolean isValid(int[][] sudokuBoard) {
        if (SudokuSolverApplication.boardHumanlySolvable(sudokuBoard)) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] != 0) {
                        if (SudokuSolverApplication.checkBoardError(board, i, j)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }


    //This method prints out the given sudoku board whether it is solved or not.
    public static void printBoard(int[][] board) {
        System.out.println("\n-------------------------");
        for (int i = 0; i < board.length; i++) {
            System.out.print("| ");
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] + " ");
                if (j == 2 || j == 5 || j == 8) {
                    System.out.print("| ");
                }
            }
            if (i == 2 || i == 5) {
                System.out.println("\n-------------------------");
            } else {
                System.out.println();
            }
        }
        System.out.println("-------------------------");
    }


    @Override
    public String toString() {
        String result = "";
        result += "\n-------------------------";
        for (int i = 0; i < board.length; i++) {
            result += "| ";
            for (int j = 0; j < board.length; j++) {
                result += board[i][j] + " ";
                if (j == 2 || j == 5 || j == 8) {
                    result += "| ";
                }
            }
            if (i == 2 || i == 5) {
                result += "\n-------------------------";
            } else {
                result += "\n";
            }
        }
        result += "-------------------------";
        return result;
    }


    public int[][] getBoard() {
        return board;
    }

}
