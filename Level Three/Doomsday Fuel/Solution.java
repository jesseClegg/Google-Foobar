import java.util.ArrayList;

/*
 *This class produces the answer to an absorbing markov chain problem
 *
 * solution() method converts the int [][] m Matrix with r transient states and s absorbing states into the sub-matrices Q, R, I
 * Q: rxr matrix where the rows and columns represent the probability of moving from a Transient state to another Transient
 * R: sxr matrix where the rows and columns represent probabilities of moving from a Transient state to a Terminal/Absorbing state
 * I: sxs Identity matrix
 *
 * final answer probabilities are obtained from the first row of matrix FR where:
 * F: the inverse of I-Q
 * and R, I, and Q are the same as above
 *
 * A Fraction class is used in the formation of Matrices to maintain precision of numerical values without the heavy use of bigDecimal()
 *
 */
public class Solution {


    public static int[] solution(int[][] m) {

        if (isTerminalState(m[0])) {
            int solution[] = new int[m[0].length + 1];
            solution[0] = 1;
            solution[solution.length - 1] = 1;
            for (int i = 1; i < solution.length - 1; i++) {
                solution[i] = 0;
            }
            return solution;
        }

        ArrayList<Integer> terminalList = new ArrayList<Integer>();
        ArrayList<Integer> transientList = new ArrayList<Integer>();
        ArrayList<Integer> rowDenominators = new ArrayList<Integer>();

        for (int i = 0; i < m.length; i++) {
            int currentRowDenominator = 0;
            for (int j = 0; j < m[0].length; j++) {
                currentRowDenominator = currentRowDenominator + m[i][j];
            }
            if (currentRowDenominator == 0) {
                terminalList.add(i);
            } else {
                transientList.add(i);
                rowDenominators.add(currentRowDenominator);
            }
        }

        Fraction[][] Q = createMatrixQ(transientList, rowDenominators, m);

        Fraction[][] R = createMatrixR(m, transientList, terminalList, rowDenominators);

        Fraction[][] I = generateIdentityMatrix(transientList.size());

        Fraction[][] F = subtractTwoSquareMatrices(I, Q);

        Fraction[][] inverse = invertMatrix(F);

        Fraction[][] FR = multiplyMatrices(inverse, R);

        return getFinalAnswer(FR[0]);
    }

    public static int[] getFinalAnswer(Fraction[] row) {
        long[] allDenominators = new long[row.length];

        for (int i = 0; i < row.length; i++) {
            allDenominators[i] = row[i].getDenominator();
        }

        long lcm = lcm(allDenominators);

        int[] finalAnswer = new int[row.length + 1];

        for (int i = 0; i < row.length; i++) {
            finalAnswer[i] = (int) ((row[i].getNumerator()) * (lcm / row[i].getDenominator()));
        }

        finalAnswer[finalAnswer.length - 1] = (int) lcm;

        return finalAnswer;
    }

