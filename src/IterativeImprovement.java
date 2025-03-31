// Author: Elliot Silberwasser - M-INFOS Heuristic Optimization INFO-H413:
import java.util.*;
import java.io.*;

// Exercice 1.1:
public class IterativeImprovement {
    public static void main(String[] args) throws IOException {
        //if (args.length != 3) {
        //    System.out.println("Usage: java IterativeImrpovemnt <pivoting_rule> <neighborhood> <init_method>");
        //    System.out.println("Exemple: java Scheduler --first --transpose --srz");
        //    return;
        //}

        List<String> validPivotingRules = Arrays.asList("--first", "--best");
        List<String> validNeighborhoods = Arrays.asList("--exchange", "--transpose");
        List<String> validInitMethods = Arrays.asList("--random", "--srz");

        //String pivotingRule = args[0];
        //String neighborhood = args[1];
        //String initMethod = args[2];

        //if (!validPivotingRules.contains(pivotingRule)) {
        //    System.out.println("Erreur: Heuristic not valide. Options: --first, --best");
        //    return;
        //}
        //if (!validNeighborhoods.contains(neighborhood)) {
        //    System.out.println("Erreur: Pivoting Rule not valide. Options: --exchange, --transpose");
        //    return;
        //}
        //if (!validInitMethods.contains(initMethod)) {
        //    System.out.println("Erreur: Init Method not valid. Options: --random, --srz");
        //    return;
        //}

        int[][] processingTimeTest = {
                {3, 3, 4, 2, 3},
                {2, 1, 3, 3, 1},
                {4, 2, 1, 2, 3}
        };

        int[] permutationTest = {1, 3, 4 ,2};

        //int[][] processingTimes = readFile("src/Benchmarks/ta051");
        System.out.println(computeTotalCompletionTime(computeCompletionTimeMatrix(processingTimeTest, permutationTest)));
        int[] initialPermutation = initializePermutation(processingTimeTest, "--SRZ");
        System.out.println(Arrays.toString(initialPermutation));
        //int[] bestPermutation;
        //if (pivotingRule.equals("--first")) {
        //    bestPermutation = firstImprovement(processingTimes, initialPermutation, neighborhood);
        //}

        //else {
        //    bestPermutation = bestImprovement(processingTimes, initialPermutation, neighborhood);
        //}

        //System.out.println("Best permutation: " + Arrays.toString(bestPermutation));
        //System.out.println("Total completion time: " + computeCompletionTime(processingTimes, bestPermutation));
    }

    public static int[][] readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        String[] parts = line.trim().split("\\s+");

        int numJobs = Integer.parseInt(parts[0]);
        int numMachines = Integer.parseInt(parts[1]);
        int[][] processingTime = new int[numMachines][numJobs];

        for (int job = 0; job < numJobs; job++) {
            line = br.readLine();
            parts = line.trim().split("\\s+");  // put in an array the values of the line separates by a space.
            for (int k = 0; k < parts.length; k += 2) {
                int machine = Integer.parseInt(parts[k]) - 1;  // -1 for the index (machine 1 -> index 0 in the code).
                int processingtTime = Integer.parseInt(parts[k + 1]); // odd index = processingValue for the job.
                processingTime[machine][job] = processingtTime;
            }
        }

