package com.dova.apimaster.executor.ast.impl;

import com.dova.apimaster.executor.ast.OperateExecutor;
import com.dova.apimaster.executor.ast.domain.AstError;
import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.domain.Operator;
import com.dova.apimaster.executor.ast.helper.Assert;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.Annotations;

import java.util.List;

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
        Long a = (Long)first;
        Long b = (Long)second;
        return a+b;
    }

    @Override
    protected Object operateMinus(Object first, Object second){
        print("MINUS first:%s second:%s", first, second);
        first =  convertJsonNode(first);
        second = convertJsonNode(second);
        if(first instanceof Double || second instanceof Double){
            return Double.valueOf(String.valueOf(first)) - Double.valueOf(String.valueOf(second));
        }
        Long a = (Long)first;
        Long b = (Long)second;
        return a-b;
    }

    @Override
    protected Object operateIndex(Object first, Object second){
        print("INDEX first:%s second:%s", first, second);
        second = convertJsonNode(second);
        Long index = (Long)second;
        if(first instanceof List){
            if(((List) first).size() <= index){
                throw new IndexOutOfBoundsException(String.format("size:%d index:%d",((List) first).size(),index));
            }
            return ((List) first).get(index.intValue());
        }else if(first instanceof ArrayNode){
            if(((ArrayNode) first).size() <= index){
                throw new IndexOutOfBoundsException(String.format("size:%d index:%d",((ArrayNode) first).size(),index));
            }
            return ((ArrayNode) first).get(index.intValue());
        }else {
            throw new RuntimeException("unsupported type for INDEX," + first.getClass());
        }
    }

    @Override
    protected Object operateMulti(Object first, Object second){
        print("MULTI first:%s second:%s", first, second);
        first =  convertJsonNode(first);
        second = convertJsonNode(second);
        if(first instanceof Double || second instanceof Double){
            return Double.valueOf(String.valueOf(first)) * Double.valueOf(String.valueOf(second));
        }
        Long a = (Long)first;
        Long b = (Long)second;
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
        Long a = (Long)first;
        Long b = (Long)second;
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
        first = convertJsonNode(first);
        second = convertJsonNode(second);
        return first.equals(second);
        /*
        int compare = compare(first,second);
        return  compare == 0;
        */
    }


    @Override
    protected Object operateAssign(Object first, Object second){
        print("ASSIGN first:%s second:%s", first, second);
        if(second instanceof JsonNode && ((JsonNode) second).isMissingNode()){
            return first;
        }
        AstNode astNode = (AstNode)first;
        Assert.assertion((Operator)astNode.originValue == Operator.AT && astNode.getChildSize() == 2, AstError.UnExpected,"赋值操作符的前一个操作符必须是 AT");
        ObjectNode parent = (ObjectNode) astNode.getChild(0).get();
        String name = (String)astNode.getChild(1).get();

        if(second instanceof JsonNode){
            parent.set(name,(JsonNode) second);
        }else if(second instanceof Long){
            parent.put(name, (Long)second);
        }else if(second instanceof Double){
            parent.put(name, (Double) second);
        }else if(second instanceof String ){
            parent.put(name, (String)second);
        }else {
            throw new RuntimeException("Assign unknown second type:" + second.getClass());
        }
        return parent.get(name);
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
    protected Long operateMod(Object first, Object second){
        print("MOD first:%s second:%s", first, second);
        Long a = (Long)first;
        Long b = (Long)second;
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
        if((first instanceof Long || first instanceof Double)
                && (second instanceof Long || second instanceof Double)){
            return Double.valueOf(first.toString()).compareTo(Double.valueOf(second.toString()));
        }

        return first.toString().compareTo(second.toString());
    }

    private Object convertJsonNode(Object origin){
        if(origin instanceof JsonNode){
            JsonNode tmp = (JsonNode)origin;
            if(tmp instanceof IntNode){
                return tmp.asLong();
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
