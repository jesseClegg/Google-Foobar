public class Solution {

    public static String solution(long x, long y) {

        //calculate how which diagonal row of floyd trianlge we are in
        long diagonalRow=x+(y-1);

        //find max value for that diagonal row
        long maxValueInRow=(diagonalRow*(diagonalRow+1))/2;

        //determine current value as a relation to the max value
        long valueFound=maxValueInRow-(y-1);

        String answer=valueFound+"";
        return answer;
    }
}
