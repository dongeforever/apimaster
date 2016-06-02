package com.dova.apimaster.executor.ast.domain;

import java.util.*;

/**
 * Created by liuzhendong on 16/5/23.
 */
public enum Operator {
    BRACKET1(0,0,"(",0),
    BRACKET2(0,0,")",0),
    AT(0,2,".",1),
    INDEX(1,2,"[]",1),
    MUTLI(2,2,"*",2),
    DIV(3,2,"/",2),
    MOD(4,2,"%",2),
    ADD(5,2,"+",3),
    MINUS(6,2,"-",3),
    ASSIGN(7,2,"=",4),
    EQ(8,2,"==",5),
    LT(9,2,"<",5),
    GT(10,2,">",5),
    LTEQ(11,2,"<=",5),
    GTEQ(12,2,">=",5),
    OR(13,2,"||",6),
    AND(14,2,"&&",6);




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