    public static Fraction[][] invertMatrix(Fraction[][] toInvert) {
        int n = toInvert.length;
        Fraction X[][] = new Fraction[n][n];
        Fraction B[][] = new Fraction[n][n];
        int index[] = new int[n];

        //must fill with zeros to avoid null entries
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = new Fraction(0, 0);
            }
        }

        //setting up the identity matrix
        for (int i = 0; i < n; ++i) {
            B[i][i] = new Fraction(1, 1);
        }

        //get A into upper triangular
        gaussian(toInvert, index);

        for (int i = 0; i < n - 1; ++i) {
            for (int j = i + 1; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    B[index[j]][k] = subTractTwoFractions(B[index[j]][k], multiplyTwoFractions(toInvert[index[j]][i], B[index[i]][k]));
                }
            }
        }

        // Perform backward substitutions
        for (int i = 0; i < n; ++i) {
            X[n - 1][i] = divideTwoFractions(B[index[n - 1]][i], toInvert[index[n - 1]][n - 1]);
            for (int j = n - 2; j >= 0; --j) {
                X[j][i] = B[index[j]][i];
                for (int k = j + 1; k < n; ++k) {
                    X[j][i] = subTractTwoFractions(X[j][i], multiplyTwoFractions(toInvert[index[j]][k], X[k][i]));
                }
                X[j][i] = divideTwoFractions(X[j][i], toInvert[index[j]][j]);
            }
        }
        return X;
    }

    public static void gaussian(Fraction[][] toInvert, int index[]) {
        int n = index.length;
        Fraction C[] = new Fraction[n];

        // Initialize the index
        for (int i = 0; i < n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i = 0; i < n; ++i) {
            Fraction C1 = new Fraction(0, 0);
            for (int j = 0; j < n; ++j) {
                Fraction C0 = toInvert[i][j];
                double c0 = C0.getDecimalValue();
                c0 = Math.abs(c0);
                if (c0 > C1.getDecimalValue()) {
                    C1 = C0;
                }
            }
            C[i] = C1;
        }

        //Find pivots in each column
        int k = 0;

        for (int j = 0; j < n - 1; ++j) {
            Fraction PI1 = new Fraction(0, 0);
            double pi1 = 0;
            for (int i = j; i < n; ++i) {
                Fraction PI0 = toInvert[i][j];
                double pi0 = PI0.getDecimalValue();
                pi0 = Math.abs(pi0);
                double ctemp = C[index[i]].getDecimalValue();
                pi0 /= ctemp;
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            //swap rows based on pivot order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;

            for (int i = j + 1; i < n; ++i) {
                Fraction PJ = divideTwoFractions(toInvert[index[i]][j], toInvert[index[j]][j]);
                toInvert[index[i]][j] = PJ;
                for (int l = j + 1; l < n; ++l) {
                    toInvert[index[i]][l] = subTractTwoFractions(toInvert[index[i]][l], multiplyTwoFractions(PJ, toInvert[index[j]][l]));
                }
            }
        }
    }

    public static Fraction[][] subtractTwoSquareMatrices(Fraction[][] identity, Fraction[][] q) {
        Fraction[][] fundamental = new Fraction[q.length][q[0].length];
        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < q[0].length; j++) {
                fundamental[i][j] = subTractTwoFractions(identity[i][j], q[i][j]);
            }
        }
        return fundamental;
    }

    public static Fraction[][] generateIdentityMatrix(int n) {
        Fraction[][] I = new Fraction[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    I[i][j] = new Fraction(1, 1);
                } else {
                    I[i][j] = new Fraction(0, 0);
                }
            }
        }
        return I;
    }

    public static Fraction[][] createMatrixQ(ArrayList<Integer> transientList, ArrayList<Integer> rowDenominators, int[][] m) {
        Fraction[][] Q = new Fraction[transientList.size()][transientList.size()];
        for (int i = 0; i < transientList.size(); i++) {
            for (int j = 0; j < transientList.size(); j++) {
                Q[i][j] = new Fraction(m[transientList.get(i)][transientList.get(j)], rowDenominators.get(i));
            }
        }
        return Q;
    }

    public static Fraction[][] createMatrixR(int[][] m, ArrayList<Integer> transientList, ArrayList<Integer> TerminalList, ArrayList<Integer> rowDenominators) {
        Fraction[][] R = new Fraction[transientList.size()][TerminalList.size()];
        for (int i = 0; i < transientList.size(); i++) {
            for (int j = 0; j < TerminalList.size(); j++) {
                R[i][j] = new Fraction(m[transientList.get(i)][TerminalList.get(j)], rowDenominators.get(i));
            }
        }
        return R;
    }

    public static Fraction[][] multiplyMatrices(Fraction[][] A, Fraction[][] B) {
        Fraction[][] C = new Fraction[A.length][B[0].length];

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                C[i][j] = new Fraction(0, 0);
            }
        }

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    C[i][j] = addTwoFractions(C[i][j], multiplyTwoFractions(A[i][k], B[k][j]));
                }
            }
        }

        return C;
    }

    public static long lcm(long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) {
            result = lcm(result, input[i]);
        }
        return result;
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    private static int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static boolean isTerminalState(int[] arrayToCheck) {
        for (int i = 0; i < arrayToCheck.length; i++) {
            if (arrayToCheck[i] != 0) {
                return false;
            }
        }
        return true;
    }

