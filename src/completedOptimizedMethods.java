import java.util.*;

public class completedOptimizedMethods {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int value;
        int[][] sudokuBoard = new int[9][9];
        for (int i = 0; i < sudokuBoard.length; i++) {
            for (int j = 0; j < sudokuBoard.length; j++) {
                value = scan.nextInt();
                sudokuBoard[i][j] = value;
            }
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


    //This method checks for an error in the sub grid of the given index.
    public static boolean checkGridError(int[][] board, int i, int j) {
        int heightStart, widthStart;

        if (i <= 2) {
            heightStart = 0;
        } else if (i <= 5) {
            heightStart = 3;
        } else {
            heightStart = 6;
        }

        if (j <= 2) {
            widthStart = 0;
        } else if (j <= 5) {
            widthStart = 3;
        } else {
            widthStart = 6;
        }

        for (int height = heightStart; height <= heightStart + 2; height++) {
            for (int width = widthStart; width <= widthStart + 2; width++) {
                if (height != i && width != j) {
                    if (board[height][width] == board[i][j]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //This method checks for an error in the column of the given index.
    public static boolean checkColumnError(int[][] board, int i, int j) {
        for (int k = 0; k < board.length; k++) {
            if (k != i) {
                if (board[k][j] == board[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }


    //This method checks for an error in the row of the given index.
    public static boolean checkRowError(int[][] board, int i, int j) {
        for (int k = 0; k < board[0].length; k++) {
            if (k != j) {
                if (board[i][k] == board[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }


    //This method uses the column, grid, and row methods to check if a given index is valid where it is.
    public static boolean checkBoardError(int[][] board, int i, int j) {
        if (checkColumnError(board, i, j) || checkRowError(board, i, j) || checkGridError(board, i, j)) {
            return true;
        } else {
            return false;
        }
    }


    //This method checks if the given sudoku board is complete and has no empty spaces.
    public static boolean boardComplete(int[][] board) {
        for (int[] row: board) {
            for (int column: row) {
                if (column == 0) {
                    return false;
                }
            }
        }
        return true;
    }


    //This method tries placing a number in the given index and returns false if it is unsuccessful.
    public static boolean placeNumber(int[][] board, int i, int j) {
        if (board[i][j] >= 9) {
            board[i][j] = 0;
            return false;
        }
        board[i][j]++;
        while (checkBoardError(board, i, j) && board[i][j] <= 9) {
            if (board[i][j] != 9) {
                board[i][j]++;
            } else {
                board[i][j] = 0;
                return false;
            }
        }
        return true;
    }



    //This method solves the given sudoku puzzle using a brute force approach.
    public static int[][] solveBoard(int[][] board, int i, int j) {
        if (j == 9) {
            j = 0;
            i++;
        }

        if (board[i][j] == 0) {
            if (!placeNumber(board, i, j)) {
                return board;
            } else {
                if (boardComplete(board)) {
                    return board;
                } else {
                    int[][] currentBoard = solveBoard(board, i, j + 1);
                    while (!boardComplete(currentBoard)) {
                        if (!placeNumber(board, i, j)) {
                            return board;
                        } else {
                            currentBoard = solveBoard(board, i, j + 1);
                        }
                    }
                    return currentBoard;
                }
            }
        } else {
            return solveBoard(board, i, j + 1);
        }
    }



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




    public static int[][] generateRandomSolvedBoard(int[][] emptyBoard) {
        int length = emptyBoard.length;
        ArrayList<Integer> topRow = new ArrayList<>();
        topRow.add(1);
        topRow.add(2);
        topRow.add(3);
        topRow.add(4);
        topRow.add(5);
        topRow.add(6);
        topRow.add(7);
        topRow.add(8);
        topRow.add(9);

        for (int i = 0; i < emptyBoard.length; i++) {
            Random random = new Random();
            int randomIndex = random.nextInt(length);
            emptyBoard[0][i] = topRow.get(randomIndex);
            topRow.remove(randomIndex);
            length--;
        }

        int[][] solvedBoard = solveBoard(emptyBoard, 1, 0);
        return solvedBoard;
    }



    public static int[] findCandidates(int[][] sudokuBoard, int i, int j) {
        int[] candidates = new int[9];
        int numCandidates = 0;

        for (int x = 0; x < 9; x++) {
            sudokuBoard[i][j] = x + 1;
            if (!checkBoardError(sudokuBoard, i, j)) {
                candidates[numCandidates] = x + 1;
                numCandidates++;
            }
        }
        sudokuBoard[i][j] = 0;

        int[] validCandidates = new int[numCandidates];
        System.arraycopy(candidates, 0, validCandidates, 0, numCandidates);
        return validCandidates;
    }

    //Counts how many times a given number shows up as a candidate in a given row.
    public static int countByRow(int[][] sudokuBoard, int i, int num) {
        int counter = 0;
        for (int j = 0; j < sudokuBoard.length; j++) {
            if (sudokuBoard[i][j] == 0) {
                int[] list = findCandidates(sudokuBoard, i, j);
                for (int candidate : list) {
                    if (candidate == num) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }


    public static int countByColumn(int[][] sudokuBoard, int j, int num) {
        int counter = 0;
        for (int i = 0; i < sudokuBoard.length; i++) {
            if (sudokuBoard[i][j] == 0) {
                int[] list = findCandidates(sudokuBoard, i, j);
                for (int candidate : list) {
                    if (candidate == num) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }


    public static int countByGrid(int[][] sudokuBoard, int i, int j, int num) {
        int heightStart, widthStart, counter = 0;

        if (i <= 2) {
            heightStart = 0;
        } else if (i <= 5) {
            heightStart = 3;
        } else {
            heightStart = 6;
        }

        if (j <= 2) {
            widthStart = 0;
        } else if (j <= 5) {
            widthStart = 3;
        } else {
            widthStart = 6;
        }

        for (int height = heightStart; height <= heightStart + 2; height++) {
            for (int width = widthStart; width <= widthStart + 2; width++) {
                if (sudokuBoard[height][width] == 0) {
                    int[] list = findCandidates(sudokuBoard, height, width);
                    for (int candidate : list) {
                        if (candidate == num) {
                            counter++;
                        }
                    }
                }
            }
        }
        return counter;
    }



    public static void nakedSingles(int[][] sudokuBoard) {
        boolean changeMade;
        int[] candidateList;
        int length = sudokuBoard.length;

        do {
            changeMade = false;

            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if (sudokuBoard[i][j] == 0) {
                        candidateList = findCandidates(sudokuBoard, i, j);
                        if (candidateList.length == 1) {
                            sudokuBoard[i][j] = candidateList[0];
                            changeMade = true;
                        }
                    }
                }
            }

        } while(changeMade);
    }



    public static void hiddenSingles(int[][] sudokuBoard) {
        boolean changeMade;
        int[] candidateList;
        int length = sudokuBoard.length;

        do {
            changeMade = false;


            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if (sudokuBoard[i][j] == 0) {
                        candidateList = findCandidates(sudokuBoard, i, j);
                        for (int index = 0; index < candidateList.length; index++) {
                            int candidateRowCounter = countByRow(sudokuBoard, i, candidateList[index]);
                            int candidateColumnCounter = countByColumn(sudokuBoard, j, candidateList[index]);
                            int candidateGridCounter = countByGrid(sudokuBoard, i, j, candidateList[index]);
                            if (candidateRowCounter == 1 || candidateColumnCounter == 1 || candidateGridCounter == 1) {
                                sudokuBoard[i][j] = candidateList[index];
                                changeMade = true;
                                break;
                            }
                        }
                    }
                }
            }

        } while(changeMade);
    }




    /*
    public static void hiddenNakedSingles(int[][] sudokuBoard) {
        boolean changeMade;
        int[] candidateList;
        int length = sudokuBoard.length;

        do {
            changeMade = false;

            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if (sudokuBoard[i][j] == 0) {
                        candidateList = findCandidates(sudokuBoard, i, j);
                        if (candidateList.length == 1) {
                            sudokuBoard[i][j] = candidateList[0];
                            changeMade = true;
                        } else {
                            for (int index = 0; index < candidateList.length; index++) {
                                int candidateRowCounter = countByRow(sudokuBoard, i, candidateList[index]);
                                int candidateColumnCounter = countByColumn(sudokuBoard, j, candidateList[index]);
                                int candidateGridCounter = countByGrid(sudokuBoard, i, j, candidateList[index]);
                                if (candidateRowCounter == 1 || candidateColumnCounter == 1 || candidateGridCounter == 1) {
                                    sudokuBoard[i][j] = candidateList[index];
                                    changeMade = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

        } while(changeMade);
    }
*/

}
