package com.resourceallocation;

import com.resourceallocation.algorithm.AuctionAlgorithm;
import com.resourceallocation.algorithm.HungarianAlgorithm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ResourceAllocationTest {
    @Test
    public void testExcessHungarianAlgorithm() {

        HungarianAlgorithm algorithm = new HungarianAlgorithm();

        int[][] costMatrix = {
                {90, 75, 85},
                {35, 85, 55},
                {125, 95, 90},
                {45, 110, 95} // Excess row
        };
        double[] weights = {1.0};

        int[] result = algorithm.assignResources(costMatrix, weights);
        System.out.println("Hungarian Assignments: " + Arrays.toString(result));



    }
    @Test
    public void testExcessAuctionAlgorithm() {
        AuctionAlgorithm algorithm = new AuctionAlgorithm();
        int[][] costMatrix = {
                {90, 75, 85},
                {35, 85, 55},
                {125, 95, 90},
                {45, 110, 95}, // Excess row
        };
        double[] weights = {1.0};
        int[] result = algorithm.solveLargeDataset(costMatrix, weights);

        System.out.println("Excess Auction Assignments: " + Arrays.toString(result));
    }

    @Test
    public void testHungarianAlgorithm() {
        
        int[][] costMatrix = {
                {4, 2, 3},
                {2, 3, 1},
                {3, 4, 2}
            };
            int[][] availabilityMatrix = {
                {1, 1, 0},
                {1, 0, 1},
                {1, 1, 1}
            };
            int[][] reliabilityMatrix = {
                {3, 5, 1},
                {4, 1, 6},
                {5, 5, 5}
            };
        
            double[] weights = {1.0, 2.0, 0.5}; // Weights for cost, availability, and reliability respectively
        
            HungarianAlgorithm algorithm = new HungarianAlgorithm();
            int[] assignment = algorithm.assignResources(costMatrix, weights, availabilityMatrix, reliabilityMatrix);
        
            System.out.println("Excess Auction Assignment: " + Arrays.toString(assignment));
    }

    @Test
    public void testAuctionAlgorithm(){

        int[][] costMatrix = {
                {10, 19, 8, 15},
                {10, 18, 7, 17},
                {13, 16, 9, 14},
                {12, 19, 8, 18}
        };

        int[][] availabilityMatrix = {
                {1, 1, 0, 1},
                {1, 0, 1, 1},
                {1, 1, 1, 0},
                {0, 1, 1, 1}
        };

        int[][] reliabilityMatrix = {
                {3, 5, 2, 4},
                {4, 1, 3, 5},
                {2, 4, 5, 3},
                {5, 3, 4, 2}
        };

        double[] weights = {1.0, 2.0, 0.5}; // Weights for cost, availability, and reliability respectively

        AuctionAlgorithm auctionAlgorithm = new AuctionAlgorithm();
        int[] assignment = auctionAlgorithm.solveLargeDataset(costMatrix, weights, availabilityMatrix, reliabilityMatrix);

        System.out.println("Assignment Auction: " + Arrays.toString(assignment));

    }

    @Test
    public void dataForwarding(){

        int[][] costMatrix = {
                {10, 19, 8, 15},
                {10, 18, 7, 17},
                {13, 16, 9, 14},
                {12, 19, 8, 18}
        };

        int[][] availabilityMatrix = {
                {1, 1, 0, 1},
                {1, 0, 1, 1},
                {1, 1, 1, 0},
                {0, 1, 1, 1}
        };

        int[][] reliabilityMatrix = {
                {3, 5, 2, 4},
                {4, 1, 3, 5},
                {2, 4, 5, 3},
                {5, 3, 4, 2}
        };

        if(costMatrix.length > 100)
        {
                double[] weights = {1.0, 2.0, 0.5}; // Weights for cost, availability, and reliability respectively

                AuctionAlgorithm auctionAlgorithm = new AuctionAlgorithm();
                int[] assignment = auctionAlgorithm.solveLargeDataset(costMatrix, weights, availabilityMatrix, reliabilityMatrix);
        }

        else{
             
                double[] weights = {1.0, 2.0, 0.5}; // Weights for cost, availability, and reliability respectively
                HungarianAlgorithm algorithm = new HungarianAlgorithm();
                int[] assignment = algorithm.assignResources(costMatrix, weights, availabilityMatrix, reliabilityMatrix);

        }

    }
}



