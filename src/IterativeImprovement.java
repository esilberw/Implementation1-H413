// Author: Elliot Silberwasser - M-INFOS Heuristic Optimization INFO-H413:
import java.util.*;
import java.io.*;

// Exercice 1.1:
public class IterativeImprovement {
    public static void main(String[] args) throws IOException {
        //int[][] processingTimeTest = {
        //        {3, 3, 4, 2, 3},
        //        {2, 1, 3, 3, 1},
        //        {4, 2, 1, 2, 3}
        //};

        //int[] permutationTest = {0, 1, 2, 3, 4};

        int[][] processingTimes = readFile("src/Benchmarks/ta051");

        int numJobs = processingTimes[0].length;
        int[] initialPermutation = initializePermutation(numJobs, "");
        int[] bestPermutation = bestImprovement(processingTimes, initialPermutation, "insert");

        System.out.println("Best permutation: " + Arrays.toString(bestPermutation));
        System.out.println("Total completion time: " + computeCompletionTime(processingTimes, bestPermutation));
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
            parts = line.trim().split("\\s+");  // put in an array the value of the line separates by an espace.
            for (int k = 0; k < parts.length; k += 2) {
                int machine = Integer.parseInt(parts[k]) - 1;  // -1 for the index machine 1, index 0 in the code.
                int processingtTime = Integer.parseInt(parts[k + 1]); // odd index = processingValue for the job.
                processingTime[machine][job] = processingtTime;
            }
        }

        br.close();
        return processingTime;
    }

    private static int[] initializePermutation(int numJobs, String initMethod) {
        int[] permutation = new int[numJobs];
        for (int i = 0; i < numJobs; i++) {
            permutation[i] = i;
        }

        // switch(initMethod):
            //case "random":
        getRandomPermutation(permutation);
        //break;
        //  case "srz":
        return permutation;
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
        int minCompletionTime = computeCompletionTime(processingTimes, firstImprovePermutation);

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

                    int newCompletionTime = computeCompletionTime(processingTimes, newPermutation);
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
        int[] bestPermutation = Arrays.copyOf(permutation, numJobs);
        int minCompletionTime = computeCompletionTime(processingTimes, bestPermutation);

        boolean improved = true;

        while (improved) {
            improved = false;

            for (int i = 0; i < numJobs; i++) {
                for (int j = 0; j < numJobs; j++) {
                    if (i == j) continue;  // avoid incorrect moves

                    int[] newPermutation = Arrays.copyOf(bestPermutation, numJobs);

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

                    int newCompletionTime = computeCompletionTime(processingTimes, newPermutation);

                    // Search the min Completion time of all permutations:
                    if (newCompletionTime <minCompletionTime) {
                        minCompletionTime = newCompletionTime;
                        bestPermutation = Arrays.copyOf(newPermutation, numJobs);
                        improved = true;
                    }
                }
            }
        }
        return bestPermutation;
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

    private static int computeCompletionTime(int[][] processingTimes, int[] permutation) {
        int numJobs = processingTimes[0].length;
        int numMachines = processingTimes.length;
        int[][] completionTimes = new int[numMachines][numJobs];

        for (int j = 0; j < numJobs; j++) {
            int job = permutation[j];

            for (int m = 0; m < numMachines; m++) {

                if (m == 0 && j == 0) {
                    completionTimes[m][j] = processingTimes[m][job];
                }
                else if (m == 0) {
                    completionTimes[m][j] = completionTimes[m][j - 1] + processingTimes[m][job];
                }
                else if (j == 0) {
                    completionTimes[m][j] = completionTimes[m - 1][j] + processingTimes[m][job];
                }
                else {
                    completionTimes[m][j] = Math.max(completionTimes[m - 1][j], completionTimes[m][j - 1]) + processingTimes[m][job];
                }
            }
        }

        int totalCompletionTime = 0;
        for (int j = 0; j < numJobs; j++) {
            totalCompletionTime += completionTimes[numMachines -1][j];
        }

        return totalCompletionTime;
    }

}

