// Author: Elliot Silberwasser - M-INFOS Heuristic Optimization INFO-H413:

import java.util.*;
import java.io.*;
import java.io.File;

public class IterativeImprovement {
    static int[][] processingTimesMatrix;
    static int numJobs;
    static int numMachines;
    static String taskFile;
    static int[] bestKnonwTCT;
    static float avgRelativePercDeviation;
    static int numTest = 10;
    static File[] files;

    public static void main(String[] args) throws IOException {
        readBestKnownTCT("./bestKnownTCT/bestKnownTCT.txt");
        File benchmarkFolder = new File("./Benchmarks");
        files = benchmarkFolder.listFiles();
        Arrays.sort(files, Comparator.comparing(File::getName));

        if (args[0].equals("--test") && args.length < 5) {
            if (args.length == 4) {
                String pivotingRule = args[1];
                String neighborhood = args[2];
                String initMethod = args[3];


                if (pivotingRule.equals("--best") && initMethod.equals("--random")) {
                    switch (neighborhood) {
                        case "--exchange":
                            int[] testBestExchangeRandomIIValues = testBestExchangeRandomII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testBestExchangeRandomIIValues[(files.length * numTest)]);
                            break;
                        case "--transpose":
                            int[] testBestTransposeRandomIIValues = testBestTransposeRandomII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testBestTransposeRandomIIValues[(files.length * numTest)]);
                            break;

                        case "--insert":
                            int[] testBestInsertRandomIIValues = testBestInsertRandomII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testBestInsertRandomIIValues[(files.length * numTest)]);
                            break;

                    }
                }

                if (pivotingRule.equals("--best") && initMethod.equals("--srz")) {
                    switch (neighborhood) {
                        case "--exchange":
                            int[] testBestExchangeSrzIIValues = testBestExchangeSrzII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testBestExchangeSrzIIValues[(files.length * numTest)]);
                            break;
                        case "--transpose":
                            int[] testBestTransposeSrzIIValues = testBestTransposeSrzII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testBestTransposeSrzIIValues[(files.length * numTest)]);
                            break;

