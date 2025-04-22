import java.util.*;

public class testProgram {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int[][] sudokuBoard = new int[9][9];
        int[][] solvedSudokuBoard;
        int value;
        solvedSudokuBoard = generateRandomSolvedBoard(sudokuBoard, 0, 0);
        printBoard(solvedSudokuBoard);
        /*
        System.out.println("Enter the board top to bottom from left to right (Enter 0 for empty spaces): ");
        for (int i = 0; i < sudokuBoard.length; i++) {
            for (int j = 0; j < sudokuBoard.length; j++) {
                value = scan.nextInt();
                sudokuBoard[i][j] = value;
            }
        }
        printBoard(sudokuBoard);
        System.out.println("\n\n");

        solvedSudokuBoard = solve(sudokuBoard);
        if (!boardComplete(solvedSudokuBoard)) {
            System.out.println("Board not solvable.");
        } else {
            printBoard(solvedSudokuBoard);
        }
        */

    }
/*
    public static int[][] generateRandomSolvedBoard(int[][] emptyBoard, int i, int j) {
        if (j == 9) {
            j = 0;
            i++;
        }

        if (!placeRandomNumber(emptyBoard, i, j)) {
            return emptyBoard;
        } else {
            if (boardComplete(emptyBoard)) {
                return emptyBoard;
            } else {
                int[][] currentBoard = generateRandomSolvedBoard(emptyBoard, i, j + 1);
                while (!boardComplete(currentBoard)) {
                    if (!placeRandomNumber(currentBoard, i, j)) {
                        return emptyBoard;
                    } else {
                        currentBoard = generateRandomSolvedBoard(emptyBoard, i, j + 1);
                    }
                }
                return currentBoard;
            }
        }
    }

    public static boolean placeRandomNumber(int[][] board, int i, int j) {
        int counter = 0;
        board[i][j] = (int) (Math.random() * (8) + 1);
        while (checkBoardError(board, i, j)) {
            board[i][j] = (int) (Math.random() * (8) + 1);
            if (counter == 20) {
                board[i][j] = 0;
                return false;
            } else {
                counter++;
            }
        }
        return true;
    }
    */


    public static int[][] generateRandomSolvedBoard(int[][] emptyBoard, int i, int j) {
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

        for (int x = 0; x < length; x++) {
            Random random = new Random();
            int randomIndex = random.nextInt(9);
            emptyBoard[0][x] = topRow.get(randomIndex);
            topRow.remove(randomIndex);
        }

        int[][] b = solveBoard(emptyBoard, 1, 0);
        return b;
    }





    public static int[][] solveBoardFrom(int[][] board, int i, int j) {
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
                    int[][] currentBoard = solveBoardFrom(board, i, j + 1);
                    while (!boardComplete(currentBoard)) {
                        if (!placeNumber(board, i, j)) {
                            return board;
                        } else {
                            currentBoard = solveBoardFrom(board, i, j + 1);
                        }
                    }
                    return currentBoard;
                }
            }
        } else {
            return solveBoardFrom(board, i, j + 1);
        }
    }

    public static int[][] solve(int[][] board) {
        int[][] thisBoard = solveBoardFrom(board, 0, 0);
        if (!boardComplete(thisBoard)) {
            System.out.println("Board not solvable.");
            return null;
        } else {
            return thisBoard;
        }
    }

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

    public static boolean boardComplete(int[][] board) {
        for (int[] row : board) {
            for (int column : row) {
                if (column == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkBoardError(int[][] board, int i, int j) {
        if (checkColumnError(board, i, j) || checkRowError(board, i, j) || checkGridError(board, i, j)) {
            return true;
        } else {
            return false;
        }
    }

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
}

