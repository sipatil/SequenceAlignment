public class BasicSequenceAlignment extends SequenceAlignment {
    private long[][] optimizerMatrix;

    public BasicSequenceAlignment(String x, String y) {
        super(x, y);
        this.optimizerMatrix = new long[x.length()+ 1][y.length()+ 1];
    }

    public long computeOptimalAlignmentScore() {
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
                optimizerMatrix[i][j] = Math.min(getMismatchCost(i - 1, j - 1) + optimizerMatrix[i - 1][j - 1],
                        Math.min(DELTA + optimizerMatrix[i - 1][j], DELTA + optimizerMatrix[i][j - 1]));
            }
        }

        constructOptimalAlignment();

        return optimizerMatrix[optimizerMatrix.length - 1][optimizerMatrix[0].length - 1];
    }

    private void constructOptimalAlignment() {
        int i = x.length();
        int j = y.length();

        while (i != 0 && j != 0) {
            if (optimizerMatrix[i][j] == optimizerMatrix[i - 1][j - 1] + getMismatchCost(i - 1, j - 1)) {
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

        aligned_x.reverse();
        aligned_y.reverse();
    }

    private int getMismatchCost(int index_x, int index_y) {
        char base1 = x.charAt(index_x);
        char base2 = y.charAt(index_y);
        return ALPHA[BASE_TO_INDEX_MAP.get(base1)][BASE_TO_INDEX_MAP.get(base2)];
    }

}