                        case "--insert":
                            int[] testBestInsertSrzIIValues = testBestInsertSrzII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testBestInsertSrzIIValues[(files.length * numTest)]);
                            break;
                    }

                }

                if (pivotingRule.equals("--first") && initMethod.equals("--random")) {
                    switch (neighborhood) {
                        case "--exchange":
                            int[] testFirstExchangeRandomIIValues = testFirstExchangeRandomII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testFirstExchangeRandomIIValues[(files.length * numTest)]);
                            break;
                        case "--transpose":
                            int[] testFirstTransposeRandomIIValues = testFirstTransposeRandomII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testFirstTransposeRandomIIValues[(files.length * numTest)]);
                            break;

                        case "--insert":
                            int[] testFirstInsertRandomIIValues = testFirstInsertRandomII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testFirstInsertRandomIIValues[(files.length * numTest)]);
                            break;
                    }
                }

                if (pivotingRule.equals("--first") && initMethod.equals("--srz")) {
                    switch (neighborhood) {
                        case "--exchange":
                            int[] testFirstExchangeSrzIIValues = testFirstExchangeSrzII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testFirstExchangeSrzIIValues[(files.length * numTest)]);
                            break;
                        case "--transpose":
                            int[] testFirstTransposeSrzIIValues = testFirstTransposeSrzII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testFirstTransposeSrzIIValues[(files.length * numTest)]);
                            break;

                        case "--insert":
                            int[] testFirstInsertSrzIIValues = testFirstInsertSrzII();
                            System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                            System.out.println("Sum of Completion Time: " + testFirstInsertSrzIIValues[(files.length * numTest)]);
                            break;
                    }
                }
            } else if (args[1].equals("--vnd")) {
                String neighborhoodOrder = args[2];
                switch (neighborhoodOrder) {
                    case "--one":
                        int[] testFirstOrderCompletionTimeValues = testFirstOrderVND();
                        System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                        System.out.println("Sum of Completion Time: " + testFirstOrderCompletionTimeValues[(files.length * numTest)]);
                        break;
                    case "--two":
                        int[] testSecondOrderCompletionTimeValues = testSecondOrderVND();
                        System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                        System.out.println("Sum of Completion Time: " + testSecondOrderCompletionTimeValues[(files.length * numTest)]);
                        break;
                }
            }
            else if (args[1].equals("--ils")){
                int[] testILSValues = testILS();
                System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                System.out.println("Sum of Completion Time: " + testILSValues[(files.length * numTest)]);
                }

            else if (args[1].equals("--gls")){
                int[] testGLSValues = testGLS();
                System.out.println("(Float) Average relative percentage deviation: " + avgRelativePercDeviation);
                System.out.println("Sum of Completion Time: " + testGLSValues[(files.length * numTest)]);
            }
            else {
                System.out.println("Usage of test:\n$java IterativeImprovement --test --<pivoting_rule> --<neighborhood> --<init_method> to test the algorithms and compute the average relative percentage deviation and the sum of completion time");
                System.out.println("For the VND Variants:\n$java IterativeImprovement --test --vnd --<neighborhood_order>");
            }
        } else if (args.length < 1) {
            System.out.println("Usage: $java IterativeImprovement --<file_name> --<pivoting_rule> --<neighborhood> --<init_method>\nif you want to launch VND algorithms: $java IterativeImprovement <vnd> <neighborhood_order>");
            System.out.println("Exemple: $java IterativeImprovement ./Benchmarks/ta051 --first --transpose --srz\n         $java IterativeIImprovement ./Benchmarks/ta051 --vnd --one\n");
        }
        else if (args.length < 3) {
            readFile(args[0]);
            int maxIter;
            if (numJobs==50) {
                maxIter = 122*500;
            }
            else if(numJobs ==100){
                maxIter = 1619*500;
            }
            else{
                maxIter = 33208*100;
            }
            System.out.println("Max Iterations: " + maxIter);
            if (args[1].equals("--ils")) {
                int[] testILS = ILS_withHistory(maxIter, 3, 8, 8);
                System.out.println(Arrays.toString(testILS));
                int[][] CTMatrix= computeCompletionTimeMatrix(testILS);
                System.out.println(computeTotalCompletionTime(CTMatrix));
            }

            else if (args[1].equals("--gls")){
                int[] testGLS = guidedLocalSearch(maxIter);
                System.out.println(Arrays.toString(testGLS));
                int[][] CTMatrix= computeCompletionTimeMatrix(testGLS);
                System.out.println(computeTotalCompletionTime(CTMatrix));
            }
        }
        else {
            List<String> validPivotingRules = Arrays.asList("--first", "--best", "--vnd");
            List<String> validNeighborhoods = Arrays.asList("--exchange", "--transpose", "--insert", "--one", "--two");
            List<String> validInitMethods = Arrays.asList("--random", "--srz");

            taskFile = args[0];
            String pivotingRule = args[1];
            String neighborhood = args[2];
            String initMethod;

            if (args.length == 3) {
                initMethod = "--srz";
            } else {
                // II Algorithms, need to specify the initMethod choice.
                initMethod = args[3];
            }

            if (!validPivotingRules.contains(pivotingRule)) {
                System.out.println("Error: Heuristic not valide. Options: --first, --best");
                return;
            }
            if (!validNeighborhoods.contains(neighborhood)) {
                System.out.println("Error: Pivoting Rule not valide. Options: --exchange, --transpose, --insert, --one, two");
                return;
            }
            if (args.length == 3 && !validInitMethods.contains(initMethod)) {
                System.out.println("Error: Init Method not valid. Options: --random, --srz");
                return;
            }


            readFile(taskFile);
            int[] initialPermutation = initializePermutation(initMethod);
            System.out.println(Arrays.toString(initialPermutation));
            int[] bestPermutation;

            if (pivotingRule.equals("--first")) {
                bestPermutation = firstImprovement(initialPermutation, neighborhood);
            } else if (pivotingRule.equals("--best")) {
                bestPermutation = bestImprovement(initialPermutation, neighborhood);
            } else {
                bestPermutation = variableNeighborhoodDescent(neighborhood);
            }

            System.out.println("Best permutation: " + Arrays.toString(bestPermutation));
            System.out.println("Total completion time : " + computeTotalCompletionTime(computeCompletionTimeMatrix(bestPermutation)));
        }
    }

    public static void readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        String[] parts = line.trim().split("\\s+");

        numJobs = Integer.parseInt(parts[0]);
        numMachines = Integer.parseInt(parts[1]);
        processingTimesMatrix = new int[numMachines][numJobs];

        for (int job = 0; job < numJobs; job++) {
            line = br.readLine();
            parts = line.trim().split("\\s+");  // put in an array the values of the line separates by a space.
            for (int k = 0; k < parts.length; k += 2) {
                int machine = Integer.parseInt(parts[k]) - 1;  // -1 for the index (machine 1 -> index 0 in the code).
                int processingtTime = Integer.parseInt(parts[k + 1]); // odd index = processingValue for the job.
                processingTimesMatrix[machine][job] = processingtTime;
            }
        }

        br.close();
    }


    public static void readBestKnownTCT(String filePath) {
        ArrayList<Integer> valuesList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("[,\\s]+");
                if (parts.length >= 2) {
                    int value = Integer.parseInt(parts[1].trim());
                    valuesList.add(value);
                }
            }


        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        int[] result = new int[valuesList.size()];
        for (int i = 0; i < valuesList.size(); i++) {
            result[i] = valuesList.get(i);
        }

        bestKnonwTCT = result;
    }


    public static int[] initializePermutation(String initMethod) {
        int[] permutation = new int[numJobs];
        for (int i = 0; i < numJobs; i++) {
            permutation[i] = i;
        }

        switch (initMethod) {
            case "--random":
                getRandomPermutation(permutation);
                break;
            case "--SRZ":
                permutation = getSRZPermutation();
                break;
        }
        return permutation;
    }

    public static int[] getSRZPermutation() {
        int[] T_i = computeTiArray();

        Integer[] starting_seq = new Integer[T_i.length];
        for (int i = 0; i < T_i.length; i++) {
            starting_seq[i] = i;
        }

        Arrays.sort(starting_seq, Comparator.comparingInt(i -> T_i[i]));

        int[] startingSeq = Arrays.stream(starting_seq).mapToInt(Integer::intValue).toArray();
        int[] initSol = generateBestInitSolution(startingSeq);
        return initSol;
    }

    public static int[] generateBestInitSolution(int[] startingSeq) {
        int numJobs = startingSeq.length;
        int[] minCTSeq = new int[numJobs];
        Arrays.fill(minCTSeq, -1); // Sentinel value to init
        minCTSeq[0] = startingSeq[0];

        for (int step = 1; step < numJobs; step++) {
            int jobToInsert = startingSeq[step];
            int minCT = Integer.MAX_VALUE;
            int bestInsertPos = 0;
            int[] tempSequence = new int[step + 1];

            for (int insertPos = 0; insertPos <= step; insertPos++) {
                int tempIdx = 0;
                for (int k = 0; k <= step; k++) {
                    if (k == insertPos) {
                        tempSequence[k] = jobToInsert;
                    } else {
                        tempSequence[k] = minCTSeq[tempIdx++];
                    }
                }

                int[][] currentCTMatrix = computeCompletionTimeMatrix(tempSequence);
                int currentCT = computeTotalCompletionTime(currentCTMatrix);

                if (currentCT <= minCT) {
                    minCT = currentCT;
                    bestInsertPos = insertPos;
                }
            }

            // Complete the partial solution with the best job (=min Global CT of the seq of the partial solution) is found for the step index.
            int[] newBestSequence = new int[step + 1];
            int idx = 0;
            for (int k = 0; k <= step; k++) {
                if (k == bestInsertPos) {
                    newBestSequence[k] = jobToInsert;
                } else {
                    newBestSequence[k] = minCTSeq[idx++];
                }
            }
            minCTSeq = newBestSequence;
        }
        return minCTSeq;
    }

    public static int[] computeTiArray() {

        int[] T_i = new int[numJobs]; // init with 0, use it for the addition of the Ti.

        for (int j = 0; j < numJobs; j++) {
            for (int m = 0; m < numMachines; m++) {
                T_i[j] += processingTimesMatrix[m][j];
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

    public static int[] firstImprovement(int[] permutation, String neighborhood) {
        int[] currentPermutation = Arrays.copyOf(permutation, numJobs);
        int[][] completionTimeMatrix = computeCompletionTimeMatrix(currentPermutation);
        int currentCost = computeTotalCompletionTime(completionTimeMatrix);

        boolean improved = true;

        while (improved) {
            improved = false;

            for (int i = 0; i < numJobs - 1; i++) {
                for (int j = i + 1; j < numJobs; j++) {
                    swap(currentPermutation, i, j);

                    int fromIndex = Math.min(i, j);
                    int[][] newMatrix = computeCompletionTimeMatrixAfterMove(currentPermutation, completionTimeMatrix, fromIndex);
                    int newCost = computeTotalCompletionTime(newMatrix);

                    if (newCost < currentCost) {

                        completionTimeMatrix = newMatrix;
                        currentCost = newCost;
                        improved = true;
                        break;
                    } else {
                        swap(currentPermutation, i, j);
                    }
                }

                if (improved) break;
            }
        }

        return currentPermutation;
    }

    public static int[] bestImprovement(int[] permutation, String neighborhood) {
        int[] bestImprovementPermutation = Arrays.copyOf(permutation, numJobs);
        int[][] completionTimeMatrix = computeCompletionTimeMatrix(bestImprovementPermutation);
        int minCompletionTime = computeTotalCompletionTime(completionTimeMatrix);

        boolean improved = true;

        while (improved) {
            improved = false;

            for (int i = 0; i < numJobs; i++) {
                for (int j = i + 1; j < numJobs; j++) {
                    int[] newPermutation = Arrays.copyOf(bestImprovementPermutation, numJobs);

                    switch (neighborhood) {
                        case "--exchange":
                            swap(newPermutation, i, j);
                            break;
                        case "--transpose":
                            if (j == i + 1) swap(newPermutation, i, j); // swap only if both jobs are adjacent
                            break;
                        case "--insert":
                            insert(newPermutation, i, j);
                            break;
                    }

                    int[][] newCompletionTimeMatrix = computeCompletionTimeMatrixAfterMove(newPermutation, completionTimeMatrix, i);
                    int newCompletionTime = computeTotalCompletionTime(newCompletionTimeMatrix);

                    // Search the min Completion time of all permutations:
                    if (newCompletionTime < minCompletionTime) {
                        minCompletionTime = newCompletionTime;
                        bestImprovementPermutation = Arrays.copyOf(newPermutation, numJobs);
                        completionTimeMatrix = newCompletionTimeMatrix;
                        improved = true; // true but we continue the for i and the for j loops before return
                    }
                }
            }
        }
        return bestImprovementPermutation;
    }

    public static int[] variableNeighborhoodDescent(String neighborhoodOrder) {
        int[] currentSolution = getSRZPermutation(); // initial Solution SRZ

        int currentCompletionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(currentSolution));

        String[] neighborhoods;
        if (neighborhoodOrder.equals("--one")) {
            neighborhoods = new String[]{"--transpose", "--exchange", "--insert"};
        } else {
            neighborhoods = new String[]{"--transpose", "--insert", "--exchange"};
        }

        boolean improved = true;

        while (improved) {
            improved = false;
            for (String neighborhood : neighborhoods) {
                int[] newSolution = firstImprovement(currentSolution, neighborhood);
                int newCompletionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(newSolution));

                if (newCompletionTime < currentCompletionTime) {
                    currentSolution = newSolution;
                    currentCompletionTime = newCompletionTime;
                    improved = true;
                    break; // Go back to the first N_i
                }
            }
        }
        ;

        return currentSolution;
    }


    // HYBRID SLS METHOD:
    public static int[] ILS_withHistory(int maxIterations, int kMin, int kMax, int eliteSize) {
        int[] permutation = new int[numJobs];
        for (int i = 0; i < numJobs; i++) permutation[i] = i;
        getRandomPermutation(permutation);

        int[] current = bestImprovement(permutation, "--exchange");
        int[][] currentMatrix = computeCompletionTimeMatrix(current);
        int currentCost = computeTotalCompletionTime(currentMatrix);

        int[] best = Arrays.copyOf(current, current.length);
        int bestCost = currentCost;

        // Elite memory
        List<int[]> eliteSolutions = new ArrayList<>();
        List<Integer> eliteCosts = new ArrayList<>();
        eliteSolutions.add(Arrays.copyOf(best, best.length));
        eliteCosts.add(bestCost);

        Random rand = new Random();
        int noImprovementCounter = 0;
        int maxNoImprovement = 15000;

        for (int iter = 0; iter < maxIterations; iter++) {
            if (iter % 1000 == 0) {
                System.out.println("Iter " + iter + " | Best cost: " + bestCost);
            }

            // Perturbation
            int[] perturbed = Arrays.copyOf(best, best.length);
            int k = rand.nextInt(kMax - kMin + 1) + kMin;
            randomKOpt(perturbed, k, rand);

            // Local search
            int[] improved = bestImprovement(perturbed, "--exchange");
            int newCost = computeTotalCompletionTime(computeCompletionTimeMatrix(improved));

            if (newCost < bestCost) {
                best = Arrays.copyOf(improved, improved.length);
                bestCost = newCost;

                eliteSolutions.add(best);
                eliteCosts.add(bestCost);

                if (eliteSolutions.size() > eliteSize) {
                    int idxWorst = 0;
                    for (int i = 1; i < eliteCosts.size(); i++) {
                        if (eliteCosts.get(i) > eliteCosts.get(idxWorst)) {
                            idxWorst = i;
                        }
                    }
                    eliteSolutions.remove(idxWorst);
                    eliteCosts.remove(idxWorst);
                }

                noImprovementCounter = 0;  // reset since improved
            } else {
                noImprovementCounter++;
            }

            if (noImprovementCounter >= maxNoImprovement) {
                System.out.println("Stopped early at iter " + iter + " due to stagnation.");
                break;
            }
        }

        return best;
    }

    private static void randomKOpt(int[] permutation, int k, Random rand) {
        int n = permutation.length;

        // Select K elements randomly
        Set<Integer> positions = new HashSet<>();
        while (positions.size() < k) {
            positions.add(rand.nextInt(n));
        }
        List<Integer> posList = new ArrayList<>(positions);

        // shuffle the selected elements.
        List<Integer> selected = new ArrayList<>();
        for (int pos : posList) {
            selected.add(permutation[pos]);
        }
        Collections.shuffle(selected, rand);

        // Insert the selected elements
        for (int i = 0; i < posList.size(); i++) {
            permutation[posList.get(i)] = selected.get(i);
        }
    }

    // SIMPLE SLS METHOD:
    public static int[] guidedLocalSearch(int maxIterations) {
        // Generate initiale candidate solution s:
        int[] permuatation = new int[numJobs];
        for (int i = 0; i < numJobs; i++){
            permuatation[i] = i;
        }
        getRandomPermutation(permuatation);
        int[][] initMatrix = computeCompletionTimeMatrix(permuatation);
        int initialCost = computeTotalCompletionTime(initMatrix);

        double alpha = 0.6;  // Increased value to enhance influence of penalties
        // Penalty Update: alpha . (f(s0) / n)
        double lambda = alpha * initialCost / (numJobs - 1);  // TODO expliquer le moins dans le rapport vient du fait qu'on teste le nb de characteristiques.

        int[] current = permuatation.clone();
        int[][] penalties = new int[numJobs][numJobs];  // penalty init: set all penalties to 0

        int[][] currentMatrix = computeCompletionTimeMatrix(current);
        int currentCost = computeTotalCompletionTime(currentMatrix);
        int[] best = current.clone();
        int bestCost = currentCost;

        int noImprovementCounter = 0;
        int maxNoImprovement = 15000;

        for (int iter = 0; iter < maxIterations; iter++) {

            if (iter % 1000 == 0) {
                System.out.println("Iter " + iter + " | Best cost: " + bestCost);
            }

            // Inject small perturbation every 2000 non-improving iterations
            if (noImprovementCounter % 2000 == 0 && noImprovementCounter > 0) {
                randomKOpt(current, 3, new Random());
                currentMatrix = computeCompletionTimeMatrix(current);
                currentCost = computeTotalCompletionTime(currentMatrix);
            }

            int penaltyTerm = computePenaltyTerm(current, penalties);
            int augmentedCost = currentCost + (int)(lambda * penaltyTerm);

            // Perform subsidiary local search on s; using Iterative Best Improvement with 2-exchange:
            int[] candidate = bestImprovement(current.clone(), "--exchange");
            int[][] candidateMatrix = computeCompletionTimeMatrix(candidate);
            int candidateCost = computeTotalCompletionTime(candidateMatrix);
            int newPenaltyTerm = computePenaltyTerm(candidate, penalties);
            int newAugmentedCost = candidateCost + (int)(lambda * newPenaltyTerm);

            // Pertubated criterion:
            if (newAugmentedCost < augmentedCost && !Arrays.equals(candidate, current)) {
                current = candidate;
                currentCost = candidateCost;
            } else {
                double maxUtility = -1;
                List<int[]> maxPairs = new ArrayList<>();

                for (int i = 0; i < current.length - 1; i++) {
                    int a = current[i];
                    int b = current[i + 1];
                    int cost = 0;
                    for (int m = 0; m < numMachines; m++) {
                        cost += processingTimesMatrix[m][a] + processingTimesMatrix[m][b];
                    }
                    double utility = cost / (1.0 + penalties[a][b]);

                    if (utility > maxUtility) {
                        maxUtility = utility;
                        maxPairs.clear();
                        maxPairs.add(new int[]{a, b});
                    } else if (utility == maxUtility) {
                        maxPairs.add(new int[]{a, b});
                    }
                }

                for (int[] pair : maxPairs) {
                    penalties[pair[0]][pair[1]] += 2; // Stronger penalty increment to enhance diversification
                }
            }

            // Acceptance criterion
            if (currentCost < bestCost) {
                best = current.clone();
                bestCost = currentCost;
                noImprovementCounter = 0; // reset since improvement occurred
            } else {
                noImprovementCounter++;
            }

            if (noImprovementCounter >= maxNoImprovement) {
                System.out.println("Stopped early at iteration " + iter + " due to no improvement.");
                break;
            }
        }

        return best;
    }

    private static int computePenaltyTerm(int[] permutation, int[][] penalties) {
        int total = 0;
        for (int i = 0; i < permutation.length - 1; i++) {
            int a = permutation[i];
            int b = permutation[i + 1];
            total += penalties[a][b];
        }
        return total;
    }


    public static void swap(int[] permutation, int i, int j) {
        int temp = permutation[i];
        permutation[i] = permutation[j];
        permutation[j] = temp;
    }

    public static void insert(int[] permutation, int i, int j) {
        int temp = permutation[i];
        if (i < j) {
            System.arraycopy(permutation, i + 1, permutation, i, j - i);
        } else {
            System.arraycopy(permutation, j, permutation, j + 1, i - j);
        }
        permutation[j] = temp;
    }


    public static int[][] deepCopyMatrix(int[][] matrix) {
        int[][] copy = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }
        return copy;
    }


    public static int[][] computeCompletionTimeMatrixAfterMove(
            int[] permutation,
            int[][] currentCompletionMatrix,
            int i) {

        int numJobs = permutation.length;
        int[][] updatedMatrix = deepCopyMatrix(currentCompletionMatrix);

        for (int jobIdx = i; jobIdx < numJobs; jobIdx++) {
            int job = permutation[jobIdx];
            for (int m = 0; m < numMachines; m++) {
                if (m == 0 && jobIdx == 0) {
                    updatedMatrix[m][jobIdx] = processingTimesMatrix[m][job];
                } else if (m == 0) {
                    updatedMatrix[m][jobIdx] = updatedMatrix[m][jobIdx - 1] + processingTimesMatrix[m][job];
                } else if (jobIdx == 0) {
                    updatedMatrix[m][jobIdx] = updatedMatrix[m - 1][jobIdx] + processingTimesMatrix[m][job];
                } else {
                    updatedMatrix[m][jobIdx] = Math.max(updatedMatrix[m - 1][jobIdx], updatedMatrix[m][jobIdx - 1]) + processingTimesMatrix[m][job];
                }
            }
        }

        return updatedMatrix;
    }

    public static int[][] computeCompletionTimeMatrix(int[] permutation) {
        int numJobs = permutation.length;  // permutation.length to compute the SRZ initial solution. (partial computations)
        int[][] completionTimeMatrix = new int[numMachines][numJobs];

        for (int j = 0; j < numJobs; j++) {
            int job = permutation[j];

            for (int m = 0; m < numMachines; m++) {

                if (m == 0 && j == 0) {
                    completionTimeMatrix[m][j] = processingTimesMatrix[m][job];
                } else if (m == 0) {
                    completionTimeMatrix[m][j] = completionTimeMatrix[m][j - 1] + processingTimesMatrix[m][job];
                } else if (j == 0) {
                    completionTimeMatrix[m][j] = completionTimeMatrix[m - 1][j] + processingTimesMatrix[m][job];
                } else {
                    completionTimeMatrix[m][j] = Math.max(completionTimeMatrix[m - 1][j], completionTimeMatrix[m][j - 1]) + processingTimesMatrix[m][job];
                }
            }
        }

        return completionTimeMatrix;

    }

    public static int computeTotalCompletionTime(int[][] completionTimeMatrix) {
        int numJobs = completionTimeMatrix[0].length;
        int numMachines = completionTimeMatrix.length;
        int totalCompletionTime = 0;
        for (int j = 0; j < numJobs; j++) {
            totalCompletionTime += completionTimeMatrix[numMachines - 1][j];
        }

        return totalCompletionTime;
    }

    public static int computeMakespan(int[][] completionTimeMatrix) {
        int numJobs = completionTimeMatrix[0].length;
        int numMachines = completionTimeMatrix.length;
        return completionTimeMatrix[numMachines - 1][numJobs - 1];
    }


    // BEST TEST:
    public static int[] testBestExchangeRandomII() throws IOException {
        System.out.println("Best Exchange Random Iterative Improvement:");
        int[] bestIICompletionTime = new int[(files.length * numTest) + 2]; // + 1 for the average relative percentage deviation and the sum of CT

        int index = 0;
        int bestValueIndex = 0;
        long totalComputationTIme = 0;
        float averageTotalComputationTime = 0;
        float averageRelativePercDeviation = 0;
        int previousNumJobs = numJobs;
        for (File file : files) {
            readFile(String.valueOf(file));
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            if (numJobs > previousNumJobs) { // to compute statistics for each size of instance.
                System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
                averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
                System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
                totalComputationTIme = 0;
                previousNumJobs = numJobs;
            }

            System.out.println(file);

            for (int i = 0; i < numTest; i++) {
                long startTime = System.currentTimeMillis();
                int[] bestIIPermutation = bestImprovement(initializePermutation("--random"), "--exchange");
                long endTime = System.currentTimeMillis();
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(bestIIPermutation));
                System.out.println("Completion Time: " + completionTime);
                long timeDuration = endTime - startTime;
                System.out.println("Computation Time: " + timeDuration + " ms");
                totalComputationTIme += timeDuration;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                bestIICompletionTime[index] = completionTime;
                index++;
            }
        }
        System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
        averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
        System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        bestIICompletionTime[index] = (int) totalComputationTIme;
        bestIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return bestIICompletionTime;
    }

    public static int[] testBestTransposeRandomII() throws IOException {
        System.out.println("Best Transpose Random Iterative Improvement:");
        int[] bestIICompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        int sumCT = 0;
        float averageRelativePercDeviation = 0;

        for (File file : files) {
            readFile(String.valueOf(file));
            System.out.println(file);

            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            for (int i = 0; i < numTest; i++) {

                int[] bestIIPermutation = bestImprovement(initializePermutation("--random"), "--transpose");
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(bestIIPermutation));
                System.out.println("CT: " + completionTime);
                sumCT += completionTime;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                bestIICompletionTime[index] = completionTime;
                index++;
            }
        }
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        bestIICompletionTime[index] = sumCT;
        bestIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return bestIICompletionTime;
    }

    public static int[] testBestInsertRandomII() throws IOException {
        System.out.println("Best Insert Random Iterative Improvement:");
        int[] bestIICompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        int sumCT = 0;
        float averageRelativePercDeviation = 0;

        for (File file : files) {
            readFile(String.valueOf(file));
            System.out.println(file);

            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            for (int i = 0; i < numTest; i++) {

                int[] bestIIPermutation = bestImprovement(initializePermutation("--random"), "--insert");
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(bestIIPermutation));
                System.out.println("CT: " + completionTime);
                sumCT += completionTime;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                bestIICompletionTime[index] = completionTime;
                index++;
            }
        }
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        bestIICompletionTime[index] = sumCT;
        bestIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return bestIICompletionTime;
    }

    public static int[] testBestExchangeSrzII() throws IOException {
        System.out.println("Best Exchange SRZ Iterative Improvement:");
        int[] bestIICompletionTime = new int[(files.length * numTest) + 2]; // + 1 for the average relative percentage deviation and the sum of CT

        int index = 0;
        int bestValueIndex = 0;
        long totalComputationTIme = 0;
        float averageTotalComputationTime = 0;
        float averageRelativePercDeviation = 0;
        int previousNumJobs = numJobs;
        for (File file : files) {
            readFile(String.valueOf(file));
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            if (numJobs > previousNumJobs) { // to compute statistics for each size of instance.
                System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
                averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
                System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
                totalComputationTIme = 0;
                previousNumJobs = numJobs;
            }

            System.out.println(file);

            for (int i = 0; i < numTest; i++) {
                long startTime = System.currentTimeMillis();
                int[] bestIIPermutation = bestImprovement(initializePermutation("--srz"), "--exchange");
                long endTime = System.currentTimeMillis();
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(bestIIPermutation));
                System.out.println("Completion Time: " + completionTime);
                long timeDuration = endTime - startTime;
                System.out.println("Computation Time: " + timeDuration + " ms");
                totalComputationTIme += timeDuration;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                bestIICompletionTime[index] = completionTime;
                index++;
            }
        }
        System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
        averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
        System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        bestIICompletionTime[index] = (int) totalComputationTIme;
        bestIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return bestIICompletionTime;
    }


    public static int[] testBestTransposeSrzII() throws IOException {
        System.out.println("Best Transpose SRZ Iterative Improvement:");
        int[] bestIICompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        int sumCT = 0;
        float averageRelativePercDeviation = 0;
        for (File file : files) {
            readFile(String.valueOf(file));
            System.out.println(file);
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            for (int i = 0; i < numTest; i++) {

                int[] bestIIPermutation = bestImprovement(initializePermutation("--srz"), "--transpose");
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(bestIIPermutation));
                System.out.println("CT: " + completionTime);
                sumCT += completionTime;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                bestIICompletionTime[index] = completionTime;
                index++;
            }
        }
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        bestIICompletionTime[index] = sumCT;
        bestIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return bestIICompletionTime;
    }

    public static int[] testBestInsertSrzII() throws IOException {
        System.out.println("Best Insert SRZ Iterative Improvement:");
        int[] bestIICompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        int sumCT = 0;
        float averageRelativePercDeviation = 0;
        for (File file : files) {
            readFile(String.valueOf(file));
            System.out.println(file);

            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            for (int i = 0; i < numTest; i++) {

                int[] bestIIPermutation = bestImprovement(initializePermutation("--srz"), "--insert");
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(bestIIPermutation));
                System.out.println("CT: " + completionTime);
                sumCT += completionTime;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                bestIICompletionTime[index] = completionTime;
                index++;
            }
        }
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        bestIICompletionTime[index] = sumCT;
        bestIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return bestIICompletionTime;
    }


    // FIRST TEST:
    public static int[] testFirstExchangeRandomII() throws IOException {
        System.out.println("First Exchange Random Iterative Improvement:");
        int[] firstIICompletionTime = new int[(files.length * numTest) + 2]; // + 1 for the average relative percentage deviation and the sum of CT

        int index = 0;
        int bestValueIndex = 0;
        long totalComputationTIme = 0;
        float averageTotalComputationTime = 0;
        float averageRelativePercDeviation = 0;
        int previousNumJobs = numJobs;
        for (File file : files) {
            readFile(String.valueOf(file));
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            if (numJobs > previousNumJobs) { // to compute statistics for each size of instance.
                System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
                averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
                System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
                totalComputationTIme = 0;
                previousNumJobs = numJobs;
            }

            System.out.println(file);

            for (int i = 0; i < numTest; i++) {
                long startTime = System.currentTimeMillis();
                int[] bestIIPermutation = firstImprovement(initializePermutation("--random"), "--exchange");
                long endTime = System.currentTimeMillis();
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(bestIIPermutation));
                System.out.println("Completion Time: " + completionTime);
                long timeDuration = endTime - startTime;
                System.out.println("Computation Time: " + timeDuration + " ms");
                totalComputationTIme += timeDuration;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                firstIICompletionTime[index] = completionTime;
                index++;
            }
        }
        System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
        averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
        System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        firstIICompletionTime[index] = (int) totalComputationTIme;
        firstIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return firstIICompletionTime;
    }


    public static int[] testFirstTransposeRandomII() throws IOException {
        System.out.println("First Transpose Random Iterative Improvement:");
        int[] firstIICompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        int sumCT = 0;
        float averageRelativePercDeviation = 0;
        for (File file : files) {
            readFile(String.valueOf(file));
            System.out.println(file);
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;
            for (int i = 0; i < numTest; i++) {

                int[] firstIIPermutation = firstImprovement(initializePermutation("--random"), "--transpose");
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(firstIIPermutation));
                System.out.println("CT: " + completionTime);
                sumCT += completionTime;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                firstIICompletionTime[index] = completionTime;
                index++;
            }
        }
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        firstIICompletionTime[index] = sumCT;
        firstIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return firstIICompletionTime;
    }

    public static int[] testFirstInsertRandomII() throws IOException {
        System.out.println("First Insert Random Iterative Improvement:");
        int[] firstIICompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        int sumCT = 0;
        float averageRelativePercDeviation = 0;

        for (File file : files) {
            readFile(String.valueOf(file));
            System.out.println(file);
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            for (int i = 0; i < numTest; i++) {

                int[] firstIIPermutation = firstImprovement(initializePermutation("--random"), "--insert");
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(firstIIPermutation));
                System.out.println("CT: " + completionTime);
                sumCT += completionTime;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                firstIICompletionTime[index] = completionTime;
                index++;
            }
        }
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        firstIICompletionTime[index] = sumCT;
        firstIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return firstIICompletionTime;
    }

    public static int[] testFirstExchangeSrzII() throws IOException {
        System.out.println("First Exchange SRZ Iterative Improvement:");
        int[] firstIICompletionTime = new int[(files.length * numTest) + 2]; // + 1 for the average relative percentage deviation and the sum of CT

        int index = 0;
        int bestValueIndex = 0;
        long totalComputationTIme = 0;
        float averageTotalComputationTime = 0;
        float averageRelativePercDeviation = 0;
        int previousNumJobs = numJobs;
        for (File file : files) {
            readFile(String.valueOf(file));
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            if (numJobs > previousNumJobs) { // to compute statistics for each size of instance.
                System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
                averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
                System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
                totalComputationTIme = 0;
                previousNumJobs = numJobs;
            }

            System.out.println(file);

            for (int i = 0; i < numTest; i++) {
                long startTime = System.currentTimeMillis();
                int[] bestIIPermutation = firstImprovement(initializePermutation("--srz"), "--exchange");
                long endTime = System.currentTimeMillis();
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(bestIIPermutation));
                System.out.println("Completion Time: " + completionTime);
                long timeDuration = endTime - startTime;
                System.out.println("Computation Time: " + timeDuration + " ms");
                totalComputationTIme += timeDuration;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                firstIICompletionTime[index] = completionTime;
                index++;
            }
        }
        System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
        averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
        System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        firstIICompletionTime[index] = (int) totalComputationTIme;
        firstIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return firstIICompletionTime;
    }


    public static int[] testFirstTransposeSrzII() throws IOException {
        System.out.println("First Transpose SRZ Iterative Improvement:");
        int[] firstIICompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        int sumCT = 0;
        float averageRelativePercDeviation = 0;

        for (File file : files) {
            readFile(String.valueOf(file));
            System.out.println(file);
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            int[] firstIIPermutation = firstImprovement(initializePermutation("--srz"), "--transpose");
            for (int i = 0; i < numTest; i++) {

                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(firstIIPermutation));
                System.out.println("CT: " + completionTime);
                sumCT += completionTime;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                firstIICompletionTime[index] = completionTime;
                index++;
            }
        }
        averageRelativePercDeviation = averageRelativePercDeviation / (1 * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        firstIICompletionTime[index] = sumCT;
        firstIICompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return firstIICompletionTime;
    }


    public static int[] testFirstInsertSrzII() throws IOException {
        System.out.println("First Insert SRZ Iterative Improvement:");
        int[] firstIICompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        int sumCT = 0;
        float averageRelativePercDeviation = 0;

        for (File file : files) {
            readFile(String.valueOf(file));
            System.out.println(file);
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            for (int i = 0; i < numTest; i++) {

                int[] firstIIPermutation = firstImprovement(initializePermutation("--srz"), "--insert");
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(firstIIPermutation));
                System.out.println("CT: " + completionTime);
                sumCT += completionTime;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                firstIICompletionTime[index] = completionTime;
                index++;
            }
        }
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        firstIICompletionTime[index] = sumCT;
        firstIICompletionTime[index + 1] = (int) averageRelativePercDeviation;

        return firstIICompletionTime;
    }

    // VND TEST:
    public static int[] testFirstOrderVND() throws IOException {
        System.out.println("First Order VND:");
        int[] VNDCompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        long totalComputationTIme = 0;
        float averageTotalComputationTime = 0;
        float averageRelativePercDeviation = 0;
        int previousNumJobs = numJobs;
        for (File file : files) {
            readFile(String.valueOf(file));
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            if (numJobs > previousNumJobs) { // to compute statistics for each size of instance.
                System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
                averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
                System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
                totalComputationTIme = 0;
                previousNumJobs = numJobs;
            }

            System.out.println(file);

            for (int i = 0; i < numTest; i++) {
                long startTime = System.currentTimeMillis();
                int[] VNDPermutation = variableNeighborhoodDescent("--one");
                long endTime = System.currentTimeMillis();
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(VNDPermutation));
                System.out.println("Completion Time: " + completionTime);
                long timeDuration = endTime - startTime;
                System.out.println("Computation Time: " + timeDuration + " ms");
                totalComputationTIme += timeDuration;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                VNDCompletionTime[index] = completionTime;
                index++;
            }
        }
        System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
        averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
        System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        VNDCompletionTime[index] = (int) totalComputationTIme;
        VNDCompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return VNDCompletionTime;
    }


    public static int[] testSecondOrderVND() throws IOException {
        int[] VNDCompletionTime = new int[files.length * numTest + 2];

        int index = 0;
        int bestValueIndex = 0;
        long totalComputationTIme = 0;
        float averageTotalComputationTime = 0;
        float averageRelativePercDeviation = 0;
        int previousNumJobs = numJobs;
        for (File file : files) {
            readFile(String.valueOf(file));
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            if (numJobs > previousNumJobs) { // to compute statistics for each size of instance.
                System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
                averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
                System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
                totalComputationTIme = 0;
                previousNumJobs = numJobs;
            }

            System.out.println(file);

            for (int i = 0; i < numTest; i++) {
                long startTime = System.currentTimeMillis();
                int[] VNDPermutation = variableNeighborhoodDescent("--two");
                long endTime = System.currentTimeMillis();
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(VNDPermutation));
                System.out.println("Completion Time: " + completionTime);
                long timeDuration = endTime - startTime;
                System.out.println("Computation Time: " + timeDuration + " ms");
                totalComputationTIme += timeDuration;
                averageRelativePercDeviation += computeRelativePercDeviation(completionTime, bestValue);
                VNDCompletionTime[index] = completionTime;
                index++;
            }
        }
        System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
        averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
        System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        VNDCompletionTime[index] = (int) totalComputationTIme;
        VNDCompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return VNDCompletionTime;
    }


    public static int[] testILS() throws IOException {
        int[] ILSCompletionTime = new int[files.length * numTest + 2];
        int index = 0;
        int bestValueIndex = 0;
        long totalComputationTIme = 0;
        float averageTotalComputationTime = 0;
        float averageRelativePercDeviation = 0;
        int previousNumJobs = numJobs;
        int maxIter;
        float AVRP50=0;
        float AVRP100=0;
        float AVRP200=0;

        for (File file : files) {
            readFile(String.valueOf(file));
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            if (numJobs==50) {
                maxIter = 122*500;
            }
            else if(numJobs ==100){
                AVRP50 = AVRP50 / (numTest * 10);
                System.out.println("AVRP 50: " + AVRP50);
                maxIter = 161*500;
            }
            else{
                AVRP100 = AVRP100 / (numTest * 10);
                System.out.println("AVRP 100: " + AVRP100);
                maxIter = 33208*100;
                numTest = 3; // CASE FOR 200 JOBS
            }

            if (numJobs > previousNumJobs) { // to compute statistics for each size of instance.
                System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
                averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
                System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
                totalComputationTIme = 0;
                previousNumJobs = numJobs;
            }

            System.out.println(file);

            for (int i = 0; i < numTest; i++) {
                long startTime = System.currentTimeMillis();
                int[] ILSPermuation = ILS_withHistory(maxIter, 3, 8, 8);
                long endTime = System.currentTimeMillis();
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(ILSPermuation));

                System.out.println("Completion Time: " + completionTime);
                long timeDuration = endTime - startTime;
                System.out.println("Computation Time: " + timeDuration + " ms");
                totalComputationTIme += timeDuration;
                float AVRP = computeRelativePercDeviation(completionTime, bestValue);
                averageRelativePercDeviation += AVRP ;
                ILSCompletionTime[index] = completionTime;
                index++;

                if (numJobs==50) {
                    AVRP50 += AVRP;
                }
                else if (numJobs ==100){
                    AVRP100 += AVRP;
                }
                else{
                    AVRP200 += AVRP;
                }
            }
        }


        AVRP200 = AVRP200 / (numTest * 10);
        System.out.println("AVRP 50: " + AVRP50 + " | AVRP 100: " + AVRP100 + " | AVRP 200: " + AVRP200);
        System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
        averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
        System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        ILSCompletionTime[index] = (int) totalComputationTIme;
        ILSCompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return ILSCompletionTime;

    }

    public static int[] testGLS() throws IOException {
        int[] GLSCompletionTime = new int[files.length * numTest + 2];
        int index = 0;
        int bestValueIndex = 0;
        long totalComputationTIme = 0;
        float averageTotalComputationTime = 0;
        float averageRelativePercDeviation = 0;
        int previousNumJobs = numJobs;
        int maxIter;
        float AVRP50=0;
        float AVRP100=0;
        float AVRP200=0;

        for (File file : files) {
            readFile(String.valueOf(file));
            int bestValue = bestKnonwTCT[bestValueIndex];
            bestValueIndex++;

            if (numJobs==50) {
                maxIter = 122*500;
            }
            else if(numJobs ==100){
                AVRP50 = AVRP50 / (numTest * 10);
                System.out.println("AVRP 50: " + AVRP50);
                maxIter = 161*500;
            }
            else{
                AVRP100 = AVRP100 / (numTest * 10);
                System.out.println("AVRP 100: " + AVRP100);
                maxIter = 33208*500;
                numTest = 3; // CASE FOR 200 JOBS
            }

            if (numJobs > previousNumJobs) { // to compute statistics for each size of instance.
                System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
                averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
                System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
                totalComputationTIme = 0;
                previousNumJobs = numJobs;
            }

            System.out.println(file);

            for (int i = 0; i < numTest; i++) {
                long startTime = System.currentTimeMillis();
                int[] GLSPermuation = guidedLocalSearch(maxIter);
                long endTime = System.currentTimeMillis();
                int completionTime = computeTotalCompletionTime(computeCompletionTimeMatrix(GLSPermuation));

                System.out.println("Completion Time: " + completionTime);
                long timeDuration = endTime - startTime;
                System.out.println("Computation Time: " + timeDuration + " ms");
                totalComputationTIme += timeDuration;
                float AVRP = computeRelativePercDeviation(completionTime, bestValue);
                averageRelativePercDeviation += AVRP ;
                GLSCompletionTime[index] = completionTime;
                index++;

                if (numJobs==50) {
                    AVRP50 += AVRP;
                }
                else if (numJobs ==100){
                    AVRP100 += AVRP;
                }
                else{
                    AVRP200 += AVRP;
                }
            }
        }


        AVRP200 = AVRP200 / (numTest * 10);
        System.out.println("AVRP 50: " + AVRP50 + " | AVRP 100: " + AVRP100 + " | AVRP 200: " + AVRP200);
        System.out.println("Total computation time for instance size of " + previousNumJobs + " jobs: " + totalComputationTIme + "ms");
        averageTotalComputationTime = (float) totalComputationTIme / (numTest * 10);
        System.out.println("Average computation time for instance size of " + previousNumJobs + " jobs: " + averageTotalComputationTime + "ms");
        averageRelativePercDeviation = averageRelativePercDeviation / (numTest * files.length);
        avgRelativePercDeviation = averageRelativePercDeviation;
        GLSCompletionTime[index] = (int) totalComputationTIme;
        GLSCompletionTime[index + 1] = (int) averageRelativePercDeviation;
        return GLSCompletionTime;

    }

    public static float computeRelativePercDeviation(int completionTime, int bestKnown) {
        return 100 * ((float) (completionTime - bestKnown) / bestKnown);
    }
}