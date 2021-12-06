import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        InputGenerator p = new InputGenerator(in.nextLine());
        String x = p.getX();
        String y = p.getY();
        System.out.println(x);
        System.out.println(y);


        Runtime rt = Runtime.getRuntime();
        long totalMemory = rt.totalMemory();

        long startTime  = System.nanoTime();
        SequenceAlignment sa = new SequenceAlignment(x, y);
        sa.constructAlignment();
        long endTime = System.nanoTime();
        long freeMemory = rt.freeMemory();

        long memoryUsed = totalMemory - freeMemory;
        long totalTime = (long) ((endTime - startTime) / Math.pow(10, 9));

        System.out.println(sa.getOptimalAlignmentCost());
        System.out.println(sa.getAlignmentForX());
        System.out.println(sa.getAlignmentForY());
        System.out.println(memoryUsed);
        System.out.println(totalTime);

    }

    public int getProblemSize() {
        return 0;
    }
}
