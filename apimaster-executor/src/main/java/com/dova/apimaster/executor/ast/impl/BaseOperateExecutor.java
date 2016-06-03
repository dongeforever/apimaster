package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.OperateExecutor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Joiner;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class BaseOperateExecutor extends OperateExecutor{

    @Override
    protected Object operateAdd(Object first, Object second){
        print("ADD first:%s second:%s", first, second);
        first =  convertJsonNode(first);
        second = convertJsonNode(second);
        if(first instanceof String || second instanceof String){
            return String.valueOf(first) + String.valueOf(second);
        }
        if(first instanceof Double || second instanceof  Double){
            return Double.valueOf(String.valueOf(first)) + Double.valueOf(String.valueOf(second));
        }
        Integer a = (Integer)first;
        Integer b = (Integer)second;
        return a+b;
    }

    @Override
    protected Object operateMulti(Object first, Object second){
        print("MULTI first:%s second:%s", first, second);
        first =  convertJsonNode(first);
        second = convertJsonNode(second);
        if(first instanceof Double || second instanceof Double){
            return Double.valueOf(String.valueOf(first)) * Double.valueOf(String.valueOf(second));
        }
        Integer a = (Integer)first;
        Integer b = (Integer)second;
        return a*b;
    }

    @Override
    protected Object operateDiv(Object first, Object second){
        print("MULTI first:%s second:%s", first, second);
        first =  convertJsonNode(first);
        second = convertJsonNode(second);
        if(first instanceof Double || second instanceof Double){
            return Double.valueOf(String.valueOf(first)) / Double.valueOf(String.valueOf(second));
        }
        Integer a = (Integer)first;
        Integer b = (Integer)second;
        return a/b;
    }

    @Override
    protected JsonNode operateAt(Object first, Object second){
        print("operat AT first:%s second:%s", first, second);
        JsonNode node = (JsonNode)first;
        return node.path(second.toString());
    }

    @Override
    protected Boolean operateEqual(Object first, Object second){
        print("EQ first:%s second:%s", first.toString(), second.toString());
        if(first ==  second || first.equals(second)){
            return true;
        }

        int compare = compare(first,second);
        return  compare == 0;
    }


    @Override
    protected Boolean operateLt(Object first, Object second){
        print("LT first:%s second:%s", first, second);
        int res = compare(first, second);
        return res < 0;
    }

    @Override
    protected Boolean operateGt(Object first, Object second){
        print("GT first:%s second:%s", first, second);
        int res = compare(first, second);
        return res > 0;
    }

    @Override
    protected Boolean operateLtEq(Object first, Object second){
        print("LTEQ first:%s second:%s", first, second);
        int res = compare(first, second);
        return res <= 0;
    }
    @Override
    protected Boolean operateGtEq(Object first, Object second){
        print("GTEQ first:%s second:%s", first, second);
        int res = compare(first,second);
        return res >=0;
    }

    @Override
    protected Boolean operateOr(Object first, Object second){
        print("OR first:%s second:%s", first, second);
        Boolean a = (Boolean)first;
        Boolean b = (Boolean)second;
        return a || b;
    }

    @Override
    protected Boolean operateAnd(Object first, Object second){
        print("AND first:%s second:%s", first, second);
        Boolean a = (Boolean)first;
        Boolean b = (Boolean)second;
        return a && b;
    }

    @Override
    protected Integer operateMod(Object first, Object second){
        print("MOD first:%s second:%s", first, second);
        Integer a = (Integer)first;
        Integer b = (Integer)second;
        return a % b;
    }

    protected int compare(Object first, Object second){
        print("compare first:%s second:%s",first.getClass(),second.getClass());
        if(first.getClass() == second.getClass() && first instanceof Comparable){
            return ((Comparable) first).compareTo(second);
        }
        if(first instanceof JsonNode){
            first = convertJsonNode((JsonNode) first);
        }
        if(second instanceof JsonNode){
            second = convertJsonNode((JsonNode) second);
        }
        if((first instanceof Integer || first instanceof Double)
                && (second instanceof Integer || second instanceof Double)){
            return Double.valueOf(first.toString()).compareTo(Double.valueOf(second.toString()));
        }

        return first.toString().compareTo(second.toString());
    }

    private Object convertJsonNode(Object origin){
        if(origin instanceof JsonNode){
            JsonNode tmp = (JsonNode)origin;
            if(tmp instanceof IntNode){
                return tmp.asInt();
            }else if(tmp instanceof DoubleNode){
                return tmp.asDouble();
            }else {
                return tmp.asText();
            }
        }else {
            return  origin;
        }

    }
}