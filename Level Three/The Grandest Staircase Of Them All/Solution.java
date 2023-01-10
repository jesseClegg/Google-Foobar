public class Solution {
    /*
     * -This class solves the grandest-staircase-of-them-all challenge through dynamic programming.
     * -Alternatively, this same problem could be  calculated as the number of distinct partitions on n according to partition theory.
     * -Using a recursive memoization approach, there are no redundant sub-problem calculations, thus dramatically increasing computational efficiency
     * -Using array indices as lookup keys allows for near immediate lookup time.
     * -This does increase the space requirement for this implementation,
     *   but given the considerable amount of lookups performed in the algorithm, and the relatively low cost for memory to store integers,
     *    the small compromise on space to yield a large increase in computational efficiency seems to be the optimal choice.
     *
     */
    public static int solution(int n){
        int memoSize=n+1;
        int [][] memo = new int[memoSize][memoSize];

        //set memo entries to any number less than 0 to represent result not yet calculated
        int notYetCalculatedValue=-1;
        for(int i=0; i< memo.length; i++){
            for(int j=0; j<memo[0].length; j++){
                memo[i][j]=notYetCalculatedValue;
            }
        }
        return findStairCombos(n, n, memo);
    }
    public static int findStairCombos(int previousStep, int bricksRemaining, int [][] memo){

        //Base Cases:

        //this is a successful combination (i.e. correct amount of bricks)
        if(bricksRemaining==0 && previousStep>=1){
            return 1;
        }
        //we did not have the bricks required to build this potential step (i.e. not enough bricks)
        if(bricksRemaining<0){
            return 0;
        }
        //we have not used all bricks, and the previous step height was 1 (i.e. too many bricks)
        if(bricksRemaining>0&&previousStep==1){
            return 0;
        }

        //if we have calculated this sub-staircase before, return that value
        if(memo[previousStep][bricksRemaining]>=0){
            return memo[previousStep][bricksRemaining];
        }

        //max height of next step must be at least one less than previous step
        int nextStepMax=previousStep-1;
        int totalWays=0;

        //Try all potential next step candidates, summing all successful combinations and sub-combinations
        for(int potentialNextStep=1; potentialNextStep<=nextStepMax; potentialNextStep++){
            totalWays+= findStairCombos(potentialNextStep,bricksRemaining-potentialNextStep, memo);
        }

        //store result in memo for future repeat sub-problems
        memo[previousStep][bricksRemaining]=totalWays;
        //printMemo(memo); //uncomment this line and method for a step by step visualization of sub problem results being calculated
        return totalWays;
    }


//    public static void printMemo(int [][] memo){
//
//        //print X axis labels
//        System.out.print("         ");
//        for(int k=0; k<memo[0].length; k++){
//            if(k>99){
//                System.out.print(k+" |  ");
//            }else if(k<10){
//                System.out.print(k+"  |   ");
//            }else{
//                System.out.print(k+"  |  ");
//            }
//
//        }
//        System.out.println();
//
//        //main loop
//        for(int i=0; i< memo.length; i++){
//
//            //print Y axis labels
//            if(i>99){
//                System.out.print(i+" |   ");
//            }else if(i<10){
//                System.out.print(" "+i+"  |   ");
//            }else{
//                System.out.print(" "+i+" |   ");
//            }
//            //print entries of the matrix
//            for(int j=0; j<memo[0].length; j++){
//                if(memo[i][j]==-1){
//                    System.out.print(" #     ");
//                }else{
//                    if(memo[i][j]>99){
//                        System.out.print("["+memo[i][j]+"]  ");
//                    }else if(memo[i][j]>9){
//                        System.out.print("["+memo[i][j]+"]   ");
//                    }else{
//                        System.out.print("["+memo[i][j]+"]    ");
//                    }
//                }
//
//            }
//            System.out.println();
//        }
//        System.out.println();
//        System.out.println();
//    }
}
