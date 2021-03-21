package main;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.lang3.math.NumberUtils;

class CustomPair {
    private static Stack<Integer> stack = new Stack<Integer>();
    private String key;
    private Integer value;
    public CustomPair(String key, Integer value) {
        this.key = key;
        this.value = value;
    }
    public void pushRepeatDigit(Integer item){
        stack.push(item);
    }
    public Integer popRepeatDigit(){
        return stack.pop();
    }
    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
    public void addKey(String k){
        key = key + k;
    }
    public void addValue(Integer k){
        value = value + k;
    }
}
class WrongStringException extends Exception {

    public WrongStringException(String message) {
        super(message);
    }
}
class EmptyStatmentException extends Exception {

    public EmptyStatmentException(String message) {
        super(message);
    }
}
class NoBracketExeption extends Exception {

    public NoBracketExeption(String message) {
        super(message);
    }
}
public class Main {
    public static void stringUnpacker(String inputString ) throws WrongStringException, EmptyStatmentException, NoBracketExeption {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9\\[\\]]+");
        Matcher matcher = pattern.matcher(inputString);
        int openBracketCount = 0;
        int closeBracketCount = 0;
        for (int i = 0 ; i < inputString.length(); i++){
            if (inputString.charAt(i) == ']')
                closeBracketCount++;
            if (inputString.charAt(i) == '[')
                openBracketCount++;
        }
        if (closeBracketCount != openBracketCount){
            throw new NoBracketExeption("Open brackets " + openBracketCount + " != " + closeBracketCount + " Close brackets" );
        }
        if (!matcher.matches()) {
            throw new WrongStringException(inputString + " bad syntax");
        }
        CustomPair result = recursiveStringMaker(inputString, 0);
        System.out.println(result.getKey());
    }
    public static CustomPair recursiveStringMaker(String string_ , int i) throws EmptyStatmentException  {
        CustomPair concatPair = new CustomPair( ""  , 0);
        String stringDigit = "";
        while (i < string_.length()){
            if(NumberUtils.isNumber(String.valueOf(string_.charAt(i)))){
                while (NumberUtils.isNumber(String.valueOf(string_.charAt(i)))) {
                    stringDigit = stringDigit + string_.charAt(i);
                    i++;
                }
                i--;
                concatPair.pushRepeatDigit(Integer.parseInt(stringDigit));
                stringDigit = "";
            }
            else if (string_.charAt(i) == '['){
                i++;
                CustomPair tmp = recursiveStringMaker(string_ , i);
                concatPair.addKey(tmp.getKey());
                i = tmp.getValue();
            }
            else if (string_.charAt(i) == ']'){
                if (concatPair.getKey().equals(""))
                    throw new EmptyStatmentException("Empty Statment");
                int poped = concatPair.popRepeatDigit();
                String k = concatPair.getKey();
                for (int j = 0 ; j  < poped - 1; j++){
                    concatPair.addKey(k);
                }
                concatPair.setValue(i);
                return concatPair;
            }
            else  {
                concatPair.addKey(String.valueOf(string_.charAt(i)));
            }
            i++;
        }
        return concatPair;
    }
    public static void main(String[] args) throws WrongStringException, EmptyStatmentException, NoBracketExeption {
        stringUnpacker("qwe3[xyz]pp4[xy]z");
        stringUnpacker("2[3[x]y3[z]w]");
        stringUnpacker("2[2[2[r]x]y]");
//        stringUnpacker("2[x?]");
//        stringUnpacker("2[2[]x]");
        stringUnpacker("2[x]3[qwe");
    }
}
