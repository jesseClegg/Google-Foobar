public class Solution {

    public static int[] solution(int[] pegs) {
        int[] answer = {-1, -1};
        int a;
        int b;
        int upperBound = (pegs[1] - pegs[0]) - 1;
        int lowerBound = 2;
        int distance = 0;
        int firstGear = 0;
        int nextGear = 0;
        int lastGear = 0;
        int thisGear = 0;


        if ((pegs.length) == 2) {//Case of only 2 pegs
            distance = (pegs[1] - pegs[0]);
            if (distance % 3 == 0) {
                b = (distance / 3);
                a = 2 * b;
            } else {
                float d = distance / 3;
                answer[0] = firstGear;
                answer[1] = -1;
                return answer;
            }
        }

        for (int j = lowerBound; j <= upperBound; j++) {//Case of more than 2 pegs
            firstGear = j;
            thisGear = firstGear;
            for (int i = 0; i < pegs.length; i++) {
                if (i == pegs.length - 1) { //check if last peg
                    lastGear = thisGear;
                    if ((lastGear * 2) == firstGear) {
                        answer[0] = firstGear;
                        answer[1] = 1;
                        return answer;
                    }
                    if (lastGear * 2 == firstGear + 1) {
                        answer[0] = (firstGear * 3) + 1;
                        answer[1] = 3;
                        return answer;
                    }
                    if (lastGear * 2 == firstGear + 2) {
                        answer[0] = (firstGear * 3) + 2;
                        answer[1] = 3;
                        return answer;

                    } else {
                        break;
                    }
                }
                distance = pegs[i + 1] - pegs[i];

                if (distance < 2) {//check if distance =1
                    answer[0] = -1;
                    answer[1] = -1;
                    return answer;
                }
                nextGear = distance - thisGear;

                if (nextGear < 1) {//if other radius too large
                    break;
                }
                thisGear = nextGear;
            }
        }
        return answer;
    }

}

