package com.resourceallocation;

import com.resourceallocation.Optimization.CostAdjustment;
import com.resourceallocation.algorithm.AuctionAlgorithm;
import com.resourceallocation.algorithm.HungarianAlgorithm;

/*
 * This class deals with allocations of resources to patients
 * Parameters
 * @param costMatrix
 * @param algorithmToUse
 */
public class ResourceAllocator {

    int[][] taskResources;
    int algorithm;
    HungarianAlgorithm hungarianAlgorithm;
    AuctionAlgorithm auctionAlgorithm;
    int[] assignment;
    Exception exception = new Exception();
    CostAdjustment costAdjuster = new CostAdjustment();
    
    public ResourceAllocator(int[][] costs, int algorithmToUse)
    {
            this.taskResources = costs;
            this.algorithm = algorithmToUse;
            this.hungarianAlgorithm = new HungarianAlgorithm();
            this.auctionAlgorithm = new AuctionAlgorithm();
    }

    public int[] assign(double[] weights, int[][]... factorMatrices)
    {
        //Creating a placeholder for the assignments
        int[] assignmentInter = {};

        //If the algorithm is 1, use hungarian
        if(this.algorithm == 1)
        {
            //Incase their are more than 100 columns or 100 rows, divert to auction algorithm
            if(taskResources.length > 100 || taskResources[0].length > 100){
                this.algorithm = 3;
                System.out.println("Data too large to use hungarian algorithm");
            }else{
                assignmentInter = this.hungarianAlgorithm.assignResources(taskResources, weights, factorMatrices);
            }
            
        }

        //If the algorithm chosen is 2, use auction
        if(this.algorithm == 2){
            assignmentInter = this.auctionAlgorithm.solveLargeDataset(taskResources, weights, factorMatrices);
        }

        if(this.algorithm == 3){
            assignmentInter = this.auctionAlgorithm.solveLargeDataset(taskResources, weights, factorMatrices);
            this.algorithm = 1;
        }

        //Returns the allocation in a 1D array.
        //It also stores the allocation incase it is needed elsewhere
        this.assignment = assignmentInter;
        return this.assignment;
    }

    public boolean adjustCosts(int[] tasksToAdjust, int urgencyLevel){

        boolean exceptionPresent = false;
        int[][] costResourceMatrix = new int[this.taskResources.length][tasksToAdjust.length]; 

        for(int i = 0; i < tasksToAdjust.length; i++)
        {
            if(tasksToAdjust[i] > this.taskResources.length)
            {
                return true;
            }
        }

        for(int a = 0; a < this.taskResources.length; a++)
        {
            for(int b = 0; b < tasksToAdjust.length; b++)
            {
                costResourceMatrix[a][b] = tasksToAdjust[b];
            }
        }

        try{
            costAdjuster.adjustCostsDynamically(costResourceMatrix, urgencyLevel);

            for(int a = 0; a < tasksToAdjust.length; a++)
            {
                for(int b = 0; b < this.taskResources.length; b++)
                {
                    this.taskResources[b][tasksToAdjust[a]] = costResourceMatrix[b][a];
                }
            }
        }
        catch(IllegalArgumentException ex){
            exception = ex;
            exceptionPresent = true;
        }

        return exceptionPresent;

    }


}
