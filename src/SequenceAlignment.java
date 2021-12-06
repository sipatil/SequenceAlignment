import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SequenceAlignment {

    public static int DELTA;
    public static Map<Character, Integer> BASE_TO_INDEX_MAP = new HashMap<>();
    public static int[][] ALPHA;

    static {
        DELTA = 3;

        BASE_TO_INDEX_MAP.put('A', 0);
        BASE_TO_INDEX_MAP.put('C', 1);
        BASE_TO_INDEX_MAP.put('G', 2);
        BASE_TO_INDEX_MAP.put('T', 3);

        ALPHA = new int[][]{
                {0, 110, 48, 94},
                {110, 0, 118, 48},
                {48, 118, 0, 110},
                {94, 48, 110, 0}};
    }

    private String x;
    private String y;
    private String aligned_x;
    private String aligned_y;
    private int[][] optimizerMatrix;

    public SequenceAlignment(String x, String y) {
        this.x = x;
        this.y = y;
        this.optimizerMatrix = new int[x.length()+ 1][y.length()+ 1];
    }

    public String getAlignmentForX() {
        return this.aligned_x;
    }

    public String getAlignmentForY() {
        return this.aligned_y;
    }

    public int getOptimalAlignmentCost() {
        if (optimizerMatrix == null) return 0;
        return optimizerMatrix[optimizerMatrix.length - 1][optimizerMatrix[0].length - 1];
    }

    public void constructAlignment() {
        int x_length = x.length();
        int y_length = y.length();

        for (int i = 0; i <= x_length; i++) {
            optimizerMatrix[i][0] = i * DELTA;
        }
        for (int i = 0; i <= y_length; i++) {
            optimizerMatrix[0][i] = i * DELTA;
        }

        for (int i = 1; i <= x_length; i++) {
            for (int j = 1; j <= y_length; j++) {
                optimizerMatrix[i][j] = Math.min(getMismatchCost(i,j) + optimizerMatrix[i - 1][j - 1],
                        Math.min(DELTA + optimizerMatrix[i - 1][j], DELTA + optimizerMatrix[i][j - 1]));
            }
        }
        reconstructSequences(optimizerMatrix);
    }

    private int getMismatchCost(int index_x, int index_y) {
        char base1 = x.charAt(index_x - 1);
        char base2 = y.charAt(index_y - 1);
        return ALPHA[BASE_TO_INDEX_MAP.get(base1)][BASE_TO_INDEX_MAP.get(base2)];
    }

    private void reconstructSequences(int[][] optimizerMatrix) {
        int i = x.length();
        int j = y.length();

        StringBuilder aligned_x = new StringBuilder();
        StringBuilder aligned_y = new StringBuilder();

        while (i != 0 && j != 0) {
            if (optimizerMatrix[i][j] == optimizerMatrix[i - 1][j - 1] + getMismatchCost(i,j)) {
                aligned_x.append(x.charAt(--i));
                aligned_y.append(y.charAt(--j));
            }  else if (optimizerMatrix[i][j] == optimizerMatrix[i - 1][j] + DELTA) {
                aligned_x.append(x.charAt(--i));
                aligned_y.append("_");
            } else if (optimizerMatrix[i][j] == optimizerMatrix[i][j - 1] + DELTA) {
                aligned_x.append("_");
                aligned_y.append(y.charAt(--j));
            }
        }

        while (i > 0) {
            aligned_x.append(x.charAt(--i));
            aligned_y.append("_");
        }

        while (j > 0) {
            aligned_x.append("_");
            aligned_y.append(y.charAt(--j));
        }

        this.aligned_x = format(aligned_x.reverse());
        this.aligned_y = format(aligned_y.reverse());
    }

    private String format(StringBuilder sb) {
        if (sb.length() <= 50) return sb.toString();
        return sb.substring(0, 50) + " " + sb.substring(50);
    }

}
