
import java.math.BigInteger;
public class Solution {
    public static String solution(String x, String y) {
        String impossible = "impossible";

        BigInteger bigX = new BigInteger(x);
        BigInteger bigY = new BigInteger(y);

        BigInteger hardZero = new BigInteger("0");
        BigInteger hardOne = new BigInteger("1");

        BigInteger difference= new BigInteger("0");
        BigInteger little = new BigInteger("0");
        BigInteger big = new BigInteger("0");

        BigInteger totalGenerations = new BigInteger("0");
        BigInteger bigCycles = new BigInteger("0");
        BigInteger subCycles = new BigInteger("0");
        BigInteger standardCycles = new BigInteger("0");

        BigInteger tempSwap = new BigInteger("0");
        BigInteger tempDiv = new BigInteger("0");

        if(bigX.compareTo(hardOne)==0 && bigY.compareTo(hardOne)==0){//check if set is (1,1), so no cycles needed
            return "0";
        }
        if((bigX.compareTo(hardZero)==0) || (bigY.compareTo(hardZero)==0)){//check if either number==0, impossible
            return impossible;
        }
        int comparedValue = bigX.compareTo(bigY);
        if(comparedValue==0){//check if same number, impossible
            return impossible;
        }
        if(comparedValue==-1){//establish big and little, also handled in loop but removes confusion
            little = bigX;
            big = bigY;
        } else if(comparedValue==1){
            little = bigY;
            big = bigX;
        }
        while(true){
            if(little.compareTo(big)==1){
                tempSwap=little;
                little=big;
                big=tempSwap;
            }
            difference=big.subtract(little);
            if(difference.compareTo(little)==1){
                tempDiv=difference.divide(little);
                bigCycles=bigCycles.add(tempDiv);
                tempDiv=tempDiv.multiply(little);
                big=big.subtract(tempDiv);
                difference=big.subtract(little);
            }
            if(little.compareTo(hardOne)==0){
                subCycles=big.subtract(hardOne);
                break;
            }
            big=little;
            little=difference;
            standardCycles=standardCycles.add(hardOne);
            if(little.compareTo(hardZero)<=0){
                return impossible;
            }
        }
        totalGenerations=standardCycles.add(subCycles.add(bigCycles));
        return totalGenerations.toString();
    }
}