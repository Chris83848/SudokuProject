import java.util.*;

public class solveBoard {
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
        printBoard(sudokuBoard);
        int[][] newBoard = solveSudokuBoard(sudokuBoard);
        printBoard(newBoard);

    }


    public static int[][] solveSudokuBoard(int[][] sudokuBoard) {
        int length = sudokuBoard.length;
        ;
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
            /*
            else if (hiddenSingles(currentBoard, cellCandidates)) {
                changeMade = true;
            }
            */


        } while (changeMade);


        return currentBoard;
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

    public static boolean hiddenSingles(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        int length = sudokuBoard.length;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (sudokuBoard[i][j] == 0) {
                    for (int index = 0; index < candidates[i][j].size(); index++) {
                        int candidateRowCounter = countByRow(sudokuBoard, candidates, i, candidates[i][j].get(index));
                        if (candidateRowCounter == 1) {
                            sudokuBoard[i][j] = candidates[i][j].get(index);
                            return true;
                        }
                        int candidateColumnCounter = countByColumn(sudokuBoard, candidates, j, candidates[i][j].get(index));
                        if (candidateColumnCounter == 1) {
                            sudokuBoard[i][j] = candidates[i][j].get(index);
                            return true;
                        }
                        int candidateGridCounter = countByGrid(sudokuBoard, candidates, i, j, candidates[i][j].get(index));
                        if (candidateGridCounter == 1) {
                            sudokuBoard[i][j] = candidates[i][j].get(index);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static boolean checkBoardError(int[][] board, int i, int j) {
        if (checkColumnError(board, i, j) || checkRowError(board, i, j) || checkGridError(board, i, j)) {
            return true;
        } else {
            return false;
        }
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

    public static void updateCandidates(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i, int j) {
        candidates[i][j].clear();
        updateRow(sudokuBoard, candidates, i);
        updateColumn(sudokuBoard, candidates, j);
        updateGrid(sudokuBoard, candidates, i, j);
    }

    public static void updateRow(int[][] sudokuBoard, ArrayList<Integer>[][] candidates, int i) {
        int length = sudokuBoard.length;

        for (int j = 0; j < length; j++) {
            if (sudokuBoard[i][j] == 0) {
                for (int candidate : candidates[i][j]) {
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
                for (int candidate : candidates[i][j]) {
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
                    for (int candidate : candidates[height][width]) {
                        sudokuBoard[height][width] = candidate;
                        if (checkBoardError(sudokuBoard, height, width)) {
                            candidates[i][j].remove(Integer.valueOf(candidate));
                            break;
                        }
                    }
                    sudokuBoard[height][width] = 0;
                }
            }
        }
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

    public static int[] CandidatesInGrid() {
        int[] ignore = new int[7];
        return ignore;
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


    // If any candidate lists in a row are equal to each other and of length two, then a naked pair is
    // identified. The candidates from this naked pair are then removed from the rest of the row
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


    public static boolean nakedTriples(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
        boolean changeMade = false;
        if (nakedTriplesRows(sudokuBoard, candidates)) {
            changeMade = true;
        }
        if (nakedTriplesColumns()) {
            changeMade = true;
        }
        if (nakedTriplesGrids()) {
            changeMade = true;
        }
        return changeMade;
    }


    public static boolean nakedTriplesRows(int[][] sudokuBoard, ArrayList<Integer>[][] candidates) {
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

    public static boolean nakedTriplesColumns() {
        boolean changeMade = false;
        return changeMade;
    }

    public static boolean nakedTriplesGrids() {
        boolean changeMade = false;
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


    public static boolean removeFromRow() {
        return true;
    }

    public static boolean removeFromColumn() {
        return true;
    }

    public static boolean removeFromGrid() {
        return true;
    }

    public static int identifyGridRow(int i) {
        if (i <= 2) {
            return 0;
        } else if (i <= 5) {
            return 3;
        } else {
            return 6;
        }
    }

    public static int identifyGridColumn(int j) {
        if (j <= 2) {
            return 0;
        } else if (j <= 5) {
            return 3;
        } else {
            return 6;
        }
    }
}