        br.close();
        return processingTime;
    }

    private static int[] initializePermutation(int[][] processingTimes, String initMethod) {
        int numJobs = processingTimes[0].length;
        int[] permutation = new int[numJobs];
        for (int i = 0; i < numJobs; i++) {
            permutation[i] = i;
        }

        switch(initMethod) {
            case "--random":
                getRandomPermutation(permutation);
                break;
            case "--SRZ":
                permutation = getSRZPermutation(processingTimes);
                break;
        }
        return permutation;
    }

    public static int[] getSRZPermutation(int[][]processingTimes){
        int numJobs = processingTimes[0].length;
        int numMachines = processingTimes.length;

        int[] T_i = computeTiMatrix(processingTimes);

        Integer[] starting_seq = new Integer[T_i.length];
        for (int i = 0; i < T_i.length; i++) {
            starting_seq[i] = i;
        }

        Arrays.sort(starting_seq, Comparator.comparingInt(i -> T_i[i]));

        int[] startingSeq = Arrays.stream(starting_seq).mapToInt(Integer::intValue).toArray();
        int[] initSol = new int[numJobs];


    }

    private static int[] computeTiMatrix(int[][] processingTimes){
        int numJobs = processingTimes[0].length;
        int numMachines = processingTimes.length;
        int[] T_i = new int[numJobs]; // init with 0, usefull for the addition of the Ti.

        for (int j = 0; j < numJobs; j++){
            for (int m = 0; m < numMachines; m++){
                T_i[j] += processingTimes[m][j];
            }
        }
        return T_i;
    }

    public static void getRandomPermutation(int[] permutation) {
        Random rand = new Random();
        for (int i = permutation.length - 1; i > 0; i--) {

            int j = rand.nextInt(i + 1); // generate a random index between [0; i + 1[
            swap(permutation, i, j);
        }
    }

    private static int[] firstImprovement(int[][] processingTimes, int[] permutation, String neighborhood) {
        int numJobs = permutation.length;
        int[] firstImprovePermutation = Arrays.copyOf(permutation, numJobs);
        int[][] completionTimeMatrix = computeCompletionTimeMatrix(processingTimes,firstImprovePermutation);
        int minCompletionTime = computeTotalCompletionTime(completionTimeMatrix);
        boolean improved = true;

        while (improved) {
            improved = false;

            for (int i = 0; i < numJobs; i++) {
                for (int j = 0; j < numJobs; j++) {
                    if (i == j) continue;  // avoid incorrect moves

                    int[] newPermutation = Arrays.copyOf(firstImprovePermutation, numJobs);

                    switch (neighborhood) {
                        case "exchange":
                            swap(newPermutation, i, j);
                            break;
                        case "transpose":
                            if (j == i + 1) swap(newPermutation, i, j); // swap only if both jobs are adjacent
                            break;
                        case "insert":
                            insert(newPermutation, i, j);
                            break;
                    }

                    int[][] newCompletionTimeMatrix = computeCompletionTimeMatrix(processingTimes, newPermutation);
                    int newCompletionTime = computeTotalCompletionTime(newCompletionTimeMatrix);

                    if (newCompletionTime < minCompletionTime) {
                        minCompletionTime = newCompletionTime;
                        firstImprovePermutation = Arrays.copyOf(newPermutation, numJobs);
                        improved = true;
                        break;  // First improvement is found, we stop the for j loop
                    }
                }

                if (improved) {
                    break;  // First improvement is found, we stop the for i loop
                }

            }
        }
        return firstImprovePermutation;
    }

    private static int[] bestImprovement(int[][] processingTimes, int[] permutation, String neighborhood) {
        int numJobs = permutation.length;
        int[] bestImprovementPermutation = Arrays.copyOf(permutation, numJobs);
        int[][] completionTimeMatrix = computeCompletionTimeMatrix(processingTimes, bestImprovementPermutation);
        int minCompletionTime = computeTotalCompletionTime(completionTimeMatrix);

        boolean improved = true;

        while (improved) {
            improved = false;

            for (int i = 0; i < numJobs; i++) {
                for (int j = 0; j < numJobs; j++) {
                    if (i == j) continue;  // avoid incorrect moves

                    int[] newPermutation = Arrays.copyOf(bestImprovementPermutation, numJobs);

                    // Apply the neighborhood operation
                    switch (neighborhood) {
                        case "exchange":
                            swap(newPermutation, i, j);
                            break;
                        case "transpose":
                            if (j == i + 1) swap(newPermutation, i, j); // swap only if both jobs are adjacent
                            break;
                        case "insert":
                            insert(newPermutation, i, j);
                            break;
                    }
                    int[][] newCompletionTimeMatrix = computeCompletionTimeMatrix(processingTimes, newPermutation);
                    int newCompletionTime = computeTotalCompletionTime(newCompletionTimeMatrix);

                    // Search the min Completion time of all permutations:
                    if (newCompletionTime <minCompletionTime) {
                        minCompletionTime = newCompletionTime;
                        bestImprovementPermutation = Arrays.copyOf(newPermutation, numJobs);
                        improved = true; // true but we continue the for i and the for j loops before return
                    }
                }
            }
        }
        return bestImprovementPermutation;
    }

    private static void swap(int[] permutation, int i, int j) {
        int temp = permutation[i];
        permutation[i] = permutation[j];
        permutation[j] = temp;
    }

    private static void insert(int[] permutation, int i, int j) {
        int temp = permutation[i];
        if (i < j) {
            System.arraycopy(permutation, i + 1, permutation, i, j - i);
        } else {
            System.arraycopy(permutation, j, permutation, j + 1, i - j);
        }
        permutation[j] = temp;
    }


    private static int[][] computeCompletionTimeMatrix(int[][] processingTimes, int[] permutation) {
        int numJobs = permutation.length;  // permutation.length to compute the SRZ initial solution. (partial computations)
        int numMachines = processingTimes.length;
        int[][] completionTimeMatrix = new int[numMachines][numJobs];

        for (int j = 0; j < numJobs; j++) {
            int job = permutation[j];

            for (int m = 0; m < numMachines; m++) {

                if (m == 0 && j == 0) {
                    completionTimeMatrix[m][j] = processingTimes[m][job];
                } else if (m == 0) {
                    completionTimeMatrix[m][j] = completionTimeMatrix[m][j - 1] + processingTimes[m][job];
                } else if (j == 0) {
                    completionTimeMatrix[m][j] = completionTimeMatrix[m - 1][j] + processingTimes[m][job];
                } else {
                    completionTimeMatrix[m][j] = Math.max(completionTimeMatrix[m - 1][j], completionTimeMatrix[m][j - 1]) + processingTimes[m][job];
                }
            }
        }

        return completionTimeMatrix;

    }

    private static int computeTotalCompletionTime(int[][] completionTimeMatrix){
        int numJobs = completionTimeMatrix[0].length;
        int numMachines = completionTimeMatrix.length;
        int totalCompletionTime = 0;
        for (int j = 0; j < numJobs; j++) {
            totalCompletionTime += completionTimeMatrix[numMachines -1][j];
        }

        return totalCompletionTime;
    }

    private static int computeMakespan(int[][] completionTimeMatrix){
        int numJobs = completionTimeMatrix[0].length;
        int numMachines = completionTimeMatrix.length;
        return completionTimeMatrix[numMachines - 1][numJobs - 1];
    }
}

