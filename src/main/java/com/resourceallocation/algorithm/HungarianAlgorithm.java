package com.resourceallocation.algorithm;

import java.util.Arrays;

public class HungarianAlgorithm {

    public int[] assignResources(int[][] costMatrix, double[] weights, int[][]... factorMatrices) {
        int rows = costMatrix.length;
        int cols = costMatrix[0].length;

        int[][] excessMatrix = null; // To store excess rows

        // Handle excess rows by redistributing their values into columns
        if (rows > cols) {
            excessMatrix = extractExcessRows(costMatrix, cols);
            costMatrix = redistributeExcessRows(costMatrix, cols);
            rows = costMatrix.length;
        }

        int n = Math.max(rows, cols); // Determine square matrix size
        int[][] paddedMatrix = new int[n][n];

        // Step 1: Create a padded square matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i < rows && j < cols) {
                    paddedMatrix[i][j] = costMatrix[i][j];
                } else {
                    paddedMatrix[i][j] = Integer.MAX_VALUE; // Pad with large values
                }
            }
        }

        // Apply weights and factor matrices to construct the scoring matrix
        int[][] scoringMatrix = new int[n][n];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double compositeScore = paddedMatrix[i][j] * weights[0];
                for (int k = 0; k < factorMatrices.length; k++) {
                    compositeScore += factorMatrices[k][i][j] * weights[k + 1];
                }
                scoringMatrix[i][j] = (int) compositeScore;
            }
        }

        // Step 2: Subtract the row minimum
        for (int i = 0; i < n; i++) {
            int rowMin = Arrays.stream(scoringMatrix[i]).min().orElse(0);
            for (int j = 0; j < n; j++) {
                scoringMatrix[i][j] -= rowMin;
            }
        }

        // Step 3: Subtract the column minimum
        for (int j = 0; j < n; j++) {
            int colMin = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (scoringMatrix[i][j] < colMin) {
                    colMin = scoringMatrix[i][j];
                }
            }
            for (int i = 0; i < n; i++) {
                scoringMatrix[i][j] -= colMin;
            }
        }

        // Step 4: Perform assignment
        int[] assignment = new int[rows];
        Arrays.fill(assignment, -1);
        boolean[] rowCover = new boolean[n];
        boolean[] colCover = new boolean[n];
        boolean done = false;

        while (!done) {
            done = true;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (scoringMatrix[i][j] == 0 && !rowCover[i] && !colCover[j]) {
                        if (i < rows && j < cols) {
                            assignment[i] = j; // Assign valid rows to columns
                        }
                        rowCover[i] = true;
                        colCover[j] = true;
                        done = false;
                        break;
                    }
                }
            }
        }

        // Print the excess matrix if it exists
        if (excessMatrix != null) {
            System.out.println("Excess Matrix for Hungarian:");
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

    // Redistributes excess row values to columns proportionally
    private int[][] redistributeExcessRows(int[][] costMatrix, int cols) {
        int rows = costMatrix.length;
        int[][] redistributedMatrix = new int[cols][cols];

        // Copy existing values to redistributed matrix
        for (int i = 0; i < cols; i++) {
            System.arraycopy(costMatrix[i], 0, redistributedMatrix[i], 0, cols);
        }

        // Add excess row values to corresponding columns
        for (int i = cols; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                redistributedMatrix[j][j] += costMatrix[i][j];
            }
        }

        return redistributedMatrix;
    }
}
