import java.util.*;
import java.util.stream.Collectors;

public class methodTester {
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
                {1, 2, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 0, 0, 0, 0, 6, 0},
                {0, 4, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 5, 0, 0, 0, 0, 0, 0},
                {0, 0, 6, 0, 0, 0, 0, 0, 0},
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

        System.out.println(nakedQuads(testBoardGPT, testListGPT));

        System.out.println("\n\n\n");
        printBoard(testBoardGPT);
        for (int i = 0; i < testListGPT.length; i++) {
            for (int j = 0; j < testListGPT[i].length; j++) {
                System.out.print(testListGPT[i][j] + "\t"); // Print each ArrayList with a tab for spacing
            }
            System.out.println(); // Newline after each row
        }
    }

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





        } while(changeMade);
        System.out.println();
        for (int i = 0; i < cellCandidates.length; i++) {
            for (int j = 0; j < cellCandidates[i].length; j++) {
                System.out.print(cellCandidates[i][j] + "\t"); // Print each ArrayList with a tab for spacing
            }
            System.out.println(); // Newline after each row
        }

        if (boardComplete(currentBoard)) {
            return currentBoard;
        } else {
            System.out.println("Board not solvable without guessing.");
            return currentBoard;
        }
    }

    public static int identifyGridColumn(int column) {
        return column / 3 * 3;
    }

    public static int identifyGridRow(int row) {
        return row / 3 * 3;
    }


    public static boolean naakedTriplesRows(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<int[]> indices;
        ArrayList<int[][]> combinations;
        ArrayList<Integer> union, comboIndices;

        for (int i = 0; i < length; i++) {
            indices = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0 && (candidates[i][j].size() == 2 || candidates[i][j].size() == 3)) {
                    indices.add(new int[]{i, j});
                }
            }
            if (indices.size() >= 3) {
                combinations = tripleCombinations(indices);

                for (int[][] combination : combinations) {
                    union = new ArrayList<>();
                    comboIndices = new ArrayList<>();
                    for (int[] combo : combination) {
                        for (int candidate : candidates[combo[0]][combo[1]]) {
                            comboIndices.add(combo[1]);
                            if (!union.contains(candidate)) {
                                union.add(candidate);
                            }
                        }
                    }
                    if (union.size() == 3) {
                        for (int j = 0; j < length; j++) {
                            if (sudokuBoard[i][j] == 0 && !comboIndices.contains(j)) {
                                if (candidates[i][j].removeAll(union)) {
                                    changeMade = true;
                                }
                            }
                        }
                    }
                }

            }
        }
        return changeMade;
    }

    public static ArrayList<int[][]> tripleCombinations(ArrayList<int[]> indices) {
        ArrayList<int[][]> combos = new ArrayList<>();
        for (int i = 0; i < indices.size() - 2; i++) {
            for (int j = i + 1; j < indices.size() - 1; j++) {
                for (int k = j + 1; k < indices.size(); k++) {
                    int[][] currentCombo = new int[3][2];
                    currentCombo[0] = indices.get(i);
                    currentCombo[1] = indices.get(j);
                    currentCombo[2] = indices.get(k);
                    combos.add(currentCombo);
                }
            }
        }
        return combos;
    }

    public static ArrayList<int[][]> quadCombinations(ArrayList<int[]> indices) {
        ArrayList<int[][]> combos = new ArrayList<>();
        for (int i = 0; i < indices.size() - 3; i++) {
            for (int j = i + 1; j < indices.size() - 2; j++) {
                for (int k = j + 1; k < indices.size() - 1; k++) {
                    for (int n = k + 1; n < indices.size(); n++) {
                        int[][] currentCombo = new int[4][2];
                        currentCombo[0] = indices.get(i);
                        currentCombo[1] = indices.get(j);
                        currentCombo[2] = indices.get(k);
                        currentCombo[3] = indices.get(n);
                        combos.add(currentCombo);
                    }
                }
            }
        }
        return combos;
    }

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

    public static boolean nakedPairsGrids(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        boolean changeMade = false;
        ArrayList<int[]> indices;

        for (int heightStart = 0; heightStart < 3; heightStart++) {
            for (int widthStart = 0; widthStart < 3; widthStart++) {
                indices = new ArrayList<>();

                for (int height = heightStart * 3; height <= heightStart * 3 + 2; height++) {
                    for (int width = widthStart * 3; width <= widthStart * 3 + 2; width++) {
                        if (sudokuBoard[height][width] == 0 && candidates[height][width].size() == 2) {
                            indices.add(new int[]{height, width});
                        }
                    }
                }

                for (int x = 0; x < indices.size(); x++) {
                    for (int y = x + 1; y < indices.size(); y++) {
                        int[] idx1 = indices.get(x);
                        int[] idx2 = indices.get(y);
                        ArrayList<Integer> pair1 = candidates[idx1[0]][idx1[1]];
                        ArrayList<Integer> pair2 = candidates[idx2[0]][idx2[1]];

                        if (pair1.equals(pair2)) {
                            for (int height = heightStart * 3; height <= heightStart * 3 + 2; height++) {
                                for (int width = widthStart * 3; width <= widthStart * 3 + 2; width++) {
                                    if (sudokuBoard[height][width] == 0 && (height != idx1[0] || width != idx1[1]) && (height != idx2[0] || width != idx2[1])) {
                                        if (candidates[height][width].removeAll(pair1)) {
                                            changeMade = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return changeMade;
    }

    public static boolean nakedsPairsGrids(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;
        boolean changeMade = false;
        int heightStart = 0, widthStart = 0;
        ArrayList<ArrayList<Integer>> indices;

        while (heightStart < 9) {
            indices = new ArrayList<>();
            for (int height = heightStart; height <= heightStart + 2; height++) {
                for (int width = widthStart; width <= widthStart + 2; width++) {
                    if (sudokuBoard[height][width] == 0) {
                        if (candidates[height][width].size() == 2) {
                            indices.add(candidates[height][width]);
                        }
                    }
                }
            }
            for (int x = 0; x < indices.size(); x++) {
                for (int y = x + 1; y < indices.size(); y++) {
                    ArrayList<Integer> idx1 = indices.get(x);
                    ArrayList<Integer> idx2 = indices.get(y);
                    if (idx1.equals(idx2)) {
                        for (int height = heightStart; height <= heightStart + 2; height++) {
                            for (int width = widthStart; width <= widthStart + 2; width++) {
                                if (sudokuBoard[height][width] == 0 && candidates[height][width] != idx1 && candidates[height][width] != idx2) {
                                    if (candidates[height][width].removeAll(idx1)) {
                                        changeMade = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            widthStart += 3;
            if (widthStart == 9) {
                widthStart = 0;
                heightStart += 3;
            }
        }
        return changeMade;
    }

    public static boolean nakedPairsColumns(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int idx1, idx2, length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<Integer> indices, union;

        for (int i = 0; i < length; i++) {
            indices = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[j][i] == 0 && candidates[j][i].size() == 2) {
                    indices.add(j);
                }
            }

            for (int x = 0; x < indices.size(); x++) {
                for (int y = x + 1; y < indices.size(); y++) {
                    idx1 = indices.get(x);
                    idx2 = indices.get(y);

                    if (candidates[idx1][i].equals(candidates[idx2][i])) {
                        union = candidates[idx1][i];
                        for (int n = 0; n < length; n++) {
                            if (n != idx1 && n != idx2 && sudokuBoard[n][i] == 0 ) {
                                if (candidates[n][i].removeAll(union)) {
                                    changeMade = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return changeMade;
    }

    public static boolean nakedPairsRows(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int idx1, idx2, length = sudokuBoard.length;
        boolean changeMade = false;
        ArrayList<Integer> indices, union;

        for (int i = 0; i < length; i++) {
            indices = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0 && candidates[i][j].size() == 2) {
                    indices.add(j);
                }
            }

            for (int x = 0; x < indices.size(); x++) {
                for (int y = x + 1; y < indices.size(); y++) {
                    idx1 = indices.get(x);
                    idx2 = indices.get(y);

                    if (candidates[i][idx1].equals(candidates[i][idx2])) {
                        union = candidates[i][idx1];
                        for (int n = 0; n < length; n++) {
                            if (n != idx1 && n != idx2 && sudokuBoard[i][n] == 0 ) {
                                if (candidates[i][n].removeAll(union)) {
                                    changeMade = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return changeMade;
    }

    public static boolean pointingPairs(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int heightStart = 0, widthStart = 0;
        ArrayList<Integer> possibleRow;
        ArrayList<Integer> possibleColumn;
        for (int grid = 1; grid <= 9; grid++) {
            for (int num = 1; num <= 9; num++) {
                possibleRow = new ArrayList<>();
                possibleColumn = new ArrayList<>();
                for (int height = heightStart; height <= heightStart + 2; height++) {
                    for (int width = widthStart; width <= widthStart + 2; width++) {
                        if (sudokuBoard[height][width] == 0) {
                            if (candidates[height][width].contains(num)) {
                                possibleRow.add(height);
                                possibleColumn.add(width);
                            }
                        }
                    }
                }
                if (allEqual(possibleRow)) {
                    if (removeCandidatesFromRowOutsideGrid(sudokuBoard, candidates, possibleRow.get(0), num, possibleColumn.get(0))) {
                        return true;
                    }
                }
                if (allEqual(possibleColumn)) {
                    if (removeCandidatesFromColumnOutsideGrid(sudokuBoard, candidates, possibleColumn.get(0), num, possibleRow.get(0))) {
                        return true;
                    }
                }
            }
            widthStart += 3;
            if (widthStart == 9) {
                widthStart = 0;
                heightStart += 3;
            }
        }
        return false;
    }

    public static boolean removeCandidatesFromColumnOutsideGrid(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int column, int candidate, int grid) {
        boolean deletionMade = false;
        int length = sudokuBoard.length;
        int jumper = 0;
        if (grid >= 6) {
            jumper = 6;
        } else if (grid >= 3) {
            jumper = 3;
        }

        for (int i = 0; i < length; i++) {
            if (i == jumper) {
                i += 3;
                if (i == 9) {
                    break;
                }
            }
            if (sudokuBoard[i][column] == 0) {
                if (candidates[i][column].contains(candidate)) {
                    candidates[i][column].remove(Integer.valueOf(candidate));
                    deletionMade = true;
                }
            }
        }
        if (deletionMade) {
            return true;
        }
        return false;
    }

    public static boolean removeCandidatesFromRowOutsideGrid(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int row, int candidate, int grid) {
        boolean deletionMade = false;
        int length = sudokuBoard.length;
        int jumper = 0;
        if (grid >= 6) {
            jumper = 6;
        } else if (grid >= 3) {
            jumper = 3;
        }

        for (int j = 0; j < length; j++) {
            if (j == jumper) {
                j += 3;
                if (j == 9) {
                    break;
                }
            }
            if (sudokuBoard[row][j] == 0) {
                if (candidates[row][j].contains(candidate)) {
                    candidates[row][j].remove(Integer.valueOf(candidate));
                    deletionMade = true;
                }
            }
        }
        if (deletionMade) {
            return true;
        }
        return false;
    }

    public static boolean hiddenSingles(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0) {
                    for (int index = 0; index < candidates[i][j].size(); index++) {
                        if (countAll(sudokuBoard, candidates, i, j, candidates[i][j].get(index))) {
                            sudokuBoard[i][j] = candidates[i][j].get(index);
                            updateCandidates(sudokuBoard, candidates, i, j);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean countAll(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j, int num) {
        if (countByGrid(sudokuBoard, candidates, i, j, num) == 1 || countByRow(sudokuBoard, candidates, i, num) == 1 || countByColumn(sudokuBoard, candidates, j, num) == 1) {
            return true;
        }
        return false;
    }

    public static int countByColumn(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int j, int num) {
        int counter = 0;
        for (int i = 0; i < sudokuBoard.length; i++) {
            if (sudokuBoard[i][j] == 0) {
                if (candidates[i][j].contains(num)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int countByRow(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int num) {
        int counter = 0;
        for (int j = 0; j < sudokuBoard.length; j++) {
            if (sudokuBoard[i][j] == 0) {
                if (candidates[i][j].contains(num)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int countByGrid(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j, int num) {
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
                    if (candidates[height][width].contains(num)) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }



    public static boolean nakedSingles(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0) {
                    if (candidates[i][j].size() == 1) {
                        sudokuBoard[i][j] = candidates[i][j].get(0);
                        updateCandidates(sudokuBoard, candidates, i, j);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static void updateCandidates(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j) {
        candidates[i][j] = new ArrayList<>();
        updateRow(sudokuBoard, candidates, i);
        updateColumn(sudokuBoard, candidates, j);
        updateGrid(sudokuBoard, candidates, i, j);
    }

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

    public static void updateGrid(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j) {
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
                if (sudokuBoard[height][width] == 0) {
                    for (int candidate: candidates[height][width]) {
                        sudokuBoard[height][width] = candidate;
                        if (checkBoardError(sudokuBoard, height, width)) {
                            candidates[height][width].remove(Integer.valueOf(candidate));
                            break;
                        }
                    }
                    sudokuBoard[height][width] = 0;
                }
            }
        }
    }

    public static void findCandidates(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        for (int i = 0; i < sudokuBoard.length; i++) {
            for (int j = 0; j < sudokuBoard.length; j++) {
                if (sudokuBoard[i][j] == 0) {
                    for (int x = 0; x < 9; x++) {
                        sudokuBoard[i][j] = x + 1;
                        if (!checkBoardError(sudokuBoard, i, j)) {
                            candidates[i][j].add(x + 1);
                        }
                    }
                    sudokuBoard[i][j] = 0;
                }
            }
        }
    }

    public static boolean checkBoardError(int[][] board, int i, int j) {
        if (checkColumnError(board, i, j) || checkRowError(board, i, j) || checkGridError(board, i, j)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkGridError(int[][] board, int i, int j) {

        // Determines what grid coordinates are located in
        int rowStart = identifyGridRow(i);
        int columnStart = identifyGridColumn(j);

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


    public static boolean checkColumnError(int[][] board, int i, int j) {

        //Loops through column and checks for errors within it compared to given coordinates
        for (int k = 0; k < board.length; k++) {
            if (k != i && board[k][j] == board[i][j]) {
                return true;
            }
        }
        return false;
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

    public static boolean allEqual(ArrayList<Integer> list) {
        if (list.size() < 2 || list.size() > 3) {
            return false;
        }
        int compare = list.get(0);
        for (int num : list) {
            if (num != compare) {
                return false;
            }
        }
        return true;
    }

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

    public static void addUniqueCandidates(ArrayList<Integer> candidates, ArrayList<Integer> candidateList) {
        for (int candidate : candidateList) {
            if (!candidates.contains(candidate)) {
                candidates.add(candidate);
            }
        }
    }

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
                if (sudokuBoard[i][column] == 0 && !containsNum(columns, column)) {
                    if (candidates[i][column].removeAll(union)) {
                        changeMade = true;
                    }
                }
            }
        }
        return changeMade;
    }

    public static boolean containsNum(int[] array, int x) {
        for (int num : array) {
            if (num == x) {
                return true;
            }
        }
        return false;
    }


    public static int[][] nakedTriplePair(ArrayList<int[]> indices, ArrayList<Integer>[][] candidates) {
        for (int index1 = 0; index1 < indices.size() - 2; index1++) {
            int[] firstIndex = indices.get(index1);
            for (int index2 = index1 + 1; index2 < indices.size() - 1; index2++) {
                int[] secondIndex = indices.get(index2);
                for (int index3 = index2 + 1; index3 < indices.size(); index3++) {
                    int[] thirdIndex = indices.get(index3);

                    int[] union = findUnion(
                            candidates[firstIndex[0]][firstIndex[1]],
                            candidates[secondIndex[0]][secondIndex[1]],
                            candidates[thirdIndex[0]][thirdIndex[1]]
                    );
                    if (union.length == 3) {
                        return new int[][]{union, firstIndex, secondIndex, thirdIndex};
                    }
                }
            }
        }
        return null;
    }

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
                if (sudokuBoard[row][j] == 0 && !containsNum(rows, row)) {
                    if (candidates[row][j].removeAll(union)) {
                        changeMade = true;
                    }
                }
            }
        }
        return changeMade;
    }

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

            rowStart += 3;
            if (rowStart == 9) {
                rowStart = 0;
                columnStart += 3;
            }
        }
        return changeMade;
    }

    public static boolean arrayListContainsArray(ArrayList<int[]> arrayList, int[] array) {
        for (int[] currentArray : arrayList) {
            if (Arrays.equals(currentArray, array)) {
                return true;
            }
        }
        return false;
    }

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
            rowStart += 3;
            if (rowStart == 9) {
                rowStart = 0;
                columnStart += 3;
            }
        }
        return changeMade;
    }


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


    public static boolean arrayContainsNum(int[] array, int x) {
        for (int num : array) {
            if (num == x) {
                return true;
            }
        }
        return false;
    }

}
