import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Paths;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final Runtime RUNTIME = Runtime.getRuntime();

    private static boolean runEfficiently;
    private static String inputFilePath;

    public static void main(String[] args) {
        parseCommandLineArguments(args);
        try {
            InputGenerator inputGenerator = new InputGenerator(inputFilePath);
            String x = inputGenerator.getX();
            String y = inputGenerator.getY();
            int problemSize = x.length() + y.length();
            LOGGER.log(Level.INFO, "Problem size: " + problemSize);

            FileWriter fileWriter = createOutputFile();
            runSequenceAlignmentAlgo(x, y, fileWriter);
            fileWriter.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while reading the input file", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while creating the output file " +
                    "or while writing to the output file.", e);
        }
    }

    private static void runSequenceAlignmentAlgo(String x, String y, FileWriter fileWriter)
            throws IOException {
        float beforeUsedMem = RUNTIME.totalMemory() -  RUNTIME.freeMemory();
        long startTime  = System.nanoTime();

        if (runEfficiently) LOGGER.log(Level.INFO, "Computing Sequence Alignment Efficiently");
        SequenceAlignment sa = runEfficiently ? new EfficientSequenceAlignment(x, y) :  new BasicSequenceAlignment(x, y);
        sa.runAlgorithm();

        long endTime = System.nanoTime();
        float afterUsedMem = RUNTIME.totalMemory() - RUNTIME.freeMemory();

        fileWriter.write( sa.getAlignment() + "\n");
        fileWriter.write( "Alignment score: " + sa.getAlignmentScore() + "\n");

        float totalTime = (float) ((endTime - startTime) / Math.pow(10, 9));
        fileWriter.write(totalTime + " seconds\n");

        float actualMemoryUsed = (afterUsedMem - beforeUsedMem) / 1024;
        fileWriter.write(actualMemoryUsed + " KB\n");
    }

    private static void parseCommandLineArguments(String[] args) {
        if (args.length == 2) {
            if (args[0].equals("-e")) {
                runEfficiently = true;
                inputFilePath = args[1];
            } else {
                LOGGER.log(Level.SEVERE, "Must provide argument \"-e\" to run the program memory efficiently.");
                System.exit(0);
            }
        } else if (args.length == 1) {
            inputFilePath = args[0];
        } else {
            LOGGER.log(Level.SEVERE, "Must provide correct arguments to run the program.");
            System.exit(0);
        }
    }

    private static FileWriter createOutputFile() throws IOException {
        String currentpath = System.getProperty("user.dir");
        String fileSeparator = System.getProperty("file.separator");
        String directoryPath = Paths.get(currentpath).getParent() + fileSeparator + "output";
        File directory = new File(directoryPath);
        if (! directory.exists()){
            directory.mkdir();
        }

        String filename = "output-" + new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(System.currentTimeMillis()) + ".txt";
        String filePath = directoryPath + fileSeparator + filename;

        File file = new File(filePath);
        file.createNewFile();
        LOGGER.log(Level.INFO, "Output file created: " + filePath);

        return new FileWriter(filePath);
    }
}
