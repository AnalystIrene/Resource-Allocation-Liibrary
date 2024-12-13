package com.resourceallocation.algorithm;

import java.util.Arrays;

public class AuctionAlgorithm {
    public int[] solveLargeDataset(int[][] costMatrix, double[] weights, int[][]... factorMatrices) {
        int rows = costMatrix.length;
        int cols = costMatrix[0].length;

        // Extract excess rows if they exist
        int[][] excessMatrix = null;
        if (rows > cols) {
            excessMatrix = extractExcessRows(costMatrix, cols);
            rows = cols; // Update rows to match columns after extraction
        }

        int[] assignment = new int[rows];
        double[] prices = new double[cols];
        double epsilon = 1e-2; // Small value to adjust prices

        Arrays.fill(assignment, -1); // -1 indicates unassigned tasks

        // Step 1: Calculate the composite scoring matrix
        int[][] scoringMatrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Start with the costMatrix and add weighted contributions from factorMatrices
                double compositeScore = costMatrix[i][j] * weights[0];
                for (int k = 0; k < factorMatrices.length; k++) {
                    compositeScore += factorMatrices[k][i][j] * weights[k + 1];
                }
                scoringMatrix[i][j] = (int) compositeScore;
            }
        }

        boolean allAssigned = false;
        while (!allAssigned) {
            allAssigned = true;

            for (int task = 0; task < rows; task++) {
                if (assignment[task] == -1) { // Unassigned task
                    allAssigned = false;

                    // Find the best and second-best resources for this task
                    double maxProfit = Double.NEGATIVE_INFINITY;
                    double secondMaxProfit = Double.NEGATIVE_INFINITY;
                    int bestResource = -1;

                    for (int resource = 0; resource < cols; resource++) {
                        double profit = scoringMatrix[task][resource] - prices[resource];

                        if (profit > maxProfit) {
                            secondMaxProfit = maxProfit;
                            maxProfit = profit;
                            bestResource = resource;
                        } else if (profit > secondMaxProfit) {
                            secondMaxProfit = profit;
                        }
                    }

                    // Update the price of the best resource
                    prices[bestResource] += maxProfit - secondMaxProfit + epsilon;

                    // Reassign the resource to the current task
                    for (int i = 0; i < rows; i++) {
                        if (assignment[i] == bestResource) {
                            assignment[i] = -1; // Unassign the previous task
                            break;
                        }
                    }
                    assignment[task] = bestResource;
                }
            }
        }

        // Print the excess matrix if it exists
        if (excessMatrix != null) {
            System.out.println("Excess Matrix for Auction:");
            for (int[] row : excessMatrix) {
                System.out.println(Arrays.toString(row));
            }
        }

        return assignment;
    }

    // Extracts excess rows and returns them as a separate matrix
    private int[][] extractExcessRows(int[][] costMatrix, int cols) {
        int rows = costMatrix.length;
        int excessRows = rows - cols;

        int[][] excessMatrix = new int[excessRows][cols];
        for (int i = 0; i < excessRows; i++) {
            System.arraycopy(costMatrix[cols + i], 0, excessMatrix[i], 0, cols);
        }

        return excessMatrix;
    }
}