//    public static void printFractionMatrix(Fraction [][] mat){
//    System.out.println("------------------");
//    for (int i=0; i<mat.length; i++){
//        for(int j=0; j<mat[i].length; j++){
//            System.out.print(mat[i][j]+" ");
//        }
//        System.out.println( );
//    }
//    System.out.println("------------------");
//    System.out.println( );
//}

    public static Fraction asFraction(long a, long b) {
        long gcd = gcd(a, b);
        Fraction simplified = new Fraction((a / gcd), (b / gcd));
        return simplified;
    }

    public static Fraction simplifyFraction(Fraction fraction) {
        long denominator = fraction.getDenominator();
        long oldNumerator = fraction.getNumerator();
        if (denominator == 0 || oldNumerator == 0) {
            return new Fraction(0, 1);
        }
        return asFraction(oldNumerator, denominator);
    }

    public static Fraction multiplyTwoFractions(Fraction f1, Fraction f2) {
        long newNumerator = f1.getNumerator() * f2.getNumerator();
        long newDenominator = f1.getDenominator() * f2.getDenominator();

        return simplifyFraction(new Fraction(newNumerator, newDenominator));
    }

    public static Fraction addTwoFractions(Fraction f1, Fraction f2) {
        if (f1.getNumerator() == 0) {
            return f2;
        }
        if (f2.getNumerator() == 0) {
            return f1;
        }
        long newNumeratorF1 = f1.getNumerator() * f2.getDenominator();
        long newNumeratorf2 = f2.getNumerator() * f1.getDenominator();
        long newDenominator = f1.getDenominator() * f2.getDenominator();

        newNumeratorF1 = newNumeratorF1 + newNumeratorf2;
        Fraction result = new Fraction(newNumeratorF1, newDenominator);
        return simplifyFraction(result);
    }

    public static Fraction subTractTwoFractions(Fraction f1, Fraction f2) {
        if (f2.getDenominator() == 0 || f2.getNumerator() == 0) {
            return f1;
        }
        if (f1.getDenominator() == 0 || f1.getNumerator() == 0) {
            return new Fraction(f2.getNumerator() * (-1), f2.getDenominator());
        }
        long newNumeratorF1 = f1.getNumerator() * f2.getDenominator();
        long newNumeratorf2 = f2.getNumerator() * f1.getDenominator();
        long newDenominator = f1.getDenominator() * f2.getDenominator();

        newNumeratorF1 = newNumeratorF1 - newNumeratorf2;
        Fraction result = new Fraction(newNumeratorF1, newDenominator);
        return simplifyFraction(result);
    }

    public static Fraction divideTwoFractions(Fraction f1, Fraction f2) {
        f2 = new Fraction(f2.getDenominator(), f2.getNumerator());
        Fraction result = multiplyTwoFractions(f1, f2);
        return simplifyFraction(result);
    }

    public static class Fraction {
        private long numerator;
        private long denominator;

        Fraction(long numerator, long denominator) {
            if (denominator < 0) {
                this.numerator = Math.abs(numerator) * (-1);
                this.denominator = Math.abs(denominator);
            } else {
                this.numerator = numerator;
                this.denominator = denominator;
            }
        }

        @Override
        public String toString() {
            return "[" + numerator +
                    "/" + denominator +
                    ']';
        }

        public double getDecimalValue() {
            if (this.numerator == 0 || this.denominator == 0) {
                return 0.0;
            } else {
                return (double) this.numerator / (double) this.denominator;
            }
        }

        public long getNumerator() {
            return numerator;
        }

        public void setNumerator(long numerator) {
            this.numerator = numerator;
        }

        public long getDenominator() {
            return denominator;
        }

        public void setDenominator(long denominator) {
            this.denominator = denominator;
        }

    }
}
