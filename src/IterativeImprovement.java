// Author: Elliot Silberwasser - M-INFOS Heuristic Optimization INFO-H413:
import java.util.*;
import java.io.*;

// Exercice 1.1:
public class IterativeImprovement {
    public static void main(String[] args) {
        int[][] processingTimes = readFile("src/Benchmarks/ta051.txt");
        if (processingTimes == null) return;

        int numJobs = processingTimes.length;
        System.out.println(numJobs);
        int[] initialPermutation = initializePermutation(numJobs, "");
        int[] bestPermutation = firstImprovement(processingTimes, initialPermutation, "exchange");

        System.out.println("Best permutation: " + Arrays.toString(bestPermutation));
        System.out.println("Total completion time: " + computeCompletionTime(processingTimes, bestPermutation));
    }

    private static int[][] readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String[] firstLine = br.readLine().split(" ");
            int numJobs = Integer.parseInt(firstLine[0]);
            int numMachines = Integer.parseInt(firstLine[1]);

            int[][] processingTimes = new int[numJobs][numMachines];
            for (int i = 0; i < numJobs; i++) {
                String[] line = br.readLine().split(" ");
                for (int j = 0; j < numMachines; j++) {
                    processingTimes[i][j] = Integer.parseInt(line[j]);
                }
            }
            return processingTimes;
        } catch (IOException e) {
            System.out.println("File not found: " + filePath);
            return null;
        }
    }

    private static int[] initializePermutation(int numJobs, String initMethod) {
        int[] permutation = new int[numJobs];
        for (int i = 0; i < numJobs; i++) {
            permutation[i] = i;
        }

        // switch(initMethod):
            //case(random):
        System.out.println(Arrays.toString(permutation));
        getRandomPermutation(permutation);
        System.out.println(Arrays.toString(permutation));
        //break;
        // case(srz):

        return permutation;
    }


    public static void getRandomPermutation(int[] permutation) {
        Random rand = new Random();
        for (int i = permutation.length - 1; i > 0; i--) {

            int j = rand.nextInt(i + 1); // generate a random index
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }
    }


    private static List<Integer> generateRandomPermutation(int n) {
        // random
        List<Integer> permutation = new ArrayList<Integer>();
        for (int i = 1; i <= n; i++) {
            permutation.add(i);
        }
        Collections.shuffle(permutation); // Random Uniform Permutation
        return permutation;
    }

    private static int[] firstImprovement(int[][] processingTimes, int[] permutation, String neighborhood) {
        int numJobs = permutation.length;
        int[] bestPermutation = Arrays.copyOf(permutation, numJobs);
        int bestCompletionTime = computeCompletionTime(processingTimes, bestPermutation);

        boolean improved = true;
        while (improved) {
            improved = false;

            for (int i = 0; i < numJobs - 1; i++) {
                for (int j = i + 1; j < numJobs; j++) {
                    int[] newPermutation = Arrays.copyOf(bestPermutation, numJobs);
                    switch (neighborhood) {
                        case "exchange":
                            swap(newPermutation, i, j);
                            break;
                        case "transpose":
                            if (j == i + 1) swap(newPermutation, i, j);
                            break;
                        case "insert":
                            insert(newPermutation, i, j);
                            break;
                    }
                    int newCompletionTime = computeCompletionTime(processingTimes, newPermutation);
                    if (newCompletionTime < bestCompletionTime) {
                        bestCompletionTime = newCompletionTime;
                        bestPermutation = newPermutation;
                        improved = true;
                        break;
                    }
                }
                if (improved) break;
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
        int numJobs = processingTimes.length;
        int numMachines = processingTimes[0].length;
        int[][] completionTimes = new int[numJobs][numMachines];

        for (int j = 0; j < numMachines; j++) {
            for (int i = 0; i < numJobs; i++) {
                int job = permutation[i];
                if (i == 0 && j == 0) {
                    completionTimes[i][j] = processingTimes[job][j];
                } else if (i == 0) {
                    completionTimes[i][j] = completionTimes[i][j - 1] + processingTimes[job][j];
                } else if (j == 0) {
                    completionTimes[i][j] = completionTimes[i - 1][j] + processingTimes[job][j];
                } else {
                    completionTimes[i][j] = Math.max(completionTimes[i - 1][j], completionTimes[i][j - 1]) + processingTimes[job][j];
                }
            }
        }
        return completionTimes[numJobs - 1][numMachines - 1];
    }
}
