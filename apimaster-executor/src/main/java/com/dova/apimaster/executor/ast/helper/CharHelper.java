package com.dova.apimaster.executor.ast.helper;

import com.dova.apimaster.executor.ast.domain.Operator;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by liuzhendong on 16/5/26.
 */
public class CharHelper {

    private static char[] digits = {'0','1','2','3','4','5','6','7','8','9'};

    private static char[] opEnd = {']',')'};

    private static char[] opStart = Operator.getStarts();

    private static char[] blanks = {
            '\r','\n',' ','\t'
    };

    private static char[] letters = {
            'a' , 'b' , 'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' , 'o' , 'p' ,
            'q' , 'r' , 's' , 't' , 'u' , 'v' , 'w' , 'x' ,
            'y' , 'z',
            'A' , 'B' , 'C' , 'D' , 'E' , 'F' , 'G' , 'H' ,
            'I' , 'J' , 'K' , 'L' , 'M' , 'N' , 'O' , 'P' ,
            'Q' , 'R' , 'S' , 'T' , 'U' , 'V' , 'W' , 'X' ,
            'Y' , 'Z'
    };

    private static boolean contains(char[] chars, char c){
        for (int i = 0; i < chars.length; i++) {
            if(c == chars[i]){
                return true;
            }
        }
        return  false;
    }

    public static boolean isDigit(char c){
       return contains(digits, c);
    }

    public static boolean isOpStart(char c){
        return contains(opStart, c);
    }

    public static boolean isLetter(char c){
        return contains(letters, c);
    }

    public static boolean isBlank(char c){
        return contains(blanks,c);
    }

    public static boolean isOpEnd(char c){
        return contains(opEnd, c);
    }


    public static char[] getDigits() {
        return digits;
    }

    public static void setDigits(char[] digits) {
        CharHelper.digits = digits;
    }

    public static char[] getOpStart() {
        return opStart;
    }

    public static void setOpStart(char[] opStart) {
        CharHelper.opStart = opStart;
    }

    public static char[] getBlanks() {
        return blanks;
    }

    public static void setBlanks(char[] blanks) {
        CharHelper.blanks = blanks;
    }

    public static char[] getLetters() {
        return letters;
    }

    public static void setLetters(char[] letters) {
        CharHelper.letters = letters;
    }
}
