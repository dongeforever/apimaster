package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.OperateExecutor;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class BaseOperateExecutor extends OperateExecutor{

    @Override
    protected Object operateAdd(Object first, Object second){
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
        if(first instanceof Double || second instanceof Double){
            return Double.valueOf(String.valueOf(first)) * Double.valueOf(String.valueOf(second));
        }
        Integer a = (Integer)first;
        Integer b = (Integer)second;
        return a*b;
    }

    @Override
    protected Integer operateAt(Object first, Object second){
        print("operat AT first:%s second:%s", first, second);
        Integer a = (Integer)first;
        Integer b = (Integer)second;
        return a+b;
    }

    @Override
    protected Boolean operateEqual(Object first, Object second){
        if(first ==  second || first.equals(second)){
            return true;
        }
        if(first instanceof JsonNode){
            first = ((JsonNode) first).asText();
        }
        if(second instanceof JsonNode){
            second = ((JsonNode) second).asText();
        }
        return first.toString().equals(second.toString());
    }


    @Override
    protected Boolean operateLt(Object first, Object second){
        int res = compare(first, second);
        return res < 0;
    }

    @Override
    protected Boolean operateGt(Object first, Object second){
        int res = compare(first, second);
        return res > 0;
    }

    @Override
    protected Boolean operateLtEq(Object first, Object second){
        int res = compare(first, second);
        return res <= 0;
    }
    @Override
    protected Boolean operateGtEq(Object first, Object second){
        int res = compare(first,second);
        return res >=0;
    }

    protected int compare(Object first, Object second){
        if(first.getClass() == second.getClass() && first instanceof Comparable){
            return ((Comparable) first).compareTo(second);
        }
        if(first instanceof JsonNode){
            first = ((JsonNode) first).asText();
        }
        if(second instanceof JsonNode){
            second = ((JsonNode) second).asText();
        }

        if((first instanceof Integer || first instanceof Double)
                && (second instanceof Integer || second instanceof Double)){
            return Double.valueOf(first.toString()).compareTo(Double.valueOf(second.toString()));
        }

        return first.toString().compareTo(second.toString());
    }
}
