package com.resourceallocation.Optimization;

public class CostAdjustment {
    public void adjustCostsDynamically(int[][] costMatrix, int urgencyLevel) {
        if (urgencyLevel < 0 || urgencyLevel > 10) {
            throw new IllegalArgumentException("Urgency level must be between 0 and 10.");
        }

        double scalingFactor = 1 + (urgencyLevel / 10.0);
        
        for (int i = 0; i < costMatrix.length; i++) {
            for (int j = 0; j < costMatrix[i].length; j++) {
                costMatrix[i][j] *= scalingFactor;
            }
        }
    }
}


