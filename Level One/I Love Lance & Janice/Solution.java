public class Solution {

    //Decrypts the message, method name is dictated by google and cannot be changed
    public static String solution(String s) {
        char [] decrypted = new char[s.length()];
        char index;
        int key=25;
        int lowerBound=97;
        int upperBound=122;
        int ascii;
        int result;

        for (int i=0; i<s.length();i++) {
            index=s.charAt(i);
            ascii=(int)index;
            if (ascii>=lowerBound&&ascii<=upperBound) {
                result=lowerBound+(key-(ascii-lowerBound));
                decrypted[i]=(char)result;
            }
            else {
                decrypted[i]=s.charAt(i);
            }
        }
        s=String.valueOf(decrypted);
        return s;
    }

    //Encrypts the message for testing purposes
//    public static String encrypt(String message) {
//        int asciiValue;
//        int key=25;
//        int lowerBound=97;
//        int upperBound=122;
//
//        char [] encrypted = new char[message.length()];
//        for (int i=0; i<message.length();i++) {
//            asciiValue=message.charAt(i);
//            char b=(char) asciiValue;
//            if(asciiValue>=lowerBound&&asciiValue<=upperBound) {
//                int newVal=(key-(asciiValue-lowerBound))+lowerBound;
//                b=(char)newVal;
//            }
//            encrypted[i]=b;
//        }
//        return String.valueOf(encrypted);
//    }


}
