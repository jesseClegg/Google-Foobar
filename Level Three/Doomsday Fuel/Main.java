public class Main {

    public static void main(String[] args) {

        int[][] test1 = {
                {0, 2, 1, 0, 0},
                {0, 0, 0, 3, 4},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        //OUTPUT=[7, 6, 8, 21]

        int[][] test2 = {
                {0, 1, 0, 0, 0, 1},  // s0, the initial state, goes to s1 and s5 with equal probability
                {4, 0, 0, 3, 2, 0},  // s1 can become s0, s3, or s4, but with different probabilities
                {0, 0, 0, 0, 0, 0},  // s2 is terminal, and unreachable (never observed in practice)
                {0, 0, 0, 0, 0, 0},  // s3 is terminal
                {0, 0, 0, 0, 0, 0},  // s4 is terminal
                {0, 0, 0, 0, 0, 0},  // s5 is terminal
        };
        //OUTPUT=[0, 3, 2, 9, 14]

        int[] result1 = Solution.solution(test1);
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i] + " ");
        }

        System.out.println();
        System.out.println("---------------");

        int[] result2 = Solution.solution(test2);
        for (int i = 0; i < result2.length; i++) {
            System.out.print(result2[i] + " ");
        }
    }
}
