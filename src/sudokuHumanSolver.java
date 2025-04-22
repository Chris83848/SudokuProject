import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class sudokuHumanSolver {
    public static void main(String[] args) {
        int[][] testBoardMedium = {
                {0, 0, 6, 0, 5, 0, 0, 4, 0},
                {0, 0, 0, 0, 1, 0, 5, 0, 2},
                {0, 4, 0, 9, 0, 0, 1, 8, 0},
                {0, 0, 4, 5, 0, 0, 8, 0, 1},
                {0, 5, 0, 0, 0, 0, 7, 6, 0},
                {0, 7, 0, 0, 0, 0, 0, 0, 0},
                {9, 0, 0, 0, 6, 0, 0, 2, 0},
                {3, 8, 0, 7, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 9, 0, 0, 0},
        };
        int[][] testBoardHard = {
                {0, 0, 0, 0, 3, 0, 4, 0, 0},
                {0, 0, 0, 0, 4, 0, 0, 2, 0},
                {8, 6, 0, 7, 0, 0, 0, 0, 0},
                {0, 4, 0, 0, 8, 0, 7, 0, 0},
                {0, 1, 0, 0, 2, 5, 0, 0, 3},
                {0, 0, 2, 1, 0, 0, 9, 0, 0},
                {9, 7, 0, 8, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 5, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 6, 0, 0},
        };
        int[][] testBoardGPT = {
                {0, 2, 0, 0, 7, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 8, 4, 0},
                {0, 0, 0, 5, 0, 0, 1, 0, 0},
                {9, 0, 0, 0, 1, 0, 7, 6, 4},
                {5, 0, 0, 0, 6, 0, 0, 0, 0},
                {4, 0, 0, 0, 9, 0, 0, 3, 0},
                {0, 0, 7, 9, 0, 0, 0, 0, 0},
                {0, 3, 0, 4, 0, 0, 0, 5, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 8},
        };
        ArrayList<Integer>[][] testListMedium = new ArrayList[9][9];
        ArrayList<Integer>[][] testListHard = new ArrayList[9][9];
        ArrayList<Integer>[][] testListGPT = new ArrayList[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                testListMedium[i][j] = new ArrayList<>();
                testListHard[i][j] = new ArrayList<>();
                testListGPT[i][j] = new ArrayList<>();
            }
        }

        findCandidates(testBoardMedium, testListMedium);
        findCandidates(testBoardHard, testListHard);
        findCandidates(testBoardGPT, testListGPT);


        printBoard(testBoardGPT);
        for (int i = 0; i < testListGPT.length; i++) {
            for (int j = 0; j < testListGPT[i].length; j++) {
                System.out.print(testListGPT[i][j] + "\t"); // Print each ArrayList with a tab for spacing
            }
            System.out.println(); // Newline after each row
        }

        int[][] solvedBoard = solveSudokuBoardHumanly(testBoardGPT);

        System.out.println("\n\n\n");
        printBoard(solvedBoard);

    }


    // Returns whether given board is humanly solvable
    public static boolean boardHumanlySolvable(int[][] sudokuBoard) {
        return boardComplete(solveSudokuBoardHumanly(sudokuBoard));
    }


    // Humanly solves as much of the board as possible without guessing and returns it
    public static int[][] solveSudokuBoardHumanly(int[][] sudokuBoard) {
        int length = sudokuBoard.length;;
        int[][] currentBoard = new int[9][9];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                currentBoard[i][j] = sudokuBoard[i][j];
            }
        }

        ArrayList<Integer>[][] cellCandidates = new ArrayList[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cellCandidates[i][j] = new ArrayList<>();
            }
        }
        findCandidates(currentBoard, cellCandidates);


        boolean changeMade;

        do {
            changeMade = false;

            if (nakedSingles(currentBoard, cellCandidates)) {
                changeMade = true;
            }

            else if (hiddenSingles(currentBoard, cellCandidates)) {
                changeMade = true;
            }

            else if (pointingPairs(currentBoard, cellCandidates)) {
                changeMade = true;
            }

            else if (nakedPairs(currentBoard, cellCandidates)) {
                changeMade = true;
            }

            else if (nakedTriples(currentBoard, cellCandidates)) {
                changeMade = true;
            }

            else if (nakedQuads(currentBoard, cellCandidates)) {
                changeMade = true;
            }


        } while(changeMade);
        System.out.println();
        for (int i = 0; i < cellCandidates.length; i++) {
            for (int j = 0; j < cellCandidates[i].length; j++) {
                System.out.print(cellCandidates[i][j] + "\t"); // Print each ArrayList with a tab for spacing
            }
            System.out.println(); // Newline after each row
        }

        return currentBoard;
    }


    // Finds the candidate lists of each empty cell in the sudoku Board and puts them in their own data structure
    public static void findCandidates(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        for (int i = 0; i < sudokuBoard.length; i++) {
            for (int j = 0; j < sudokuBoard.length; j++) {
                if (sudokuBoard[i][j] == 0) {
                    for (int candidate = 1; candidate <= 9; candidate++) {
                        sudokuBoard[i][j] = candidate;
                        if (!checkBoardError(sudokuBoard, i, j)) {
                            candidates[i][j].add(candidate);
                        }
                    }
                    sudokuBoard[i][j] = 0;
                }
            }
        }
    }


    // Checks each candidate list, and if any list has a length of one, then that candidate is placed in
    // that empty cell
    public static boolean nakedSingles(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        boolean changeMade = false;
        int length = sudokuBoard.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0 && candidates[i][j].size() == 1) {
                    sudokuBoard[i][j] = candidates[i][j].get(0);
                    updateCandidates(sudokuBoard, candidates, i, j);
                    changeMade = true;
                }
            }
        }
        return changeMade;
    }


    // Checks each candidate list in each empty cell, and if that is the only cell one candidate is included
    // in its row, column, or grid, then that candidate is placed in that cell
    public static boolean hiddenSingles(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        boolean changeMade = false;
        int length = sudokuBoard.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0) {
                    for (int index = 0; index < candidates[i][j].size(); index++) {
                        if (hiddenSingleFound(sudokuBoard, candidates, i, j, candidates[i][j].get(index))) {
                            sudokuBoard[i][j] = candidates[i][j].get(index);
                            updateCandidates(sudokuBoard, candidates, i, j);
                            changeMade = true;
                        }
                    }
                }
            }
        }
        return changeMade;
    }


    // Checks if a candidate appears only once in the given row, column, or grid
    public static boolean hiddenSingleFound(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j, int num) {
        if (countByGrid(sudokuBoard, candidates, i, j, num) == 1 || countByRow(sudokuBoard, candidates, i, num) == 1 || countByColumn(sudokuBoard, candidates, j, num) == 1) {
            return true;
        }
        return false;
    }


    // Counts how many times a candidate appears in a given grid
    public static int countByGrid(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j, int num) {
        int rowStart, columnStart, counter = 0;

        rowStart = identifyGridCoordinate(i);
        columnStart = identifyGridCoordinate(j);

        for (int row = rowStart; row <= rowStart + 2; row++) {
            for (int column = columnStart; column <= columnStart + 2; column++) {
                if (sudokuBoard[row][column] == 0 && candidates[row][column].contains(num)) {
                    counter++;
                }
            }
        }
        return counter;
    }


    // Counts how many times a candidate appears in a given row
    public static int countByRow(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int num) {
        int counter = 0;
        for (int j = 0; j < sudokuBoard.length; j++) {
            if (sudokuBoard[i][j] == 0 && candidates[i][j].contains(num)) {
                counter++;
            }
        }
        return counter;
    }


    // Counts how many times a candidate appears in a given column
    public static int countByColumn(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int j, int num) {
        int counter = 0;
        for (int i = 0; i < sudokuBoard.length; i++) {
            if (sudokuBoard[i][j] == 0 && candidates[i][j].contains(num)) {
                counter++;
            }
        }
        return counter;
    }


    // Updates the candidates in each empty cell in the given grid, row, and column
    public static void updateCandidates(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j) {
        candidates[i][j].clear();
        updateRow(sudokuBoard, candidates, i);
        updateColumn(sudokuBoard, candidates, j);
        updateGrid(sudokuBoard, candidates, i, j);
    }


    // Updates the candidates in each empty cell in the given row
    public static void updateRow(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i) {
        int length = sudokuBoard.length;

        for (int j = 0; j < length; j++) {
            if (sudokuBoard[i][j] == 0) {
                for (int candidate: candidates[i][j]) {
                    sudokuBoard[i][j] = candidate;
                    if (checkBoardError(sudokuBoard, i, j)) {
                        candidates[i][j].remove(Integer.valueOf(candidate));
                        break;
                    }
                }
                sudokuBoard[i][j] = 0;
            }
        }
    }


    // Updates the candidates in each empty cell in the given column
    public static void updateColumn(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int j) {
        int length = sudokuBoard.length;

        for (int i = 0; i < length; i++) {
            if (sudokuBoard[i][j] == 0) {
                for (int candidate: candidates[i][j]) {
                    sudokuBoard[i][j] = candidate;
                    if (checkBoardError(sudokuBoard, i, j)) {
                        candidates[i][j].remove(Integer.valueOf(candidate));
                        break;
                    }
                }
                sudokuBoard[i][j] = 0;
            }
        }
    }


    // Updates the candidates in each empty cell in the given grid
    public static void updateGrid(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j) {
        int rowStart, columnStart;

        rowStart = identifyGridCoordinate(i);
        columnStart = identifyGridCoordinate(j);

        for (int row = rowStart; row <= rowStart + 2; row++) {
            for (int column = columnStart; column <= columnStart + 2; column++) {
                if (sudokuBoard[row][column] == 0) {
                    for (int candidate: candidates[row][column]) {
                        sudokuBoard[row][column] = candidate;
                        if (checkBoardError(sudokuBoard, row, column)) {
                            candidates[row][column].remove(Integer.valueOf(candidate));
                            break;
                        }
                    }
                    sudokuBoard[row][column] = 0;
                }
            }
        }
    }


    // Identifies candidates that go only go in a certain row/column of a grid, and removes those candidates
    // from the row/column outside the grid
    public static boolean pointingPairs(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        boolean changeMade = false;
        int rowStart = 0, columnStart = 0;
        ArrayList<Integer> possibleRow, possibleColumn;

        for (int grid = 1; grid <= 9; grid++) {
            for (int num = 1; num <= 9; num++) {
                possibleRow = findPossibleRow(sudokuBoard, candidates, rowStart, columnStart, num);
                possibleColumn = findPossibleColumn(sudokuBoard, candidates, rowStart, columnStart, num);

                if (!possibleRow.isEmpty() && possibleRow.size() <= 3 && allEqual(possibleRow)) {
                    if (removeFromRow(sudokuBoard, candidates, possibleRow.get(0), num, possibleColumn.get(0))) {
                        changeMade = true;
                    }
                }
                if (!possibleColumn.isEmpty() && possibleColumn.size() <= 3 && allEqual(possibleColumn)) {
                    if (removeFromColumn(sudokuBoard, candidates, possibleColumn.get(0), num, possibleRow.get(0))) {
                        changeMade = true;
                    }
                }
            }
            columnStart += 3;
            if (columnStart == 9) {
                columnStart = 0;
                rowStart += 3;
            }
        }
        return changeMade;
    }


    // Finds location of a certain candidate in a grid and returns those locations
    public static ArrayList<Integer> findPossibleRow(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int rowStart, int columnStart, int num) {
        ArrayList<Integer> possibleRows = new ArrayList<>();
        for (int row = rowStart; row <= rowStart + 2; row++) {
            for (int column = columnStart; column <= columnStart + 2; column++) {
                if (sudokuBoard[row][column] == 0 && candidates[row][column].contains(num)) {
                    possibleRows.add(row);
                }
            }
        }
        return possibleRows;
    }


    // Finds location of a certain candidate in a grid and returns those locations
    public static ArrayList<Integer> findPossibleColumn(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int rowStart, int columnStart, int num) {
        ArrayList<Integer> possibleColumns = new ArrayList<>();
        for (int row = rowStart; row <= rowStart + 2; row++) {
            for (int column = columnStart; column <= columnStart + 2; column++) {
                if (sudokuBoard[row][column] == 0 && candidates[row][column].contains(num)) {
                    possibleColumns.add(column);
                }
            }
        }
        return possibleColumns;
    }


    // Removes given candidate from given row outside given grid
    public static boolean removeFromRow(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int row, int candidate, int column) {
        boolean changeMade = false;
        int length = sudokuBoard.length;
        int gridStart = identifyGridCoordinate(column);
        int gridEnd = gridStart + 3;

        for (int j = 0; j < length; j++) {
            if (j >= gridStart && j <= gridEnd) {
                continue;
            }
            if (sudokuBoard[row][j] == 0 && candidates[row][j].contains(candidate)) {
                candidates[row][j].remove(Integer.valueOf(candidate));
                changeMade = true;
            }
        }
        return changeMade;
    }


    // Removes given candidate from given column outside of grid given
    public static boolean removeFromColumn(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int column, int candidate, int row) {
        boolean changeMade = false;
        int length = sudokuBoard.length;
        int gridStart = identifyGridCoordinate(row);
        int gridEnd = gridStart + 3;

        for (int i = 0; i < length; i++) {
            if (i >= gridStart && i <= gridEnd) {
                continue;
            }
            if (sudokuBoard[i][column] == 0 && candidates[i][column].contains(candidate)) {
                candidates[i][column].remove(Integer.valueOf(candidate));
                changeMade = true;
            }
        }
        return changeMade;
    }


    // Checks if all values in Array List are equal
    public static boolean allEqual(ArrayList<Integer> list) {
        int compare = list.get(0);
        for (int num : list) {
            if (num != compare) {
                return false;
            }
        }
        return true;
    }


    // Checks for naked pairs in each row, column, and grid and removes them from the rest of that unit
    // Returns boolean on if any changes were made to the candidate lists in the board
    public static boolean nakedPairs(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        boolean changeMade = false;
        if (nakedPairsRows(sudokuBoard, candidates)) {
            changeMade = true;
        }
        if (nakedPairsColumns(sudokuBoard, candidates)) {
            changeMade = true;
        }
        if (nakedPairsGrids(sudokuBoard, candidates)) {
            changeMade = true;
        }
        return changeMade;
    }


    // Checks for naked pairs in each row and removes those candidates from the rest of the row
    // Returns boolean on if any changes were made to the candidate lists of any row
    public static boolean nakedPairsRows(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;

        // Loop through each separate unit
        for (int i = 0; i < length; i++) {

            // Find indices of matching requirements in unit
            indices = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0 && candidates[i][j].size() == 2) {
                    indices.add(new int[]{i, j});
                }
            }

            // Acquire combination if there is one
            int[][] nakedPair = nakedDoublePair(indices);
            if (nakedPair == null) {
                continue;
            }

            // Remove combination from rest of unit
            int idx1 = nakedPair[0][1];
            int idx2 = nakedPair[1][1];

            for (int column = 0; column < length; column++) {
                if (column != idx1 && column != idx2 && sudokuBoard[i][column] == 0 && candidates[i][column].removeAll(candidates[i][idx1])) {
                    changeMade = true;
                }
            }
        }
        return changeMade;
    }


    // Checks for naked pairs in each column and removes those candidates from the rest of the column
    // Returns boolean on if any changes were made to the candidate lists of any column
    public static boolean nakedPairsColumns(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;

        // Loop through each separate unit
        for (int j = 0; j < length; j++) {

            // Find indices of matching requirements in unit
            indices = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                if (sudokuBoard[i][j] == 0 && candidates[i][j].size() == 2) {
                    indices.add(new int[]{i, j});
                }
            }

            // Acquire combination if there is one
            int[][] nakedPair = nakedDoublePair(indices);
            if (nakedPair == null) {
                continue;
            }

            // Remove combination from rest of unit
            int idx1 = nakedPair[0][0];
            int idx2 = nakedPair[1][0];

            for (int row = 0; row < length; row++) {
                if (row != idx1 && row != idx2 && sudokuBoard[row][j] == 0 &&
                        candidates[row][j].removeAll(candidates[row][idx1])) {
                    changeMade = true;
                }
            }
        }
        return changeMade;
    }


    // Checks for naked pairs in each grid and removes those candidates from the rest of the grid
    // Returns boolean on if any changes were made to the candidate lists of any grid
    public static boolean nakedPairsGrids(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int rowStart = 0, columnStart = 0;
        boolean changeMade = false;
        ArrayList<int[]> indices;

        // Loop through each separate unit
        for (int grid = 1; grid <= 9; grid++) {

            // Find indices of matching requirements in unit
            indices = new ArrayList<>();
            for (int row = rowStart; row <= rowStart + 2; row++) {
                for (int column = columnStart; column <= columnStart + 2; column++) {
                    if (sudokuBoard[row][column] == 0 && candidates[row][column].size() == 2) {
                        indices.add(new int[]{row, column});
                    }
                }
            }

            // Acquire combination if there is one
            int[][] nakedPair = nakedDoublePair(indices);
            if (nakedPair == null) {
                continue;
            }

            // Remove combination from rest of unit
            int row1 = nakedPair[0][0];
            int column1 = nakedPair[0][1];
            int row2 = nakedPair[1][0];
            int column2 = nakedPair[1][1];

            for (int row = rowStart; row <= rowStart + 2; row++) {
                for (int column = columnStart; column <= columnStart + 2; column++) {
                    if ((row != row1 || column != column1) && (row != row2 || column != column2) &&
                            sudokuBoard[row][column] == 0 &&
                            candidates[row][column].removeAll(candidates[row1][column1])) {
                        changeMade = true;
                    }
                }
            }

            // Increments to next grid
            columnStart += 3;
            if (columnStart == 9) {
                columnStart = 0;
                rowStart += 3;
            }
        }
        return changeMade;
    }


    // Loops through index list to find if any two indices match
    // Returns matching indices in 2D array or returns null if no matching indices found
    public static int[][] nakedDoublePair(ArrayList<int[]> indices) {
        for (int i = 0; i < indices.size() - 1; i++) {
            for (int j = i + 1; j < indices.size(); j++) {
                if (indices.get(i).equals(indices.get(j))) {
                    return new int[][]{indices.get(i), indices.get(j)};
                }
            }
        }
        return null;
    }


    // Checks for naked triples in each row, column, and grid and removes them from the rest of that unit
    // Returns boolean on if any changes were made to the candidate lists in the board
    public static boolean nakedTriples(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        boolean changeMade = false;
        if (nakedTriplesRows(sudokuBoard, candidates)) {
            changeMade = true;
        }
        if (nakedTriplesColumns(sudokuBoard, candidates)) {
            changeMade = true;
        }
        if (nakedTriplesGrids(sudokuBoard, candidates)) {
            changeMade = true;
        }
        return changeMade;
    }


    // Checks for naked triples in each row and removes those candidates from the rest of the row
    // Returns boolean on if any changes were made to the candidate lists of any row
    public static boolean nakedTriplesRows(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;

        // Loop through each row
        for (int i = 0; i < length; i++) {

            // Collect indices of each candidate list of size two or three
            indices = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0 && (candidates[i][j].size() == 2 || candidates[i][j].size() == 3)) {
                    indices.add(new int[]{i, j});
                }
            }
            if (indices.size() < 3) {
                continue;
            }

            // Find a naked triple pair if one exists
            int[][] nakedTriple = nakedTriplePair(indices, candidates);
            if (nakedTriple == null) {
                continue;
            }
            ArrayList<Integer> union = Arrays.stream(nakedTriple[0])
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));


            // Remove numbers from union from rest of row
            int[] columns = new int[3];
            columns[0] = nakedTriple[1][nakedTriple[1].length - 1];
            columns[1] = nakedTriple[2][nakedTriple[2].length - 1];
            columns[2] = nakedTriple[3][nakedTriple[3].length - 1];

            for (int column = 0; column < length; column++) {
                if (sudokuBoard[i][column] == 0 && !arrayContainsNum(columns, column)) {
                    if (candidates[i][column].removeAll(union)) {
                        changeMade = true;
                    }
                }
            }
        }
        return changeMade;
    }


    // Checks for naked triples in each column and removes those candidates from the rest of the column
    // Returns boolean on if any changes were made to the candidate lists of any column
    public static boolean nakedTriplesColumns(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;

        // Loop through each column
        for (int j = 0; j < length; j++) {

            // Collect indices of each candidate list of size two or three
            indices = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                if (sudokuBoard[i][j] == 0 && (candidates[i][j].size() == 2 || candidates[i][j].size() == 3)) {
                    indices.add(new int[]{i, j});
                }
            }
            if (indices.size() < 3) {
                continue;
            }

            // Find a naked triple pair if one exists
            int[][] nakedTriple = nakedTriplePair(indices, candidates);
            if (nakedTriple == null) {
                continue;
            }
            ArrayList<Integer> union = Arrays.stream(nakedTriple[0])
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));


            // Remove numbers from union from rest of column
            int[] rows = new int[3];
            rows[0] = nakedTriple[1][0];
            rows[1] = nakedTriple[2][0];
            rows[2] = nakedTriple[3][0];

            for (int row = 0; row < length; row++) {
                if (sudokuBoard[row][j] == 0 && !arrayContainsNum(rows, row)) {
                    if (candidates[row][j].removeAll(union)) {
                        changeMade = true;
                    }
                }
            }
        }
        return changeMade;
    }

    // Checks for naked triples in each grid and removes those candidates from the rest of the grid
    // Returns boolean on if any changes were made to the candidate lists of any grid
    public static boolean nakedTriplesGrids(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;
        int rowStart = 0, columnStart = 0;

        // Loop through each grid
        for (int grid = 1; grid <= length; grid++) {

            // Collect indices of each candidate list of size two or three
            indices = new ArrayList<>();
            for (int row = rowStart; row <= rowStart + 2; row++) {
                for (int column = columnStart; column <= columnStart + 2; column++) {
                    if (sudokuBoard[row][column] == 0 && (candidates[row][column].size() == 2 || candidates[row][column].size() == 3)) {
                        indices.add(new int[]{row, column});
                    }
                }
            }
            if (indices.size() < 3) {
                continue;
            }

            // Find a naked triple pair if one exists
            int[][] nakedTriple = nakedTriplePair(indices, candidates);
            if (nakedTriple == null) {
                continue;
            }
            ArrayList<Integer> union = Arrays.stream(nakedTriple[0])
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));


            // Remove numbers from union from rest of grid
            ArrayList<int[]> nakedTripleIndices = new ArrayList<>();
            nakedTripleIndices.add(nakedTriple[1]);
            nakedTripleIndices.add(nakedTriple[2]);
            nakedTripleIndices.add(nakedTriple[3]);

            for (int row = rowStart; row <= rowStart + 2; row++) {
                for (int column = columnStart; column <= columnStart + 2; column++) {
                    int[] index = new int[]{row, column};
                    if (sudokuBoard[row][column] == 0 && !arrayListContainsArray(nakedTripleIndices, index)) {
                        if (candidates[row][column].removeAll(union)) {
                            changeMade = true;
                        }
                    }
                }
            }

            // Increments to next grid
            columnStart += 3;
            if (columnStart == 9) {
                columnStart = 0;
                rowStart += 3;
            }
        }
        return changeMade;
    }


    // Loops through index list to find if any three indices form naked triple
    // Returns matching union and indices in 2D array or returns null if no naked triple found
    public static int[][] nakedTriplePair(ArrayList<int[]> indices, ArrayList<Integer>[][] candidates) {

        // Loops through index list to find three given indices at a time
        for (int index1 = 0; index1 < indices.size() - 2; index1++) {
            int[] firstIndex = indices.get(index1);
            for (int index2 = index1 + 1; index2 < indices.size() - 1; index2++) {
                int[] secondIndex = indices.get(index2);
                for (int index3 = index2 + 1; index3 < indices.size(); index3++) {
                    int[] thirdIndex = indices.get(index3);

                    // Finds union of indices
                    int[] union = findUnion(
                            candidates[firstIndex[0]][firstIndex[1]],
                            candidates[secondIndex[0]][secondIndex[1]],
                            candidates[thirdIndex[0]][thirdIndex[1]]
                    );

                    // If length of union is 3, naked triple found
                    if (union.length == 3) {
                        return new int[][]{union, firstIndex, secondIndex, thirdIndex};
                    }
                }
            }
        }
        return null;
    }


    // Returns the union of the given Array Lists
    public static int[] findUnion(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3) {
        ArrayList<Integer> candidates = new ArrayList<>();

        addUniqueCandidates(candidates, list1);
        addUniqueCandidates(candidates, list2);
        addUniqueCandidates(candidates, list3);

        int[] union = new int[candidates.size()];
        for (int i = 0; i < union.length; i++) {
            union[i] = candidates.get(i);
        }
        return union;
    }


    // Checks for naked quads in each row, column, and grid and removes them from the rest of that unit
    // Returns boolean on if any changes were made to the candidate lists in the board
    public static boolean nakedQuads(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        boolean changeMade = false;
        if (nakedQuadsRows(sudokuBoard, candidates)) {
            changeMade = true;
        }
        if (nakedQuadsColumns(sudokuBoard, candidates)) {
            changeMade = true;
        }
        if (nakedQuadsGrids(sudokuBoard, candidates)) {
            changeMade = true;
        }
        return changeMade;
    }


    // Checks for naked quads in each row and removes those candidates from the rest of the row
    // Returns boolean on if any changes were made to the candidate lists of any row
    public static boolean nakedQuadsRows(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;

        // Loop through each row
        for (int i = 0; i < length; i++) {

            // Collect indices of each candidate list of size 2-4
            indices = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0 && (candidates[i][j].size() >= 2 && candidates[i][j].size() <= 4)) {
                    indices.add(new int[]{i, j});
                }
            }
            if (indices.size() < 4) {
                continue;
            }

            // Find a naked quad pair if one exists
            int[][] nakedQuad = nakedQuadPair(indices, candidates);
            if (nakedQuad == null) {
                continue;
            }
            ArrayList<Integer> union = Arrays.stream(nakedQuad[0])
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));


            // Remove numbers from union from rest of row
            int[] columns = new int[4];
            columns[0] = nakedQuad[1][nakedQuad[1].length - 1];
            columns[1] = nakedQuad[2][nakedQuad[2].length - 1];
            columns[2] = nakedQuad[3][nakedQuad[3].length - 1];
            columns[3] = nakedQuad[4][nakedQuad[4].length - 1];

            for (int column = 0; column < length; column++) {
                if (sudokuBoard[i][column] == 0 && !arrayContainsNum(columns, column)) {
                    if (candidates[i][column].removeAll(union)) {
                        changeMade = true;
                    }
                }
            }
        }
        return changeMade;
    }


    // Checks for naked quads in each column and removes those candidates from the rest of the column
    // Returns boolean on if any changes were made to the candidate lists of any column
    public static boolean nakedQuadsColumns(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;

        // Loop through each column
        for (int j = 0; j < length; j++) {

            // Collect indices of each candidate list of size 2-4
            indices = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                if (sudokuBoard[i][j] == 0 && (candidates[i][j].size() >= 2 && candidates[i][j].size() <= 4)) {
                    indices.add(new int[]{i, j});
                }
            }
            if (indices.size() < 4) {
                continue;
            }

            // Find a naked quad pair if one exists
            int[][] nakedQuad = nakedQuadPair(indices, candidates);
            if (nakedQuad == null) {
                continue;
            }
            ArrayList<Integer> union = Arrays.stream(nakedQuad[0])
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));


            // Remove numbers from union from rest of column
            int[] rows = new int[4];
            rows[0] = nakedQuad[1][0];
            rows[1] = nakedQuad[2][0];
            rows[2] = nakedQuad[3][0];
            rows[3] = nakedQuad[4][0];

            for (int row = 0; row < length; row++) {
                if (sudokuBoard[row][j] == 0 && !arrayContainsNum(rows, row)) {
                    if (candidates[row][j].removeAll(union)) {
                        changeMade = true;
                    }
                }
            }
        }
        return changeMade;
    }


    // Checks for naked quads in each grid and removes those candidates from the rest of the grid
    // Returns boolean on if any changes were made to the candidate lists of any grid
    public static boolean nakedQuadsGrids(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;
        int rowStart = 0, columnStart = 0;

        // Loop through each grid
        for (int grid = 1; grid <= length; grid++) {

            // Collect indices of each candidate list of size 2-4
            indices = new ArrayList<>();
            for (int row = rowStart; row <= rowStart + 2; row++) {
                for (int column = columnStart; column <= columnStart + 2; column++) {
                    if (sudokuBoard[row][column] == 0 && (candidates[row][column].size() >= 2 && candidates[row][column].size() <= 4)) {
                        indices.add(new int[]{row, column});
                    }
                }
            }
            if (indices.size() < 4) {
                continue;
            }

            // Find a naked quad pair if one exists
            int[][] nakedQuad = nakedQuadPair(indices, candidates);
            if (nakedQuad == null) {
                continue;
            }
            ArrayList<Integer> union = Arrays.stream(nakedQuad[0])
                    .boxed()
                    .collect(Collectors.toCollection(ArrayList::new));


            // Remove numbers from union from rest of grid
            ArrayList<int[]> nakedQuadIndices = new ArrayList<>();
            nakedQuadIndices.add(nakedQuad[1]);
            nakedQuadIndices.add(nakedQuad[2]);
            nakedQuadIndices.add(nakedQuad[3]);
            nakedQuadIndices.add(nakedQuad[4]);

            for (int row = rowStart; row <= rowStart + 2; row++) {
                for (int column = columnStart; column <= columnStart + 2; column++) {
                    int[] index = new int[]{row, column};
                    if (sudokuBoard[row][column] == 0 && !arrayListContainsArray(nakedQuadIndices, index)) {
                        if (candidates[row][column].removeAll(union)) {
                            changeMade = true;
                        }
                    }
                }
            }

            // Increments to next grid
            columnStart += 3;
            if (columnStart == 9) {
                columnStart = 0;
                rowStart += 3;
            }
        }
        return changeMade;
    }


    // Loops through index list to find if any four indices form naked quad
    // Returns matching union and indices in 2D array or returns null if no naked triple found
    public static int[][] nakedQuadPair(ArrayList<int[]> indices, ArrayList<Integer>[][] candidates) {

        // Loops through index list to find four given indices at a time
        for (int index1 = 0; index1 < indices.size() - 3; index1++) {
            int[] firstIndex = indices.get(index1);
            for (int index2 = index1 + 1; index2 < indices.size() - 2; index2++) {
                int[] secondIndex = indices.get(index2);
                for (int index3 = index2 + 1; index3 < indices.size() - 1; index3++) {
                    int[] thirdIndex = indices.get(index3);
                    for (int index4 = index3 + 1; index4 < indices.size(); index4++) {
                        int[] fourthIndex = indices.get(index4);

                        // Finds union of indices
                        int[] union = findUnion(
                                candidates[firstIndex[0]][firstIndex[1]],
                                candidates[secondIndex[0]][secondIndex[1]],
                                candidates[thirdIndex[0]][thirdIndex[1]],
                                candidates[fourthIndex[0]][fourthIndex[1]]
                        );

                        // If length of union is 4, naked quad found
                        if (union.length == 4) {
                            return new int[][]{union, firstIndex, secondIndex, thirdIndex, fourthIndex};
                        }
                    }
                }
            }
        }
        return null;
    }


    // Returns the union of the given Array Lists
    public static int[] findUnion(ArrayList<Integer> list1, ArrayList<Integer> list2, ArrayList<Integer> list3, ArrayList<Integer> list4) {
        ArrayList<Integer> candidates = new ArrayList<>();

        addUniqueCandidates(candidates, list1);
        addUniqueCandidates(candidates, list2);
        addUniqueCandidates(candidates, list3);
        addUniqueCandidates(candidates, list4);

        int[] union = new int[candidates.size()];
        for (int i = 0; i < union.length; i++) {
            union[i] = candidates.get(i);
        }
        return union;
    }


    // Adds unique candidates to union
    public static void addUniqueCandidates(ArrayList<Integer> union, ArrayList<Integer> candidateList) {
        for (int candidate : candidateList) {
            if (!union.contains(candidate)) {
                union.add(candidate);
            }
        }
    }


    // Returns whether given array contains given value
    public static boolean arrayContainsNum(int[] array, int x) {
        for (int num : array) {
            if (num == x) {
                return true;
            }
        }
        return false;
    }


    // Returns whether given arrayList contains given array
    public static boolean arrayListContainsArray(ArrayList<int[]> arrayList, int[] array) {
        for (int[] currentArray : arrayList) {
            if (Arrays.equals(currentArray, array)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isValid(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 0) {
                    if (checkBoardError(board, i, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    // Checks if given coordinates are valid in their row, column, and grid
    public static boolean checkBoardError(int[][] board, int i, int j) {
        if (checkColumnError(board, i, j) || checkRowError(board, i, j) || checkGridError(board, i, j)) {
            return true;
        } else {
            return false;
        }
    }


    // Checks if given coordinates are valid in that grid
    public static boolean checkGridError(int[][] board, int i, int j) {

        // Determines what grid coordinates are located in
        int rowStart = identifyGridCoordinate(i);
        int columnStart = identifyGridCoordinate(j);

        // Loops through grid and checks for errors within grid compared to given coordinates
        for (int row = rowStart; row <= rowStart + 2; row++) {
            for (int column = columnStart; column <= columnStart + 2; column++) {
                if ((row != i || column != j) && board[row][column] == board[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }


    // Checks if the given coordinates are valid in that column
    public static boolean checkColumnError(int[][] board, int i, int j) {

        //Loops through column and checks for errors within it compared to given coordinates
        for (int k = 0; k < board.length; k++) {
            if (k != i && board[k][j] == board[i][j]) {
                return true;
            }
        }
        return false;
    }


    // Checks if the given coordinates are valid in that row
    public static boolean checkRowError(int[][] board, int i, int j) {

        //Loops through row and checks for errors within it compared to given coordinates
        for (int k = 0; k < board[0].length; k++) {
            if (k != j && board[i][k] == board[i][j]) {
                return true;
            }
        }
        return false;
    }


    // Identifies starting row/column of grid of given row
    public static int identifyGridCoordinate(int coordinate) {
        return coordinate / 3 * 3;
    }


    // Returns whether the given 2D array is full of occupants
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


    // Prints out given sudoku board
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
