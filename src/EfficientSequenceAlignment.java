//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class EfficientSequenceAlignment {
//
//
//    private static void getAlignmentForMemoryEfficientAlgo(ParseInputFromFile input) {
//        String inputA = input.getInputA();
//        String inputB = input.getInputB();
//        Map<Integer, Integer> alignment = new LinkedHashMap<>();
//        memoryEficientDnCAlignment(inputA, inputB, input, alignment);
//        System.out.println(alignment.toString());
//    }
//
//    private static void memoryEficientDnCAlignment(String inputA, String inputB, ParseInputFromFile input,
//                                                   Map<Integer, Integer> alignment) {
//        int sizeA = inputA.length();
//        int sizeB = inputB.length();
//        System.out.println("A--" + inputA);
//        System.out.println("B--" + inputB);
//
//        if (sizeA > 1 && sizeB > 1) {
//            if (sizeA <= 2 || sizeB <= 2) {
//                getAlignmentForBasicAlgo(inputA, inputB, input, alignment);
//            } else {
//                List<Integer> alignmentMapFirstHalf = getOptimalIndexForB(inputA.substring(0, sizeA / 2 + 1), inputB,
//                        input);
//                List<Integer> alignmentMapSecondHalf = getOptimalIndexForB(
//                        StringUtils.reverse(inputB.substring(0, sizeA / 2 + 1)), StringUtils.reverse(inputB), input);
//
//                List<Integer> finalalignmentMap = new ArrayList<Integer>();
//                for (int i = 0 ; i < alignmentMapFirstHalf.size();i++) {
//                    finalalignmentMap.add(alignmentMapFirstHalf.get(i)+alignmentMapSecondHalf.get(i));
//                }
//
//                int maxAlignmentVal = 0;
//                int maxAlignmentIndex = 0;
//                for (int i = 0; i < sizeB; i++) {
//                    int optVal = finalalignmentMap.get(i);
//                    if (optVal > maxAlignmentVal) {
//                        maxAlignmentVal = optVal;
//                        maxAlignmentIndex = i;
//                    }
//                }
//
//                memoryEficientDnCAlignment(inputA.substring(0, sizeA / 2), inputB.substring(0, maxAlignmentIndex), input,
//                        alignment);
//                alignment.put(sizeA / 2, maxAlignmentIndex);
//                memoryEficientDnCAlignment(inputA.substring(sizeA / 2 + 1, sizeA),
//                        inputB.substring(maxAlignmentIndex + 1, sizeB), input, alignment);
//                System.out.println(alignment.toString());
//            }
//        }
//
//    }
//
//    private static List<Integer> getOptimalIndexForB(String inputA, String inputB, ParseInputFromFile input) {
//        List<List<Integer>> optimizerMatrix = new ArrayList<>();
//        int sizeA = inputA.length();
//        int sizeB = inputB.length();
//
//        for (int i = 0; i < 2; i++) {
//            List<Integer> temp = getListOfNZeros(sizeB);
//            temp.set(0, i * input.getDelta());
//            optimizerMatrix.add(temp);
//        }
//
//        for (int i = 0; i < sizeB; i++) {
//            optimizerMatrix.get(0).set(i, i * input.getDelta());
//        }
//
//        for (int j = 1; j < sizeB; j++) {
//            for (int i = 1; i < sizeA; i++) {
//                int min = Math.min(
//                        input.getAlphaVals().get(String.valueOf(inputA.charAt(i))).get(String.valueOf(inputB.charAt(j)))
//                                + optimizerMatrix.get(1).get(j),
//                        Math.min(input.getDelta() + optimizerMatrix.get(0).get(j),
//                                input.getDelta() + optimizerMatrix.get(1).get(j - 1)));
//                optimizerMatrix.get(1).set(j, min);
//                optimizerMatrix.set(0, optimizerMatrix.get(1));
//            }
//        }
//
//        System.out.println("A--" + inputA);
//        System.out.println("B--" + inputB);
//
//        return optimizerMatrix.get(1);
//    }
//}
