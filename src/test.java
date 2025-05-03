import java.util.*;

public class test {

    public static final int GRID_SIZE = 9;

    public static int[][] createPuzzle(int[][] solvedGrid, String difficulty) {
        // Define clue counts for each difficulty level
        Map<String, Integer> difficultyLevels = new HashMap<>();
        difficultyLevels.put("easy", 36);   // 36-49 clues
        difficultyLevels.put("medium", 32); // 32-36 clues
        difficultyLevels.put("hard", 24);   // 24-32 clues

        int numClues = difficultyLevels.getOrDefault(difficulty, 36);

        // Copy the solved grid to avoid modifying the original
        int[][] puzzle = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(solvedGrid[i], 0, puzzle[i], 0, GRID_SIZE);
        }

        // Generate cell pairs for symmetry
        List<int[]> cellPairs = new ArrayList<>();
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c <= r; c++) { // Only iterate half for symmetry
                cellPairs.add(new int[]{r, c});
            }
        }
        Collections.shuffle(cellPairs); // Randomize the cell order

        int cluesRemoved = GRID_SIZE * GRID_SIZE;
        for (int[] pair : cellPairs) {
            if (cluesRemoved <= numClues) break;

            int r = pair[0];
            int c = pair[1];
            int symR = GRID_SIZE - 1 - r;
            int symC = GRID_SIZE - 1 - c;

            // Backup original values
            int temp1 = puzzle[r][c];
            int temp2 = puzzle[symR][symC];

            // Remove the numbers
            puzzle[r][c] = 0;
            puzzle[symR][symC] = 0;

            // Check for uniqueness
            if (!hasUniqueSolution(puzzle)) {
                // Restore if puzzle no longer has a unique solution
                puzzle[r][c] = temp1;
                puzzle[symR][symC] = temp2;
            } else {
                cluesRemoved -= (r == symR && c == symC) ? 1 : 2;
            }
        }

        return puzzle;
    }

    public static boolean hasUniqueSolution(int[][] puzzle) {
        // Implement your Sudoku solver here.
        // Return true if the puzzle has exactly one solution, otherwise false.
        // Example placeholder:
        return true; // Replace with actual solving logic
    }

    public static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print((cell == 0 ? "." : cell) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] solvedGrid = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };


        int[][] board = SudokuBoardGenerator.generateMediumBoardPuzzle();
        SudokuUtils.printBoard(board);
        SudokuUtils.printBoard(SudokuSolverApplication.solveRecursively(board));

        /*int[][] easyPuzzle = createPuzzle(solvedGrid, "easy");
        System.out.println("Easy Puzzle:");
        printGrid(easyPuzzle);

        int[][] mediumPuzzle = createPuzzle(solvedGrid, "medium");
        System.out.println("\nMedium Puzzle:");
        printGrid(mediumPuzzle);

        int[][] hardPuzzle = createPuzzle(solvedGrid, "hard");
        System.out.println("\nHard Puzzle:");
        printGrid(hardPuzzle);

         */
    }
}

