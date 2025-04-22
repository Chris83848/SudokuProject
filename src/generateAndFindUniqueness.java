public class generateAndFindUniqueness {


/*
    public static boolean testUniqueness(int[][] sudokuBoard) {
        int[] numSolutions = {0};
        int[][] board = new int[9][9];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                board[x][y] = sudokuBoard[x][y];
            }
        }
        helpTestUniqueness(board, 0, 0, numSolutions);
        if (numSolutions[0] == 1) {
            return true;
        } else {
            return false;
        }
    }


    //This method runs similar to the solveBoard method, but instead looks for more than one solution.
    public static int[][] helpTestUniqueness(int[][] sudokuBoard, int i, int j, int[] solutions) {
        if (j == 9) {
            j = 0;
            i++;
        }

        if (sudokuBoard[i][j] == 0) {
            if (!placeNumber(sudokuBoard, i, j)) {
                return sudokuBoard;
            } else {
                if (boardComplete(sudokuBoard)) {
                    solutions[0]++;
                    if (solutions[0] > 1) {
                        return sudokuBoard;
                    }
                    sudokuBoard[i][j] = 0;
                    return sudokuBoard;
                } else {
                    int[][] currentBoard = helpTestUniqueness(sudokuBoard, i, j + 1, solutions);
                    while (!boardComplete(currentBoard)) {
                        if (!placeNumber(sudokuBoard, i, j)) {
                            return sudokuBoard;
                        } else {
                            currentBoard = helpTestUniqueness(sudokuBoard, i, j + 1, solutions);
                        }
                    }
                    return currentBoard;
                }
            }
        } else {
            return helpTestUniqueness(sudokuBoard, i, j + 1, solutions);
        }
    }


    public static boolean boardHumanlySolvable(int[][] sudokuBoard) {
        return boardComplete(solveSudokuBoardHumanly(sudokuBoard));
    }
    */
}


