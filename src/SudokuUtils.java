import java.util.ArrayList;
import java.util.Arrays;

public class SudokuUtils {

    // Identifies starting row/column of grid of given row
    public static int identifyGridCoordinate(int coordinate) {
        return coordinate / 3 * 3;
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
}
