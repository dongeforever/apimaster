package com.dova.apimaster.executor.ast.domain;

import java.util.*;

/**
 * Created by liuzhendong on 16/5/23.
 */
public enum Operator {
    AT(0,2,".",1),
    INDEX(1,2,"[]",1),
    ASSIGN(2,2,"=",4),
    EQ(3,2,"==",5),
    LT(4,2,"<",5),
    GT(5,2,">",5),
    LTEQ(6,2,"<=",5),
    GTEQ(7,2,">=",5),
    ADD(8,2,"+",3),
    MINUS(9,2,"-",3),
    MUTLI(10,2,"*",2),
    DIV(11,2,"/",2);

    public final  int value;
    public final int argNum;
    public final String desc;
    public final int priority;
    private Operator(int value,int argNum,String desc,int priority){
        this.value = value;
        this.argNum = argNum;
        this.desc = desc;
        this.priority = priority;
    }
    public static Operator of(String op){
        switch (op){
            case ".":
                return AT;
            case "[]":
                return INDEX;
            case "=":
                return ASSIGN;
            case "==":
                return EQ;
            case "<":
                return LT;
            case ">":
                return GT;
            case "<=":
                return LTEQ;
            case ">=":
                return GTEQ;
            case "+":
                return ADD;
            case "-":
                return MINUS;
            case "*":
                return MUTLI;
            case "/":
                return DIV;
            default:
                throw  new UnsupportedOperationException("unknown operator:" + op);
        }
    }

    public static char[] getStarts(){
        Operator[] ops = Operator.values();
        Set<Character> chars = new HashSet<>();
        for(Operator op : ops){
            chars.add(op.desc.charAt(0));
        }
        char[] res = new char[chars.size()];
        int i = 0;
        for(Character c : chars){
            res[i] = c.charValue();
            i++;
        }
        return res;
    }
}

