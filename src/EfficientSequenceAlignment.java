public class EfficientSequenceAlignment extends SequenceAlignment  {
    public EfficientSequenceAlignment(String x, String y) {
        super(x, y);
    }

    public long computeOptimalAlignmentScore() {
        return computeOptimalAlignment(0, x.length(), 0, y.length());
    }

    private long computeOptimalAlignment(int x_start, int x_end,  int y_start, int y_end) {
        int x_length = x_end - x_start;
        int y_length = y_end - y_start;

        if (x_length <= 2 || y_length <= 2) {
            BasicSequenceAlignment bsa  = new BasicSequenceAlignment(
                    x.substring(x_start, x_end),
                    y.substring(y_start, y_end));
            long score = bsa.computeOptimalAlignmentScore();
            this.aligned_x.append(bsa.getAligned_x());
            this.aligned_y.append(bsa.getAligned_y());
            return score;
        }

        int y_optimalPoint = y_start + findOptimalMidpoint(x.substring(x_start, x_end), y.substring(y_start, y_end));
        int x_mid = x_start + (x_length % 2 == 0 ? x_length/2 : x_length/2 + 1);

        long score = computeOptimalAlignment(x_start, x_mid, y_start, y_optimalPoint);
        score += computeOptimalAlignment(x_mid, x_end, y_optimalPoint, y_end);
        return score;
    }

    private int findOptimalMidpoint(String x, String y) {
        int x_length = x.length();
        int y_length = y.length();

        int x_mid = x_length % 2 == 0 ? x_length/2 : x_length/2 + 1;
        long[] scoresForX = computeAlignmentScores(x.substring(0, x_mid), y);
        long[] scoresForXr = computeAlignmentScores(
                new StringBuilder(x.substring(x_mid)).reverse().toString(),
                new StringBuilder(y).reverse().toString());
        x = null;
        y = null;

        int optimalPoint = 0;
        long minScore = Long.MAX_VALUE;
        for (int i = 0; i <= y_length; i++) {
            long score = scoresForX[i] + scoresForXr[y_length - i];
            if (minScore > score) {
                minScore = score;
                optimalPoint = i;
            }
        }
        return optimalPoint;
    }

    private long[] computeAlignmentScores(String x, String y) {
        int x_length = x.length();
        int y_length = y.length();

        long[] prev = new long[y_length + 1];
        for (int i = 0; i <= y_length; i++) {
            prev[i] = i * DELTA;
        }

        for (int i = 1; i <= x_length; i++) {
            long[] current = new long[y_length + 1];
            current[0] = i * DELTA;
            for (int j = 1; j <= y_length; j++) {
                int mismatchCost = getMismatchCost(x.charAt(i - 1), y.charAt(j - 1));
                current[j] = Math.min(mismatchCost + prev[j - 1],
                        Math.min(DELTA + prev[j], DELTA + current[j - 1]));
            }
            prev = current;
        }
        return prev;
    }

    private int getMismatchCost(char base1, char base2) {
        return ALPHA[BASE_TO_INDEX_MAP.get(base1)][BASE_TO_INDEX_MAP.get(base2)];
    }

}
