package seamcarving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findSeam(Picture picture, EnergyFunction f) {
        // TODO: Replace with your code
        List<Integer> result = new ArrayList<>();
        double[][] M = new double[picture.width()][picture.height()];
        for (int y = 0; y < picture.height(); y++) {
            M[0][y] = f.apply(picture, 0, y);
        }
        for (int x = 1; x < picture.width(); x++) {
            for (int y = picture.height() - 1; y >= 0; y--) {
                int maxY = Math.min(y + 1, picture.height() - 1);
                int minY = Math.max(y - 1, 0);
                double min = Double.MAX_VALUE;
                int parentY = 0;
                for (int yIndex = minY; yIndex <= maxY; yIndex++) {
                    double cur = M[x - 1][yIndex];
                    double test = Math.min(cur, min);
                    if (test == cur) {
                        min = cur;
                        parentY = yIndex;
                    }
                }
                M[x][y] = f.apply(picture, x, y) + M[x - 1][parentY];
            }
        }
        int target = findMinInColumn(M, picture.width() - 1, picture);
        for (int x = picture.width() - 1; x >= 0; x--) {
                int maxY = Math.min(target + 1, picture.height() - 1);
                int minY = Math.max(target - 1, 0);
                double min = Double.MAX_VALUE;
                int storeY = 0;
                for (int yIndex = minY; yIndex <= maxY; yIndex++) {
                    double cur = M[x][yIndex];
                    double test = Math.min(cur, min);
                    if (test == cur) {
                        min = cur;
                        storeY = yIndex;
                    }
                }
                result.add(storeY);
                target = storeY;
        }
        Collections.reverse(result);
        return result;
    }

    private int findMinInColumn(double[][] M, int corX, Picture picture) {
        double minValue = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int y = 0; y < picture.height(); y++) {
            double curMinValue = minValue;
            minValue = Math.min(minValue, M[corX][y]);
            if (curMinValue != minValue){
                minIndex = y;
            }
        }
        return minIndex;
    }
}